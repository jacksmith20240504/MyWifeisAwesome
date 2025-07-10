package com.example.sweetreminder.data
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Compliment::class], version = 1)
abstract class ComplimentDatabase : RoomDatabase() {
    abstract fun dao(): ComplimentDao

    companion object {
        @Volatile
        private var INSTANCE: ComplimentDatabase? = null

        fun instance(ctx: Context): ComplimentDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    ctx.applicationContext,
                    ComplimentDatabase::class.java,
                    "compliments.db"
                ).build().also { INSTANCE = it }
            }
    }
}
