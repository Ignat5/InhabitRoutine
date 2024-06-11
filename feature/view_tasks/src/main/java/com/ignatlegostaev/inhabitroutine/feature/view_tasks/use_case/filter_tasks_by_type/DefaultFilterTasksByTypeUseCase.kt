package com.ignatlegostaev.inhabitroutine.feature.view_tasks.use_case.filter_tasks_by_type

import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskType
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.model.TaskFilterByType

internal class DefaultFilterTasksByTypeUseCase : FilterTasksByTypeUseCase {
    override operator fun invoke(
        allTasks: List<TaskModel.Task>,
        filterByType: TaskFilterByType
    ): List<TaskModel.Task> {
        return when (filterByType) {
            TaskFilterByType.OnlyRecurring -> allTasks.filter { it.type == TaskType.RecurringTask }
            TaskFilterByType.OnlySingle -> allTasks.filter { it.type == TaskType.SingleTask }
        }
    }
}