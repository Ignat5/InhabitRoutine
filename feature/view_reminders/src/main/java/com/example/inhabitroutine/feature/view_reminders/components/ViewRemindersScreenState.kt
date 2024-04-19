package com.example.inhabitroutine.feature.view_reminders.components

import androidx.compose.runtime.Immutable
import com.example.inhabitroutine.core.presentation.components.state.ScreenState
import com.example.inhabitroutine.domain.model.reminder.ReminderModel

@Immutable
data class ViewRemindersScreenState(
    val allReminders: List<ReminderModel>
) : ScreenState
