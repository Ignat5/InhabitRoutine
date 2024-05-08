package com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_task_type

import com.ignatlegostaev.inhabitroutine.core.presentation.components.result.ScreenResult
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskType

sealed interface PickTaskTypeScreenResult : ScreenResult {
    data class Confirm(val taskType: TaskType) : PickTaskTypeScreenResult
    data object Dismiss : PickTaskTypeScreenResult
}