package com.example.inhabitroutine.domain.task.api.use_case.calculate_statistics

import com.example.inhabitroutine.core.util.ResultModel

interface CalculateStatisticsUseCase {
    suspend operator fun invoke(taskId: String): ResultModel<TaskStatisticsModel, Throwable>
}