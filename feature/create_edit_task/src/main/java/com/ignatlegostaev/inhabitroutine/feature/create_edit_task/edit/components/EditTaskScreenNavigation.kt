package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.navigation.ScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation

sealed interface EditTaskScreenNavigation : ScreenNavigation {
    data class Base(
        val baseNavigation: BaseCreateEditTaskScreenNavigation
    ) : EditTaskScreenNavigation

    data class ViewTaskStatistics(
        val taskId: String
    ) : EditTaskScreenNavigation

    data object Back : EditTaskScreenNavigation
}