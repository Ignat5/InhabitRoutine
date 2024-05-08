package com.ignatlegostaev.inhabitroutine.domain.task.api.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskProgress

interface UpdateTaskProgressByIdUseCase {
    suspend operator fun invoke(
        taskId: String,
        taskProgress: TaskProgress
    ): ResultModel<Unit, Throwable>
}