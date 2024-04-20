package com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit

import com.example.inhabitroutine.domain.model.reminder.ReminderModel
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.BaseCreateEditReminderStateHolder
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit.components.EditReminderScreenEvent
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit.components.EditReminderScreenResult
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit.components.EditReminderScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalTime

class EditReminderStateHolder(
    private val reminderModel: ReminderModel,
    holderScope: CoroutineScope
) : BaseCreateEditReminderStateHolder<EditReminderScreenEvent, EditReminderScreenState, EditReminderScreenResult>(
    holderScope = holderScope,
    initReminderTime = reminderModel.time,
    initReminderType = reminderModel.type,
    initReminderSchedule = reminderModel.schedule
) {

    override val uiScreenState: StateFlow<EditReminderScreenState> =
        combine(
            inputHoursState,
            inputMinutesState,
            inputReminderTypeState,
            inputReminderScheduleState,
            canConfirmState
        ) { inputHours, inputMinutes, inputReminderType, inputReminderSchedule, canConfirm ->
            EditReminderScreenState(
                inputHours = inputHours,
                inputMinutes = inputMinutes,
                inputReminderType = inputReminderType,
                inputReminderSchedule = inputReminderSchedule,
                canConfirm = canConfirm
            )
        }.stateIn(
            holderScope,
            SharingStarted.Eagerly,
            EditReminderScreenState(
                inputHours = inputHoursState.value,
                inputMinutes = inputMinutesState.value,
                inputReminderType = inputReminderTypeState.value,
                inputReminderSchedule = inputReminderScheduleState.value,
                canConfirm = canConfirmState.value
            )
        )

    override fun onEvent(event: EditReminderScreenEvent) {
        when (event) {
            is EditReminderScreenEvent.Base -> onBaseEvent(event.baseEvent)
        }
    }

    override fun onConfirm() {
        setUpResult(
            EditReminderScreenResult.Confirm(
                reminderModel = reminderModel.copy(
                    time = LocalTime(
                        hour = inputHoursState.value,
                        minute = inputMinutesState.value
                    ),
                    type = inputReminderTypeState.value,
                    schedule = inputReminderScheduleState.value
                )
            )
        )
    }

    override fun onDismiss() {
        setUpResult(EditReminderScreenResult.Dismiss)
    }

}