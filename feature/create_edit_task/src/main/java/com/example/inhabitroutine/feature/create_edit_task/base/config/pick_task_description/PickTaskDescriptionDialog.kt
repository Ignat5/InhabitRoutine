package com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_description

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.core.presentation.ui.base.BaseDialogWithResult
import com.example.inhabitroutine.core.presentation.ui.common.BaseOutlinedTextField
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialog
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogActionType
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogDefaults
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_description.components.PickTaskDescriptionScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_description.components.PickTaskDescriptionScreenResult
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_description.components.PickTaskDescriptionScreenState

private const val MIN_LINES = 2
private const val MAX_LINES = 5

@Composable
internal fun PickTaskDescriptionDialog(
    stateHolder: PickTaskDescriptionStateHolder,
    onResult: (PickTaskDescriptionScreenResult) -> Unit
) {
    BaseDialogWithResult(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        PickTaskDescriptionDialogStateless(state, onEvent)
    }
}

@Composable
private fun PickTaskDescriptionDialogStateless(
    state: PickTaskDescriptionScreenState,
    onEvent: (PickTaskDescriptionScreenEvent) -> Unit
) {
    BaseDialog(
        onDismissRequest = { onEvent(PickTaskDescriptionScreenEvent.OnDismissRequest) },
        actionType = BaseDialogActionType.ConfirmDismiss(
            confirmButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.confirm),
                    onClick = { onEvent(PickTaskDescriptionScreenEvent.OnConfirmClick) }
                )
            },
            dismissButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.cancel),
                    onClick = { onEvent(PickTaskDescriptionScreenEvent.OnDismissRequest) }
                )
            }
        )
    ) {
        val focusRequester = remember { FocusRequester() }
        BaseOutlinedTextField(
            value = state.inputDescription,
            onValueChange = { onEvent(PickTaskDescriptionScreenEvent.OnInputDescriptionUpdate(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            label = {
                Text(text = stringResource(id = R.string.task_config_description))
            },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            minLines = MIN_LINES,
            maxLines = MAX_LINES
        )
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}