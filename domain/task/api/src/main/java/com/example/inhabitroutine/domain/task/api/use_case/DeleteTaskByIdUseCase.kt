package com.example.inhabitroutine.domain.task.api.use_case

import com.example.inhabitroutine.core.util.ResultModel

interface DeleteTaskByIdUseCase {
    suspend operator fun invoke(taskId: String): ResultModel<Unit, Throwable>
}