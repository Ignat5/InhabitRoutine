package com.ignatlegostaev.inhabitroutine.navigation.view_schedule

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ignatlegostaev.inhabitroutine.core.presentation.model.UIResultModel
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.ViewScheduleScreen
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.ViewScheduleScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenNavigation
import com.ignatlegostaev.inhabitroutine.navigation.AppNavDest
import com.ignatlegostaev.inhabitroutine.navigation.TargetNavDest
import com.ignatlegostaev.inhabitroutine.navigation.backwardExitTransition
import com.ignatlegostaev.inhabitroutine.navigation.forwardPopEnterTransition
import com.ignatlegostaev.inhabitroutine.navigation.topDestinationEnterTransition
import com.ignatlegostaev.inhabitroutine.navigation.topDestinationExitTransition
import com.ignatlegostaev.inhabitroutine.presentation.base.BaseDestination
import com.ignatlegostaev.inhabitroutine.presentation.view_schedule.AndroidViewScheduleViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest

private const val READY_ADDITIONAL_DELAY_MILLIS = 200L

fun NavGraphBuilder.viewScheduleDestination(
    onReady: () -> Unit,
    onNavigate: (TargetNavDest) -> Unit,
    onMenuClick: () -> Unit
) {
    composable(
        route = AppNavDest.ViewScheduleDestination.route,
        enterTransition = {
            topDestinationEnterTransition()
        },
        exitTransition = {
            when (targetState.destination.route) {
                AppNavDest.ViewHabitsDestination.route,
                AppNavDest.ViewTasksDestination.route -> {
                    topDestinationExitTransition()
                }

                AppNavDest.EditTaskDestination.route -> {
                    backwardExitTransition()
                }

                AppNavDest.CreateTaskDestination.route -> {
                    backwardExitTransition()
                }

                AppNavDest.SearchTasksDestination.route -> {
                    backwardExitTransition()
                }

                else -> ExitTransition.None
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                AppNavDest.EditTaskDestination.route -> {
                    forwardPopEnterTransition()
                }

                AppNavDest.CreateTaskDestination.route -> {
                    forwardPopEnterTransition()
                }

                AppNavDest.SearchTasksDestination.route -> {
                    forwardPopEnterTransition()
                }

                else -> EnterTransition.None
            }
        },
        popExitTransition = { ExitTransition.None }
    ) {
        val viewModel: AndroidViewScheduleViewModel = hiltViewModel()
        BaseDestination(
            viewModel = viewModel,
            onNavigation = { destination ->
                when (destination) {
                    is ViewScheduleScreenNavigation.CreateTask -> {
                        onNavigate(
                            TargetNavDest.Destination(
                                route = AppNavDest.buildCreateTaskRoute(destination.taskId)
                            )
                        )
                    }

                    is ViewScheduleScreenNavigation.EditTask -> {
                        onNavigate(
                            TargetNavDest.Destination(
                                route = AppNavDest.buildEditTaskRoute(destination.taskId)
                            )
                        )
                    }

                    is ViewScheduleScreenNavigation.SearchTasks -> {
                        onNavigate(
                            TargetNavDest.Destination(
                                route = AppNavDest.SearchTasksDestination.route
                            )
                        )
                    }
                }
            },
            configContent = { config, onEvent ->
                ViewScheduleScreenConfig(config, onEvent)
            },
            screenContent = { state, onEvent ->
                ViewScheduleScreen(state, onEvent, onMenuClick)
            }
        )
        LaunchedEffect(Unit) {
            viewModel.uiScreenState.collectLatest { state ->
                if (state.allTasksResult is UIResultModel.Data) {
//                    delay(READY_ADDITIONAL_DELAY_MILLIS) // be sure that ui is ready as well
                    onReady()
                    this.cancel()
                }
            }
        }
    }
}