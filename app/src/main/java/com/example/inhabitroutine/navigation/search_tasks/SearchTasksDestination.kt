package com.example.inhabitroutine.navigation.search_tasks

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.example.inhabitroutine.feature.search_tasks.SearchTasksScreen
import com.example.inhabitroutine.feature.search_tasks.components.SearchTasksScreenNavigation
import com.example.inhabitroutine.navigation.AppNavDest
import com.example.inhabitroutine.navigation.TargetNavDest
import com.example.inhabitroutine.navigation.backwardExitTransition
import com.example.inhabitroutine.navigation.backwardPopExitTransition
import com.example.inhabitroutine.navigation.forwardEnterTransition
import com.example.inhabitroutine.navigation.forwardPopEnterTransition
import com.example.inhabitroutine.presentation.base.BaseDestination
import com.example.inhabitroutine.presentation.search_tasks.AndroidSearchTasksViewModel

fun NavGraphBuilder.searchTasksDestination(onNavigate: (TargetNavDest) -> Unit) {
    composable(
        route = AppNavDest.SearchTasksDestination.route,
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
        val viewModel: AndroidSearchTasksViewModel = hiltViewModel()
        BaseDestination(
            viewModel = viewModel,
            onNavigation = { destination ->
                when (destination) {
                    is SearchTasksScreenNavigation.EditTask -> {
                        onNavigate(TargetNavDest.Destination(
                            route = AppNavDest.buildEditTaskRoute(destination.taskId),
                            navOptions = navOptions {
                                this.popUpTo(route = AppNavDest.SearchTasksDestination.route) {
                                    this.inclusive = true
                                }
                            }
                        ))
                    }

                    is SearchTasksScreenNavigation.Back -> {
                        onNavigate(TargetNavDest.Back)
                    }
                }
            },
            configContent = { _, _ -> },
            screenContent = { state, onEvent ->
                SearchTasksScreen(state, onEvent)
            }
        )
    }
}