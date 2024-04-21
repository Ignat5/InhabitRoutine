package com.example.inhabitroutine.feature.create_edit_task.edit.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenEvent

sealed interface EditTaskScreenEvent : ScreenEvent {
    data class Base(
        val baseEvent: BaseCreateEditTaskScreenEvent
    ) : EditTaskScreenEvent

    data object OnBackRequest : EditTaskScreenEvent
}