package com.treadlog.data

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileWriter

object CsvHelper {
    fun backupToCsv(context: Context, csvContent: String) {
        try {
            // Target: Documents/Treadmill Logs
            val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            val appFolder = File(documentsDir, "Treadmill Logs")
            
            if (!appFolder.exists()) {
                val created = appFolder.mkdirs()
                if (!created) {
                    Log.e("CsvHelper", "Failed to create Treadmill Logs directory")
                    return
                }
            }

            val file = File(appFolder, "TreadLog_History.csv")
            val writer = FileWriter(file, false) // false to overwrite
            writer.write(csvContent)
            writer.flush()
            writer.close()
            Log.d("CsvHelper", "Successfully backed up to ${file.absolutePath}")
            
        } catch (e: Exception) {
            Log.e("CsvHelper", "Error writing CSV backup: ${e.message}")
        }
    }
}
