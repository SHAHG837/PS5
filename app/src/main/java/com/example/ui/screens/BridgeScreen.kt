package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DesktopMac
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.theme.PS5Cyan
import com.example.ui.theme.PS5DarkNavy
import com.example.ui.viewmodel.GamepadViewModel

@Composable
fun BridgeScreen(viewModel: GamepadViewModel) {
    val isServerRunning by viewModel.isServerRunning.collectAsStateWithLifecycle()
    val connectedClients by viewModel.connectedClientsCount.collectAsStateWithLifecycle()
    val pingMs by viewModel.pingLatencyMs.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PS5DarkNavy)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Title
        Text(
            text = "Cross-Platform Gamepad Bridge",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Text(
            text = "Stream low-latency gamepad inputs to Android, iOS, PC, Mac, & TVs",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Banner Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF131A2A)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_ps5_banner_1786488629309),
                    contentDescription = "PS5 Controller Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Column {
                        Text(
                            text = "Murtaza Shah Ji App Engine",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = PS5Cyan
                        )
                        Text(
                            text = "Zero-delay UDP Gamepad Packet Broadcaster",
                            fontSize = 11.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Wi-Fi Server Control Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF131A2A)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = "UDP Server",
                            tint = PS5Cyan,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Wi-Fi Gamepad Server",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = if (isServerRunning) "Broadcasting on 192.168.1.105:8080" else "Server Stopped",
                                fontSize = 11.sp,
                                color = if (isServerRunning) Color(0xFF00E676) else Color.Red
                            )
                        }
                    }

                    Switch(
                        checked = isServerRunning,
                        onCheckedChange = { viewModel.toggleServer() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = PS5Cyan,
                            checkedTrackColor = Color(0xFF0072CE)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF0F172A))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Active Clients", fontSize = 10.sp, color = Color.Gray)
                            Text("$connectedClients Device", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF0F172A))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("UDP Packet Latency", fontSize = 10.sp, color = Color.Gray)
                            Text(String.format("%.1f ms", pingMs), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PS5Cyan)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "PLATFORM COMPATIBILITY & SETUP MATRIX",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Compatibility Matrix Grid
        val platforms = listOf(
            Triple("Android Phones & Tablets", "Downloadable APK / Google Play. Auto-detects as XInput or DualSense controller.", Icons.Default.Android),
            Triple("iOS & iPadOS Devices", "Downloadable via App Store / Safari Web Companion. Connect via WebSockets bridge.", Icons.Default.PhoneIphone),
            Triple("Windows PC & Gaming Rig", "Install ViGEmBus driver or x360ce receiver. Plays Steam, Epic Games, Game Pass.", Icons.Default.Laptop),
            Triple("Mac & macOS Studio", "Native Bluetooth HID controller mode or Web Receiver in Safari/Chrome.", Icons.Default.DesktopMac),
            Triple("Smart TVs & Android TV", "Direct Bluetooth pair or LAN TV App. Turn smartphone into TV remote gamepad.", Icons.Default.Tv)
        )

        platforms.forEach { (title, desc, icon) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131A2A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = PS5Cyan,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = desc, fontSize = 11.sp, color = Color.Gray)
                    }

                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Compatible",
                        tint = Color(0xFF00E676),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
