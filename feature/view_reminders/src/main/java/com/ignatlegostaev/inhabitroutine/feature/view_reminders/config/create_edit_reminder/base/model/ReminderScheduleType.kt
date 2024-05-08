package com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.model

import com.ignatlegostaev.inhabitroutine.domain.model.reminder.content.ReminderSchedule

enum class ReminderScheduleType {
    AlwaysEnabled,
    DaysOfWeek
}

internal val ReminderSchedule.type: ReminderScheduleType
    get() = when (this) {
        is ReminderSchedule.AlwaysEnabled -> ReminderScheduleType.AlwaysEnabled
        is ReminderSchedule.DaysOfWeek -> ReminderScheduleType.DaysOfWeek
    }