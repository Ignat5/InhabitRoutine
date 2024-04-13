package com.example.inhabitroutine.feature.view_schedule.vm

import com.example.inhabitroutine.core.presentation.base.BaseViewModel
import com.example.inhabitroutine.feature.view_schedule.vm.components.ViewScheduleScreenConfig
import com.example.inhabitroutine.feature.view_schedule.vm.components.ViewScheduleScreenEvent
import com.example.inhabitroutine.feature.view_schedule.vm.components.ViewScheduleScreenNavigation
import com.example.inhabitroutine.feature.view_schedule.vm.components.ViewScheduleScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ViewScheduleViewModel(
    viewModelScope: CoroutineScope
) : BaseViewModel<ViewScheduleScreenEvent, ViewScheduleScreenState, ViewScheduleScreenNavigation, ViewScheduleScreenConfig>(
    viewModelScope
) {

    override val uiScreenState: StateFlow<ViewScheduleScreenState> =
        MutableStateFlow(ViewScheduleScreenState(testString = "hash ov vm: ${this}"))

    override fun onEvent(event: ViewScheduleScreenEvent) {

    }

}