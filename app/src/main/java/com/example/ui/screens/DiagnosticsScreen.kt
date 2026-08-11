package com.example.ui.screens

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.HapticFeedbackConstants
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.PS5Cyan
import com.example.ui.theme.PS5DarkNavy
import com.example.ui.viewmodel.GamepadViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DiagnosticsScreen(viewModel: GamepadViewModel) {
    val inputState by viewModel.inputState.collectAsStateWithLifecycle()
    val diagnosticLogs by viewModel.diagnosticLogs.collectAsStateWithLifecycle()
    val pingMs by viewModel.pingLatencyMs.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val view = LocalView.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PS5DarkNavy)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Hardware Diagnostics & Drift Calibration",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Analog stick drift tester, haptic rumble & latency check",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Button(
                onClick = { viewModel.runDiagnosticCalibration() },
                colors = ButtonDefaults.buttonColors(containerColor = PS5Cyan),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CompassCalibration,
                    contentDescription = "Calibrate",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Calibrate", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stick Drift & Axis Plots
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Left Stick Plot
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131A2A)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "LEFT STICK DRIFT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = PS5Cyan
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0F172A))
                            .border(1.dp, Color(0xFF334155), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(110.dp)) {
                            val c = Offset(size.width / 2f, size.height / 2f)
                            val r = size.width / 2f - 10f

                            // Axis Crosshair
                            drawLine(Color.DarkGray, Offset(c.x, 10f), Offset(c.x, size.height - 10f), strokeWidth = 1f)
                            drawLine(Color.DarkGray, Offset(10f, c.y), Offset(size.width - 10f, c.y), strokeWidth = 1f)

                            // Deadzone circle
                            drawCircle(PS5Cyan.copy(alpha = 0.2f), radius = r * 0.15f, center = c)

                            // Active coordinate point
                            val pt = Offset(
                                c.x + (inputState.leftStick.x * r),
                                c.y + (inputState.leftStick.y * r)
                            )
                            drawCircle(Color.Red, radius = 6f, center = pt)
                            drawLine(Color.Red, c, pt, strokeWidth = 2f)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Offset: (${String.format("%.2f", inputState.leftStick.x)}, ${String.format("%.2f", inputState.leftStick.y)})",
                        fontSize = 10.sp,
                        color = Color.LightGray
                    )
                }
            }

            // Right Stick Plot
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131A2A)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "RIGHT STICK DRIFT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = PS5Cyan
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0F172A))
                            .border(1.dp, Color(0xFF334155), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(110.dp)) {
                            val c = Offset(size.width / 2f, size.height / 2f)
                            val r = size.width / 2f - 10f

                            drawLine(Color.DarkGray, Offset(c.x, 10f), Offset(c.x, size.height - 10f), strokeWidth = 1f)
                            drawLine(Color.DarkGray, Offset(10f, c.y), Offset(size.width - 10f, c.y), strokeWidth = 1f)

                            drawCircle(PS5Cyan.copy(alpha = 0.2f), radius = r * 0.15f, center = c)

                            val pt = Offset(
                                c.x + (inputState.rightStick.x * r),
                                c.y + (inputState.rightStick.y * r)
                            )
                            drawCircle(Color.Red, radius = 6f, center = pt)
                            drawLine(Color.Red, c, pt, strokeWidth = 2f)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Offset: (${String.format("%.2f", inputState.rightStick.x)}, ${String.format("%.2f", inputState.rightStick.y)})",
                        fontSize = 10.sp,
                        color = Color.LightGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // DualSense Haptic Motor Test Bar
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF131A2A)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Vibration,
                        contentDescription = "Haptic Test",
                        tint = PS5Cyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DualSense Tactile Haptic Vibration Test",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Light Tap", fontSize = 11.sp, color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            val vibrator = context.getSystemService(Vibrator::class.java)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator?.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
                            } else {
                                @Suppress("DEPRECATION")
                                vibrator?.vibrate(300)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PS5Cyan),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Dual Rumble", fontSize = 11.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "CALIBRATION & LATENCY LOGS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        // Logs Table
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(diagnosticLogs) { log ->
                val dateStr = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(log.timestamp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF131A2A)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = log.deviceName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "${log.connectionType} • ${log.statusText}",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = String.format("%.1f ms", log.avgLatencyMs),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PS5Cyan
                            )
                            Text(
                                text = dateStr,
                                fontSize = 10.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }
}
