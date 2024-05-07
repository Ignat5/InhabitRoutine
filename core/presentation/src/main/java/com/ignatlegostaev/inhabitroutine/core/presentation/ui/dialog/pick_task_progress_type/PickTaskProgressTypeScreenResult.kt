package com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_task_progress_type

import com.ignatlegostaev.inhabitroutine.core.presentation.components.result.ScreenResult
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskProgressType

sealed interface PickTaskProgressTypeScreenResult : ScreenResult {
    data class Confirm(val taskProgressType: TaskProgressType) : PickTaskProgressTypeScreenResult
    data object Dismiss : PickTaskProgressTypeScreenResult
}