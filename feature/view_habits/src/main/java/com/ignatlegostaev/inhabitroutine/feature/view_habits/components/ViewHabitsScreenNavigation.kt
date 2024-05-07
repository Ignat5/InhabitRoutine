package com.ignatlegostaev.inhabitroutine.feature.view_habits.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.navigation.ScreenNavigation

sealed interface ViewHabitsScreenNavigation : ScreenNavigation {
    data class EditTask(val taskId: String) : ViewHabitsScreenNavigation
    data class ViewStatistics(val taskId: String) : ViewHabitsScreenNavigation
    data class CreateTask(val taskId: String) : ViewHabitsScreenNavigation
    data object SearchTasks : ViewHabitsScreenNavigation
}