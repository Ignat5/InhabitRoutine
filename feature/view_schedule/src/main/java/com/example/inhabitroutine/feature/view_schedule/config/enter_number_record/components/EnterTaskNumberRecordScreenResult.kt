package com.example.inhabitroutine.feature.view_schedule.config.enter_number_record.components

import com.example.inhabitroutine.core.presentation.components.result.ScreenResult
import kotlinx.datetime.LocalDate

sealed interface EnterTaskNumberRecordScreenResult : ScreenResult {
    data class Confirm(
        val taskId: String,
        val date: LocalDate,
        val number: Double
    ) : EnterTaskNumberRecordScreenResult

    data object Dismiss : EnterTaskNumberRecordScreenResult
}