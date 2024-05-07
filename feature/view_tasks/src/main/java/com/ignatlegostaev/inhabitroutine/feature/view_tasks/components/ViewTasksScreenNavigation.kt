package com.ignatlegostaev.inhabitroutine.feature.view_tasks.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.navigation.ScreenNavigation

sealed interface ViewTasksScreenNavigation : ScreenNavigation {
    data class EditTask(val taskId: String) : ViewTasksScreenNavigation
    data class CreateTask(val taskId: String) : ViewTasksScreenNavigation
    data object SearchTasks : ViewTasksScreenNavigation
}