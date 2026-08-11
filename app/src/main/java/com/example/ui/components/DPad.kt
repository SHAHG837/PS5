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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.PS5Cyan

@Composable
fun DPad(
    modifier: Modifier = Modifier,
    size: Dp = 150.dp,
    onUpChanged: (Boolean) -> Unit,
    onDownChanged: (Boolean) -> Unit,
    onLeftChanged: (Boolean) -> Unit,
    onRightChanged: (Boolean) -> Unit
) {
    val view = LocalView.current

    var upPressed by remember { mutableStateOf(false) }
    var downPressed by remember { mutableStateOf(false) }
    var leftPressed by remember { mutableStateOf(false) }
    var rightPressed by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .size(size)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        val pxSize = size.toPx()
                        val third = pxSize / 3f

                        val isUp = offset.y < third && offset.x in third..(third * 2)
                        val isDown = offset.y > third * 2 && offset.x in third..(third * 2)
                        val isLeft = offset.x < third && offset.y in third..(third * 2)
                        val isRight = offset.x > third * 2 && offset.y in third..(third * 2)

                        if (isUp || isDown || isLeft || isRight) {
                            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                        }

                        upPressed = isUp
                        downPressed = isDown
                        leftPressed = isLeft
                        rightPressed = isRight

                        onUpChanged(isUp)
                        onDownChanged(isDown)
                        onLeftChanged(isLeft)
                        onRightChanged(isRight)

                        tryAwaitRelease()

                        upPressed = false
                        downPressed = false
                        leftPressed = false
                        rightPressed = false

                        onUpChanged(false)
                        onDownChanged(false)
                        onLeftChanged(false)
                        onRightChanged(false)
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val pxSize = size.toPx()
            val armWidth = pxSize / 3.2f
            val armLength = pxSize / 2.2f
            val centerOffset = (pxSize - armWidth) / 2f
            val cornerRadius = CornerRadius(12f, 12f)

            val baseColor = Color(0xFF1E293B)
            val pressedColor = PS5Cyan

            // Central Cross Background
            drawRoundRect(
                color = baseColor,
                topLeft = Offset(centerOffset, 0f),
                size = Size(armWidth, pxSize),
                cornerRadius = cornerRadius
            )
            drawRoundRect(
                color = baseColor,
                topLeft = Offset(0f, centerOffset),
                size = Size(pxSize, armWidth),
                cornerRadius = cornerRadius
            )

            // UP Arm
            if (upPressed) {
                drawRoundRect(
                    color = pressedColor,
                    topLeft = Offset(centerOffset, 0f),
                    size = Size(armWidth, armLength),
                    cornerRadius = cornerRadius
                )
            }

            // DOWN Arm
            if (downPressed) {
                drawRoundRect(
                    color = pressedColor,
                    topLeft = Offset(centerOffset, pxSize - armLength),
                    size = Size(armWidth, armLength),
                    cornerRadius = cornerRadius
                )
            }

            // LEFT Arm
            if (leftPressed) {
                drawRoundRect(
                    color = pressedColor,
                    topLeft = Offset(0f, centerOffset),
                    size = Size(armLength, armWidth),
                    cornerRadius = cornerRadius
                )
            }

            // RIGHT Arm
            if (rightPressed) {
                drawRoundRect(
                    color = pressedColor,
                    topLeft = Offset(pxSize - armLength, centerOffset),
                    size = Size(armLength, armWidth),
                    cornerRadius = cornerRadius
                )
            }

            // Directional Arrow Triangles
            val arrowSize = 16f

            // UP Arrow
            val upPath = Path().apply {
                moveTo(pxSize / 2f, 16f)
                lineTo(pxSize / 2f - arrowSize, 16f + arrowSize)
                lineTo(pxSize / 2f + arrowSize, 16f + arrowSize)
                close()
            }
            drawPath(upPath, if (upPressed) Color.Black else Color.White.copy(alpha = 0.9f))

            // DOWN Arrow
            val downPath = Path().apply {
                moveTo(pxSize / 2f, pxSize - 16f)
                lineTo(pxSize / 2f - arrowSize, pxSize - 16f - arrowSize)
                lineTo(pxSize / 2f + arrowSize, pxSize - 16f - arrowSize)
                close()
            }
            drawPath(downPath, if (downPressed) Color.Black else Color.White.copy(alpha = 0.9f))

            // LEFT Arrow
            val leftPath = Path().apply {
                moveTo(16f, pxSize / 2f)
                lineTo(16f + arrowSize, pxSize / 2f - arrowSize)
                lineTo(16f + arrowSize, pxSize / 2f + arrowSize)
                close()
            }
            drawPath(leftPath, if (leftPressed) Color.Black else Color.White.copy(alpha = 0.9f))

            // RIGHT Arrow
            val rightPath = Path().apply {
                moveTo(pxSize - 16f, pxSize / 2f)
                lineTo(pxSize - 16f - arrowSize, pxSize / 2f - arrowSize)
                lineTo(pxSize - 16f - arrowSize, pxSize / 2f + arrowSize)
                close()
            }
            drawPath(rightPath, if (rightPressed) Color.Black else Color.White.copy(alpha = 0.9f))
        }
    }
}
