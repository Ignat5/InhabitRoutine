package com.example.inhabitroutine.feature.create_edit_task.edit.components

import com.example.inhabitroutine.core.presentation.components.navigation.ScreenNavigation
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation

sealed interface EditTaskScreenNavigation : ScreenNavigation {
    data class Base(
        val baseNavigation: BaseCreateEditTaskScreenNavigation
    ) : EditTaskScreenNavigation

    data class ViewTaskStatistics(
        val taskId: String
    ) : EditTaskScreenNavigation

    data object Back : EditTaskScreenNavigation
}