package com.ignatlegostaev.inhabitroutine.feature.view_reminders.components

import androidx.compose.runtime.Immutable
import com.ignatlegostaev.inhabitroutine.core.presentation.components.state.ScreenState
import com.ignatlegostaev.inhabitroutine.core.presentation.model.UIResultModel
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.model.ViewRemindersMessage

@Immutable
data class ViewRemindersScreenState(
    val allRemindersResult: UIResultModel<List<ReminderModel>>,
    val message: ViewRemindersMessage
) : ScreenState
