package com.example.inhabitroutine.core.presentation.ui.dialog.pick_date.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import kotlinx.datetime.LocalDate

sealed interface PickDateScreenEvent : ScreenEvent {
    data class OnDateClick(val date: LocalDate) : PickDateScreenEvent
    data object OnPrevMonthClick : PickDateScreenEvent
    data object OnNextMonthClick : PickDateScreenEvent
    data object OnConfirmClick : PickDateScreenEvent
    data object OnDismissRequest : PickDateScreenEvent
}