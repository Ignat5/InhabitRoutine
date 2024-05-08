package com.ignatlegostaev.inhabitroutine.navigation.edit_task

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.EditTaskConfig
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.EditTaskScreen
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenNavigation
import com.ignatlegostaev.inhabitroutine.navigation.AppNavDest
import com.ignatlegostaev.inhabitroutine.navigation.TargetNavDest
import com.ignatlegostaev.inhabitroutine.navigation.backwardExitTransition
import com.ignatlegostaev.inhabitroutine.navigation.backwardPopExitTransition
import com.ignatlegostaev.inhabitroutine.navigation.forwardEnterTransition
import com.ignatlegostaev.inhabitroutine.navigation.forwardPopEnterTransition
import com.ignatlegostaev.inhabitroutine.presentation.base.BaseDestination
import com.ignatlegostaev.inhabitroutine.presentation.edit_task.AndroidEditTaskViewModel

fun NavGraphBuilder.editTaskDestination(onNavigate: (TargetNavDest) -> Unit) {
    composable(
        route = AppNavDest.EditTaskDestination.route,
        arguments = listOf(AppNavDest.taskIdNavArg),
        enterTransition = {
            forwardEnterTransition()
        },
        exitTransition = {
            backwardExitTransition()
        },
        popEnterTransition = {
            forwardPopEnterTransition()
        },
        popExitTransition = {
            backwardPopExitTransition()
        }
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

                    is EditTaskScreenNavigation.ViewTaskStatistics -> {
                        onNavigate(
                            TargetNavDest.Destination(
                                route = AppNavDest.buildViewTaskStatisticsRoute(destination.taskId)
                            )
                        )
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