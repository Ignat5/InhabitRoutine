package com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create

import androidx.compose.runtime.Composable
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.base.BaseDialogWithResult
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.BaseCreateEditReminderDialog
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.components.CreateReminderScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.components.CreateReminderScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.components.CreateReminderScreenState

@Composable
fun CreateReminderDialog(
    stateHolder: CreateReminderStateHolder,
    onResult: (CreateReminderScreenResult) -> Unit
) {
    BaseDialogWithResult(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        CreateReminderDialogStateless(state, onEvent)
    }
}

@Composable
private fun CreateReminderDialogStateless(
    state: CreateReminderScreenState,
    onEvent: (CreateReminderScreenEvent) -> Unit
) {
    BaseCreateEditReminderDialog(
        state = state,
        onEvent = {
            onEvent(CreateReminderScreenEvent.Base(it))
        }
    )
}