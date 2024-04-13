package com.example.inhabitroutine.core.presentation.base

import com.example.inhabitroutine.core.presentation.components.config.BaseScreenConfig
import com.example.inhabitroutine.core.presentation.components.config.ScreenConfig
import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.core.presentation.components.navigation.BaseScreenNavigation
import com.example.inhabitroutine.core.presentation.components.navigation.ScreenNavigation
import com.example.inhabitroutine.core.presentation.components.state.ScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.job

abstract class BaseViewModel<SE : ScreenEvent, SS : ScreenState, SN : ScreenNavigation, SC : ScreenConfig>(
    protected val viewModelScope: CoroutineScope
) {

    /* base */
    abstract val uiScreenState: StateFlow<SS>
    abstract fun onEvent(event: SE)

    /* navigation */
    private val _uiScreenNavigationState =
        MutableStateFlow<BaseScreenNavigation<SN>>(BaseScreenNavigation.Idle)

    val uiScreenNavigationState: StateFlow<BaseScreenNavigation<SN>> =
        _uiScreenNavigationState.asStateFlow()

    protected fun setUpNavigationState(destination: SN) = _uiScreenNavigationState.update {
        BaseScreenNavigation.Destination(destination)
    }

    fun onNavigationHandled() = _uiScreenNavigationState.update { BaseScreenNavigation.Idle }

    /* config */
    private val _uiScreenConfigState =
        MutableStateFlow<BaseScreenConfig<SC>>(BaseScreenConfig.Idle)

    val uiScreenConfigState: StateFlow<BaseScreenConfig<SC>> =
        _uiScreenConfigState.asStateFlow()

    protected fun setUpConfigState(config: SC) =
        _uiScreenConfigState.update { BaseScreenConfig.Config(config) }

    protected fun onIdleToAction(action: () -> Unit) {
        resetConfigState()
        action()
    }

    protected fun resetConfigState() = _uiScreenConfigState.update { BaseScreenConfig.Idle }

    /* other */

    protected fun provideChildScope() =
        CoroutineScope(SupervisorJob(viewModelScope.coroutineContext.job))


}