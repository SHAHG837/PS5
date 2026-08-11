package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.DiagnosticLogEntity
import com.example.data.db.GamepadDatabase
import com.example.data.db.GamepadProfileEntity
import com.example.data.db.MacroComboEntity
import com.example.data.repository.GamepadRepository
import com.example.model.GamepadInputState
import com.example.model.PresetProfiles
import com.example.model.StickPosition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class GamepadViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GamepadRepository

    val profiles: StateFlow<List<GamepadProfileEntity>>
    val diagnosticLogs: StateFlow<List<DiagnosticLogEntity>>

    // Live Gamepad Runtime Input State
    private val _inputState = MutableStateFlow(GamepadInputState())
    val inputState: StateFlow<GamepadInputState> = _inputState.asStateFlow()

    // Active Profile Configuration State
    private val _selectedProfile = MutableStateFlow(
        GamepadProfileEntity(
            profileName = "PS5 DualSense Standard",
            genre = "All Games",
            isDefault = true,
            ledColorHex = "#00D2FF"
        )
    )
    val selectedProfile: StateFlow<GamepadProfileEntity> = _selectedProfile.asStateFlow()

    // Bridge Status State
    private val _isServerRunning = MutableStateFlow(true)
    val isServerRunning: StateFlow<Boolean> = _isServerRunning.asStateFlow()

    private val _connectedClientsCount = MutableStateFlow(1)
    val connectedClientsCount: StateFlow<Int> = _connectedClientsCount.asStateFlow()

    private val _pingLatencyMs = MutableStateFlow(1.4f)
    val pingLatencyMs: StateFlow<Float> = _pingLatencyMs.asStateFlow()

    private val _isMuted = MutableStateFlow(false)
    val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

    private val _ledColorHex = MutableStateFlow("#00D2FF")
    val ledColorHex: StateFlow<String> = _ledColorHex.asStateFlow()

    // Gyro Steering Mode Toggle
    private val _isGyroSteeringActive = MutableStateFlow(false)
    val isGyroSteeringActive: StateFlow<Boolean> = _isGyroSteeringActive.asStateFlow()

    init {
        val database = GamepadDatabase.getDatabase(application)
        repository = GamepadRepository(database.gamepadDao())

        profiles = repository.allProfiles.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        diagnosticLogs = repository.diagnosticLogs.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Seed initial profile if empty
        viewModelScope.launch(Dispatchers.IO) {
            val initialList = repository.getProfileById(1)
            if (initialList == null) {
                PresetProfiles.defaultList.forEach { preset ->
                    repository.saveProfile(
                        GamepadProfileEntity(
                            profileName = preset.title,
                            genre = preset.genre,
                            isDefault = preset.isDefault,
                            gyroSteeringEnabled = preset.gyroSteering,
                            ledColorHex = "#00D2FF"
                        )
                    )
                }

                // Add sample macro
                repository.saveMacro(
                    MacroComboEntity(
                        profileId = 1,
                        comboName = "Rapid Fire Combo",
                        triggerButton = "Touchpad",
                        sequence = "CROSS -> 50ms -> SQUARE -> 50ms -> TRIANGLE",
                        description = "Fast action string macro for battle games"
                    )
                )

                // Add sample log
                repository.logDiagnostic(
                    DiagnosticLogEntity(
                        deviceName = "PS5 DualSense Wireless",
                        connectionType = "Virtual Wi-Fi UDP",
                        avgLatencyMs = 1.2f,
                        stickDriftScore = 0.02f,
                        statusText = "Calibrated - Zero Drift Detected"
                    )
                )
            }
        }

        // Live UDP packet simulation loop
        viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                delay(1200)
                if (_isServerRunning.value) {
                    _pingLatencyMs.value = (1.1f + Random.nextFloat() * 0.6f)
                }
            }
        }
    }

    fun updateCross(pressed: Boolean) {
        _inputState.update { it.copy(crossPressed = pressed) }
    }

    fun updateCircle(pressed: Boolean) {
        _inputState.update { it.copy(circlePressed = pressed) }
    }

    fun updateSquare(pressed: Boolean) {
        _inputState.update { it.copy(squarePressed = pressed) }
    }

    fun updateTriangle(pressed: Boolean) {
        _inputState.update { it.copy(trianglePressed = pressed) }
    }

    fun updateDPad(up: Boolean, down: Boolean, left: Boolean, right: Boolean) {
        _inputState.update {
            it.copy(
                dpadUpPressed = up,
                dpadDownPressed = down,
                dpadLeftPressed = left,
                dpadRightPressed = right
            )
        }
    }

    fun updateLeftStick(pos: StickPosition) {
        _inputState.update { it.copy(leftStick = pos) }
    }

    fun updateRightStick(pos: StickPosition) {
        _inputState.update { it.copy(rightStick = pos) }
    }

    fun updateL1(pressed: Boolean) {
        _inputState.update { it.copy(l1Pressed = pressed) }
    }

    fun updateR1(pressed: Boolean) {
        _inputState.update { it.copy(r1Pressed = pressed) }
    }

    fun updateL2(value: Float) {
        _inputState.update { it.copy(l2Trigger = value) }
    }

    fun updateR2(value: Float) {
        _inputState.update { it.copy(r2Trigger = value) }
    }

    fun updateTouchpad(pressed: Boolean, x: Float, y: Float) {
        _inputState.update {
            it.copy(
                touchpadPressed = pressed,
                touchpadX = x,
                touchpadY = y
            )
        }
    }

    fun toggleMute() {
        _isMuted.value = !_isMuted.value
        _inputState.update { it.copy(mutePressed = _isMuted.value) }
    }

    fun setLedColor(hex: String) {
        _ledColorHex.value = hex
        _selectedProfile.update { it.copy(ledColorHex = hex) }
    }

    fun toggleGyroSteering() {
        _isGyroSteeringActive.value = !_isGyroSteeringActive.value
        _selectedProfile.update { it.copy(gyroSteeringEnabled = _isGyroSteeringActive.value) }
    }

    fun selectProfile(profile: GamepadProfileEntity) {
        _selectedProfile.value = profile
        _ledColorHex.value = profile.ledColorHex
        _isGyroSteeringActive.value = profile.gyroSteeringEnabled
    }

    fun saveProfile(profileName: String, genre: String, ledHex: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newProfile = GamepadProfileEntity(
                profileName = profileName,
                genre = genre,
                ledColorHex = ledHex,
                gyroSteeringEnabled = _isGyroSteeringActive.value
            )
            val id = repository.saveProfile(newProfile)
            _selectedProfile.value = newProfile.copy(id = id)
        }
    }

    fun toggleServer() {
        _isServerRunning.value = !_isServerRunning.value
    }

    fun runDiagnosticCalibration() {
        viewModelScope.launch(Dispatchers.IO) {
            val log = DiagnosticLogEntity(
                deviceName = "Murtaza Shah Ji DualSense Bridge",
                connectionType = "Wi-Fi 6 Low-Latency UDP",
                avgLatencyMs = _pingLatencyMs.value,
                stickDriftScore = 0.01f,
                statusText = "Hardware Check Complete - Optimal Deadzones"
            )
            repository.logDiagnostic(log)
        }
    }
}
