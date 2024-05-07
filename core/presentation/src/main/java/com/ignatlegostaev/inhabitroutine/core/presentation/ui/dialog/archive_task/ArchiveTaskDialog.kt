package com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.archive_task

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ignatlegostaev.inhabitroutine.core.presentation.R
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.base.BaseDialogWithResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.archive_task.components.ArchiveTaskScreenEvent
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.archive_task.components.ArchiveTaskScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.archive_task.components.ArchiveTaskScreenState
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogActionType
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogDefaults
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.base.BaseMessageDialog

@Composable
fun ArchiveTaskDialog(
    stateHolder: ArchiveTaskStateHolder,
    onResult: (ArchiveTaskScreenResult) -> Unit
) {
    BaseDialogWithResult(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        ArchiveTaskDialogStateless(state, onEvent)
    }
}

@Composable
private fun ArchiveTaskDialogStateless(
    state: ArchiveTaskScreenState,
    onEvent: (ArchiveTaskScreenEvent) -> Unit
) {
    BaseMessageDialog(
        onDismissRequest = { onEvent(ArchiveTaskScreenEvent.OnDismissRequest) },
        titleText = stringResource(id = R.string.task_action_archive_confirm_title),
        messageText = stringResource(id = R.string.task_action_archive_confirm_message),
        actionType = BaseDialogActionType.ConfirmDismiss(
            confirmButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.confirm),
                    onClick = { onEvent(ArchiveTaskScreenEvent.OnConfirmClick) }
                )
            },
            dismissButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.cancel),
                    onClick = { onEvent(ArchiveTaskScreenEvent.OnDismissRequest) }
                )
            }
        )
    )
}