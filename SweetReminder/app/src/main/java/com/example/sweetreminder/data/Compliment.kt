package com.example.sweetreminder.data
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "compliment_history")
data class Compliment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val timestamp: Long = Instant.now().toEpochMilli(),
    val theme: String,
    val content: String
)
