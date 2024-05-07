package com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_date.components

import androidx.compose.runtime.Immutable
import com.ignatlegostaev.inhabitroutine.core.presentation.components.state.ScreenState
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_date.model.UIDateItem
import kotlinx.datetime.LocalDate

@Immutable
data class PickDateScreenState(
    val startOfMonthDate: LocalDate,
    val currentPickedDate: LocalDate,
    val allDateItems: List<UIDateItem>
) : ScreenState
