package com.example.inhabitroutine.domain.model.task.content

import com.example.inhabitroutine.domain.model.task.type.ProgressLimitType
import kotlinx.datetime.LocalTime

sealed interface TaskProgress {
    data class Number(
        val limitType: ProgressLimitType,
        val limitNumber: Double,
        val limitUnit: String
    ) : TaskProgress

    data class Time(
        val limitType: ProgressLimitType,
        val limitTime: LocalTime
    ) : TaskProgress
}