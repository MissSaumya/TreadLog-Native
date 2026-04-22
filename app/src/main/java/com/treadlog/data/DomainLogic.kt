package com.treadlog.data

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object DomainLogic {
    // Math Logic from user_specifications.md
    fun calculateOilingInterval(entries: List<WorkoutEntry>): Int {
        if (entries.isEmpty()) return 60
        
        // Find Day 0 (first run)
        val sortedEntries = entries.sortedBy { LocalDate.parse(it.date) }
        val firstDate = LocalDate.parse(sortedEntries.first().date)
        
        // Group entries into 7-day rolling weeks
        val weeklyTotals = mutableMapOf<Int, Int>()
        
        for (entry in sortedEntries) {
            val entryDate = LocalDate.parse(entry.date)
            val daysSinceFirst = ChronoUnit.DAYS.between(firstDate, entryDate).toInt()
            val weekNumber = daysSinceFirst / 7
            
            weeklyTotals[weekNumber] = (weeklyTotals[weekNumber] ?: 0) + entry.minutes
        }
        
        val maxMinutesInAWeek = weeklyTotals.values.maxOrNull() ?: 0
        
        return when {
            maxMinutesInAWeek < 180 -> 60
            maxMinutesInAWeek in 180..479 -> 30
            else -> 15 // 480+ minutes
        }
    }

    fun calculateNextOilingDate(lastOiledDateString: String?, interval: Int): String? {
        if (lastOiledDateString.isNullOrEmpty()) return null
        return try {
            val lastDate = LocalDate.parse(lastOiledDateString)
            // It counts the 'day oiled' as Day 1. So we add interval - 1.
            lastDate.plusDays((interval - 1).toLong()).toString()
        } catch (e: Exception) {
            null
        }
    }

    // Export function
    fun generateCsvString(entries: List<WorkoutEntry>, lastOiledDate: String?, nextOilingDate: String?): String {
        val sb = StringBuilder()
        // BOM for excel
        sb.append("\uFEFF")
        sb.append("Date, Minutes, Notes, Logged At, Last Oiled Date, Next Oiling Date\n")
        
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val sorted = entries.sortedByDescending { LocalDate.parse(it.date, formatter) }
        
        for (e in sorted) {
            val noteSafe = if (e.notes != null) {
                // escape quotes and wrap in quotes if it contains commas
                val escaped = e.notes.replace("\"", "\"\"")
                if (escaped.contains(",")) "\"$escaped\"" else escaped
            } else ""
            
            sb.append("${e.date},${e.minutes},$noteSafe,${e.createdAt},$lastOiledDate,$nextOilingDate\n")
        }
        return sb.toString()
    }

    fun calculateCurrentWeekMinutes(entries: List<WorkoutEntry>, lastOiledDateString: String?): Int {
        if (entries.isEmpty()) return 0
        val sortedEntries = entries.sortedBy { LocalDate.parse(it.date) }
        
        val firstDate = if (!lastOiledDateString.isNullOrEmpty()) {
            try { LocalDate.parse(lastOiledDateString) } 
            catch(e: Exception) { LocalDate.parse(sortedEntries.first().date) }
        } else {
            LocalDate.parse(sortedEntries.first().date)
        }
        
        val today = LocalDate.now()
        val daysSinceFirst = ChronoUnit.DAYS.between(firstDate, today).toInt()
        val currentWeekNumber = (daysSinceFirst / 7).coerceAtLeast(0)
        
        var currentWeekTotal = 0
        for (entry in sortedEntries) {
            val entryDate = LocalDate.parse(entry.date)
            val entryDays = ChronoUnit.DAYS.between(firstDate, entryDate).toInt()
            if (entryDays / 7 == currentWeekNumber) {
                currentWeekTotal += entry.minutes
            }
        }
        return currentWeekTotal
    }

    // Import function
    fun parseCsvString(csvString: String): List<WorkoutEntry> {
        val entries = mutableListOf<WorkoutEntry>()
        val lines = csvString.split(Regex("\\r?\\n"))
        var isHeader = true
        for (line in lines) {
            val cleanLine = line.trim().removePrefix("\uFEFF")
            if (cleanLine.isEmpty()) continue
            if (isHeader) { isHeader = false; continue }

            // Regex to split by comma except inside quotes
            val cols = cleanLine.split(Regex(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")).map { it.trim() }
            if (cols.size >= 2) {
                val date = cols[0]
                val minutes = cols[1].toIntOrNull() ?: 0
                var notes: String? = if (cols.size >= 3) cols[2] else null
                
                // Clean up quotes from notes
                if (notes != null && notes.startsWith("\"") && notes.endsWith("\"")) {
                    notes = notes.substring(1, notes.length - 1).replace("\"\"", "\"")
                }
                if (notes.isNullOrBlank()) notes = null
                
                val loggedAt = if (cols.size >= 4) cols[3].toLongOrNull() ?: System.currentTimeMillis() else System.currentTimeMillis()
                
                if (minutes > 0) {
                    entries.add(WorkoutEntry(date = date, minutes = minutes, notes = notes, createdAt = loggedAt))
                }
            }
        }
        return entries
    }
}
