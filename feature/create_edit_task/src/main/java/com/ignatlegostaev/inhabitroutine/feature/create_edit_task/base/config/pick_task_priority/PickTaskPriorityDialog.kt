package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_priority

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.ignatlegostaev.inhabitroutine.core.presentation.R
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.base.BaseDialogWithResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.common.BaseFocusedOutlinedTextField
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.base.BaseDialog
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogActionType
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogDefaults
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_priority.components.PickTaskPriorityScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_priority.components.PickTaskPriorityScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_priority.components.PickTaskPriorityScreenState

@Composable
fun PickTaskPriorityDialog(
    stateHolder: PickTaskPriorityStateHolder,
    onResult: (PickTaskPriorityScreenResult) -> Unit
) {
    BaseDialogWithResult(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        PickTaskPriorityDialogStateless(state, onEvent)
    }
}

@Composable
private fun PickTaskPriorityDialogStateless(
    state: PickTaskPriorityScreenState,
    onEvent: (PickTaskPriorityScreenEvent) -> Unit
) {
    BaseDialog(
        onDismissRequest = {
            onEvent(PickTaskPriorityScreenEvent.OnDismissRequest)
        },
        actionType = BaseDialogActionType.ConfirmDismiss(
            confirmButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.confirm),
                    enabled = state.canConfirm,
                    onClick = {
                        onEvent(PickTaskPriorityScreenEvent.OnConfirmClick)
                    }
                )
            },
            dismissButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.cancel),
                    onClick = {
                        onEvent(PickTaskPriorityScreenEvent.OnDismissRequest)
                    }
                )
            }
        )
    ) {
        BaseFocusedOutlinedTextField(
            value = state.inputPriority,
            onValueChange = {
                onEvent(PickTaskPriorityScreenEvent.OnInputPriorityUpdate(it))
            },
            valueValidator = state.priorityInputValidator,
            label = {
                Text(text = stringResource(id = R.string.task_config_priority))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}