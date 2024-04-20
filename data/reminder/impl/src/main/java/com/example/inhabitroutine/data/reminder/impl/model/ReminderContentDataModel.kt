package com.example.inhabitroutine.data.reminder.impl.model

import kotlinx.datetime.DayOfWeek
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ReminderContent")
sealed interface ReminderContentDataModel {
    @Serializable
    @SerialName("ScheduleContent")
    sealed interface ScheduleContent : ReminderContentDataModel {
        @Serializable
        @SerialName("AlwaysEnabled")
        data object AlwaysEnabled : ScheduleContent

        @Serializable
        @SerialName("DaysOfWeek")
        data class DaysOfWeek(
            val daysOfWeek: Set<DayOfWeek>
        ) : ScheduleContent
    }
}