package com.example.inhabitroutine.navigation.edit_task

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation
import com.example.inhabitroutine.feature.create_edit_task.edit.EditTaskConfig
import com.example.inhabitroutine.feature.create_edit_task.edit.EditTaskScreen
import com.example.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenNavigation
import com.example.inhabitroutine.navigation.AppNavDest
import com.example.inhabitroutine.navigation.TargetNavDest
import com.example.inhabitroutine.presentation.base.BaseDestination
import com.example.inhabitroutine.presentation.edit_task.AndroidEditTaskViewModel

fun NavGraphBuilder.editTaskDestination(onNavigate: (TargetNavDest) -> Unit) {
    composable(
        route = AppNavDest.EditTaskDestination.route,
        arguments = listOf(AppNavDest.taskIdNavArg)
    ) {
        val viewModel: AndroidEditTaskViewModel = hiltViewModel()
        BaseDestination(
            viewModel = viewModel,
            onNavigation = { destination ->
                when (destination) {
                    is EditTaskScreenNavigation.Base -> {
                        when (val baseSN = destination.baseNavigation) {
                            is BaseCreateEditTaskScreenNavigation.ViewReminders -> {
                                onNavigate(
                                    TargetNavDest.Destination(
                                        route = AppNavDest.buildViewRemindersRoute(baseSN.taskId)
                                    )
                                )
                            }
                        }
                    }

                    is EditTaskScreenNavigation.Back -> {
                        onNavigate(TargetNavDest.Back)
                    }
                }
            },
            configContent = { config, onEvent ->
                EditTaskConfig(config, onEvent)
            },
            screenContent = { state, onEvent ->
                EditTaskScreen(state, onEvent)
            }
        )
    }
}