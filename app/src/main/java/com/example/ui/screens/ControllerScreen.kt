package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.components.ActionButtons
import com.example.ui.components.AnalogStick
import com.example.ui.components.DPad
import com.example.ui.components.TouchpadCenterArea
import com.example.ui.components.TriggersHeader
import com.example.ui.theme.PS5Blue
import com.example.ui.theme.PS5Cyan
import com.example.ui.theme.PS5DarkNavy
import com.example.ui.viewmodel.GamepadViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControllerScreen(
    viewModel: GamepadViewModel,
    onNavigateToStudio: () -> Unit
) {
    val inputState by viewModel.inputState.collectAsStateWithLifecycle()
    val activeProfile by viewModel.selectedProfile.collectAsStateWithLifecycle()
    val isServerRunning by viewModel.isServerRunning.collectAsStateWithLifecycle()
    val pingMs by viewModel.pingLatencyMs.collectAsStateWithLifecycle()
    val isMuted by viewModel.isMuted.collectAsStateWithLifecycle()
    val ledHex by viewModel.ledColorHex.collectAsStateWithLifecycle()
    val isGyroActive by viewModel.isGyroSteeringActive.collectAsStateWithLifecycle()

    var showColorPickerSheet by remember { mutableStateOf(false) }

    val activeLedColor = try {
        Color(android.graphics.Color.parseColor(ledHex))
    } catch (e: Exception) {
        PS5Cyan
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        PS5DarkNavy,
                        Color(0xFF0F172A),
                        PS5DarkNavy
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 8.dp)
        ) {
            // Top Status Header Bar
            Surface(
                color = Color(0xFF131A2A),
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // App Brand Title
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(activeLedColor.copy(alpha = 0.2f))
                                .border(1.dp, activeLedColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Gamepad,
                                contentDescription = "Gamepad",
                                tint = activeLedColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Murtaza Shah Ji App",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "PS5 Virtual DualSense Remote",
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    // Connection Status Badge & Quick Tools
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Gyro Steering Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isGyroActive) PS5Cyan.copy(alpha = 0.2f) else Color(0xFF1E293B))
                                .border(1.dp, if (isGyroActive) PS5Cyan else Color.Gray, RoundedCornerShape(12.dp))
                                .clickable { viewModel.toggleGyroSteering() }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Sensors,
                                    contentDescription = "Gyro Motion",
                                    tint = if (isGyroActive) PS5Cyan else Color.Gray,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isGyroActive) "GYRO ON" else "GYRO OFF",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isGyroActive) PS5Cyan else Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // LED Color Picker Button
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(activeLedColor)
                                .clickable { showColorPickerSheet = true }
                                .border(1.5.dp, Color.White, CircleShape)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        // Server Latency Status
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isServerRunning) Color(0xFF00E676).copy(alpha = 0.15f) else Color.Red.copy(alpha = 0.2f))
                                .border(1.dp, if (isServerRunning) Color(0xFF00E676) else Color.Red, RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isServerRunning) Icons.Default.Wifi else Icons.Default.WifiOff,
                                    contentDescription = "Status",
                                    tint = if (isServerRunning) Color(0xFF00E676) else Color.Red,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isServerRunning) String.format("%.1f ms", pingMs) else "OFFLINE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isServerRunning) Color(0xFF00E676) else Color.Red
                                )
                            }
                        }
                    }
                }
            }

            // Active Profile Banner Chip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0B0F19))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PROFILE: ${activeProfile.profileName} (${activeProfile.genre})",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = activeLedColor
                )

                Text(
                    text = "EDIT LAYOUT >",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray,
                    modifier = Modifier.clickable { onNavigateToStudio() }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Virtual Gamepad Main Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Triggers (L1/L2 and R1/R2)
                TriggersHeader(
                    l2Value = inputState.l2Trigger,
                    r2Value = inputState.r2Trigger,
                    onL1Changed = { viewModel.updateL1(it) },
                    onR1Changed = { viewModel.updateR1(it) },
                    onL2Changed = { viewModel.updateL2(it) },
                    onR2Changed = { viewModel.updateR2(it) }
                )

                // Center Section: DualSense Touchpad & Quick Action Buttons
                TouchpadCenterArea(
                    ledColor = activeLedColor,
                    isMuted = isMuted,
                    onTouchpadChanged = { pressed, x, y -> viewModel.updateTouchpad(pressed, x, y) },
                    onPsButtonClicked = { /* PS Home action */ },
                    onShareClicked = { /* Share Create */ },
                    onOptionsClicked = { onNavigateToStudio() },
                    onMuteToggled = { viewModel.toggleMute() }
                )

                // Main Gamepad Wings (Left D-Pad & Stick, Right Actions & Stick)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // LEFT WING: D-Pad & Left Stick L3
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        DPad(
                            size = 140.dp,
                            onUpChanged = { u -> viewModel.updateDPad(u, inputState.dpadDownPressed, inputState.dpadLeftPressed, inputState.dpadRightPressed) },
                            onDownChanged = { d -> viewModel.updateDPad(inputState.dpadUpPressed, d, inputState.dpadLeftPressed, inputState.dpadRightPressed) },
                            onLeftChanged = { l -> viewModel.updateDPad(inputState.dpadUpPressed, inputState.dpadDownPressed, l, inputState.dpadRightPressed) },
                            onRightChanged = { r -> viewModel.updateDPad(inputState.dpadUpPressed, inputState.dpadDownPressed, inputState.dpadLeftPressed, r) }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        AnalogStick(
                            stickSize = 120.dp,
                            label = "L3",
                            onPositionChanged = { viewModel.updateLeftStick(it) }
                        )
                    }

                    // RIGHT WING: Action Buttons & Right Stick R3
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        ActionButtons(
                            size = 140.dp,
                            onTriangleChanged = { viewModel.updateTriangle(it) },
                            onCircleChanged = { viewModel.updateCircle(it) },
                            onCrossChanged = { viewModel.updateCross(it) },
                            onSquareChanged = { viewModel.updateSquare(it) }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        AnalogStick(
                            stickSize = 120.dp,
                            label = "R3",
                            onPositionChanged = { viewModel.updateRightStick(it) }
                        )
                    }
                }

                // Bottom Real-time Input Monitor Bar
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF131A2A)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "L-Stick: (${String.format("%.2f", inputState.leftStick.x)}, ${String.format("%.2f", inputState.leftStick.y)})",
                            fontSize = 10.sp,
                            color = Color.LightGray
                        )

                        Text(
                            text = "R-Stick: (${String.format("%.2f", inputState.rightStick.x)}, ${String.format("%.2f", inputState.rightStick.y)})",
                            fontSize = 10.sp,
                            color = Color.LightGray
                        )

                        Text(
                            text = if (isGyroActive) "STEERING: ACTIVE" else "STEERING: OFF",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isGyroActive) PS5Cyan else Color.DarkGray
                        )
                    }
                }
            }
        }
    }

    // Modal Sheet for Quick LED Light Color Selection
    if (showColorPickerSheet) {
        ModalBottomSheet(
            onDismissRequest = { showColorPickerSheet = false },
            containerColor = Color(0xFF131A2A)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "DualSense Lightbar LED Color",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Select a color theme for Murtaza Shah Ji App virtual controller LED strip:",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))

                val presetColors = listOf(
                    "#00D2FF" to "PS Cyan",
                    "#0072CE" to "DualSense Blue",
                    "#FF4081" to "Crimson Pink",
                    "#00E676" to "Emerald Green",
                    "#FFD600" to "Golden Sun",
                    "#E040FB" to "Neon Purple"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    presetColors.forEach { (hex, name) ->
                        val c = Color(android.graphics.Color.parseColor(hex))
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                viewModel.setLedColor(hex)
                                showColorPickerSheet = false
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(c)
                                    .border(2.dp, if (ledHex == hex) Color.White else Color.Transparent, CircleShape)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = name, fontSize = 10.sp, color = Color.White)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
