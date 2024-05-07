package com.ignatlegostaev.inhabitroutine.navigation.search_tasks

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.ignatlegostaev.inhabitroutine.feature.search_tasks.SearchTasksScreen
import com.ignatlegostaev.inhabitroutine.feature.search_tasks.components.SearchTasksScreenNavigation
import com.ignatlegostaev.inhabitroutine.navigation.AppNavDest
import com.ignatlegostaev.inhabitroutine.navigation.TargetNavDest
import com.ignatlegostaev.inhabitroutine.navigation.backwardExitTransition
import com.ignatlegostaev.inhabitroutine.navigation.backwardPopExitTransition
import com.ignatlegostaev.inhabitroutine.navigation.forwardEnterTransition
import com.ignatlegostaev.inhabitroutine.navigation.forwardPopEnterTransition
import com.ignatlegostaev.inhabitroutine.presentation.base.BaseDestination
import com.ignatlegostaev.inhabitroutine.presentation.search_tasks.AndroidSearchTasksViewModel

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