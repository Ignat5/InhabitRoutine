package com.example.inhabitroutine.domain.task.api.use_case

import com.example.inhabitroutine.core.util.ResultModel

interface UpdateTaskDescriptionByIdUseCase {
    suspend operator fun invoke(
        taskId: String,
        description: String
    ): ResultModel<Unit, Throwable>
}