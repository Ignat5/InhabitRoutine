package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_time_progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.base.BaseDialogWithResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.base.BaseDialog
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogDefaults
import com.ignatlegostaev.inhabitroutine.core.presentation.R
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.common.BaseOutlinedDropdown
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.common.BaseTimeInput
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogActionType
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.util.toTitleStringId
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.ProgressLimitType
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_time_progress.components.PickTaskTimeProgressScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_time_progress.components.PickTaskTimeProgressScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_time_progress.components.PickTaskTimeProgressScreenState

@Composable
internal fun PickTaskTimeProgressDialog(
    stateHolder: PickTaskTimeProgressStateHolder,
    onResult: (PickTaskTimeProgressScreenResult) -> Unit
) {
    BaseDialogWithResult(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        PickTaskTimeProgressDialogStateless(state, onEvent)
    }
}

@Composable
private fun PickTaskTimeProgressDialogStateless(
    state: PickTaskTimeProgressScreenState,
    onEvent: (PickTaskTimeProgressScreenEvent) -> Unit
) {
    BaseDialog(
        onDismissRequest = { onEvent(PickTaskTimeProgressScreenEvent.OnDismissRequest) },
        properties = DialogProperties(dismissOnClickOutside = false),
        title = {
            BaseDialogDefaults.BaseDialogTitle(titleText = stringResource(id = R.string.task_config_progress))
        },
        actionType = BaseDialogActionType.ConfirmDismiss(
            confirmButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.confirm),
                    onClick = { onEvent(PickTaskTimeProgressScreenEvent.OnConfirmClick) }
                )
            },
            dismissButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.cancel),
                    onClick = { onEvent(PickTaskTimeProgressScreenEvent.OnDismissRequest) }
                )
            }
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val context = LocalContext.current
            val allOptions = remember { ProgressLimitType.entries }
            BaseOutlinedDropdown(
                allOptions = allOptions,
                currentOption = state.inputLimitType,
                optionText = { limitType ->
                    context.getString(limitType.toTitleStringId())
                },
                onOptionClick = {
                    onEvent(PickTaskTimeProgressScreenEvent.OnPickLimitType(it))
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BaseTimeInput(
                    initHours = state.inputHours,
                    initMinutes = state.inputMinutes,
                    onHoursUpdate = { onEvent(PickTaskTimeProgressScreenEvent.OnInputHoursUpdate(it)) },
                    onMinutesUpdate = {
                        onEvent(
                            PickTaskTimeProgressScreenEvent.OnInputMinutesUpdate(
                                it
                            )
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(id = R.string.daily_goal_suffix),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}