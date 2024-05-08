package com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit

import androidx.compose.runtime.Composable
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.base.BaseDialogWithResult
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.BaseCreateEditReminderDialog
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit.components.EditReminderScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit.components.EditReminderScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit.components.EditReminderScreenState

@Composable
fun EditReminderDialog(
    stateHolder: EditReminderStateHolder,
    onResult: (EditReminderScreenResult) -> Unit
) {
    BaseDialogWithResult(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        EditReminderDialogStateless(state, onEvent)
    }
}

@Composable
private fun EditReminderDialogStateless(
    state: EditReminderScreenState,
    onEvent: (EditReminderScreenEvent) -> Unit
) {
    BaseCreateEditReminderDialog(
        state = state,
        onEvent = {
            onEvent(EditReminderScreenEvent.Base(it))
        }
    )
}