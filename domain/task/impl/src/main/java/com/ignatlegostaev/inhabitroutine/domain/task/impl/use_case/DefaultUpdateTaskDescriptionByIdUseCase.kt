package com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskDescriptionByIdUseCase
import kotlinx.coroutines.flow.firstOrNull

internal class DefaultUpdateTaskDescriptionByIdUseCase(
    private val taskRepository: TaskRepository
) : UpdateTaskDescriptionByIdUseCase {

    override suspend operator fun invoke(
        taskId: String,
        description: String
    ): ResultModel<Unit, Throwable> {
        return taskRepository.readTaskById(taskId).firstOrNull()?.let { taskModel ->
            taskModel.copy(description = description).let { newTaskModel ->
                taskRepository.saveTask(newTaskModel)
            }
        } ?: ResultModel.failure(NoSuchElementException())
    }

}