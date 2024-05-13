package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_priority.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.state.ScreenState

data class PickTaskPriorityScreenState(
    val inputPriority: String,
    val canConfirm: Boolean,
    val priorityInputValidator: (String) -> Boolean
) : ScreenState
