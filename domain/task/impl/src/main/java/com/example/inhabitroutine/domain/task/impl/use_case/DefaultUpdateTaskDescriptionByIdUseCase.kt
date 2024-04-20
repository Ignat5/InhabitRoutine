package com.example.inhabitroutine.domain.task.impl.use_case

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskDescriptionByIdUseCase

internal class DefaultUpdateTaskDescriptionByIdUseCase(
    private val taskRepository: TaskRepository
) : UpdateTaskDescriptionByIdUseCase {

    override suspend operator fun invoke(
        taskId: String,
        description: String
    ): ResultModel<Unit, Throwable> = taskRepository.updateTaskDescriptionById(
        taskId = taskId,
        description = description
    )

}