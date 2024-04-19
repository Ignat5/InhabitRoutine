package com.example.inhabitroutine.domain.task.impl.use_case

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.model.task.content.TaskDate
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskDateByIdUseCase

internal class DefaultUpdateTaskDateByIdUseCase(
    private val taskRepository: TaskRepository
) : UpdateTaskDateByIdUseCase {

    override suspend operator fun invoke(
        taskId: String,
        taskDate: TaskDate
    ): ResultModel<Unit, Throwable> = taskRepository.updateTaskDateById(
        taskId = taskId,
        taskDate = taskDate
    )

}