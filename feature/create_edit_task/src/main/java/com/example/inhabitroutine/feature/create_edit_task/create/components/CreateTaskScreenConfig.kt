package com.example.inhabitroutine.feature.create_edit_task.create.components

import com.example.inhabitroutine.core.presentation.components.config.ScreenConfig
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenConfig
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation

sealed interface CreateTaskScreenConfig : ScreenConfig {
    data class Base(val baseConfig: BaseCreateEditTaskScreenConfig) : CreateTaskScreenConfig
}