package com.ignatlegostaev.inhabitroutine.core.test

import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskDate

class SingleTaskFactory : TaskAbstractFactory() {

    override fun build(): TaskModel.Task.SingleTask {
        return TaskModel.Task.SingleTask(
            id = super.taskId,
            title = super.taskTitle,
            description = TaskAbstractFactory.TASK_DESCRIPTION,
            date = TaskDate.Day(super.startDate),
            priority = TaskAbstractFactory.TASK_PRIORITY,
            isArchived = TaskAbstractFactory.TASK_IS_ARCHIVED,
            versionStartDate = super.startDate,
            isDraft = TaskAbstractFactory.TASK_IS_DRAFT,
            createdAt = super.createdAt
        )
    }

}