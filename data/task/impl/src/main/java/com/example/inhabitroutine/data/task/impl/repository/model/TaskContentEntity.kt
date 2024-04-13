package com.example.inhabitroutine.data.task.impl.repository.model

import com.example.inhabitroutine.domain.model.task.type.ProgressLimitType
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("TaskContent")
sealed interface TaskContentEntity {
    @Serializable
    @SerialName("ProgressContent")
    sealed interface ProgressContent : TaskContentEntity {
        @Serializable
        @SerialName("ProgressContent.YesNo")
        data object YesNo : ProgressContent

        @Serializable
        @SerialName("ProgressContent.Number")
        data class Number(
            val limitType: ProgressLimitType,
            val limitNumber: Double,
            val limitUnit: String
        ) : ProgressContent

        @Serializable
        @SerialName("ProgressContent.Time")
        data class Time(
            val limitType: ProgressLimitType,
            val limitTime: LocalTime
        ) : ProgressContent
    }

    @Serializable
    @SerialName("FrequencyContent")
    sealed interface FrequencyContent : TaskContentEntity {
        @Serializable
        @SerialName("FrequencyContent.Day")
        data object Day : FrequencyContent

        @Serializable
        @SerialName("FrequencyContent.EveryDay")
        data object EveryDay : FrequencyContent

        @Serializable
        @SerialName("FrequencyContent.DaysOfWeek")
        data class DaysOfWeek(
            val daysOfWeek: Set<DayOfWeek>
        ) : FrequencyContent
    }

    @Serializable
    @SerialName("ArchiveContent")
    data class ArchiveContent(
        val isArchived: Boolean
    ) : TaskContentEntity
}