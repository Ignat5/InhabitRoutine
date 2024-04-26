package com.example.inhabitroutine.navigation.view_tasks

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.inhabitroutine.feature.view_tasks.ViewTasksConfig
import com.example.inhabitroutine.feature.view_tasks.ViewTasksScreen
import com.example.inhabitroutine.navigation.AppNavDest
import com.example.inhabitroutine.navigation.TargetNavDest
import com.example.inhabitroutine.presentation.base.BaseDestination
import com.example.inhabitroutine.presentation.view_tasks.AndroidViewTasksViewModel

fun NavGraphBuilder.viewTasksDestination(
    onNavigate: (TargetNavDest) -> Unit
) {
    composable(route = AppNavDest.ViewTasksDestination.route) {
        val viewModel: AndroidViewTasksViewModel = hiltViewModel()
        BaseDestination(
            viewModel = viewModel,
            onNavigation = {},
            configContent = { config, onEvent ->
                ViewTasksConfig(config, onEvent)
            },
            screenContent = { state, onEvent ->
                ViewTasksScreen(state, onEvent)
            }
        )
    }
}