package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.config.ScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenConfig

sealed interface CreateTaskScreenConfig : ScreenConfig {
    data class Base(val baseConfig: BaseCreateEditTaskScreenConfig) : CreateTaskScreenConfig
    data object ConfirmLeaving : CreateTaskScreenConfig
}