package com.treadlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.treadlog.ui.MainViewModel
import com.treadlog.ui.theme.TreadLogTheme
import kotlinx.coroutines.launch
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Settings
import com.treadlog.ui.screens.DashboardScreen
import com.treadlog.ui.screens.AddWorkoutScreen
import com.treadlog.ui.screens.HistoryScreen
import com.treadlog.ui.screens.MaintenanceScreen

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TreadLogTheme {
                val viewModel: MainViewModel = viewModel()
                val state by viewModel.state.collectAsState()
                
                val tabs = listOf(
                    "Dashboard" to Icons.Rounded.Dashboard,
                    "Add" to Icons.Rounded.AddCircle,
                    "History" to Icons.Rounded.History,
                    "Maintenance" to Icons.Rounded.Settings
                )
                val pagerState = rememberPagerState(pageCount = { tabs.size })
                val coroutineScope = rememberCoroutineScope()

                Scaffold(
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface
                        ) {
                            tabs.forEachIndexed { index, (title, iconRes) ->
                                NavigationBarItem(
                                    selected = pagerState.currentPage == index,
                                    onClick = {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(index)
                                        }
                                    },
                                    icon = { Icon(iconRes, contentDescription = title) },
                                    label = { Text(title) },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                        selectedIconColor = MaterialTheme.colorScheme.primary,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.padding(innerPadding)
                    ) { page ->
                        when(page) {
                            0 -> DashboardScreen(state)
                            1 -> AddWorkoutScreen(viewModel)
                            2 -> HistoryScreen(state, viewModel)
                            3 -> MaintenanceScreen(state, viewModel)
                        }
                    }
                }
            }
        }
    }
}
