package com.example.data.repository

import com.example.data.db.DiagnosticLogEntity
import com.example.data.db.GamepadDao
import com.example.data.db.GamepadProfileEntity
import com.example.data.db.MacroComboEntity
import kotlinx.coroutines.flow.Flow

class GamepadRepository(private val dao: GamepadDao) {

    val allProfiles: Flow<List<GamepadProfileEntity>> = dao.getAllProfiles()
    val diagnosticLogs: Flow<List<DiagnosticLogEntity>> = dao.getDiagnosticLogs()

    suspend fun getProfileById(id: Long): GamepadProfileEntity? = dao.getProfileById(id)

    suspend fun saveProfile(profile: GamepadProfileEntity): Long {
        return if (profile.id == 0L) {
            dao.insertProfile(profile)
        } else {
            dao.updateProfile(profile)
            profile.id
        }
    }

    suspend fun deleteProfile(id: Long) = dao.deleteProfileById(id)

    fun getMacrosForProfile(profileId: Long): Flow<List<MacroComboEntity>> = dao.getMacrosForProfile(profileId)

    suspend fun saveMacro(macro: MacroComboEntity): Long = dao.insertMacro(macro)

    suspend fun deleteMacro(id: Long) = dao.deleteMacroById(id)

    suspend fun logDiagnostic(log: DiagnosticLogEntity): Long = dao.insertDiagnosticLog(log)
}
