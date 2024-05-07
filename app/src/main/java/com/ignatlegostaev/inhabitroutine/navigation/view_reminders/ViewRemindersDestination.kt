package com.ignatlegostaev.inhabitroutine.navigation.view_reminders

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.ViewRemindersConfig
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.ViewRemindersScreen
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenNavigation
import com.ignatlegostaev.inhabitroutine.navigation.AppNavDest
import com.ignatlegostaev.inhabitroutine.navigation.TargetNavDest
import com.ignatlegostaev.inhabitroutine.navigation.backwardPopExitTransition
import com.ignatlegostaev.inhabitroutine.navigation.forwardEnterTransition
import com.ignatlegostaev.inhabitroutine.presentation.base.BaseDestination
import com.ignatlegostaev.inhabitroutine.presentation.view_reminders.AndroidViewRemindersViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow

fun NavGraphBuilder.viewRemindersDestination(
    onNavigate: (TargetNavDest) -> Unit
) {
    composable(
        route = AppNavDest.ViewRemindersDestination.route,
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
            PermissionHandler(viewModel)
        }
    }
}

@Composable
private fun PermissionHandler(viewModel: AndroidViewRemindersViewModel) {
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { _ -> }
    LaunchedEffect(Unit) {
        viewModel.checkNotificationPermissionChannel.receiveAsFlow().collectLatest {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}