package com.ignatlegostaev.inhabitroutine.domain.task.api.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel

interface DeleteTaskByIdUseCase {
    suspend operator fun invoke(taskId: String): ResultModel<Unit, Throwable>
}