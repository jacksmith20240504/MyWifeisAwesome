package com.example.sweetreminder.data
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ComplimentDao {
    @Insert
    fun insert(compliment: Compliment)

    @Query("SELECT * FROM compliment_history ORDER BY timestamp DESC")
    fun history(): Flow<List<Compliment>>
}
