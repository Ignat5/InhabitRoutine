package com.example.inhabitroutine.feature.view_reminders.components

import com.example.inhabitroutine.core.presentation.components.config.ScreenConfig
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.CreateReminderStateHolder
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit.EditReminderStateHolder
import com.example.inhabitroutine.feature.view_reminders.config.delete_reminder.DeleteReminderStateHolder

sealed interface ViewRemindersScreenConfig : ScreenConfig {
    data class CreateReminder(
        val stateHolder: CreateReminderStateHolder
    ) : ViewRemindersScreenConfig

    data class EditReminder(
        val stateHolder: EditReminderStateHolder
    ) : ViewRemindersScreenConfig

    data class DeleteReminder(
        val stateHolder: DeleteReminderStateHolder
    ) : ViewRemindersScreenConfig
}