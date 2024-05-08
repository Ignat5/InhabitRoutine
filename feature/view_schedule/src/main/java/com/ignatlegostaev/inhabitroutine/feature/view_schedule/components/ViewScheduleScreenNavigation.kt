package com.ignatlegostaev.inhabitroutine.feature.view_schedule.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.navigation.ScreenNavigation

sealed interface ViewScheduleScreenNavigation : ScreenNavigation {
    data class CreateTask(val taskId: String) : ViewScheduleScreenNavigation
    data class EditTask(val taskId: String) : ViewScheduleScreenNavigation
    data object SearchTasks : ViewScheduleScreenNavigation
}