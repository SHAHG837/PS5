package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gamepad_profiles")
data class GamepadProfileEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val profileName: String,
    val genre: String, // FPS, Racing, Fighting, RPG, Retro, Custom
    val isDefault: Boolean = false,
    val leftStickSensitivity: Float = 1.0f,
    val rightStickSensitivity: Float = 1.0f,
    val leftStickDeadzone: Float = 0.05f,
    val rightStickDeadzone: Float = 0.05f,
    val triggerSensitivity: Float = 1.0f,
    val hapticIntensity: Float = 0.8f,
    val gyroSteeringEnabled: Boolean = false,
    val gyroSensitivity: Float = 1.5f,
    val ledColorHex: String = "#0072CE", // PS5 DualSense Cyan Blue
    val buttonLayoutJson: String = "" // Serialized button positions and keycodes
)

@Entity(tableName = "macro_combos")
data class MacroComboEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val profileId: Long = 0,
    val comboName: String,
    val triggerButton: String, // e.g. L3, R3, Touchpad, Extra_1
    val sequence: String, // e.g. "SQUARE,DOWN,CROSS,100ms,TRIANGLE"
    val description: String = ""
)

@Entity(tableName = "diagnostic_logs")
data class DiagnosticLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val deviceName: String,
    val connectionType: String, // Virtual, Bluetooth, USB-OTG
    val avgLatencyMs: Float,
    val stickDriftScore: Float, // 0.0 to 10.0
    val statusText: String
)
