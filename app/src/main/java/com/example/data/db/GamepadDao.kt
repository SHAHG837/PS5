package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GamepadDao {

    @Query("SELECT * FROM gamepad_profiles ORDER BY isDefault DESC, profileName ASC")
    fun getAllProfiles(): Flow<List<GamepadProfileEntity>>

    @Query("SELECT * FROM gamepad_profiles WHERE id = :id")
    suspend fun getProfileById(id: Long): GamepadProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: GamepadProfileEntity): Long

    @Update
    suspend fun updateProfile(profile: GamepadProfileEntity)

    @Query("DELETE FROM gamepad_profiles WHERE id = :id")
    suspend fun deleteProfileById(id: Long)

    @Query("SELECT * FROM macro_combos WHERE profileId = :profileId")
    fun getMacrosForProfile(profileId: Long): Flow<List<MacroComboEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMacro(macro: MacroComboEntity): Long

    @Query("DELETE FROM macro_combos WHERE id = :id")
    suspend fun deleteMacroById(id: Long)

    @Query("SELECT * FROM diagnostic_logs ORDER BY timestamp DESC LIMIT 20")
    fun getDiagnosticLogs(): Flow<List<DiagnosticLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiagnosticLog(log: DiagnosticLogEntity): Long
}
