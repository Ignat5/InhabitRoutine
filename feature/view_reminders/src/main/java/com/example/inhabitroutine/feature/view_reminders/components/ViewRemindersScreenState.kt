package com.example.inhabitroutine.feature.view_reminders.components

import androidx.compose.runtime.Immutable
import com.example.inhabitroutine.core.presentation.components.state.ScreenState
import com.example.inhabitroutine.domain.model.reminder.ReminderModel
import com.example.inhabitroutine.feature.view_reminders.model.ViewRemindersMessage

@Immutable
data class ViewRemindersScreenState(
    val allReminders: List<ReminderModel>,
    val message: ViewRemindersMessage
) : ScreenState
