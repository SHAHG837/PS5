package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.db.GamepadProfileEntity
import com.example.ui.theme.PS5Blue
import com.example.ui.theme.PS5Cyan
import com.example.ui.theme.PS5DarkNavy
import com.example.ui.viewmodel.GamepadViewModel

@Composable
fun LayoutStudioScreen(viewModel: GamepadViewModel) {
    val profiles by viewModel.profiles.collectAsStateWithLifecycle()
    val activeProfile by viewModel.selectedProfile.collectAsStateWithLifecycle()
    val isGyroActive by viewModel.isGyroSteeringActive.collectAsStateWithLifecycle()

    var showCreateProfileDialog by remember { mutableStateOf(false) }

    var newProfileName by remember { mutableStateOf("") }
    var newProfileGenre by remember { mutableStateOf("FPS") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PS5DarkNavy)
            .padding(16.dp)
    ) {
        // Studio Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Layout & Macro Studio",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Custom key mappings, deadzones & game profiles",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Button(
                onClick = { showCreateProfileDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = PS5Cyan),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Profile",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("New Profile", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Active Profile Tuning Card
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
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(PS5Cyan)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ACTIVE PROFILE TUNING",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PS5Cyan
                        )
                    }

                    Text(
                        text = activeProfile.profileName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Left Stick Sensitivity Slider
                Text(
                    text = "Left Thumbstick Sensitivity: ${(activeProfile.leftStickSensitivity * 100).toInt()}%",
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
                Slider(
                    value = activeProfile.leftStickSensitivity,
                    onValueChange = { newSens ->
                        viewModel.selectProfile(activeProfile.copy(leftStickSensitivity = newSens))
                    },
                    valueRange = 0.5f..2.0f,
                    colors = SliderDefaults.colors(thumbColor = PS5Cyan, activeTrackColor = PS5Cyan)
                )

                // Right Stick Sensitivity Slider
                Text(
                    text = "Right Thumbstick Sensitivity: ${(activeProfile.rightStickSensitivity * 100).toInt()}%",
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
                Slider(
                    value = activeProfile.rightStickSensitivity,
                    onValueChange = { newSens ->
                        viewModel.selectProfile(activeProfile.copy(rightStickSensitivity = newSens))
                    },
                    valueRange = 0.5f..2.0f,
                    colors = SliderDefaults.colors(thumbColor = PS5Cyan, activeTrackColor = PS5Cyan)
                )

                // Gyro Motion Steering Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Gyroscope Motion Steering",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Tilt phone to control steering wheel / aiming",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }

                    Switch(
                        checked = isGyroActive,
                        onCheckedChange = { viewModel.toggleGyroSteering() },
                        colors = SwitchDefaults.colors(checkedThumbColor = PS5Cyan, checkedTrackColor = PS5Blue)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "SAVED GAMEPAD PROFILES",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Profiles List
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(profiles) { profile ->
                val isSelected = profile.id == activeProfile.id || (activeProfile.id == 0L && profile.isDefault)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.selectProfile(profile) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF1E293B) else Color(0xFF131A2A)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        try { Color(android.graphics.Color.parseColor(profile.ledColorHex)) } catch (e: Exception) { PS5Cyan }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Gamepad,
                                    contentDescription = "Gamepad Profile",
                                    tint = Color.Black,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = profile.profileName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Genre: ${profile.genre} • Gyro: ${if (profile.gyroSteeringEnabled) "ON" else "OFF"}",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selected",
                                tint = PS5Cyan,
                                modifier = Modifier.size(22.dp)
                            )
                        } else {
                            Text(
                                text = "SELECT",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }

    // New Profile Modal Dialog
    if (showCreateProfileDialog) {
        AlertDialog(
            onDismissRequest = { showCreateProfileDialog = false },
            containerColor = Color(0xFF131A2A),
            title = {
                Text(
                    text = "Create Custom Game Profile",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = newProfileName,
                        onValueChange = { newProfileName = it },
                        label = { Text("Profile Name") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PS5Cyan,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = PS5Cyan,
                            unfocusedLabelColor = Color.Gray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = newProfileGenre,
                        onValueChange = { newProfileGenre = it },
                        label = { Text("Genre (FPS, Racing, Fighting, RPG)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PS5Cyan,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = PS5Cyan,
                            unfocusedLabelColor = Color.Gray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newProfileName.isNotBlank()) {
                            viewModel.saveProfile(newProfileName, newProfileGenre, "#00D2FF")
                            newProfileName = ""
                            showCreateProfileDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PS5Cyan)
                ) {
                    Text("Save Profile", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showCreateProfileDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }
}
