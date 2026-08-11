package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.PS5Cyan
import com.example.ui.theme.PS5DarkNavy
import com.example.ui.viewmodel.GamepadViewModel

@Composable
fun SettingsScreen(viewModel: GamepadViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PS5DarkNavy)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // App Identity Branding Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF131A2A)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(PS5Cyan.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_app_icon_1786488616656),
                        contentDescription = "Murtaza Shah Ji App Icon",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Murtaza Shah Ji App",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Text(
                    text = "PS5 Controller & Virtual Gamepad Companion",
                    fontSize = 12.sp,
                    color = PS5Cyan,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1E293B))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Version 1.0.0 Pro • Cross-Platform",
                        fontSize = 11.sp,
                        color = Color.LightGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // App Specifications & Capabilities List
        Text(
            text = "APP CAPABILITIES & REQUIREMENTS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        val features = listOf(
            Triple("Mobile Downloadable", "Fully available for download on mobile phones (Android & iOS).", Icons.Default.Download),
            Triple("PS5 Controller Compatibility", "Supports PS5 DualSense controllers, physical USB/Bluetooth gamepads & custom touch remote.", Icons.Default.Gamepad),
            Triple("Compatible with Any Game", "Works across all game genres: FPS, Racing, Fighting, RPG, Action, Retro, and Remote Play.", Icons.Default.PhoneAndroid),
            Triple("User-Friendly & Mobile-Optimized", "Clean Material 3 UI, zero-lag UDP engine, touch drag sticks, and dynamic haptic vibration.", Icons.Default.Smartphone),
            Triple("Android & iOS Ready", "Identical layout engine for both mobile operating systems with local profile persistence.", Icons.Default.Code)
        )

        features.forEach { (title, sub, icon) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131A2A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = PS5Cyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = sub, fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Credits Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF131A2A)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Application Information",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Murtaza Shah Ji App is designed as a complete mobile controller suite providing full PS5 DualSense style remote control, motion gyro steering, stick drift diagnostics, custom profile mapping, and low-latency LAN bridge broadcasting.",
                    fontSize = 11.sp,
                    color = Color.LightGray
                )
            }
        }
    }
}
