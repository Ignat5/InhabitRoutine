package com.example.inhabitroutine.feature.create_edit_task.base.components

import com.example.inhabitroutine.core.presentation.components.navigation.ScreenNavigation

sealed interface BaseCreateEditTaskScreenNavigation : ScreenNavigation {
    data class ViewReminders(
        val taskId: String
    ) : BaseCreateEditTaskScreenNavigation
}