package com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.calculate_statistics

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel

interface CalculateTaskStatisticsUseCase {
    suspend operator fun invoke(taskId: String): ResultModel<TaskStatisticsModel, Throwable>
}