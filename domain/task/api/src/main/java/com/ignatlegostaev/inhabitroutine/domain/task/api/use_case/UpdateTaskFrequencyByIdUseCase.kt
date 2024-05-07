package com.ignatlegostaev.inhabitroutine.domain.task.api.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskFrequency

interface UpdateTaskFrequencyByIdUseCase {
    suspend operator fun invoke(
        taskId: String,
        taskFrequency: TaskFrequency
    ): ResultModel<Unit, Throwable>
}