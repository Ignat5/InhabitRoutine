package com.example.inhabitroutine.navigation.view_schedule

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.inhabitroutine.feature.view_schedule.ViewScheduleScreen
import com.example.inhabitroutine.feature.view_schedule.ViewScheduleScreenConfig
import com.example.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenNavigation
import com.example.inhabitroutine.navigation.AppNavDest
import com.example.inhabitroutine.navigation.TargetNavDest
import com.example.inhabitroutine.navigation.backwardExitTransition
import com.example.inhabitroutine.navigation.forwardPopEnterTransition
import com.example.inhabitroutine.navigation.topDestinationEnterTransition
import com.example.inhabitroutine.navigation.topDestinationExitTransition
import com.example.inhabitroutine.presentation.base.BaseDestination
import com.example.inhabitroutine.presentation.view_schedule.AndroidViewScheduleViewModel

fun NavGraphBuilder.viewScheduleDestination(
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
    }
}