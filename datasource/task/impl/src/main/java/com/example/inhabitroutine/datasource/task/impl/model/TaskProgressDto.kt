package com.example.inhabitroutine.datasource.task.impl.model

import com.example.inhabitroutine.domain.model.task.type.ProgressLimitType
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("TaskProgress")
sealed interface TaskProgressDto {
    @Serializable
    @SerialName("TaskProgress.YesNo")
    data object YesNo : TaskProgressDto

    @Serializable
    @SerialName("TaskProgress.Number")
    data class Number(
        val limitType: ProgressLimitType,
        val limitNumber: Double,
        val limitUnit: String
    ) : TaskProgressDto

    @Serializable
    @SerialName("TaskProgress.Time")
    data class Time(
        val limitType: ProgressLimitType,
        val limitTime: LocalDate
    ) : TaskProgressDto
}