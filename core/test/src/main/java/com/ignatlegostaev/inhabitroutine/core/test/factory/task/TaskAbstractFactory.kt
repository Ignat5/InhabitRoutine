package com.ignatlegostaev.inhabitroutine.core.test.factory.task

import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.core.util.todayDate
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate

abstract class TaskAbstractFactory {
    protected val taskId get() = randomUUID()
    protected val taskTitle get() = "task with id $taskId"
    protected val startDate get() = Clock.System.todayDate
    protected val createdAt get() = Clock.System.now().toEpochMilliseconds()

    abstract fun build(): TaskModel

    companion object {
        const val TASK_DESCRIPTION = ""
        const val TASK_PRIORITY = 1L
        const val TASK_IS_ARCHIVED = false
        const val TASK_IS_DRAFT = false
    }
}