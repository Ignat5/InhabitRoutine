package com.example.inhabitroutine.domain.task.api.use_case

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.domain.model.task.content.TaskDate

interface UpdateTaskDateByIdUseCase {
    suspend operator fun invoke(
        taskId: String,
        taskDate: TaskDate
    ): ResultModel<Unit, Throwable>
}