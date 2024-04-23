package com.example.inhabitroutine.feature.view_schedule.config.enter_time_record.components

import com.example.inhabitroutine.core.presentation.components.result.ScreenResult
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed interface EnterTaskTimeRecordScreenResult : ScreenResult {
    data class Confirm(
        val taskId: String,
        val date: LocalDate,
        val time: LocalTime
    ) : EnterTaskTimeRecordScreenResult

    data object Dismiss : EnterTaskTimeRecordScreenResult
}