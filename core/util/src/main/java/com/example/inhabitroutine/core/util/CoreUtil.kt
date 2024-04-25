package com.example.inhabitroutine.core.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

fun randomUUID(): String = UUID.randomUUID().toString()

val Clock.System.todayDate
    get() = this.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

val LocalDate.firstDayOfYear
    get() = this.let { sourceDate ->
        sourceDate.copy(dayOfMonth = 1, monthNumber = 1)
    }

val LocalDate.firstDayOfMonth
    get() = this.let { sourceDate ->
        sourceDate.copy(dayOfMonth = 1)
    }

val LocalDate.firstDayOfWeek
    get() = this.let { sourceDate ->
        sourceDate.minus(sourceDate.dayOfWeek.ordinal, DateTimeUnit.DAY)
    }

fun LocalDate.copy(
    year: Int? = null,
    monthNumber: Int? = null,
    dayOfMonth: Int? = null
) = this.let { sourceDate ->
    LocalDate(
        year = year ?: sourceDate.year,
        monthNumber = monthNumber ?: sourceDate.monthNumber,
        dayOfMonth = dayOfMonth ?: sourceDate.dayOfMonth
    )
}