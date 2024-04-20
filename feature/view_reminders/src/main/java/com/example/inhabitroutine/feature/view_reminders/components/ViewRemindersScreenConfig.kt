package com.example.inhabitroutine.feature.view_reminders.components

import com.example.inhabitroutine.core.presentation.components.config.ScreenConfig
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.CreateReminderStateHolder

sealed interface ViewRemindersScreenConfig : ScreenConfig {
    data class CreateReminder(
        val stateHolder: CreateReminderStateHolder
    ) : ViewRemindersScreenConfig
}