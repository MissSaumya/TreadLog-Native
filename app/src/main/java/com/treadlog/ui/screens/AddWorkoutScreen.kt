package com.treadlog.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.treadlog.ui.MainViewModel
import java.time.LocalDate
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkoutScreen(viewModel: MainViewModel) {
    var minutes by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= System.currentTimeMillis()
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        viewModel.selectedAddDate = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate().toString()
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Text("Log Workout", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(32.dp))
            
            Box(modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }) {
                OutlinedTextField(
                    value = viewModel.selectedAddDate,
                    onValueChange = { },
                    label = { Text("Date (Tap to change)") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.primary.copy(alpha=0.5f),
                        disabledLabelColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            OutlinedTextField(
                value = minutes,
                onValueChange = { minutes = it },
                label = { Text("Duration (Minutes)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Button(
                onClick = { minutes = "30" },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Quick 30 Min Preset", color = MaterialTheme.colorScheme.onSecondary, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    val minInt = minutes.toIntOrNull() ?: 0
                    if (minInt > 0) {
                        viewModel.addWorkout(viewModel.selectedAddDate, minInt, notes.ifBlank { null })
                        minutes = ""
                        notes = ""
                    }
                },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Save Entry", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}
