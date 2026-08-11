package com.example.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt

@Composable
fun ActionButtons(
    modifier: Modifier = Modifier,
    size: Dp = 150.dp,
    onTriangleChanged: (Boolean) -> Unit,
    onCircleChanged: (Boolean) -> Unit,
    onCrossChanged: (Boolean) -> Unit,
    onSquareChanged: (Boolean) -> Unit
) {
    val view = LocalView.current

    var trianglePressed by remember { mutableStateOf(false) }
    var circlePressed by remember { mutableStateOf(false) }
    var crossPressed by remember { mutableStateOf(false) }
    var squarePressed by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .size(size)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        val pxSize = size.toPx()
                        val btnRadius = pxSize / 5f

                        val topPos = Offset(pxSize / 2f, btnRadius + 8f)
                        val rightPos = Offset(pxSize - btnRadius - 8f, pxSize / 2f)
                        val bottomPos = Offset(pxSize / 2f, pxSize - btnRadius - 8f)
                        val leftPos = Offset(btnRadius + 8f, pxSize / 2f)

                        fun isNear(p: Offset): Boolean {
                            val dx = offset.x - p.x
                            val dy = offset.y - p.y
                            return sqrt(dx * dx + dy * dy) <= btnRadius * 1.3f
                        }

                        val isTri = isNear(topPos)
                        val isCir = isNear(rightPos)
                        val isCro = isNear(bottomPos)
                        val isSqu = isNear(leftPos)

                        if (isTri || isCir || isCro || isSqu) {
                            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                        }

                        trianglePressed = isTri
                        circlePressed = isCir
                        crossPressed = isCro
                        squarePressed = isSqu

                        onTriangleChanged(isTri)
                        onCircleChanged(isCir)
                        onCrossChanged(isCro)
                        onSquareChanged(isSqu)

                        tryAwaitRelease()

                        trianglePressed = false
                        circlePressed = false
                        crossPressed = false
                        squarePressed = false

                        onTriangleChanged(false)
                        onCircleChanged(false)
                        onCrossChanged(false)
                        onSquareChanged(false)
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val pxSize = size.toPx()
            val btnRadius = pxSize / 5f

            val topPos = Offset(pxSize / 2f, btnRadius + 8f)
            val rightPos = Offset(pxSize - btnRadius - 8f, pxSize / 2f)
            val bottomPos = Offset(pxSize / 2f, pxSize - btnRadius - 8f)
            val leftPos = Offset(btnRadius + 8f, pxSize / 2f)

            // Button Colors
            val triColor = Color(0xFF00E676) // Green Triangle
            val cirColor = Color(0xFFFF1744) // Red Circle
            val croColor = Color(0xFF2979FF) // Blue Cross
            val squColor = Color(0xFFF50057) // Pink Square

            val bgIdle = Color(0xFF1E293B)
            val borderIdle = Color(0xFF334155)

            fun drawSingleButton(center: Offset, isPressed: Boolean, accentColor: Color, drawSymbol: () -> Unit) {
                // Outer circle
                drawCircle(
                    color = if (isPressed) accentColor else bgIdle,
                    radius = btnRadius,
                    center = center
                )
                drawCircle(
                    color = if (isPressed) accentColor else borderIdle,
                    radius = btnRadius,
                    center = center,
                    style = Stroke(width = 3f)
                )

                drawSymbol()
            }

            // TRIANGLE (Top)
            drawSingleButton(topPos, trianglePressed, triColor) {
                val symbolColor = if (trianglePressed) Color.Black else triColor
                val r = btnRadius * 0.45f
                val path = Path().apply {
                    moveTo(topPos.x, topPos.y - r)
                    lineTo(topPos.x - r * 0.866f, topPos.y + r * 0.5f)
                    lineTo(topPos.x + r * 0.866f, topPos.y + r * 0.5f)
                    close()
                }
                drawPath(path, symbolColor, style = Stroke(width = 5f))
            }

            // CIRCLE (Right)
            drawSingleButton(rightPos, circlePressed, cirColor) {
                val symbolColor = if (circlePressed) Color.Black else cirColor
                drawCircle(
                    color = symbolColor,
                    radius = btnRadius * 0.45f,
                    center = rightPos,
                    style = Stroke(width = 5f)
                )
            }

            // CROSS (Bottom)
            drawSingleButton(bottomPos, crossPressed, croColor) {
                val symbolColor = if (crossPressed) Color.Black else croColor
                val r = btnRadius * 0.4f
                drawLine(
                    color = symbolColor,
                    start = Offset(bottomPos.x - r, bottomPos.y - r),
                    end = Offset(bottomPos.x + r, bottomPos.y + r),
                    strokeWidth = 5f
                )
                drawLine(
                    color = symbolColor,
                    start = Offset(bottomPos.x - r, bottomPos.y + r),
                    end = Offset(bottomPos.x + r, bottomPos.y - r),
                    strokeWidth = 5f
                )
            }

            // SQUARE (Left)
            drawSingleButton(leftPos, squarePressed, squColor) {
                val symbolColor = if (squarePressed) Color.Black else squColor
                val r = btnRadius * 0.4f
                drawRect(
                    color = symbolColor,
                    topLeft = Offset(leftPos.x - r, leftPos.y - r),
                    size = Size(r * 2f, r * 2f),
                    style = Stroke(width = 5f)
                )
            }
        }
    }
}
