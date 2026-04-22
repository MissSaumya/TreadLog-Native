package com.treadlog.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.treadlog.data.AppDatabase
import com.treadlog.data.CsvHelper
import com.treadlog.data.DomainLogic
import com.treadlog.data.SharedPrefsRepository
import com.treadlog.data.WorkoutEntry
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AppState(
    val entries: List<WorkoutEntry> = emptyList(),
    val lastOiledDate: String? = null,
    val nextOilingDate: String? = null,
    val maintenanceInterval: Int = 60
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val workoutDao = database.workoutDao()
    private val prefsRepository = SharedPrefsRepository(application)
    private val context = application.applicationContext

    val state: StateFlow<AppState> = combine(
        workoutDao.getAllEntries(),
        prefsRepository.lastOiledDateFlow
    ) { entries, lastOiledDate ->
        val interval = DomainLogic.calculateOilingInterval(entries)
        val nextDate = DomainLogic.calculateNextOilingDate(lastOiledDate, interval)
        AppState(
            entries = entries,
            lastOiledDate = lastOiledDate,
            nextOilingDate = nextDate,
            maintenanceInterval = interval
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppState())

    fun addWorkout(date: String, minutes: Int, notes: String?) {
        viewModelScope.launch {
            val existing = workoutDao.getEntryByDate(date)
            if (existing != null) {
                // Deduplicate check, update minutes/notes
                workoutDao.updateEntry(existing.copy(minutes = minutes, notes = notes))
            } else {
                workoutDao.insertEntry(WorkoutEntry(date = date, minutes = minutes, notes = notes))
            }
            triggerCsvBackup()
        }
    }

    fun deleteWorkout(entry: WorkoutEntry) {
        viewModelScope.launch {
            workoutDao.deleteEntry(entry)
            triggerCsvBackup()
        }
    }

    fun importCsv(csvString: String) {
        viewModelScope.launch {
            val parsedEntries = DomainLogic.parseCsvString(csvString)
            for (entry in parsedEntries) {
                val existing = workoutDao.getEntryByDate(entry.date)
                if (existing == null) {
                    workoutDao.insertEntry(entry)
                }
            }
            triggerCsvBackup()
        }
    }
    
    fun setLastOiledDate(dateString: String) {
        viewModelScope.launch {
            prefsRepository.saveLastOiledDate(dateString)
            triggerCsvBackup()
        }
    }

    private suspend fun triggerCsvBackup() {
        val entries = workoutDao.getAllEntries().first()
        val lastOiledStr = prefsRepository.lastOiledDateFlow.first()
        val interval = DomainLogic.calculateOilingInterval(entries)
        val nextDate = DomainLogic.calculateNextOilingDate(lastOiledStr, interval)
        
        val csv = DomainLogic.generateCsvString(entries, lastOiledStr, nextDate)
        CsvHelper.backupToCsv(context, csv)
    }
}
