package com.example.inhabitroutine.core.presentation.ui.dialog.reset_task.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent

sealed interface ResetTaskScreenEvent : ScreenEvent {
    data object OnConfirmClick : ResetTaskScreenEvent
    data object OnDismissRequest : ResetTaskScreenEvent
}