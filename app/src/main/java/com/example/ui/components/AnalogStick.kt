package com.example.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.model.StickPosition
import com.example.ui.theme.PS5Cyan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun AnalogStick(
    modifier: Modifier = Modifier,
    stickSize: Dp = 140.dp,
    label: String = "L3",
    sensitivity: Float = 1.0f,
    deadzone: Float = 0.05f,
    onPositionChanged: (StickPosition) -> Unit,
    onL3R3Click: (() -> Unit)? = null
) {
    val view = LocalView.current
    var knobOffset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .size(stickSize)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                    },
                    onDragEnd = {
                        knobOffset = Offset.Zero
                        onPositionChanged(StickPosition(0f, 0f))
                    },
                    onDragCancel = {
                        knobOffset = Offset.Zero
                        onPositionChanged(StickPosition(0f, 0f))
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val newOffset = knobOffset + dragAmount
                        val maxRadius = stickSize.toPx() / 2f - 20f

                        val distance = sqrt(newOffset.x * newOffset.x + newOffset.y * newOffset.y)
                        val angle = atan2(newOffset.y, newOffset.x)

                        val clampedDistance = distance.coerceAtMost(maxRadius)
                        knobOffset = Offset(
                            clampedDistance * cos(angle),
                            clampedDistance * sin(angle)
                        )

                        // Normalized position (-1.0 to 1.0)
                        val rawX = (knobOffset.x / maxRadius) * sensitivity
                        val rawY = (knobOffset.y / maxRadius) * sensitivity

                        val normDistance = sqrt(rawX * rawX + rawY * rawY)
                        val finalPos = if (normDistance < deadzone) {
                            StickPosition(0f, 0f)
                        } else {
                            StickPosition(
                                x = rawX.coerceIn(-1f, 1f),
                                y = rawY.coerceIn(-1f, 1f)
                            )
                        }

                        onPositionChanged(finalPos)
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height
            val center = Offset(w / 2f, h / 2f)
            val outerRadius = w / 2f - 4f
            val knobRadius = w / 4.5f

            // Outer ring base
            drawCircle(
                color = Color(0xFF1E293B),
                radius = outerRadius,
                center = center
            )
            drawCircle(
                color = Color(0xFF0F172A),
                radius = outerRadius - 6f,
                center = center
            )

            // Inner dashed guide ring
            drawCircle(
                color = PS5Cyan.copy(alpha = 0.3f),
                radius = outerRadius * 0.6f,
                center = center,
                style = Stroke(
                    width = 2f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )
            )

            // Outer accent border
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(PS5Cyan, Color(0xFF0072CE), Color.Transparent),
                    center = center,
                    radius = outerRadius
                ),
                radius = outerRadius,
                center = center,
                style = Stroke(width = 3f)
            )

            // Active Knob position
            val currentKnobCenter = center + knobOffset

            // Connector line
            if (knobOffset != Offset.Zero) {
                drawLine(
                    color = PS5Cyan.copy(alpha = 0.6f),
                    start = center,
                    end = currentKnobCenter,
                    strokeWidth = 4f
                )
            }

            // Knob base shadow & body
            drawCircle(
                color = Color.Black.copy(alpha = 0.5f),
                radius = knobRadius + 4f,
                center = currentKnobCenter + Offset(0f, 4f)
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF475569), Color(0xFF1E293B), Color(0xFF0F172A)),
                    center = currentKnobCenter,
                    radius = knobRadius
                ),
                radius = knobRadius,
                center = currentKnobCenter
            )

            // Knob Top Grip Ring
            drawCircle(
                color = PS5Cyan,
                radius = knobRadius * 0.75f,
                center = currentKnobCenter,
                style = Stroke(width = 2.5f)
            )

            // Center texture / button label dot
            drawCircle(
                color = Color.White.copy(alpha = 0.9f),
                radius = knobRadius * 0.25f,
                center = currentKnobCenter
            )
        }
    }
}
