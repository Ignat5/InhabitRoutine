package com.example.inhabitroutine.feature.view_reminders.components

import com.example.inhabitroutine.core.presentation.components.navigation.ScreenNavigation

sealed interface ViewRemindersScreenNavigation : ScreenNavigation {
    data object Back : ViewRemindersScreenNavigation
}