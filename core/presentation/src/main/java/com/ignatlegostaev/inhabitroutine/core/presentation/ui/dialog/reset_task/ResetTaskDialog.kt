package com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.reset_task

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ignatlegostaev.inhabitroutine.core.presentation.R
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.base.BaseDialogWithResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogActionType
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogDefaults
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.base.BaseMessageDialog
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.reset_task.components.ResetTaskScreenEvent
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.reset_task.components.ResetTaskScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.reset_task.components.ResetTaskScreenState

@Composable
fun ResetTaskDialog(
    stateHolder: ResetTaskStateHolder,
    onResult: (ResetTaskScreenResult) -> Unit
) {
    BaseDialogWithResult(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        ResetTaskDialogStateless(state, onEvent)
    }
}

@Composable
private fun ResetTaskDialogStateless(
    state: ResetTaskScreenState,
    onEvent: (ResetTaskScreenEvent) -> Unit
) {
    BaseMessageDialog(
        onDismissRequest = { onEvent(ResetTaskScreenEvent.OnDismissRequest) },
        titleText = stringResource(id = R.string.task_action_reset_title),
        messageText = stringResource(id = R.string.task_action_reset_message),
        actionType = BaseDialogActionType.ConfirmDismiss(
            confirmButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.confirm),
                    onClick = { onEvent(ResetTaskScreenEvent.OnConfirmClick) }
                )
            },
            dismissButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.cancel),
                    onClick = { onEvent(ResetTaskScreenEvent.OnDismissRequest) }
                )
            }
        )
    )
}