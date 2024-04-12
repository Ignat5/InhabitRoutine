package com.example.inhabitroutine.data.model.task.content

import kotlinx.datetime.DayOfWeek

sealed interface TaskFrequencyEntity {
    data object Day : TaskFrequencyEntity
    data object EveryDay : TaskFrequencyEntity
    data class DaysOfWeek(val daysOfWeek: Set<DayOfWeek>) : TaskFrequencyEntity
}