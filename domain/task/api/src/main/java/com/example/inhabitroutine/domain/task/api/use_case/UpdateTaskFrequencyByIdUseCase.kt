package com.example.inhabitroutine.domain.task.api.use_case

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.domain.model.task.content.TaskFrequency

interface UpdateTaskFrequencyByIdUseCase {
    suspend operator fun invoke(
        taskId: String,
        taskFrequency: TaskFrequency
    ): ResultModel<Unit, Throwable>
}