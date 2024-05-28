package com.ignatlegostaev.inhabitroutine.feature.view_tasks.use_case.filter_tasks_by_status

import com.ignatlegostaev.inhabitroutine.core.util.todayDate
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskDate
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.model.TaskFilterByStatus
import kotlinx.datetime.Clock

internal class DefaultFilterTasksByStatusUseCase : FilterTasksByStatusUseCase {
    override operator fun invoke(
        allTasks: List<TaskModel.Task>,
        filterByStatus: TaskFilterByStatus
    ): List<TaskModel.Task> {
        return when (filterByStatus) {
            TaskFilterByStatus.OnlyActive -> {
                Clock.System.todayDate.let { todayDate ->
                    allTasks.filter { taskModel ->
                        if (!taskModel.isArchived) {
                            when (val taskDate = taskModel.date) {
                                is TaskDate.Period -> {
                                    taskDate.endDate?.let { endDate ->
                                        todayDate in taskDate.startDate..endDate
                                    } ?: (todayDate >= taskDate.startDate)
                                }

                                is TaskDate.Day -> taskDate.date >= todayDate
                            }
                        } else false
                    }
                }
            }

            TaskFilterByStatus.OnlyArchived -> allTasks.filter { it.isArchived }
        }
    }
}