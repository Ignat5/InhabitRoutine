package com.example.inhabitroutine.navigation.view_habits

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.inhabitroutine.feature.view_habits.ViewHabitsConfig
import com.example.inhabitroutine.feature.view_habits.ViewHabitsScreen
import com.example.inhabitroutine.feature.view_habits.components.ViewHabitsScreenNavigation
import com.example.inhabitroutine.navigation.AppNavDest
import com.example.inhabitroutine.navigation.TargetNavDest
import com.example.inhabitroutine.navigation.topDestinationEnterTransition
import com.example.inhabitroutine.navigation.topDestinationExitTransition
import com.example.inhabitroutine.presentation.base.BaseDestination
import com.example.inhabitroutine.presentation.view_habits.AndroidViewHabitsViewModel

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
            topDestinationExitTransition()
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