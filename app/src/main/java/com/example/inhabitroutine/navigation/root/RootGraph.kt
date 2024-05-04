package com.example.inhabitroutine.navigation.root

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.inhabitroutine.navigation.AppNavDest
import com.example.inhabitroutine.navigation.TargetNavDest
import com.example.inhabitroutine.navigation.create_task.createTaskDestination
import com.example.inhabitroutine.navigation.edit_task.editTaskDestination
import com.example.inhabitroutine.navigation.search_tasks.searchTasksDestination
import com.example.inhabitroutine.navigation.view_habits.viewHabitsDestination
import com.example.inhabitroutine.navigation.view_reminders.viewRemindersDestination
import com.example.inhabitroutine.navigation.view_schedule.viewScheduleDestination
import com.example.inhabitroutine.navigation.view_task_statistics.viewTaskStatisticsDestination
import com.example.inhabitroutine.navigation.view_tasks.viewTasksDestination
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RootGraph(onReady: () -> Unit = {}) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    RootModalNavigationDrawer(
        currentBackStackEntry = currentBackStackEntry,
        drawerState = drawerState,
        onRootDestinationClick = { rootDestination ->
            scope.launch {
                drawerState.close()
                if (rootDestination.destination.route != currentBackStackEntry?.destination?.route) {
                    navController.navigate(
                        route = rootDestination.destination.route,
                        navOptions = navOptions {
                            launchSingleTop = true
                            popUpTo(
                                route = AppNavDest.RootGraphDestination.route,
                                popUpToBuilder = {
                                    inclusive = true
                                }
                            )
                        }
                    )
                }
            }
        }
    ) {
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
        val onMenuClick: () -> Unit = remember {
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
                startDestination = AppNavDest.ViewScheduleDestination.route,
                route = AppNavDest.RootGraphDestination.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                viewScheduleDestination(
                    onReady = onReady,
                    onNavigate = onNavigate,
                    onMenuClick = onMenuClick
                )
                createTaskDestination(onNavigate)
                editTaskDestination(onNavigate)
                viewRemindersDestination(onNavigate)
                searchTasksDestination(onNavigate)
                viewTaskStatisticsDestination(onNavigate)
                viewTasksDestination(
                    onNavigate = onNavigate,
                    onMenuClick = onMenuClick
                )
                viewHabitsDestination(
                    onNavigate = onNavigate,
                    onMenuClick = onMenuClick
                )
            }
        }
    }
}