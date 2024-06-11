package com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case

import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskType
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTasksUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultReadTasksUseCase(
    private val taskRepository: TaskRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : ReadTasksUseCase {

    private val targetTaskTypes: Set<TaskType>
        get() = setOf(TaskType.RecurringTask, TaskType.SingleTask)

    override operator fun invoke(excludeDrafts: Boolean): Flow<List<TaskModel.Task>> =
        taskRepository.readTasksByTaskType(targetTaskTypes)
            .map { allTasks ->
                if (allTasks.isNotEmpty()) {
                    withContext(defaultDispatcher) {
                        allTasks
                            .excludeDraftsIfRequired(excludeDrafts)
                            .filterIsInstance<TaskModel.Task>()
                    }
                } else emptyList()
            }

    private fun List<TaskModel>.excludeDraftsIfRequired(exclude: Boolean): List<TaskModel> =
        this.let { allTasks ->
            if (exclude) allTasks.filterNot { it.isDraft }
            else allTasks
        }

}