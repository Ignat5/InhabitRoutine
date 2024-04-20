package com.example.inhabitroutine.feature.view_reminders.config.delete_reminder

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.core.presentation.ui.base.BaseDialogWithResult
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogActionType
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogDefaults
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseMessageDialog
import com.example.inhabitroutine.feature.view_reminders.config.delete_reminder.components.DeleteReminderScreenEvent
import com.example.inhabitroutine.feature.view_reminders.config.delete_reminder.components.DeleteReminderScreenResult
import com.example.inhabitroutine.feature.view_reminders.config.delete_reminder.components.DeleteReminderScreenState

@Composable
fun ConfirmDeleteReminderDialog(
    stateHolder: DeleteReminderStateHolder,
    onResult: (DeleteReminderScreenResult) -> Unit
) {
    BaseDialogWithResult(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        ConfirmDeleteReminderDialogStateless(state, onEvent)
    }
}

@Composable
private fun ConfirmDeleteReminderDialogStateless(
    state: DeleteReminderScreenState,
    onEvent: (DeleteReminderScreenEvent) -> Unit
) {
    BaseMessageDialog(
        onDismissRequest = { onEvent(DeleteReminderScreenEvent.OnDismissRequest) },
        titleText = stringResource(id = R.string.reminder_confirm_delete_title),
        messageText = stringResource(id = R.string.reminder_confirm_delete_message),
        actionType = BaseDialogActionType.ConfirmDismiss(
            confirmButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.confirm),
                    onClick = { onEvent(DeleteReminderScreenEvent.OnConfirmClick) }
                )
            },
            dismissButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.cancel),
                    onClick = { onEvent(DeleteReminderScreenEvent.OnDismissRequest) }
                )
            }
        )
    )
}