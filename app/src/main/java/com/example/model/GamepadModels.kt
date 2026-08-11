package com.example.model

import androidx.compose.ui.graphics.Color

data class StickPosition(
    val x: Float = 0f, // -1.0 to 1.0
    val y: Float = 0f  // -1.0 to 1.0
)

data class GamepadInputState(
    val crossPressed: Boolean = false,
    val circlePressed: Boolean = false,
    val squarePressed: Boolean = false,
    val trianglePressed: Boolean = false,
    val dpadUpPressed: Boolean = false,
    val dpadDownPressed: Boolean = false,
    val dpadLeftPressed: Boolean = false,
    val dpadRightPressed: Boolean = false,
    val l1Pressed: Boolean = false,
    val r1Pressed: Boolean = false,
    val l2Trigger: Float = 0f, // 0.0 to 1.0
    val r2Trigger: Float = 0f, // 0.0 to 1.0
    val l3Pressed: Boolean = false,
    val r3Pressed: Boolean = false,
    val psButtonPressed: Boolean = false,
    val sharePressed: Boolean = false,
    val optionsPressed: Boolean = false,
    val mutePressed: Boolean = false,
    val touchpadPressed: Boolean = false,
    val touchpadX: Float = 0.5f,
    val touchpadY: Float = 0.5f,
    val leftStick: StickPosition = StickPosition(),
    val rightStick: StickPosition = StickPosition(),
    val gyroPitch: Float = 0f,
    val gyroRoll: Float = 0f,
    val gyroYaw: Float = 0f
)

data class ProfilePreset(
    val id: String,
    val title: String,
    val genre: String,
    val description: String,
    val primaryColor: Color,
    val gyroSteering: Boolean = false,
    val isDefault: Boolean = false
)

object PresetProfiles {
    val defaultList = listOf(
        ProfilePreset(
            id = "ps5_dualsense_std",
            title = "PS5 DualSense Standard",
            genre = "All Games",
            description = "1:1 Authentic DualSense virtual gamepad layout with adaptive touch feedback.",
            primaryColor = Color(0xFF0072CE),
            isDefault = true
        ),
        ProfilePreset(
            id = "fps_shooter_pro",
            title = "FPS Shooter Pro",
            genre = "FPS / Battle Royale",
            description = "High precision analog stick curve, enlarged trigger areas, and rapid-fire macro support.",
            primaryColor = Color(0xFFFF5252),
            gyroSteering = true
        ),
        ProfilePreset(
            id = "racing_motion_wheel",
            title = "GT Motion Racing Wheel",
            genre = "Racing",
            description = "Smartphone tilt sensor motion steering with analog L2/R2 gas & brake pedals.",
            primaryColor = Color(0xFFFFAB00),
            gyroSteering = true
        ),
        ProfilePreset(
            id = "fighting_arcade",
            title = "Arcade Fighter Combos",
            genre = "Fighting / Action",
            description = "Eight-directional D-Pad layout with quick Hadouken & Combo macro hotkeys.",
            primaryColor = Color(0xFFE040FB)
        ),
        ProfilePreset(
            id = "rpg_adventure",
            title = "RPG Quest Master",
            genre = "Open World / RPG",
            description = "Spacious touchpad navigation canvas, map shortcuts, and custom inventory toggles.",
            primaryColor = Color(0xFF00E676)
        )
    )
}
