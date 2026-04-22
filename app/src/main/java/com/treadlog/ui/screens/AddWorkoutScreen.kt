package com.treadlog.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.treadlog.ui.MainViewModel
import java.time.LocalDate

@Composable
fun AddWorkoutScreen(viewModel: MainViewModel) {
    var minutes by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Log Workout", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = minutes,
            onValueChange = { minutes = it },
            label = { Text("Duration (Minutes)") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { minutes = "30" },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Quick 30 Min Preset")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = {
                val minInt = minutes.toIntOrNull() ?: 0
                if (minInt > 0) {
                    viewModel.addWorkout(date, minInt, notes.ifBlank { null })
                    minutes = ""
                    notes = ""
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Save Entry")
        }
    }
}
