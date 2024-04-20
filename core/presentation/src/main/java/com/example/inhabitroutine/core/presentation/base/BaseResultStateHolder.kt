package com.example.inhabitroutine.core.presentation.base

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.core.presentation.components.result.BaseScreenResult
import com.example.inhabitroutine.core.presentation.components.result.ScreenResult
import com.example.inhabitroutine.core.presentation.components.state.ScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseResultStateHolder<SE : ScreenEvent, SS : ScreenState, SR : ScreenResult> : BaseStateHolder<SE, SS>() {
    private val _uiScreenResult = MutableStateFlow<BaseScreenResult<SR>>(BaseScreenResult.Idle)
    val uiScreenResult: StateFlow<BaseScreenResult<SR>> = _uiScreenResult.asStateFlow()

    protected fun setUpResult(result: SR) = _uiScreenResult.update {
        BaseScreenResult.Result(result)
    }

    fun onResultHandled() = _uiScreenResult.update { BaseScreenResult.Idle }

}