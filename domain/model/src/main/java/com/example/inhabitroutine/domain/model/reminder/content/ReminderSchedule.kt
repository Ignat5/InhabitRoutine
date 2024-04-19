package com.example.inhabitroutine.domain.model.reminder.content

import kotlinx.datetime.DayOfWeek

sealed interface ReminderSchedule {
    data object AlwaysEnabled : ReminderSchedule
    data class DaysOfWeek(
        val daysOfWeek: Set<DayOfWeek>
    ) : ReminderSchedule
}