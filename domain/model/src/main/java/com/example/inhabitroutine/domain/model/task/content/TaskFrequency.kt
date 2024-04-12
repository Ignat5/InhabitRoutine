package com.example.inhabitroutine.domain.model.task.content

import kotlinx.datetime.DayOfWeek

sealed interface TaskFrequency {
    data object EveryDay : TaskFrequency
    data class DaysOfWeek(
        val daysOfWeek: Set<DayOfWeek>
    ) : TaskFrequency
}