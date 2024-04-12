package com.example.inhabitroutine.datasource.task.impl.model

import kotlinx.datetime.DayOfWeek
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("TaskFrequency")
sealed interface TaskFrequencyDto {
    @Serializable
    @SerialName("TaskFrequency.Day")
    data object Day : TaskFrequencyDto

    @Serializable
    @SerialName("TaskFrequency.EveryDay")
    data object EveryDay : TaskFrequencyDto

    @Serializable
    @SerialName("TaskFrequency.DaysOfWeek")
    data class DaysOfWeek(
        val daysOfWeek: Set<DayOfWeek>
    ) : TaskFrequencyDto
}