package com.example.inhabitroutine.presentation.base

import androidx.lifecycle.ViewModel
import com.example.inhabitroutine.core.presentation.base.BaseViewModel
import com.example.inhabitroutine.core.presentation.components.config.ScreenConfig
import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.core.presentation.components.navigation.ScreenNavigation
import com.example.inhabitroutine.core.presentation.components.state.ScreenState

abstract class BaseAndroidViewModel<SE : ScreenEvent, SS : ScreenState, SN : ScreenNavigation, SC : ScreenConfig>(

): ViewModel() {
    abstract val viewModel: BaseViewModel<SE, SS, SN, SC>
}