package com.example.inhabitroutine.core.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.inhabitroutine.core.presentation.base.BaseResultStateHolder
import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.core.presentation.components.result.BaseScreenResult
import com.example.inhabitroutine.core.presentation.components.result.ScreenResult
import com.example.inhabitroutine.core.presentation.components.state.ScreenState

@Composable
inline fun <SE : ScreenEvent, SS : ScreenState, SR : ScreenResult> BaseDialogWithResult(
    stateHolder: BaseResultStateHolder<SE, SS, SR>,
    crossinline onResult: (result: SR) -> Unit,
    crossinline content: @Composable (state: SS, onEvent: (SE) -> Unit) -> Unit
) {
    val state by stateHolder.uiScreenState.collectAsStateWithLifecycle()
    val baseSR by stateHolder.uiScreenResult.collectAsStateWithLifecycle()
    val onEvent = remember(stateHolder) {
        val callback: (event: SE) -> Unit = { event ->
            stateHolder.onEvent(event)
        }
        callback
    }
    content(state, onEvent)
    LaunchedEffect(baseSR) {
        when (val sr = baseSR) {
            is BaseScreenResult.Idle -> Unit
            is BaseScreenResult.Result -> {
                onResult(sr.result)
                stateHolder.onResultHandled()
            }
        }
    }
}