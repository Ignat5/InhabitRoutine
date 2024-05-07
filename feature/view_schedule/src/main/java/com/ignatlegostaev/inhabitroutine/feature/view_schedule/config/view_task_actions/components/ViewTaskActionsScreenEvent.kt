package com.ignatlegostaev.inhabitroutine.feature.view_schedule.config.view_task_actions.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.config.view_task_actions.model.ItemTaskAction

sealed interface ViewTaskActionsScreenEvent : ScreenEvent {
    data class OnItemActionClick(val itemType: ItemTaskAction.Type) : ViewTaskActionsScreenEvent
    data object OnEditTaskClick : ViewTaskActionsScreenEvent
    data object OnDismissRequest : ViewTaskActionsScreenEvent
}