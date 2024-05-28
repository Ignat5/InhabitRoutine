package com.ignatlegostaev.inhabitroutine.feature.view_tasks.use_case.filter_tasks_by_status

import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.model.TaskFilterByStatus

interface FilterTasksByStatusUseCase {
    operator fun invoke(
        allTasks: List<TaskModel.Task>,
        filterByStatus: TaskFilterByStatus
    ): List<TaskModel.Task>
}