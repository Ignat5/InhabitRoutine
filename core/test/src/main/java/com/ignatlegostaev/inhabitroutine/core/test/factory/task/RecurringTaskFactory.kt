package com.ignatlegostaev.inhabitroutine.core.test.factory.task

import com.ignatlegostaev.inhabitroutine.core.test.factory.task.TaskAbstractFactory
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskDate
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskFrequency

class RecurringTaskFactory : TaskAbstractFactory() {
    override fun build(): TaskModel.Task.RecurringTask {
        return TaskModel.Task.RecurringTask(
            id = super.taskId,
            title = super.taskTitle,
            description = TaskAbstractFactory.TASK_DESCRIPTION,
            date = TaskDate.Period(super.startDate, null),
            priority = TaskAbstractFactory.TASK_PRIORITY,
            isArchived = TaskAbstractFactory.TASK_IS_ARCHIVED,
            versionStartDate = super.startDate,
            isDraft = TaskAbstractFactory.TASK_IS_DRAFT,
            createdAt = super.createdAt,
            frequency = TaskFrequency.EveryDay
        )
    }
}