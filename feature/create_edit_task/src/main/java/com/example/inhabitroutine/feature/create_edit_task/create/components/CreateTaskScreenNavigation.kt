package com.example.inhabitroutine.feature.create_edit_task.create.components

import com.example.inhabitroutine.core.presentation.components.navigation.ScreenNavigation
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation

sealed interface CreateTaskScreenNavigation : ScreenNavigation {
    data class Base(val baseNavigation: BaseCreateEditTaskScreenNavigation) : CreateTaskScreenNavigation
    data object Back : CreateTaskScreenNavigation
}