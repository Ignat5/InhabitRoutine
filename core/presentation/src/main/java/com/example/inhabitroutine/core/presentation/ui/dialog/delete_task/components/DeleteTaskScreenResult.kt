package com.example.inhabitroutine.core.presentation.ui.dialog.delete_task.components

import com.example.inhabitroutine.core.presentation.components.result.ScreenResult

sealed interface DeleteTaskScreenResult : ScreenResult {
    data class Confirm(val taskId: String) : DeleteTaskScreenResult
    data object Dismiss : DeleteTaskScreenResult
}