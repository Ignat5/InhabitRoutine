package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.config

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.base.BaseMessageDialog
import com.ignatlegostaev.inhabitroutine.core.presentation.R
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogActionType
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogDefaults

@Composable
internal fun ConfirmLeavingDialog(onResult: (ConfirmLeavingScreenResult) -> Unit) {
    BaseMessageDialog(
        onDismissRequest = { onResult(ConfirmLeavingScreenResult.Dismiss) },
        titleText = stringResource(id = R.string.confirm_discard_activity_title),
        messageText = stringResource(id = R.string.confirm_discard_activity_message),
        actionType = BaseDialogActionType.ConfirmDismiss(
            confirmButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.confirm),
                    onClick = { onResult(ConfirmLeavingScreenResult.Confirm) }
                )
            },
            dismissButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.cancel),
                    onClick = { onResult(ConfirmLeavingScreenResult.Dismiss) }
                )
            }
        )
    )
}