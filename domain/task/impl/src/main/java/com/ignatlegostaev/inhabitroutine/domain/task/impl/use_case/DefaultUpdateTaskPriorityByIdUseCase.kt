package com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskPriorityByIdUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

internal class DefaultUpdateTaskPriorityByIdUseCase(
    private val taskRepository: TaskRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : UpdateTaskPriorityByIdUseCase {
    override suspend operator fun invoke(
        taskId: String,
        priority: Long
    ): ResultModel<Unit, Throwable> =
        withContext(defaultDispatcher) {
            taskRepository.readTaskById(taskId).firstOrNull()?.let { taskModel ->
                taskModel.copy(priority = priority).let { newTaskModel ->
                    taskRepository.saveTask(newTaskModel)
                }
            } ?: ResultModel.failure(NoSuchElementException())
        }

}