package com.example.inhabitroutine.feature.view_task_statistics

import com.example.inhabitroutine.core.presentation.base.BaseViewModel
import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.calculate_statistics.CalculateTaskStatisticsUseCase
import com.example.inhabitroutine.domain.task.api.use_case.calculate_statistics.TaskCompletionCount
import com.example.inhabitroutine.domain.task.api.use_case.calculate_statistics.TaskStatisticsModel
import com.example.inhabitroutine.domain.task.api.use_case.calculate_statistics.TaskStreakModel
import com.example.inhabitroutine.feature.view_task_statistics.components.ViewTaskStatisticsScreenConfig
import com.example.inhabitroutine.feature.view_task_statistics.components.ViewTaskStatisticsScreenEvent
import com.example.inhabitroutine.feature.view_task_statistics.components.ViewTaskStatisticsScreenNavigation
import com.example.inhabitroutine.feature.view_task_statistics.components.ViewTaskStatisticsScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class ViewTaskStatisticsViewModel(
    taskId: String,
    private val readTaskByIdUseCase: ReadTaskByIdUseCase,
    private val calculateTaskStatisticsUseCase: CalculateTaskStatisticsUseCase,
    override val viewModelScope: CoroutineScope
) : BaseViewModel<ViewTaskStatisticsScreenEvent, ViewTaskStatisticsScreenState, ViewTaskStatisticsScreenNavigation, ViewTaskStatisticsScreenConfig>() {

    private val taskModelState = readTaskByIdUseCase(taskId)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    private val taskStatisticsState = flow<TaskStatisticsModel> {
        calculateTaskStatisticsUseCase(taskId).let { resultModel ->
            if (resultModel is ResultModel.Success) {
                emit(resultModel.value)
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        defaultTaskStatisticsModel
    )

    override val uiScreenState: StateFlow<ViewTaskStatisticsScreenState> =
        combine(
            taskModelState,
            taskStatisticsState
        ) { taskModel, taskStatistics ->
            ViewTaskStatisticsScreenState(
                taskModel = taskModel,
                taskStatisticsModel = taskStatistics
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ViewTaskStatisticsScreenState(
                taskModel = taskModelState.value,
                taskStatisticsModel = taskStatisticsState.value
            )
        )

    override fun onEvent(event: ViewTaskStatisticsScreenEvent) {
        when (event) {
            is ViewTaskStatisticsScreenEvent.OnLeaveRequest -> onLeaveRequest()
        }
    }

    private fun onLeaveRequest() {
        setUpNavigationState(ViewTaskStatisticsScreenNavigation.Back)
    }

    private val defaultTaskStatisticsModel: TaskStatisticsModel
        get() = TaskStatisticsModel(
            habitScore = 0f,
            streakModel = TaskStreakModel(currentStreak = 0, bestStreak = 0),
            completionCount = TaskCompletionCount(
                currentWeekCompletionCount = 0,
                currentMonthCompletionCount = 0,
                currentYearCompletionCount = 0,
                allTimeCompletionCount = 0
            ),
            statusCount = emptyMap(),
            statusMap = emptyMap()
        )

}