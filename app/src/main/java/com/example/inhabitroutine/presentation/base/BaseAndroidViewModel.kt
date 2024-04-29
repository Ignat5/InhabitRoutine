package com.example.inhabitroutine.presentation.base

import androidx.lifecycle.ViewModel
import com.example.inhabitroutine.core.presentation.base.BaseViewModel
import com.example.inhabitroutine.core.presentation.components.config.BaseScreenConfig
import com.example.inhabitroutine.core.presentation.components.config.ScreenConfig
import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.core.presentation.components.navigation.BaseScreenNavigation
import com.example.inhabitroutine.core.presentation.components.navigation.ScreenNavigation
import com.example.inhabitroutine.core.presentation.components.state.ScreenState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseAndroidViewModel<SE : ScreenEvent, SS : ScreenState, SN : ScreenNavigation, SC : ScreenConfig>(

) : ViewModel() {
    protected abstract val delegateViewModel: BaseViewModel<SE, SS, SN, SC>

    val uiScreenState: StateFlow<SS> get() = delegateViewModel.uiScreenState
    open fun onEvent(event: SE) = delegateViewModel.onEvent(event)

    val uiScreenConfigState: StateFlow<BaseScreenConfig<SC>>
        get() = delegateViewModel.uiScreenConfigState

    val uiScreenNavigationState: StateFlow<BaseScreenNavigation<SN>>
        get() = delegateViewModel.uiScreenNavigationState

    fun onNavigationHandled() = delegateViewModel.onNavigationHandled()

}