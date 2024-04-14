package com.example.inhabitroutine.core.ui.dialog.pick_task_type

import com.example.inhabitroutine.core.presentation.components.result.ScreenResult
import com.example.inhabitroutine.domain.model.task.type.TaskType

sealed interface PickTaskTypeScreenResult : ScreenResult {
    data class Confirm(val taskType: TaskType) : PickTaskTypeScreenResult
    data object Dismiss : PickTaskTypeScreenResult
}