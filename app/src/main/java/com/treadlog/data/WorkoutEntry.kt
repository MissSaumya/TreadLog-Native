package com.treadlog.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "workout_entries")
data class WorkoutEntry(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val date: String, // format yyyy-MM-dd
    val minutes: Int,
    val notes: String?,
    val createdAt: Long = System.currentTimeMillis()
)
