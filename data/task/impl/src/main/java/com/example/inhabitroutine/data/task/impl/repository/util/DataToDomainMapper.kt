package com.example.inhabitroutine.data.task.impl.repository.util

import com.example.inhabitroutine.data.task.impl.repository.model.task.TaskContentDataModel
import com.example.inhabitroutine.data.task.impl.repository.model.task.TaskDataModel
import com.example.inhabitroutine.data.task.impl.repository.model.reminder.ReminderContentDataModel
import com.example.inhabitroutine.data.task.impl.repository.model.reminder.ReminderDataModel
import com.example.inhabitroutine.domain.model.reminder.ReminderModel
import com.example.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.model.task.content.TaskDate
import com.example.inhabitroutine.domain.model.task.content.TaskFrequency
import com.example.inhabitroutine.domain.model.task.content.TaskProgress
import com.example.inhabitroutine.domain.model.task.type.TaskProgressType
import com.example.inhabitroutine.domain.model.task.type.TaskType

internal fun TaskDataModel.toTaskModel(): TaskModel? = runCatching {
    when (this.type) {
        TaskType.Habit -> {
            when (this.progressType) {
                TaskProgressType.YesNo -> {
                    TaskModel.Habit.HabitYesNo(
                        id = this.id,
                        title = this.title,
                        description = this.description,
                        date = TaskDate.Period(
                            startDate = this.startDate,
                            endDate = this.endDate
                        ),
                        frequency = this.frequency.toTaskFrequency() ?: return@runCatching null,
                        isArchived = this.archive.isArchived,
                        versionSinceDate = this.versionSinceDate,
                        reminder = this.reminder?.toReminderModel(),
                        isDeleted = this.deletedAt != null,
                        createdAt = this.createdAt
                    )
                }

                TaskProgressType.Number -> {
                    TaskModel.Habit.HabitContinuous.HabitNumber(
                        id = this.id,
                        title = this.title,
                        description = this.description,
                        date = TaskDate.Period(
                            startDate = this.startDate,
                            endDate = this.endDate
                        ),
                        progress = this.progress.toTaskProgress() as? TaskProgress.Number
                            ?: return@runCatching null,
                        frequency = this.frequency.toTaskFrequency() ?: return@runCatching null,
                        isArchived = this.archive.isArchived,
                        versionSinceDate = this.versionSinceDate,
                        reminder = this.reminder?.toReminderModel(),
                        isDeleted = this.deletedAt != null,
                        createdAt = this.createdAt
                    )
                }

                TaskProgressType.Time -> {
                    TaskModel.Habit.HabitContinuous.HabitTime(
                        id = this.id,
                        title = this.title,
                        description = this.description,
                        date = TaskDate.Period(
                            startDate = this.startDate,
                            endDate = this.endDate
                        ),
                        progress = this.progress.toTaskProgress() as? TaskProgress.Time
                            ?: return@runCatching null,
                        frequency = this.frequency.toTaskFrequency() ?: return@runCatching null,
                        isArchived = this.archive.isArchived,
                        versionSinceDate = this.versionSinceDate,
                        reminder = this.reminder?.toReminderModel(),
                        isDeleted = this.deletedAt != null,
                        createdAt = this.createdAt
                    )
                }
            }
        }

        TaskType.RecurringTask -> {
            TaskModel.Task.RecurringTask(
                id = this.id,
                title = this.title,
                description = this.description,
                date = TaskDate.Period(
                    startDate = this.startDate,
                    endDate = this.endDate
                ),
                frequency = this.frequency.toTaskFrequency() ?: return@runCatching null,
                isArchived = this.archive.isArchived,
                reminder = this.reminder?.toReminderModel(),
                isDeleted = this.deletedAt != null,
                createdAt = this.createdAt
            )
        }

        TaskType.SingleTask -> {
            TaskModel.Task.SingleTask(
                id = this.id,
                title = this.title,
                description = this.description,
                date = TaskDate.Day(
                    date = this.startDate,
                ),
                isArchived = this.archive.isArchived,
                reminder = this.reminder?.toReminderModel(),
                isDeleted = this.deletedAt != null,
                createdAt = this.createdAt
            )
        }
    }
}.getOrNull()

private fun ReminderDataModel.toReminderModel(): ReminderModel {
    return ReminderModel(
        id = this.id,
        taskId = this.taskId,
        time = this.time,
        type = this.reminderType,
        schedule = this.reminderSchedule.toReminderSchedule()
    )
}

private fun ReminderContentDataModel.ScheduleContent.toReminderSchedule() =
    when (this) {
        is ReminderContentDataModel.ScheduleContent.EveryDay -> ReminderSchedule.EveryDay
        is ReminderContentDataModel.ScheduleContent.DaysOfWeek -> ReminderSchedule.DaysOfWeek(this.daysOfWeek)
    }

private fun TaskContentDataModel.ProgressContent.toTaskProgress(): TaskProgress? =
    when (this) {
        is TaskContentDataModel.ProgressContent.Number -> TaskProgress.Number(
            limitType = this.limitType,
            limitNumber = this.limitNumber,
            limitUnit = this.limitUnit
        )

        is TaskContentDataModel.ProgressContent.Time -> TaskProgress.Time(
            limitType = this.limitType,
            limitTime = this.limitTime
        )

        is TaskContentDataModel.ProgressContent.YesNo -> null
    }

private fun TaskContentDataModel.FrequencyContent.toTaskFrequency(): TaskFrequency? =
    when (this) {
        is TaskContentDataModel.FrequencyContent.EveryDay -> TaskFrequency.EveryDay
        is TaskContentDataModel.FrequencyContent.DaysOfWeek -> TaskFrequency.DaysOfWeek(this.daysOfWeek)
        is TaskContentDataModel.FrequencyContent.Day -> null
    }