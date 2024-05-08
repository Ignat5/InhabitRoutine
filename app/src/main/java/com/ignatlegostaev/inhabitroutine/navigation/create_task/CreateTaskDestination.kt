package com.ignatlegostaev.inhabitroutine.navigation.create_task

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.CreateTaskConfig
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.CreateTaskScreen
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenNavigation
import com.ignatlegostaev.inhabitroutine.navigation.AppNavDest
import com.ignatlegostaev.inhabitroutine.navigation.TargetNavDest
import com.ignatlegostaev.inhabitroutine.navigation.backwardExitTransition
import com.ignatlegostaev.inhabitroutine.navigation.backwardPopExitTransition
import com.ignatlegostaev.inhabitroutine.navigation.forwardEnterTransition
import com.ignatlegostaev.inhabitroutine.navigation.forwardPopEnterTransition
import com.ignatlegostaev.inhabitroutine.presentation.base.BaseDestination
import com.ignatlegostaev.inhabitroutine.presentation.create_task.AndroidCreateTaskViewModel

fun NavGraphBuilder.createTaskDestination(onNavigate: (TargetNavDest) -> Unit) {
    composable(
        route = AppNavDest.CreateTaskDestination.route,
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
        val viewModel: AndroidCreateTaskViewModel = hiltViewModel()
        BaseDestination(
            viewModel = viewModel,
            onNavigation = { destination ->
                when (destination) {
                    is CreateTaskScreenNavigation.Base -> {
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

                    is CreateTaskScreenNavigation.Back -> {
                        onNavigate(TargetNavDest.Back)
                    }
                }
            },
            configContent = { config, onEvent ->
                CreateTaskConfig(config, onEvent)
            },
            screenContent = { state, onEvent ->
                CreateTaskScreen(state, onEvent)
            }
        )
    }
}