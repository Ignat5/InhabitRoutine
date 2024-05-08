package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_time_progress

import com.ignatlegostaev.inhabitroutine.core.presentation.base.BaseResultStateHolder
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskProgress
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_time_progress.components.PickTaskTimeProgressScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_time_progress.components.PickTaskTimeProgressScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_time_progress.components.PickTaskTimeProgressScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalTime

class PickTaskTimeProgressStateHolder(
    initProgress: TaskProgress.Time,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<PickTaskTimeProgressScreenEvent, PickTaskTimeProgressScreenState, PickTaskTimeProgressScreenResult>() {

    private val inputLimitTypeState = MutableStateFlow(initProgress.limitType)
    private val inputHoursState = MutableStateFlow(initProgress.limitTime.hour)
    private val inputMinutesState = MutableStateFlow(initProgress.limitTime.minute)

    override val uiScreenState: StateFlow<PickTaskTimeProgressScreenState> =
        combine(
            inputLimitTypeState,
            inputHoursState,
            inputMinutesState
        ) { inputLimitType, inputHours, inputMinutes ->
            PickTaskTimeProgressScreenState(
                inputLimitType = inputLimitType,
                inputHours = inputHours,
                inputMinutes = inputMinutes
            )
        }.stateIn(
            holderScope,
            SharingStarted.Eagerly,
            PickTaskTimeProgressScreenState(
                inputLimitType = inputLimitTypeState.value,
                inputHours = inputHoursState.value,
                inputMinutes = inputMinutesState.value
            )
        )

    override fun onEvent(event: PickTaskTimeProgressScreenEvent) {
        when (event) {
            is PickTaskTimeProgressScreenEvent.OnPickLimitType ->
                onPickLimitType(event)

            is PickTaskTimeProgressScreenEvent.OnInputHoursUpdate ->
                onInputHoursUpdate(event)

            is PickTaskTimeProgressScreenEvent.OnInputMinutesUpdate ->
                onInputMinutesUpdate(event)

            is PickTaskTimeProgressScreenEvent.OnConfirmClick ->
                onConfirmClick()

            is PickTaskTimeProgressScreenEvent.OnDismissRequest ->
                onDismissRequest()
        }
    }

    private fun onPickLimitType(event: PickTaskTimeProgressScreenEvent.OnPickLimitType) {
        inputLimitTypeState.update { event.limitType }
    }

    private fun onInputHoursUpdate(event: PickTaskTimeProgressScreenEvent.OnInputHoursUpdate) {
        inputHoursState.update { event.hours }
    }

    private fun onInputMinutesUpdate(event: PickTaskTimeProgressScreenEvent.OnInputMinutesUpdate) {
        inputMinutesState.update { event.minutes }
    }

    private fun onConfirmClick() {
        setUpResult(
            PickTaskTimeProgressScreenResult.Confirm(
                taskProgress = TaskProgress.Time(
                    limitType = inputLimitTypeState.value,
                    limitTime = LocalTime(
                        hour = inputHoursState.value,
                        minute = inputMinutesState.value
                    )
                )
            )
        )
    }

    private fun onDismissRequest() {
        setUpResult(PickTaskTimeProgressScreenResult.Dismiss)
    }

}