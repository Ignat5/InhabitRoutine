package com.example.inhabitroutine.presentation.view_schedule

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import com.example.inhabitroutine.feature.view_schedule.vm.ViewScheduleViewModel
import com.example.inhabitroutine.feature.view_schedule.vm.components.ViewScheduleScreenConfig
import com.example.inhabitroutine.feature.view_schedule.vm.components.ViewScheduleScreenEvent
import com.example.inhabitroutine.feature.view_schedule.vm.components.ViewScheduleScreenNavigation
import com.example.inhabitroutine.feature.view_schedule.vm.components.ViewScheduleScreenState
import com.example.inhabitroutine.presentation.base.BaseAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidViewScheduleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val readTaskByIdUseCase: ReadTaskByIdUseCase
) : BaseAndroidViewModel<ViewScheduleScreenEvent, ViewScheduleScreenState, ViewScheduleScreenNavigation, ViewScheduleScreenConfig>() {

    override val delegateViewModel: ViewScheduleViewModel = ViewScheduleViewModel(
        readTaskByIdUseCase = readTaskByIdUseCase,
        viewModelScope = viewModelScope
    )

}