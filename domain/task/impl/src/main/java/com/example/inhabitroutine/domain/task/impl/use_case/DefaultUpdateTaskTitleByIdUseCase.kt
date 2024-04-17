package com.example.inhabitroutine.domain.task.impl.use_case

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskTitleByIdUseCase

internal class DefaultUpdateTaskTitleByIdUseCase(
    private val taskRepository: TaskRepository
) : UpdateTaskTitleByIdUseCase {

    override suspend operator fun invoke(
        taskId: String,
        title: String
    ): ResultModel<Unit, Throwable> = taskRepository.updateTaskTitleById(
        taskId = taskId,
        title = title
    )

}