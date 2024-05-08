package com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskTitleByIdUseCase
import kotlinx.coroutines.flow.firstOrNull

internal class DefaultUpdateTaskTitleByIdUseCase(
    private val taskRepository: TaskRepository
) : UpdateTaskTitleByIdUseCase {

    override suspend operator fun invoke(
        taskId: String,
        title: String
    ): ResultModel<Unit, Throwable> {
        return taskRepository.readTaskById(taskId).firstOrNull()?.let { taskModel ->
            taskModel.copy(title = title).let { newTaskModel ->
                taskRepository.saveTask(newTaskModel)
            }
        } ?: ResultModel.failure(NoSuchElementException())
    }

}