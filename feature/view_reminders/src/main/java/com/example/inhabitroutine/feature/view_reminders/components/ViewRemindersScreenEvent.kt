package com.example.inhabitroutine.feature.view_reminders.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.core.presentation.components.result.ScreenResult
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.components.CreateReminderScreenResult
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit.components.EditReminderScreenResult
import com.example.inhabitroutine.feature.view_reminders.config.delete_reminder.components.DeleteReminderScreenResult

sealed interface ViewRemindersScreenEvent : ScreenEvent {
    data class OnReminderClick(
        val reminderId: String
    ) : ViewRemindersScreenEvent

    data class OnDeleteReminderClick(
        val reminderId: String
    ) : ViewRemindersScreenEvent

    data object OnCreateReminderClick : ViewRemindersScreenEvent
    data object OnMessageShown : ViewRemindersScreenEvent
    data object OnLeaveRequest : ViewRemindersScreenEvent

    sealed interface ResultEvent : ViewRemindersScreenEvent {
        val result: ScreenResult

        data class CreateReminder(
            override val result: CreateReminderScreenResult
        ) : ResultEvent

        data class EditReminder(
            override val result: EditReminderScreenResult
        ) : ResultEvent

        data class DeleteReminder(
            override val result: DeleteReminderScreenResult
        ) : ResultEvent
    }
}