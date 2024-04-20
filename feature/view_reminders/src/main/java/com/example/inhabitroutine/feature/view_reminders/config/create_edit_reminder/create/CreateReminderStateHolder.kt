package com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create

import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.BaseCreateEditReminderStateHolder
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.components.CreateReminderScreenEvent
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.components.CreateReminderScreenResult
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.components.CreateReminderScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalTime

class CreateReminderStateHolder(
    holderScope: CoroutineScope
) : BaseCreateEditReminderStateHolder<CreateReminderScreenEvent, CreateReminderScreenState, CreateReminderScreenResult>(
    holderScope = holderScope
) {

    override val uiScreenState: StateFlow<CreateReminderScreenState> =
        combine(
            inputHoursState,
            inputMinutesState,
            inputReminderTypeState,
            inputReminderScheduleState,
            canConfirmState
        ) { inputHours, inputMinutes, inputReminderType, inputReminderSchedule, canConfirm ->
            CreateReminderScreenState(
                inputHours = inputHours,
                inputMinutes = inputMinutes,
                inputReminderType = inputReminderType,
                inputReminderSchedule = inputReminderSchedule,
                canConfirm = canConfirm
            )
        }.stateIn(
            holderScope,
            SharingStarted.Eagerly,
            CreateReminderScreenState(
                inputHours = inputHoursState.value,
                inputMinutes = inputMinutesState.value,
                inputReminderType = inputReminderTypeState.value,
                inputReminderSchedule = inputReminderScheduleState.value,
                canConfirm = canConfirmState.value
            )
        )

    override fun onEvent(event: CreateReminderScreenEvent) {
        when (event) {
            is CreateReminderScreenEvent.Base -> onBaseEvent(event.baseEvent)
        }
    }

    override fun onConfirm() {
        setUpResult(
            CreateReminderScreenResult.Confirm(
                reminderTime = LocalTime(
                    hour = inputHoursState.value,
                    minute = inputMinutesState.value
                ),
                reminderType = inputReminderTypeState.value,
                reminderSchedule = inputReminderScheduleState.value
            )
        )
    }

    override fun onDismiss() {
        setUpResult(CreateReminderScreenResult.Dismiss)
    }

}