package com.ignatlegostaev.inhabitroutine.feature.view_schedule.util

import com.ignatlegostaev.inhabitroutine.domain.model.derived.TaskStatus
import com.ignatlegostaev.inhabitroutine.domain.model.derived.TaskWithExtrasAndRecordModel

internal class DefaultSortTasksUseCase : SortTasksUseCase {
    override operator fun invoke(allTasks: List<TaskWithExtrasAndRecordModel>): List<TaskWithExtrasAndRecordModel> =
        allTasks.sortedWith(
            compareBy<TaskWithExtrasAndRecordModel> { taskWithExtrasAndRecord ->
                when (taskWithExtrasAndRecord.status) {
                    is TaskStatus.NotCompleted.Pending -> Int.MIN_VALUE
                    else -> Int.MAX_VALUE
                }
            }
                .thenByDescending { taskWithExtrasAndRecord ->
                    taskWithExtrasAndRecord.task.priority
                }
                .thenBy { taskWithExtrasAndRecord ->
                    taskWithExtrasAndRecord.taskExtras.allReminders.minByOrNull { it.time }?.time?.toMillisecondOfDay()
                        ?: Int.MAX_VALUE
                }.thenBy { taskWithExtrasAndRecord ->
                    taskWithExtrasAndRecord.task.createdAt
                }
        )
}