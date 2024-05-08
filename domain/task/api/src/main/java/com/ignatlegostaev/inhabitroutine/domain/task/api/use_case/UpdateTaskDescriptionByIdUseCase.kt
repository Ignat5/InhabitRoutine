package com.ignatlegostaev.inhabitroutine.domain.task.api.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel

interface UpdateTaskDescriptionByIdUseCase {
    suspend operator fun invoke(
        taskId: String,
        description: String
    ): ResultModel<Unit, Throwable>
}