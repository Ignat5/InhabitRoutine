package com.ignatlegostaev.inhabitroutine.domain.task.api.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel

interface UpdateTaskTitleByIdUseCase {
    suspend operator fun invoke(
        taskId: String,
        title: String
    ): ResultModel<Unit, Throwable>
}