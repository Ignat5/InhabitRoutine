package com.example.inhabitroutine.core.presentation.ui.dialog.delete_task.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent

sealed interface DeleteTaskScreenEvent : ScreenEvent {
    data object OnConfirmClick : DeleteTaskScreenEvent
    data object OnDismissRequest : DeleteTaskScreenEvent
}