package com.example.inhabitroutine.core.presentation.ui.dialog.pick_date.model

import kotlinx.datetime.LocalDate

data class PickDateRequestModel(
    val initDate: LocalDate,
    val minDate: LocalDate,
    val maxDate: LocalDate
)
