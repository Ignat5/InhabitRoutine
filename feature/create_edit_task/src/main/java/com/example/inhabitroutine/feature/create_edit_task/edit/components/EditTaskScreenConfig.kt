package com.example.inhabitroutine.feature.create_edit_task.edit.components

import com.example.inhabitroutine.core.presentation.components.config.ScreenConfig
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenConfig

sealed interface EditTaskScreenConfig : ScreenConfig {
    data class Base(
        val baseConfig: BaseCreateEditTaskScreenConfig
    ) : EditTaskScreenConfig
}