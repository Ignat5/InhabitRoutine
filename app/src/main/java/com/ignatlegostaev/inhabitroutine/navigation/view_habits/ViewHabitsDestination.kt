package com.ignatlegostaev.inhabitroutine.navigation.view_habits

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ignatlegostaev.inhabitroutine.feature.view_habits.ViewHabitsConfig
import com.ignatlegostaev.inhabitroutine.feature.view_habits.ViewHabitsScreen
import com.ignatlegostaev.inhabitroutine.feature.view_habits.components.ViewHabitsScreenNavigation
import com.ignatlegostaev.inhabitroutine.navigation.AppNavDest
import com.ignatlegostaev.inhabitroutine.navigation.TargetNavDest
import com.ignatlegostaev.inhabitroutine.navigation.backwardExitTransition
import com.ignatlegostaev.inhabitroutine.navigation.forwardPopEnterTransition
import com.ignatlegostaev.inhabitroutine.navigation.topDestinationEnterTransition
import com.ignatlegostaev.inhabitroutine.navigation.topDestinationExitTransition
import com.ignatlegostaev.inhabitroutine.presentation.base.BaseDestination
import com.ignatlegostaev.inhabitroutine.presentation.view_habits.AndroidViewHabitsViewModel

fun NavGraphBuilder.viewHabitsDestination(
    onMenuClick: () -> Unit,
    onNavigate: (TargetNavDest) -> Unit
) {
    composable(
        route = AppNavDest.ViewHabitsDestination.route,
        enterTransition = {
            topDestinationEnterTransition()
        },
        exitTransition = {
            when (targetState.destination.route) {
                AppNavDest.ViewScheduleDestination.route,
                AppNavDest.ViewTasksDestination.route -> {
                    topDestinationExitTransition()
                }

                AppNavDest.EditTaskDestination.route -> {
                    backwardExitTransition()
                }

                AppNavDest.CreateTaskDestination.route -> {
                    backwardExitTransition()
                }

                AppNavDest.ViewTaskStatisticsDestination.route -> {
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

                AppNavDest.ViewTaskStatisticsDestination.route -> {
                    forwardPopEnterTransition()
                }

                AppNavDest.SearchTasksDestination.route -> {
                    forwardPopEnterTransition()
                }

                else -> EnterTransition.None
            }
        },
        popExitTransition = {
            ExitTransition.None
        }
    ) {
        val viewModel: AndroidViewHabitsViewModel = hiltViewModel()
        BaseDestination(
            viewModel = viewModel,
            onNavigation = { destination ->
                when (destination) {
                    is ViewHabitsScreenNavigation.EditTask -> {
                        onNavigate(
                            TargetNavDest.Destination(
                                route = AppNavDest.buildEditTaskRoute(destination.taskId)
                            )
                        )
                    }

                    is ViewHabitsScreenNavigation.ViewStatistics -> {
                        onNavigate(
                            TargetNavDest.Destination(
                                route = AppNavDest.buildViewTaskStatisticsRoute(destination.taskId)
                            )
                        )
                    }

                    is ViewHabitsScreenNavigation.CreateTask -> {
                        onNavigate(
                            TargetNavDest.Destination(
                                route = AppNavDest.buildCreateTaskRoute(destination.taskId)
                            )
                        )
                    }

                    is ViewHabitsScreenNavigation.SearchTasks -> {
                        onNavigate(
                            TargetNavDest.Destination(
                                route = AppNavDest.SearchTasksDestination.route
                            )
                        )
                    }
                }
            },
            configContent = { config, onEvent ->
                ViewHabitsConfig(config, onEvent)
            },
            screenContent = { state, onEvent ->
                ViewHabitsScreen(state, onEvent, onMenuClick)
            }
        )
    }
}