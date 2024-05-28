package com.ignatlegostaev.inhabitroutine.feature.view_tasks.use_case.sort_tasks

import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskDate
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.model.TaskSort

internal class DefaultSortTasksUseCase : SortTasksUseCase {
    override operator fun invoke(
        allTasks: List<TaskModel.Task>,
        taskSort: TaskSort
    ): List<TaskModel.Task> {
        return when (taskSort) {
            TaskSort.ByPriority -> {
                allTasks.sortedByDescending { it.priority }
            }

            TaskSort.ByDate -> {
                allTasks.sortedByDescending { taskModel ->
                    when (val date = taskModel.date) {
                        is TaskDate.Period -> date.startDate
                        is TaskDate.Day -> date.date
                    }
                }
            }

            TaskSort.ByTitle -> {
                allTasks.sortedBy { it.title }
            }
        }
    }
}