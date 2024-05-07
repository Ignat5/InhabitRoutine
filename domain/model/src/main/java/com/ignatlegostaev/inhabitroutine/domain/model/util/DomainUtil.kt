package com.ignatlegostaev.inhabitroutine.domain.model.util

import com.ignatlegostaev.inhabitroutine.domain.model.record.content.RecordEntry
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.type.ReminderType
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskDate
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskFrequency
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskProgress
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.ProgressLimitType
import kotlinx.datetime.LocalDate

fun TaskFrequency.checkIfMatches(date: LocalDate) = this.let { taskFrequency ->
    when (taskFrequency) {
        is TaskFrequency.EveryDay -> true
        is TaskFrequency.DaysOfWeek -> {
            date.dayOfWeek in taskFrequency.daysOfWeek
        }
    }
}

fun TaskDate.checkIfMatches(date: LocalDate): Boolean = this.let { taskDate ->
    when (taskDate) {
        is TaskDate.Period -> {
            taskDate.endDate?.let { endDate ->
                date in taskDate.startDate..endDate
            } ?: run { date >= taskDate.startDate }
        }

        is TaskDate.Day -> taskDate.date == date
    }
}

fun TaskProgress.Number.checkIfCompleted(entry: RecordEntry.Number): Boolean =
    this.let { progressNumber ->
        when (progressNumber.limitType) {
            ProgressLimitType.AtLeast -> {
                entry.number >= progressNumber.limitNumber
            }

            ProgressLimitType.Exactly -> {
                entry.number == progressNumber.limitNumber
            }

            ProgressLimitType.NoMoreThan -> {
                entry.number <= progressNumber.limitNumber
            }
        }
    }

fun TaskProgress.Time.checkIfCompleted(entry: RecordEntry.Time): Boolean =
    this.let { progressNumber ->
        when (progressNumber.limitType) {
            ProgressLimitType.AtLeast -> {
                entry.time >= progressNumber.limitTime
            }

            ProgressLimitType.Exactly -> {
                entry.time == progressNumber.limitTime
            }

            ProgressLimitType.NoMoreThan -> {
                entry.time <= progressNumber.limitTime
            }
        }
    }

fun ReminderSchedule.checkIfMatches(date: LocalDate) =
    this.let { reminderSchedule ->
        when (reminderSchedule) {
            is ReminderSchedule.AlwaysEnabled -> true
            is ReminderSchedule.DaysOfWeek -> {
                date.dayOfWeek in reminderSchedule.daysOfWeek
            }
        }
    }

fun ReminderModel.checkIfCanSetReminderForDate(
    taskModel: TaskModel,
    targetDate: LocalDate
): Boolean {
    this.let { reminderModel ->
        if (taskModel.checkIfCanSetReminder() && reminderModel.checkIfCanSetReminder()) {
            val inDateRange = taskModel.date.checkIfMatches(targetDate)
            val isTaskScheduled = if (taskModel is TaskModel.RecurringActivity) {
                taskModel.frequency.checkIfMatches(targetDate)
            } else true
            val isReminderScheduled = reminderModel.schedule.checkIfMatches(targetDate)
            return inDateRange && isTaskScheduled && isReminderScheduled
        }
        return false
    }
}

fun TaskModel.checkIfCanSetReminder() = this.let { taskModel ->
    !taskModel.isDraft && !taskModel.isArchived
}

fun ReminderModel.checkIfCanSetReminder() = this.let { reminderModel ->
    reminderModel.type in ReminderType.allNotifiableTypes
}