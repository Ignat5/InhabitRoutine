package com.example.inhabitroutine.navigation.root

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.inhabitroutine.navigation.dest.AppNavDest
import com.example.inhabitroutine.navigation.view_schedule.viewScheduleScreen

@Composable
fun RootGraph() {
    val navController = rememberNavController()
    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppNavDest.ViewScheduleDestination.route
        ) {
            viewScheduleScreen()
        }
    }
}