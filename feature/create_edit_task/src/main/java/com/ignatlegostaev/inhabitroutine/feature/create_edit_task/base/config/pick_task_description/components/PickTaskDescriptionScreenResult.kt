package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_description.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.result.ScreenResult

sealed interface PickTaskDescriptionScreenResult : ScreenResult {
    data class Confirm(
        val description: String
    ) : PickTaskDescriptionScreenResult

    data object Dismiss : PickTaskDescriptionScreenResult
}