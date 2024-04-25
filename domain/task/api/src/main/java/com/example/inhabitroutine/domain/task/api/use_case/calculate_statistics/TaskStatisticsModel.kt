package com.example.inhabitroutine.domain.task.api.use_case.calculate_statistics

import com.example.inhabitroutine.domain.model.derived.TaskStatus
import com.example.inhabitroutine.domain.model.derived.TaskWithRecordModel
import kotlinx.datetime.LocalDate

data class TaskStatisticsModel(
    val habitScore: Float,
    val streakModel: TaskStreakModel,
    val completionCount: TaskCompletionCount,
    val statusCount: Map<TaskStatus, Int>,
    val statusMap: Map<LocalDate, TaskWithRecordModel>
)

data class TaskStreakModel(
    val currentStreak: Int,
    val bestStreak: Int
)

data class TaskCompletionCount(
    val currentWeekCompletionCount: Int,
    val currentMonthCompletionCount: Int,
    val currentYearCompletionCount: Int,
    val allTimeCompletionCount: Int
)
