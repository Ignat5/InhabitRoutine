package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.navigation.ScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation

sealed interface CreateTaskScreenNavigation : ScreenNavigation {
    data class Base(val baseNavigation: BaseCreateEditTaskScreenNavigation) : CreateTaskScreenNavigation
    data object Back : CreateTaskScreenNavigation
}