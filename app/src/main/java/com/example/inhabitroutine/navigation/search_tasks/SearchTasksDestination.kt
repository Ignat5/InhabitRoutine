package com.example.inhabitroutine.navigation.search_tasks

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.inhabitroutine.feature.search_tasks.SearchTasksScreen
import com.example.inhabitroutine.feature.search_tasks.components.SearchTasksScreenNavigation
import com.example.inhabitroutine.navigation.AppNavDest
import com.example.inhabitroutine.navigation.TargetNavDest
import com.example.inhabitroutine.presentation.base.BaseDestination
import com.example.inhabitroutine.presentation.search_tasks.AndroidSearchTasksViewModel

fun NavGraphBuilder.searchTasksDestination(onNavigate: (TargetNavDest) -> Unit) {
    composable(
        route = AppNavDest.SearchTasksDestination.route
    ) {
        val viewModel: AndroidSearchTasksViewModel = hiltViewModel()
        BaseDestination(
            viewModel = viewModel,
            onNavigation = { destination ->
                when (destination) {
                    is SearchTasksScreenNavigation.EditTask -> {

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