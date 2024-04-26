package com.example.inhabitroutine.feature.view_tasks.config.view_task_actions.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.feature.view_tasks.config.view_task_actions.model.ItemTaskAction

sealed interface ViewTaskActionsScreenEvent : ScreenEvent {
    data class OnItemActionClick(val item: ItemTaskAction) : ViewTaskActionsScreenEvent
    data object OnEditTaskClick : ViewTaskActionsScreenEvent
    data object OnDismissRequest : ViewTaskActionsScreenEvent
}