package com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.reset_task.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.event.ScreenEvent

sealed interface ResetTaskScreenEvent : ScreenEvent {
    data object OnConfirmClick : ResetTaskScreenEvent
    data object OnDismissRequest : ResetTaskScreenEvent
}