package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.config

import com.ignatlegostaev.inhabitroutine.core.presentation.components.result.ScreenResult

sealed interface ConfirmLeavingScreenResult : ScreenResult {
    data object Confirm : ConfirmLeavingScreenResult
    data object Dismiss : ConfirmLeavingScreenResult
}