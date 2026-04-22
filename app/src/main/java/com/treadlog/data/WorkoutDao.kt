package com.treadlog.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout_entries ORDER BY date DESC, createdAt DESC")
    fun getAllEntries(): Flow<List<WorkoutEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: WorkoutEntry)

    @Update
    suspend fun updateEntry(entry: WorkoutEntry)

    @Delete
    suspend fun deleteEntry(entry: WorkoutEntry)

    @Query("SELECT * FROM workout_entries WHERE date = :date LIMIT 1")
    suspend fun getEntryByDate(date: String): WorkoutEntry?
    
    @Query("DELETE FROM workout_entries")
    suspend fun deleteAll()
}
