package com.ignatlegostaev.inhabitroutine.feature.view_tasks.use_case.filter_tasks_by_type

import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.model.TaskFilterByType

interface FilterTasksByTypeUseCase {
    operator fun invoke(
        allTasks: List<TaskModel.Task>,
        filterByType: TaskFilterByType
    ): List<TaskModel.Task>
}