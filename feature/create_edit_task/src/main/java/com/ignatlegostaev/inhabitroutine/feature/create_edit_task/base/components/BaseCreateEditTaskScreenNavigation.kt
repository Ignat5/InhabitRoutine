package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.navigation.ScreenNavigation

sealed interface BaseCreateEditTaskScreenNavigation : ScreenNavigation {
    data class ViewReminders(
        val taskId: String
    ) : BaseCreateEditTaskScreenNavigation
}