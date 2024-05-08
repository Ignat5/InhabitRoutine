package com.ignatlegostaev.inhabitroutine.core.presentation.base

import com.ignatlegostaev.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.ignatlegostaev.inhabitroutine.core.presentation.components.state.ScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

abstract class BaseStateHolder<SE: ScreenEvent, SS: ScreenState> {
    protected abstract val holderScope: CoroutineScope
    abstract val uiScreenState: StateFlow<SS>
    abstract fun onEvent(event: SE)
}