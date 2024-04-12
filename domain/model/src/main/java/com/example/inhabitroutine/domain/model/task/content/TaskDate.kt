package com.example.inhabitroutine.domain.model.task.content

import kotlinx.datetime.LocalDate

sealed interface TaskDate {
    data class Day(val date: LocalDate) : TaskDate
    data class Period(
        val startDate: LocalDate,
        val endDate: LocalDate?
    ) : TaskDate
}