package com.ignatlegostaev.inhabitroutine.domain.task.api.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel

interface UpdateTaskPriorityByIdUseCase {
    suspend operator fun invoke(taskId: String, priority: Long): ResultModel<Unit, Throwable>
}