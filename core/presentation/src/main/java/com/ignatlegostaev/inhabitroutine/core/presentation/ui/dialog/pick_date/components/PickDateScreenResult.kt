package com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_date.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.result.ScreenResult
import kotlinx.datetime.LocalDate

sealed interface PickDateScreenResult : ScreenResult {
    data class Confirm(
        val date: LocalDate
    ) : PickDateScreenResult

    data object Dismiss : PickDateScreenResult
}