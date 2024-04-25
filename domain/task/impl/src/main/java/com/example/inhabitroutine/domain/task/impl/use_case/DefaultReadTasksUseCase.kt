package com.example.inhabitroutine.domain.task.impl.use_case

import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.model.task.type.TaskType
import com.example.inhabitroutine.domain.task.api.use_case.ReadTasksUseCase
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

    override operator fun invoke(): Flow<List<TaskModel.Task>> =
        taskRepository.readTasksByTaskType(targetTaskTypes)
            .map { allTasks ->
                if (allTasks.isNotEmpty()) {
                    withContext(defaultDispatcher) {
                        allTasks
                            .filter { !it.isDraft }
                            .filterIsInstance<TaskModel.Task>()
                    }
                } else emptyList()
            }

}