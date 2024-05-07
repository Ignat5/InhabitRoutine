package com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.archive_task.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.event.ScreenEvent

sealed interface ArchiveTaskScreenEvent : ScreenEvent {
    data object OnConfirmClick : ArchiveTaskScreenEvent
    data object OnDismissRequest : ArchiveTaskScreenEvent
}