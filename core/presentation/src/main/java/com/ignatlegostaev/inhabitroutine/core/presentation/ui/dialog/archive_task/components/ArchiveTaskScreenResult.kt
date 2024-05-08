package com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.archive_task.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.result.ScreenResult

sealed interface ArchiveTaskScreenResult : ScreenResult {
    data class Confirm(val taskId: String) : ArchiveTaskScreenResult
    data object Dismiss : ArchiveTaskScreenResult
}