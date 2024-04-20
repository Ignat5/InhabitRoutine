package com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_description.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent

sealed interface PickTaskDescriptionScreenEvent : ScreenEvent {
    data class OnInputDescriptionUpdate(
        val value: String
    ) : PickTaskDescriptionScreenEvent

    data object OnConfirmClick : PickTaskDescriptionScreenEvent
    data object OnDismissRequest : PickTaskDescriptionScreenEvent
}