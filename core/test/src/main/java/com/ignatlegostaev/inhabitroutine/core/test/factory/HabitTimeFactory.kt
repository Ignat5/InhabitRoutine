package com.ignatlegostaev.inhabitroutine.core.test.factory

import com.ignatlegostaev.inhabitroutine.core.test.TestUtil
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskDate
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskFrequency
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskProgress
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.ProgressLimitType
import kotlinx.datetime.LocalTime
import kotlin.random.Random
import kotlin.random.nextInt

class HabitTimeFactory : TaskAbstractFactory() {

    override fun build(): TaskModel.Habit.HabitContinuous.HabitTime {
        return TaskModel.Habit.HabitContinuous.HabitTime(
            id = super.taskId,
            title = super.taskTitle,
            description = TaskAbstractFactory.TASK_DESCRIPTION,
            date = TaskDate.Period(super.startDate, null),
            priority = TaskAbstractFactory.TASK_PRIORITY,
            isArchived = TaskAbstractFactory.TASK_IS_ARCHIVED,
            versionStartDate = super.startDate,
            isDraft = TaskAbstractFactory.TASK_IS_DRAFT,
            createdAt = super.createdAt,
            frequency = TaskFrequency.EveryDay,
            progress = TestUtil.buildRandomTaskProgressTime()
        )
    }

}