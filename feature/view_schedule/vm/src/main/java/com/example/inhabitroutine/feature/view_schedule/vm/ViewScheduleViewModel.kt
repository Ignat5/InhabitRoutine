package com.example.inhabitroutine.feature.view_schedule.vm

import com.example.inhabitroutine.core.presentation.base.BaseViewModel
import com.example.inhabitroutine.domain.model.task.type.TaskType
import com.example.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import com.example.inhabitroutine.feature.view_schedule.vm.components.ViewScheduleScreenConfig
import com.example.inhabitroutine.feature.view_schedule.vm.components.ViewScheduleScreenEvent
import com.example.inhabitroutine.feature.view_schedule.vm.components.ViewScheduleScreenNavigation
import com.example.inhabitroutine.feature.view_schedule.vm.components.ViewScheduleScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ViewScheduleViewModel(
    private val readTaskByIdUseCase: ReadTaskByIdUseCase,
    viewModelScope: CoroutineScope
) : BaseViewModel<ViewScheduleScreenEvent, ViewScheduleScreenState, ViewScheduleScreenNavigation, ViewScheduleScreenConfig>(
    viewModelScope
) {

    private val taskState = readTaskByIdUseCase("123")
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    override val uiScreenState: StateFlow<ViewScheduleScreenState> =
        MutableStateFlow(ViewScheduleScreenState(testString = "task: ${taskState.value?.date}"))

    override fun onEvent(event: ViewScheduleScreenEvent) {
        when (event) {
            is ViewScheduleScreenEvent.OnCreateTaskClick ->
                onCreateTaskClick()

            is ViewScheduleScreenEvent.PickTaskTypeResult ->
                onPickTaskTypeResult(event)
        }
    }

    private fun onPickTaskTypeResult(event: ViewScheduleScreenEvent.PickTaskTypeResult) {
        onIdleToAction {
            when (event) {
                is ViewScheduleScreenEvent.PickTaskTypeResult.Confirm -> { /* TODO */
                }

                is ViewScheduleScreenEvent.PickTaskTypeResult.Dismiss -> Unit
            }
        }
    }

    private fun onCreateTaskClick() {
        setUpConfigState(
            ViewScheduleScreenConfig.PickTaskType(TaskType.entries)
        )
    }

}