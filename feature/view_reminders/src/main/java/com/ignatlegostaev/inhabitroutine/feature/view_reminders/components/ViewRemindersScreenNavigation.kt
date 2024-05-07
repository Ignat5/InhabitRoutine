package com.ignatlegostaev.inhabitroutine.feature.view_reminders.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.navigation.ScreenNavigation

sealed interface ViewRemindersScreenNavigation : ScreenNavigation {
    data object Back : ViewRemindersScreenNavigation
}