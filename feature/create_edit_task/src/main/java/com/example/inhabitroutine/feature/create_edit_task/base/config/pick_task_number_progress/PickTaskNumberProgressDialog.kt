package com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_number_progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.inhabitroutine.core.presentation.ui.base.BaseDialogWithResult
import com.example.inhabitroutine.core.presentation.ui.common.BaseOutlinedDropdown
import com.example.inhabitroutine.core.presentation.ui.common.BaseOutlinedTextField
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialog
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogActionType
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogDefaults
import com.example.inhabitroutine.core.presentation.ui.util.limitNumberToDisplay
import com.example.inhabitroutine.core.presentation.ui.util.toTitleStringId
import com.example.inhabitroutine.domain.model.task.type.ProgressLimitType
import com.example.inhabitroutine.feature.create_edit_task.R
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_number_progress.components.PickTaskNumberProgressScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_number_progress.components.PickTaskNumberProgressScreenResult
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_number_progress.components.PickTaskNumberProgressScreenState

@Composable
internal fun PickTaskNumberProgressDialog(
    stateHolder: PickTaskNumberProgressStateHolder,
    onResult: (PickTaskNumberProgressScreenResult) -> Unit
) {
    BaseDialogWithResult(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        PickTaskNumberProgressDialogStateless(state, onEvent)
    }
}

@Composable
private fun PickTaskNumberProgressDialogStateless(
    state: PickTaskNumberProgressScreenState,
    onEvent: (PickTaskNumberProgressScreenEvent) -> Unit
) {
    BaseDialog(
        onDismissRequest = { onEvent(PickTaskNumberProgressScreenEvent.OnDismissRequest) },
        title = {
            BaseDialogDefaults.BaseDialogTitle(titleText = stringResource(id = com.example.inhabitroutine.core.presentation.R.string.task_config_progress))
        },
        actionType = BaseDialogActionType.ConfirmDismiss(
            confirmButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = com.example.inhabitroutine.core.presentation.R.string.confirm),
                    enabled = state.canConfirm,
                    onClick = { onEvent(PickTaskNumberProgressScreenEvent.OnConfirmClick) }
                )
            },
            dismissButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = com.example.inhabitroutine.core.presentation.R.string.cancel),
                    onClick = { onEvent(PickTaskNumberProgressScreenEvent.OnDismissRequest) }
                )
            }
        ),
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        val context = LocalContext.current
        val focusManager = LocalFocusManager.current
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BaseOutlinedDropdown(
                allOptions = ProgressLimitType.entries,
                currentOption = state.inputLimitType,
                optionText = { limitType ->
                    context.getString(limitType.toTitleStringId())
                },
                onOptionClick = {
                    onEvent(
                        PickTaskNumberProgressScreenEvent.OnPickProgressLimitType(
                            it
                        )
                    )
                }
            )
            BaseOutlinedTextField(
                value = state.inputLimitNumber,
                onValueChange = {
                    onEvent(
                        PickTaskNumberProgressScreenEvent.OnInputLimitNumberUpdate(
                            it
                        )
                    )
                },
                valueValidator = state.limitNumberInputValidator,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(id = com.example.inhabitroutine.core.presentation.R.string.task_config_limit_number_label))
                },
                singleLine = true,
                keyboardActions = KeyboardActions {
                    focusManager.moveFocus(FocusDirection.Next)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BaseOutlinedTextField(
                    value = state.inputLimitUnit,
                    onValueChange = {
                        onEvent(
                            PickTaskNumberProgressScreenEvent.OnInputLimitUnitUpdate(
                                it
                            )
                        )
                    },
                    modifier = Modifier.weight(1f),
                    label = {
                        Text(text = stringResource(id = com.example.inhabitroutine.core.presentation.R.string.task_config_limit_unit_label))
                    },
                    keyboardActions = KeyboardActions {
                        focusManager.clearFocus()
                    },
                    singleLine = true
                )
                Text(
                    text = stringResource(id = com.example.inhabitroutine.core.presentation.R.string.daily_goal_suffix),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}