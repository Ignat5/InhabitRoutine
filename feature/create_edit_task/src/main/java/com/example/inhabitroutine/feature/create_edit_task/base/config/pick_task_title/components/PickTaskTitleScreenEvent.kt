package com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_title.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent

sealed interface PickTaskTitleScreenEvent : ScreenEvent {
    data class OnInputValueUpdate(val value: String) : PickTaskTitleScreenEvent
    data object OnConfirmClick : PickTaskTitleScreenEvent
    data object OnDismissRequest : PickTaskTitleScreenEvent
}