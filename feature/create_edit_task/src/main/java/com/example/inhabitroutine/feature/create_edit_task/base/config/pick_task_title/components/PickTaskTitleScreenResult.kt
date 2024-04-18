package com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_title.components

import com.example.inhabitroutine.core.presentation.components.result.ScreenResult

interface PickTaskTitleScreenResult : ScreenResult {
    data class Confirm(val title: String) : PickTaskTitleScreenResult
    data object Dismiss : PickTaskTitleScreenResult
}