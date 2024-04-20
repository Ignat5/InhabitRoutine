package com.example.inhabitroutine.domain.reminder.impl.use_case.local

import com.example.inhabitroutine.domain.model.reminder.ReminderModel
import com.example.inhabitroutine.domain.model.reminder.content.ReminderSchedule

internal class CheckReminderOverlapUseCase {

    operator fun invoke(
        targetReminder: ReminderModel,
        allReminders: List<ReminderModel>,
    ): Boolean = allReminders.any { nextReminder ->
        nextReminder.time == targetReminder.time &&
                nextReminder.schedule.checkOverlap(targetReminder.schedule)

    }

    private fun ReminderSchedule.checkOverlap(targetSchedule: ReminderSchedule): Boolean =
        this.let { sourceSchedule ->
            when (sourceSchedule) {
                is ReminderSchedule.AlwaysEnabled -> true
                is ReminderSchedule.DaysOfWeek -> {
                    when (targetSchedule) {
                        is ReminderSchedule.AlwaysEnabled -> true
                        is ReminderSchedule.DaysOfWeek -> sourceSchedule.daysOfWeek.any { it in targetSchedule.daysOfWeek }
                    }
                }
            }
        }

}