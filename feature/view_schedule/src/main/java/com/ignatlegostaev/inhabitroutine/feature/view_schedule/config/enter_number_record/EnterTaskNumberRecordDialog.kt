package com.ignatlegostaev.inhabitroutine.feature.view_schedule.config.enter_number_record

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ignatlegostaev.inhabitroutine.core.presentation.R
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.base.BaseDialogWithResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.common.BaseFocusedOutlinedTextField
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.base.BaseDialog
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogActionType
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogDefaults
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.util.toDayMonthYearDisplay
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.util.toDisplay
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.config.enter_number_record.components.EnterTaskNumberRecordScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.config.enter_number_record.components.EnterTaskNumberRecordScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.config.enter_number_record.components.EnterTaskNumberRecordScreenState

@Composable
fun EnterTaskNumberRecordDialog(
    stateHolder: EnterTaskNumberRecordStateHolder,
    onResult: (EnterTaskNumberRecordScreenResult) -> Unit
) {
    BaseDialogWithResult(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        EnterTaskNumberRecordDialogStateless(state, onEvent)
    }
}

@Composable
private fun EnterTaskNumberRecordDialogStateless(
    state: EnterTaskNumberRecordScreenState,
    onEvent: (EnterTaskNumberRecordScreenEvent) -> Unit
) {
    BaseDialog(
        onDismissRequest = { onEvent(EnterTaskNumberRecordScreenEvent.OnDismissRequest) },
        actionType = BaseDialogActionType.ConfirmDismiss(
            confirmButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.confirm),
                    enabled = state.canConfirm,
                    onClick = { onEvent(EnterTaskNumberRecordScreenEvent.OnConfirmClick) }
                )
            },
            dismissButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.cancel),
                    onClick = { onEvent(EnterTaskNumberRecordScreenEvent.OnDismissRequest) }
                )
            }
        )
    ) {
        val context = LocalContext.current
        val dateText = remember(state.date) {
            state.date.toDayMonthYearDisplay()
        }
        val goalText = remember(state.taskModel.progress) {
            buildAnnotatedString {
                append(context.getString(R.string.goal))
                append(": ")
                append(state.taskModel.progress.toDisplay(context))
            }
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = state.taskModel.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = dateText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            BaseFocusedOutlinedTextField(
                value = state.inputNumber,
                onValueChange = { onEvent(EnterTaskNumberRecordScreenEvent.OnInputNumberUpdate(it)) },
                modifier = Modifier
                    .fillMaxWidth(),
                valueValidator = state.inputNumberValidator,
                label = {
                    Text(text = stringResource(id = R.string.task_enter_number_progress_label))
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = goalText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}