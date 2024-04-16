package com.example.inhabitroutine.feature.create_edit_task.create.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenEvent

sealed interface CreateTaskScreenEvent : ScreenEvent {
    data class Base(val baseEvent: BaseCreateEditTaskScreenEvent) : CreateTaskScreenEvent
}