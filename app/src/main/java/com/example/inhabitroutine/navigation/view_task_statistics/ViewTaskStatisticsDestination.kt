package com.example.inhabitroutine.navigation.view_task_statistics

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.inhabitroutine.feature.view_task_statistics.ViewTaskStatisticsScreen
import com.example.inhabitroutine.feature.view_task_statistics.components.ViewTaskStatisticsScreenNavigation
import com.example.inhabitroutine.navigation.AppNavDest
import com.example.inhabitroutine.navigation.TargetNavDest
import com.example.inhabitroutine.presentation.base.BaseDestination
import com.example.inhabitroutine.presentation.view_task_statistics.AndroidViewTaskStatisticsViewModel

fun NavGraphBuilder.viewTaskStatisticsDestination(
    onNavigate: (TargetNavDest) -> Unit
) {
    composable(
        route = AppNavDest.ViewTaskStatisticsDestination.route,
        arguments = listOf(AppNavDest.taskIdNavArg)
    ) {
        val viewModel: AndroidViewTaskStatisticsViewModel = hiltViewModel()
        BaseDestination(
            viewModel = viewModel,
            onNavigation = { destination ->
                when (destination) {
                    is ViewTaskStatisticsScreenNavigation.Back -> {
                        onNavigate(TargetNavDest.Back)
                    }
                }
            },
            configContent = { _, _ -> },
            screenContent = { state, onEvent ->
                ViewTaskStatisticsScreen(state, onEvent)
            }
        )
    }
}