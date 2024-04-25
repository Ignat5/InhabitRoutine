package com.example.inhabitroutine.presentation.view_task_statistics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.calculate_statistics.CalculateTaskStatisticsUseCase
import com.example.inhabitroutine.feature.view_task_statistics.ViewTaskStatisticsViewModel
import com.example.inhabitroutine.feature.view_task_statistics.components.ViewTaskStatisticsScreenConfig
import com.example.inhabitroutine.feature.view_task_statistics.components.ViewTaskStatisticsScreenEvent
import com.example.inhabitroutine.feature.view_task_statistics.components.ViewTaskStatisticsScreenNavigation
import com.example.inhabitroutine.feature.view_task_statistics.components.ViewTaskStatisticsScreenState
import com.example.inhabitroutine.navigation.AppNavDest
import com.example.inhabitroutine.presentation.base.BaseAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidViewTaskStatisticsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    readTaskByIdUseCase: ReadTaskByIdUseCase,
    calculateTaskStatisticsUseCase: CalculateTaskStatisticsUseCase
) : BaseAndroidViewModel<ViewTaskStatisticsScreenEvent, ViewTaskStatisticsScreenState, ViewTaskStatisticsScreenNavigation, ViewTaskStatisticsScreenConfig>() {
    override val delegateViewModel: ViewTaskStatisticsViewModel = ViewTaskStatisticsViewModel(
        taskId = checkNotNull(savedStateHandle.get<String>(AppNavDest.TASK_ID_KEY)),
        readTaskByIdUseCase = readTaskByIdUseCase,
        calculateTaskStatisticsUseCase = calculateTaskStatisticsUseCase,
        viewModelScope = viewModelScope
    )
}