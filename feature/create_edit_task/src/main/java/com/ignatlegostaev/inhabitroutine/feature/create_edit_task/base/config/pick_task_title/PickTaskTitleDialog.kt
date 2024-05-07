package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_title

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.base.BaseDialogWithResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.common.BaseFocusedOutlinedTextField
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.base.BaseDialog
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogActionType
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogDefaults
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.R
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_title.components.PickTaskTitleScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_title.components.PickTaskTitleScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_title.components.PickTaskTitleScreenState

@Composable
internal fun PickTaskTitleDialog(
    stateHolder: PickTaskTitleStateHolder,
    onResult: (PickTaskTitleScreenResult) -> Unit
) {
    BaseDialogWithResult(
        stateHolder = stateHolder,
        onResult = onResult
    ) { state, onEvent ->
        PickTaskTitleDialogStateless(state, onEvent)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PickTaskTitleDialogStateless(
    state: PickTaskTitleScreenState,
    onEvent: (PickTaskTitleScreenEvent) -> Unit
) {
    BaseDialog(
        onDismissRequest = {
            onEvent(PickTaskTitleScreenEvent.OnDismissRequest)
        },
        actionType = BaseDialogActionType.ConfirmDismiss(
            confirmButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = com.ignatlegostaev.inhabitroutine.core.presentation.R.string.confirm),
                    enabled = state.canConfirm,
                    onClick = {
                        onEvent(PickTaskTitleScreenEvent.OnConfirmClick)
                    }
                )
            },
            dismissButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = com.ignatlegostaev.inhabitroutine.core.presentation.R.string.cancel),
                    onClick = {
                        onEvent(PickTaskTitleScreenEvent.OnDismissRequest)
                    }
                )
            }
        )
    ) {
        BaseFocusedOutlinedTextField(
            value = state.inputTitle,
            onValueChange = {
                onEvent(PickTaskTitleScreenEvent.OnInputValueUpdate(it))
            },
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            label = {
                Text(text = stringResource(id = com.ignatlegostaev.inhabitroutine.core.presentation.R.string.task_config_title_label))
            }
        )
    }
}