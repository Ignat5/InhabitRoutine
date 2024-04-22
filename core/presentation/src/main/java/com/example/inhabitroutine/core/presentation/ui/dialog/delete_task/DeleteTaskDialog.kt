package com.example.inhabitroutine.core.presentation.ui.dialog.delete_task

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.core.presentation.ui.base.BaseDialogWithResult
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogActionType
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogDefaults
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseMessageDialog
import com.example.inhabitroutine.core.presentation.ui.dialog.delete_task.components.DeleteTaskScreenEvent
import com.example.inhabitroutine.core.presentation.ui.dialog.delete_task.components.DeleteTaskScreenResult
import com.example.inhabitroutine.core.presentation.ui.dialog.delete_task.components.DeleteTaskScreenState

@Composable
fun DeleteTaskDialog(
    stateHolder: DeleteTaskStateHolder,
    onResult: (DeleteTaskScreenResult) -> Unit
) {
    BaseDialogWithResult(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        DeleteTaskDialogStateless(state, onEvent)
    }
}

@Composable
private fun DeleteTaskDialogStateless(
    state: DeleteTaskScreenState,
    onEvent: (DeleteTaskScreenEvent) -> Unit
) {
    BaseMessageDialog(
        onDismissRequest = { onEvent(DeleteTaskScreenEvent.OnDismissRequest) },
        titleText = stringResource(id = R.string.task_action_delete_title),
        messageText = stringResource(id = R.string.task_action_delete_message),
        actionType = BaseDialogActionType.ConfirmDismiss(
            confirmButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.confirm),
                    onClick = { onEvent(DeleteTaskScreenEvent.OnConfirmClick) }
                )
            },
            dismissButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.cancel),
                    onClick = { onEvent(DeleteTaskScreenEvent.OnDismissRequest) }
                )
            }
        )
    )
}