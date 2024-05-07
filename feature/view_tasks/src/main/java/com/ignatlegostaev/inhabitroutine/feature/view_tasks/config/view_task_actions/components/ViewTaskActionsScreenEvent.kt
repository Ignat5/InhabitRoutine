package com.ignatlegostaev.inhabitroutine.feature.view_tasks.config.view_task_actions.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.config.view_task_actions.model.ItemTaskAction

sealed interface ViewTaskActionsScreenEvent : ScreenEvent {
    data class OnItemActionClick(val item: ItemTaskAction) : ViewTaskActionsScreenEvent
    data object OnEditTaskClick : ViewTaskActionsScreenEvent
    data object OnDismissRequest : ViewTaskActionsScreenEvent
}