package com.ignatlegostaev.inhabitroutine.presentation.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ignatlegostaev.inhabitroutine.core.presentation.components.config.BaseScreenConfig
import com.ignatlegostaev.inhabitroutine.core.presentation.components.config.ScreenConfig
import com.ignatlegostaev.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.ignatlegostaev.inhabitroutine.core.presentation.components.navigation.BaseScreenNavigation
import com.ignatlegostaev.inhabitroutine.core.presentation.components.navigation.ScreenNavigation
import com.ignatlegostaev.inhabitroutine.core.presentation.components.state.ScreenState

@Composable
inline fun <SE : ScreenEvent, SS : ScreenState, SN : ScreenNavigation, SC : ScreenConfig> BaseDestination(
    viewModel: BaseAndroidViewModel<SE, SS, SN, SC>,
    crossinline onNavigation: (destination: SN) -> Unit,
    crossinline configContent: @Composable (config: SC, onEvent: (SE) -> Unit) -> Unit,
    crossinline screenContent: @Composable (state: SS, onEvent: (SE) -> Unit) -> Unit
) {
    val state by viewModel.uiScreenState.collectAsStateWithLifecycle()
    val baseSN by viewModel.uiScreenNavigationState.collectAsStateWithLifecycle()
    val baseSC by viewModel.uiScreenConfigState.collectAsStateWithLifecycle()
    val onEvent = remember(viewModel) {
        val callback: (event: SE) -> Unit = { event ->
            viewModel.onEvent(event)
        }
        callback
    }
    screenContent(state, onEvent)
    when (val sc = baseSC) {
        is BaseScreenConfig.Idle -> Unit
        is BaseScreenConfig.Config -> {
            configContent(sc.config, onEvent)
        }
    }
    LaunchedEffect(baseSN) {
        when (val sn = baseSN) {
            is BaseScreenNavigation.Idle -> Unit
            is BaseScreenNavigation.Destination -> {
                onNavigation(sn.destination)
                viewModel.onNavigationHandled()
            }
        }
    }
}