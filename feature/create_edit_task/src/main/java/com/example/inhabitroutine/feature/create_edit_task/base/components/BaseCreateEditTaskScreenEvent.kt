package com.example.inhabitroutine.feature.create_edit_task.base.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.base.model.BaseItemTaskConfig

sealed interface BaseCreateEditTaskScreenEvent : ScreenEvent {
    data class OnItemConfigClick(
        val itemConfig: BaseItemTaskConfig
    ) : BaseCreateEditTaskScreenEvent
}