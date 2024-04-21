package com.example.inhabitroutine.feature.search_tasks.components

import com.example.inhabitroutine.core.presentation.components.navigation.ScreenNavigation

sealed interface SearchTasksScreenNavigation : ScreenNavigation {
    data class EditTask(val taskId: String) : SearchTasksScreenNavigation
    data object Back : SearchTasksScreenNavigation
}