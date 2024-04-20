package com.example.inhabitroutine.domain.task.impl.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal val todayDate: LocalDate
    get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date