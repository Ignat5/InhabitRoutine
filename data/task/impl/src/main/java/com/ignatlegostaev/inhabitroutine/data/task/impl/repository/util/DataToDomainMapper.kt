package com.ignatlegostaev.inhabitroutine.data.task.impl.repository.util

import com.ignatlegostaev.inhabitroutine.data.task.impl.repository.model.task.TaskContentDataModel
import com.ignatlegostaev.inhabitroutine.data.task.impl.repository.model.task.TaskDataModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskDate
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskFrequency
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskProgress
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskProgressType
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskType

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
                        priority = this.priority,
                        frequency = this.frequency.toTaskFrequency() ?: return@runCatching null,
                        isArchived = this.isArchived,
                        versionStartDate = versionStartDate,
                        isDraft = this.isDraft,
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
                        priority = this.priority,
                        progress = this.progress.toTaskProgress() as? TaskProgress.Number
                            ?: return@runCatching null,
                        frequency = this.frequency.toTaskFrequency() ?: return@runCatching null,
                        isArchived = this.isArchived,
                        versionStartDate = versionStartDate,
                        isDraft = this.isDraft,
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
                        priority = this.priority,
                        progress = this.progress.toTaskProgress() as? TaskProgress.Time
                            ?: return@runCatching null,
                        frequency = this.frequency.toTaskFrequency() ?: return@runCatching null,
                        isArchived = this.isArchived,
                        versionStartDate = versionStartDate,
                        isDraft = this.isDraft,
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
                priority = this.priority,
                frequency = this.frequency.toTaskFrequency() ?: return@runCatching null,
                isArchived = this.isArchived,
                versionStartDate = versionStartDate,
                isDraft = this.isDraft,
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
                priority = this.priority,
                isArchived = this.isArchived,
                versionStartDate = versionStartDate,
                isDraft = this.isDraft,
                createdAt = this.createdAt
            )
        }
    }
}.getOrNull()

internal fun TaskModel.toTaskDataModel(): TaskDataModel {
    val startDate = when (this) {
        is TaskModel.Habit -> this.date.startDate
        is TaskModel.Task.RecurringTask -> this.date.startDate
        is TaskModel.Task.SingleTask -> this.date.date
    }
    val endDate = when (this) {
        is TaskModel.Habit -> this.date.endDate
        is TaskModel.Task.RecurringTask -> this.date.endDate
        is TaskModel.Task.SingleTask -> this.date.date
    }
    val progress = when (this) {
        is TaskModel.Habit.HabitContinuous -> this.progress.toTaskProgressContent()
        else -> TaskContentDataModel.ProgressContent.YesNo
    }
    val frequency = when (this) {
        is TaskModel.Habit -> this.frequency.toTaskFrequencyContent()
        is TaskModel.Task.RecurringTask -> this.frequency.toTaskFrequencyContent()
        is TaskModel.Task.SingleTask -> TaskContentDataModel.FrequencyContent.Day
    }
    return TaskDataModel(
        id = id,
        type = type,
        progressType = progressType,
        title = title,
        description = description,
        startDate = startDate,
        endDate = endDate,
        priority = this.priority,
        progress = progress,
        frequency = frequency,
        isArchived = isArchived,
        versionStartDate = versionStartDate,
        createdAt = createdAt,
        isDraft = isDraft
    )
}

/* progress */
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

private fun TaskProgress.toTaskProgressContent(): TaskContentDataModel.ProgressContent =
    when (this) {
        is TaskProgress.Number -> TaskContentDataModel.ProgressContent.Number(
            limitType = limitType,
            limitNumber = limitNumber,
            limitUnit = limitUnit
        )

        is TaskProgress.Time -> TaskContentDataModel.ProgressContent.Time(
            limitType = limitType,
            limitTime = limitTime
        )
    }

/* frequency */

private fun TaskContentDataModel.FrequencyContent.toTaskFrequency(): TaskFrequency? =
    when (this) {
        is TaskContentDataModel.FrequencyContent.EveryDay -> TaskFrequency.EveryDay
        is TaskContentDataModel.FrequencyContent.DaysOfWeek -> TaskFrequency.DaysOfWeek(this.daysOfWeek)
        is TaskContentDataModel.FrequencyContent.Day -> null
    }

private fun TaskFrequency.toTaskFrequencyContent(): TaskContentDataModel.FrequencyContent =
    when (this) {
        is TaskFrequency.EveryDay -> TaskContentDataModel.FrequencyContent.EveryDay
        is TaskFrequency.DaysOfWeek -> TaskContentDataModel.FrequencyContent.DaysOfWeek(
            this.daysOfWeek
        )
    }