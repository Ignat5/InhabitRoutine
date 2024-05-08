package com.ignatlegostaev.inhabitroutine.feature.search_tasks.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.navigation.ScreenNavigation

sealed interface SearchTasksScreenNavigation : ScreenNavigation {
    data class EditTask(val taskId: String) : SearchTasksScreenNavigation
    data object Back : SearchTasksScreenNavigation
}