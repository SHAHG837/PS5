package com.example.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PS5Blue
import com.example.ui.theme.PS5Cyan

@Composable
fun TouchpadCenterArea(
    modifier: Modifier = Modifier,
    ledColor: Color = PS5Cyan,
    isMuted: Boolean = false,
    onTouchpadChanged: (Boolean, Float, Float) -> Unit,
    onPsButtonClicked: () -> Unit,
    onShareClicked: () -> Unit,
    onOptionsClicked: () -> Unit,
    onMuteToggled: () -> Unit
) {
    val view = LocalView.current
    var touchPoint by remember { mutableStateOf<Offset?>(null) }
    var isTouchpadPressed by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Shoulder Aux Row (Share, LED Light Bar, Options)
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Share / Create Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF1E293B))
                    .clickable {
                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        onShareClicked()
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Create / Share",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("CREATE", fontSize = 10.sp, color = Color.LightGray, fontWeight = FontWeight.Bold)
                }
            }

            // DualSense LED Light Strip Banner
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                ledColor,
                                ledColor,
                                Color.Transparent
                            )
                        )
                    )
            )

            // Options Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF1E293B))
                    .clickable {
                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        onOptionsClicked()
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("OPTIONS", fontSize = 10.sp, color = Color.LightGray, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "Options",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // DualSense Touchpad Surface
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(95.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(if (isTouchpadPressed) Color(0xFF1E293B) else Color(0xFF0F172A))
                .border(2.dp, ledColor.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { offset ->
                            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                            touchPoint = offset
                            isTouchpadPressed = true

                            val normX = (offset.x / size.width).coerceIn(0f, 1f)
                            val normY = (offset.y / size.height).coerceIn(0f, 1f)
                            onTouchpadChanged(true, normX, normY)

                            tryAwaitRelease()

                            touchPoint = null
                            isTouchpadPressed = false
                            onTouchpadChanged(false, 0.5f, 0.5f)
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxWidth().height(95.dp)) {
                // Subtle touchpad grid texture
                val borderGlow = ledColor
                drawRoundRect(
                    color = borderGlow.copy(alpha = 0.15f),
                    topLeft = Offset(4f, 4f),
                    size = Size(size.width - 8f, size.height - 8f),
                    cornerRadius = CornerRadius(10f, 10f)
                )

                touchPoint?.let { pt ->
                    drawCircle(
                        color = ledColor.copy(alpha = 0.4f),
                        radius = 28f,
                        center = pt
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 8f,
                        center = pt
                    )
                }
            }

            Text(
                text = "TOUCHPAD CANVAS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.4f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // PS Button & Mute Button Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // PS Logo Home Button
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E293B))
                    .border(1.5.dp, PS5Cyan, CircleShape)
                    .clickable {
                        view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                        onPsButtonClicked()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "PS",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Mute Button with Amber Indicator LED
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isMuted) Color(0xFFFFAB00).copy(alpha = 0.2f) else Color(0xFF1E293B))
                    .border(1.dp, if (isMuted) Color(0xFFFFAB00) else Color.Gray, RoundedCornerShape(8.dp))
                    .clickable {
                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        onMuteToggled()
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mute Mic",
                        tint = if (isMuted) Color(0xFFFFAB00) else Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isMuted) "MUTED" else "MIC",
                        fontSize = 10.sp,
                        color = if (isMuted) Color(0xFFFFAB00) else Color.LightGray,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
