package com.example.inhabitroutine.navigation.root

import android.annotation.SuppressLint
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.inhabitroutine.navigation.AppNavDest
import com.example.inhabitroutine.navigation.TargetNavDest
import com.example.inhabitroutine.navigation.create_task.createTaskDestination
import com.example.inhabitroutine.navigation.edit_task.editTaskDestination
import com.example.inhabitroutine.navigation.search_tasks.searchTasksDestination
import com.example.inhabitroutine.navigation.view_reminders.viewRemindersDestination
import com.example.inhabitroutine.navigation.view_schedule.viewScheduleDestination
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RootGraph() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val onNavigate: (TargetNavDest) -> Unit = remember(navController) {
        val callback: (TargetNavDest) -> Unit = { targetNavDest ->
            when (targetNavDest) {
                is TargetNavDest.Destination -> {
                    navController.navigate(
                        route = targetNavDest.route,
                        navOptions = targetNavDest.navOptions
                    )
                }

                is TargetNavDest.Back -> {
                    navController.popBackStack()
                }
            }
        }
        callback
    }
    val onMenuClick: () -> Unit = remember(drawerState) {
        val callback: () -> Unit = {
            scope.launch {
                drawerState.open()
            }
        }
        callback
    }
    Scaffold { _ ->
        NavHost(
            navController = navController,
            startDestination = AppNavDest.ViewScheduleDestination.route
        ) {
            viewScheduleDestination(
                onNavigate = onNavigate,
                onMenuClick = onMenuClick
            )
            createTaskDestination(onNavigate)
            editTaskDestination(onNavigate)
            viewRemindersDestination(onNavigate)
            searchTasksDestination(onNavigate)
        }
    }
}