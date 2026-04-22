package com.treadlog.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.treadlog.ui.AppState
import com.treadlog.ui.MainViewModel
import java.time.LocalDate

@Composable
fun MaintenanceScreen(state: AppState, viewModel: MainViewModel) {
    var oiledDate by remember { mutableStateOf(state.lastOiledDate ?: LocalDate.now().toString()) }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Maintenance", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Status", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Text("Due in ${state.maintenanceInterval} days cycle.")
        Text("Next Service: ${state.nextOilingDate ?: "Unknown"}", style = MaterialTheme.typography.headlineSmall)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("Update Oiled Date", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = oiledDate,
            onValueChange = { oiledDate = it },
            label = { Text("Date Oiled") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = { viewModel.setLastOiledDate(oiledDate) }) {
            Text("Save Oiled Date")
        }
    }
}
