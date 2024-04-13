package com.example.inhabitroutine.data.task.impl.repository.model.reminder

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
        @SerialName("ReminderSchedule.EveryDay")
        data object EveryDay : ScheduleContent

        @Serializable
        @SerialName("ReminderSchedule.DaysOfWeek")
        data class DaysOfWeek(
            val daysOfWeek: Set<DayOfWeek>
        ) : ScheduleContent
    }
}