package com.example.inhabitroutine.navigation.view_reminders

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.inhabitroutine.feature.view_reminders.ViewRemindersConfig
import com.example.inhabitroutine.feature.view_reminders.ViewRemindersScreen
import com.example.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenNavigation
import com.example.inhabitroutine.navigation.AppNavDest
import com.example.inhabitroutine.navigation.TargetNavDest
import com.example.inhabitroutine.presentation.base.BaseDestination
import com.example.inhabitroutine.presentation.view_reminders.AndroidViewRemindersViewModel

fun NavGraphBuilder.viewRemindersDestination(
    onNavigate: (TargetNavDest) -> Unit
) {
    composable(
        route = AppNavDest.ViewRemindersDestination.route,
        arguments = listOf(AppNavDest.taskIdNavArg)
    ) {
        val viewModel: AndroidViewRemindersViewModel = hiltViewModel()
        BaseDestination(
            viewModel = viewModel,
            onNavigation = { destination ->
                when (destination) {
                    is ViewRemindersScreenNavigation.Back -> {
                        onNavigate(TargetNavDest.Back)
                    }
                }
            },
            configContent = { config, onEvent ->
                ViewRemindersConfig(config, onEvent)
            }
        ) { state, onEvent ->
            ViewRemindersScreen(state, onEvent)
        }
    }
}