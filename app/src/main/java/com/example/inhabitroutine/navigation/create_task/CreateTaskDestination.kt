package com.example.inhabitroutine.navigation.create_task

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.inhabitroutine.feature.create_edit_task.create.CreateTaskConfig
import com.example.inhabitroutine.feature.create_edit_task.create.CreateTaskScreen
import com.example.inhabitroutine.navigation.AppNavDest
import com.example.inhabitroutine.navigation.TargetNavDest
import com.example.inhabitroutine.presentation.base.BaseDestination
import com.example.inhabitroutine.presentation.create_task.AndroidCreateTaskViewModel

fun NavGraphBuilder.createTaskDestination(onNavigate: (TargetNavDest) -> Unit) {
    composable(
        route = AppNavDest.CreateTaskDestination.route,
        arguments = listOf(AppNavDest.taskIdNavArg)
    ) {
        val viewModel: AndroidCreateTaskViewModel = hiltViewModel()
        BaseDestination(
            viewModel = viewModel,
            onNavigation = {},
            configContent = { config, onEvent ->
                CreateTaskConfig(config, onEvent)
            },
            screenContent = { state, onEvent ->
                CreateTaskScreen(state, onEvent)
            }
        )
    }
}