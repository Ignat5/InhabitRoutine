package com.ignatlegostaev.inhabitroutine.feature.view_reminders.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.ignatlegostaev.inhabitroutine.core.presentation.components.result.ScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.components.CreateReminderScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit.components.EditReminderScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.delete_reminder.components.DeleteReminderScreenResult

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