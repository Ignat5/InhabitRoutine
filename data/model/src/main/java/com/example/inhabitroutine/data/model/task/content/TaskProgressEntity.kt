package com.example.inhabitroutine.data.model.task.content

import com.example.inhabitroutine.domain.model.task.type.ProgressLimitType
import kotlinx.datetime.LocalTime

sealed interface TaskProgressEntity {
    data object YesNo : TaskProgressEntity

    data class Number(
        val limitType: ProgressLimitType,
        val limitNumber: Double,
        val limitUnit: String
    ) : TaskProgressEntity

    data class Time(
        val limitType: ProgressLimitType,
        val limitTime: LocalTime
    ) : TaskProgressEntity
}