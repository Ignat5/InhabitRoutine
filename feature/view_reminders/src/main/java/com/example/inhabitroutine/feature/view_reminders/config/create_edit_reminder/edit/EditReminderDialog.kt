package com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.core.presentation.ui.base.BaseDialogWithResult
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogDefaults
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.BaseCreateEditReminderDialog
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit.components.EditReminderScreenEvent
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit.components.EditReminderScreenResult
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit.components.EditReminderScreenState

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