package com.example.inhabitroutine.domain.task.api.use_case

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.domain.model.task.content.TaskProgress

interface UpdateTaskProgressByIdUseCase {
    suspend operator fun invoke(
        taskId: String,
        taskProgress: TaskProgress
    ): ResultModel<Unit, Throwable>
}