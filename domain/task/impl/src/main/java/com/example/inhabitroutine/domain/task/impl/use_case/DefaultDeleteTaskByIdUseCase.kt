package com.example.inhabitroutine.domain.task.impl.use_case

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.task.api.use_case.DeleteTaskByIdUseCase

internal class DefaultDeleteTaskByIdUseCase(
    private val taskRepository: TaskRepository
) : DeleteTaskByIdUseCase {

    override suspend operator fun invoke(taskId: String): ResultModel<Unit, Throwable> =
        taskRepository.deleteTaskById(taskId)

}