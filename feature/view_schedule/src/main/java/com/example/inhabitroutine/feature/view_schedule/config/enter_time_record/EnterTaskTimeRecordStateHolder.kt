package com.example.inhabitroutine.feature.view_schedule.config.enter_time_record

import com.example.inhabitroutine.core.presentation.base.BaseResultStateHolder
import com.example.inhabitroutine.domain.model.record.content.RecordEntry
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.feature.view_schedule.config.enter_time_record.components.EnterTaskTimeRecordScreenEvent
import com.example.inhabitroutine.feature.view_schedule.config.enter_time_record.components.EnterTaskTimeRecordScreenResult
import com.example.inhabitroutine.feature.view_schedule.config.enter_time_record.components.EnterTaskTimeRecordScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class EnterTaskTimeRecordStateHolder(
    private val taskModel: TaskModel.Habit.HabitContinuous.HabitTime,
    entry: RecordEntry.HabitEntry.Continuous.Time?,
    private val date: LocalDate,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<EnterTaskTimeRecordScreenEvent, EnterTaskTimeRecordScreenState, EnterTaskTimeRecordScreenResult>() {

    private val inputHoursState =
        MutableStateFlow((entry as? RecordEntry.Time)?.time?.hour ?: DEFAULT_HOURS)

    private val inputMinutesState =
        MutableStateFlow((entry as? RecordEntry.Time)?.time?.minute ?: DEFAULT_MINUTE)

    override val uiScreenState: StateFlow<EnterTaskTimeRecordScreenState> =
        combine(
            inputHoursState,
            inputMinutesState
        ) { inputHours, inputMinutes ->
            EnterTaskTimeRecordScreenState(
                taskModel = taskModel,
                inputHours = inputHours,
                inputMinutes = inputMinutes,
                date = date
            )
        }.stateIn(
            holderScope,
            SharingStarted.Eagerly,
            EnterTaskTimeRecordScreenState(
                taskModel = taskModel,
                inputHours = inputHoursState.value,
                inputMinutes = inputMinutesState.value,
                date = date
            )
        )

    override fun onEvent(event: EnterTaskTimeRecordScreenEvent) {
        when (event) {
            is EnterTaskTimeRecordScreenEvent.OnInputHoursUpdate ->
                onInputHoursUpdate(event)

            is EnterTaskTimeRecordScreenEvent.OnInputMinutesUpdate ->
                onInputMinutesUpdate(event)

            is EnterTaskTimeRecordScreenEvent.OnConfirmClick ->
                onConfirmClick()

            is EnterTaskTimeRecordScreenEvent.OnDismissRequest ->
                onDismissRequest()
        }
    }

    private fun onInputHoursUpdate(event: EnterTaskTimeRecordScreenEvent.OnInputHoursUpdate) {
        inputHoursState.update { event.value }
    }

    private fun onInputMinutesUpdate(event: EnterTaskTimeRecordScreenEvent.OnInputMinutesUpdate) {
        inputMinutesState.update { event.value }
    }

    private fun onConfirmClick() {
        setUpResult(
            EnterTaskTimeRecordScreenResult.Confirm(
                taskId = taskModel.id,
                date = date,
                time = LocalTime(hour = inputHoursState.value, minute = inputMinutesState.value)
            )
        )
    }

    private fun onDismissRequest() {
        setUpResult(EnterTaskTimeRecordScreenResult.Dismiss)
    }

    companion object {
        private const val DEFAULT_HOURS = 0
        private const val DEFAULT_MINUTE = 0
    }

}