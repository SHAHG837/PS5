package com.example.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PS5Cyan

@Composable
fun TriggersHeader(
    modifier: Modifier = Modifier,
    l2Value: Float = 0f,
    r2Value: Float = 0f,
    onL1Changed: (Boolean) -> Unit,
    onR1Changed: (Boolean) -> Unit,
    onL2Changed: (Float) -> Unit,
    onR2Changed: (Float) -> Unit
) {
    val view = LocalView.current
    var l1Pressed by remember { mutableStateOf(false) }
    var r1Pressed by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Triggers Group (L2 analog slider & L1 button)
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // L1 Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                        .background(if (l1Pressed) PS5Cyan else Color(0xFF1E293B))
                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = {
                                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                                    l1Pressed = true
                                    onL1Changed(true)
                                },
                                onDragEnd = {
                                    l1Pressed = false
                                    onL1Changed(false)
                                },
                                onDragCancel = {
                                    l1Pressed = false
                                    onL1Changed(false)
                                },
                                onDrag = { _, _ -> }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "L1",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (l1Pressed) Color.Black else Color.White
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // L2 Trigger Pressure Display & Slider
                Column(modifier = Modifier.weight(1.5f)) {
                    Text(
                        text = "L2: ${(l2Value * 100).toInt()}%",
                        fontSize = 9.sp,
                        color = PS5Cyan,
                        fontWeight = FontWeight.Bold
                    )
                    Slider(
                        value = l2Value,
                        onValueChange = {
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                            onL2Changed(it)
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = PS5Cyan,
                            activeTrackColor = PS5Cyan,
                            inactiveTrackColor = Color(0xFF1E293B)
                        ),
                        modifier = Modifier.height(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Right Triggers Group (R2 analog slider & R1 button)
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // R2 Trigger Pressure Display & Slider
                Column(modifier = Modifier.weight(1.5f)) {
                    Text(
                        text = "R2: ${(r2Value * 100).toInt()}%",
                        fontSize = 9.sp,
                        color = PS5Cyan,
                        fontWeight = FontWeight.Bold
                    )
                    Slider(
                        value = r2Value,
                        onValueChange = {
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                            onR2Changed(it)
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = PS5Cyan,
                            activeTrackColor = PS5Cyan,
                            inactiveTrackColor = Color(0xFF1E293B)
                        ),
                        modifier = Modifier.height(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // R1 Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                        .background(if (r1Pressed) PS5Cyan else Color(0xFF1E293B))
                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = {
                                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                                    r1Pressed = true
                                    onR1Changed(true)
                                },
                                onDragEnd = {
                                    r1Pressed = false
                                    onR1Changed(false)
                                },
                                onDragCancel = {
                                    r1Pressed = false
                                    onR1Changed(false)
                                },
                                onDrag = { _, _ -> }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "R1",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (r1Pressed) Color.Black else Color.White
                    )
                }
            }
        }
    }
}
