package com.ignatlegostaev.inhabitroutine.domain.reminder.impl.util

import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.content.ReminderSchedule

internal fun ReminderModel.checkOverlap(
    allReminders: List<ReminderModel>,
): Boolean = this.let { targetReminder ->
    allReminders.any { nextReminder ->
        nextReminder.time == targetReminder.time &&
                nextReminder.schedule.checkOverlap(targetReminder.schedule)

    }
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

//internal fun ReminderSchedule.checkIfMatches(date: LocalDate) =
//    this.let { reminderSchedule ->
//        when (reminderSchedule) {
//            is ReminderSchedule.AlwaysEnabled -> true
//            is ReminderSchedule.DaysOfWeek -> {
//                date.dayOfWeek in reminderSchedule.daysOfWeek
//            }
//        }
//    }
//
//internal fun TaskModel.checkIfCanSetReminder() = this.let { taskModel ->
//    !taskModel.isDraft && !taskModel.isArchived
//}
//
//internal fun ReminderModel.checkIfCanSetReminder() = this.let { reminderModel ->
//    reminderModel.type in setOf(ReminderType.Notification)
//}
//
//internal fun checkIfCanSetReminderForDate(
//    taskModel: TaskModel,
//    reminderModel: ReminderModel,
//    targetDate: LocalDate
//): Boolean {
//    if (taskModel.checkIfCanSetReminder() && reminderModel.checkIfCanSetReminder()) {
//        val inDateRange = taskModel.date.checkIfMatches(targetDate)
//        val isTaskScheduled = if (taskModel is TaskModel.RecurringActivity) {
//            taskModel.frequency.checkIfMatches(targetDate)
//        } else true
//        val isReminderScheduled = reminderModel.schedule.checkIfMatches(targetDate)
//        return inDateRange && isTaskScheduled && isReminderScheduled
//    }
//    return false
//}