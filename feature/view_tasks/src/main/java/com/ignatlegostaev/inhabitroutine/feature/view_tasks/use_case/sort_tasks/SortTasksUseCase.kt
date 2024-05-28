package com.ignatlegostaev.inhabitroutine.feature.view_tasks.use_case.sort_tasks

import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.model.TaskSort

interface SortTasksUseCase {
    operator fun invoke(
        allTasks: List<TaskModel.Task>,
        taskSort: TaskSort
    ): List<TaskModel.Task>
}