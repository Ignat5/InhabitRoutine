package com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.domain.model.reminder.type.ReminderType
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.model.ReminderScheduleType
import kotlinx.datetime.DayOfWeek

sealed interface BaseCreateEditReminderScreenEvent : ScreenEvent {
    data class OnInputHoursUpdate(val hours: Int) : BaseCreateEditReminderScreenEvent
    data class OnInputMinutesUpdate(val minutes: Int) : BaseCreateEditReminderScreenEvent
    data class OnPickReminderType(val type: ReminderType) : BaseCreateEditReminderScreenEvent

    data class OnPickReminderScheduleType(
        val type: ReminderScheduleType
    ) : BaseCreateEditReminderScreenEvent

    data class OnDayOfWeekClick(
        val dayOfWeek: DayOfWeek
    ) : BaseCreateEditReminderScreenEvent

    data object OnConfirmClick : BaseCreateEditReminderScreenEvent
    data object OnDismissRequest : BaseCreateEditReminderScreenEvent
}