package com.example.inhabitroutine.core.presentation.ui.dialog.pick_date.components

import com.example.inhabitroutine.core.presentation.components.state.ScreenState
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_date.model.UIDateItem
import kotlinx.datetime.LocalDate

data class PickDateScreenState(
    val startOfMonthDate: LocalDate,
    val currentPickedDate: LocalDate,
    val allDateItems: List<UIDateItem>
) : ScreenState
