package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_priority.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.event.ScreenEvent

sealed interface PickTaskPriorityScreenEvent : ScreenEvent {
    data class OnInputPriorityUpdate(val value: String) : PickTaskPriorityScreenEvent
    data object OnConfirmClick : PickTaskPriorityScreenEvent
    data object OnDismissRequest : PickTaskPriorityScreenEvent
}