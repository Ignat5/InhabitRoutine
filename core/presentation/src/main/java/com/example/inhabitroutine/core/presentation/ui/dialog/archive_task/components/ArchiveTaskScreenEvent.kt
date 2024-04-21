package com.example.inhabitroutine.core.presentation.ui.dialog.archive_task.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent

sealed interface ArchiveTaskScreenEvent : ScreenEvent {
    data object OnConfirmClick : ArchiveTaskScreenEvent
    data object OnDismissRequest : ArchiveTaskScreenEvent
}