package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        GamepadProfileEntity::class,
        MacroComboEntity::class,
        DiagnosticLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GamepadDatabase : RoomDatabase() {

    abstract fun gamepadDao(): GamepadDao

    companion object {
        @Volatile
        private var INSTANCE: GamepadDatabase? = null

        fun getDatabase(context: Context): GamepadDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GamepadDatabase::class.java,
                    "murtaza_shah_ji_gamepad.db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
