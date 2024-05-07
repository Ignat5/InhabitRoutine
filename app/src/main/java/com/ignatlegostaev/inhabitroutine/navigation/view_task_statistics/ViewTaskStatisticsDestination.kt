package com.ignatlegostaev.inhabitroutine.navigation.view_task_statistics

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ignatlegostaev.inhabitroutine.feature.view_task_statistics.ViewTaskStatisticsScreen
import com.ignatlegostaev.inhabitroutine.feature.view_task_statistics.components.ViewTaskStatisticsScreenNavigation
import com.ignatlegostaev.inhabitroutine.navigation.AppNavDest
import com.ignatlegostaev.inhabitroutine.navigation.TargetNavDest
import com.ignatlegostaev.inhabitroutine.navigation.backwardPopExitTransition
import com.ignatlegostaev.inhabitroutine.navigation.forwardEnterTransition
import com.ignatlegostaev.inhabitroutine.presentation.base.BaseDestination
import com.ignatlegostaev.inhabitroutine.presentation.view_task_statistics.AndroidViewTaskStatisticsViewModel

fun NavGraphBuilder.viewTaskStatisticsDestination(
    onNavigate: (TargetNavDest) -> Unit
) {
    composable(
        route = AppNavDest.ViewTaskStatisticsDestination.route,
        arguments = listOf(AppNavDest.taskIdNavArg),
        enterTransition = {
            forwardEnterTransition()
        },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = {
            backwardPopExitTransition()
        }
    ) {
        val viewModel: AndroidViewTaskStatisticsViewModel = hiltViewModel()
        BaseDestination(
            viewModel = viewModel,
            onNavigation = { destination ->
                when (destination) {
                    is ViewTaskStatisticsScreenNavigation.Back -> {
                        onNavigate(TargetNavDest.Back)
                    }
                }
            },
            configContent = { _, _ -> },
            screenContent = { state, onEvent ->
                ViewTaskStatisticsScreen(state, onEvent)
            }
        )
    }
}