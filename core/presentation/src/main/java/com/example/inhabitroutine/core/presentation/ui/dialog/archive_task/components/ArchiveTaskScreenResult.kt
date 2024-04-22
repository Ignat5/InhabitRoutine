package com.example.inhabitroutine.core.presentation.ui.dialog.archive_task.components

import com.example.inhabitroutine.core.presentation.components.result.ScreenResult

sealed interface ArchiveTaskScreenResult : ScreenResult {
    data class Confirm(val taskId: String) : ArchiveTaskScreenResult
    data object Dismiss : ArchiveTaskScreenResult
}