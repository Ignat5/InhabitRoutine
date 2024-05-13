package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_priority.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.result.ScreenResult

sealed interface PickTaskPriorityScreenResult : ScreenResult {
    data class Confirm(val priority: Long) : PickTaskPriorityScreenResult
    data object Dismiss : PickTaskPriorityScreenResult
}