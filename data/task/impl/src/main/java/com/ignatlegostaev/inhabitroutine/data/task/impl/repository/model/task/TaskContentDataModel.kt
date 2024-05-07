package com.ignatlegostaev.inhabitroutine.data.task.impl.repository.model.task

import com.ignatlegostaev.inhabitroutine.domain.model.task.type.ProgressLimitType
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("TaskContent")
sealed interface TaskContentDataModel {
    @Serializable
    @SerialName("ProgressContent")
    sealed interface ProgressContent : TaskContentDataModel {
        @Serializable
        @SerialName("YesNo")
        data object YesNo : ProgressContent

        @Serializable
        @SerialName("Number")
        data class Number(
            val limitType: ProgressLimitType,
            val limitNumber: Double,
            val limitUnit: String
        ) : ProgressContent

        @Serializable
        @SerialName("Time")
        data class Time(
            val limitType: ProgressLimitType,
            val limitTime: LocalTime
        ) : ProgressContent
    }

    @Serializable
    @SerialName("FrequencyContent")
    sealed interface FrequencyContent : TaskContentDataModel {
        @Serializable
        @SerialName("Day")
        data object Day : FrequencyContent

        @Serializable
        @SerialName("EveryDay")
        data object EveryDay : FrequencyContent

        @Serializable
        @SerialName("DaysOfWeek")
        data class DaysOfWeek(
            val daysOfWeek: Set<DayOfWeek>
        ) : FrequencyContent
    }
}