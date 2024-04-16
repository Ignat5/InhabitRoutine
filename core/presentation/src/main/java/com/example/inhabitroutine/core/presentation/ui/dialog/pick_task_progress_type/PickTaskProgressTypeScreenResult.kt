package com.example.inhabitroutine.core.presentation.ui.dialog.pick_task_progress_type

import com.example.inhabitroutine.core.presentation.components.result.ScreenResult
import com.example.inhabitroutine.domain.model.task.type.TaskProgressType

sealed interface PickTaskProgressTypeScreenResult : ScreenResult {
    data class Confirm(val taskProgressType: TaskProgressType) : PickTaskProgressTypeScreenResult
    data object Dismiss : PickTaskProgressTypeScreenResult
}