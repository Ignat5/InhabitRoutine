package com.example.inhabitroutine.feature.view_schedule.config.enter_time_record

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.core.presentation.ui.base.BaseDialogWithResult
import com.example.inhabitroutine.core.presentation.ui.common.BaseTimeInput
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialog
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogActionType
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogDefaults
import com.example.inhabitroutine.core.presentation.ui.util.toDayMonthYearDisplay
import com.example.inhabitroutine.core.presentation.ui.util.toDisplay
import com.example.inhabitroutine.feature.view_schedule.config.enter_number_record.components.EnterTaskNumberRecordScreenEvent
import com.example.inhabitroutine.feature.view_schedule.config.enter_time_record.components.EnterTaskTimeRecordScreenEvent
import com.example.inhabitroutine.feature.view_schedule.config.enter_time_record.components.EnterTaskTimeRecordScreenResult
import com.example.inhabitroutine.feature.view_schedule.config.enter_time_record.components.EnterTaskTimeRecordScreenState

@Composable
fun EnterTaskTimeRecordDialog(
    stateHolder: EnterTaskTimeRecordStateHolder,
    onResult: (EnterTaskTimeRecordScreenResult) -> Unit
) {
    BaseDialogWithResult(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        EnterTaskTimeRecordDialogStateless(state, onEvent)
    }
}

@Composable
private fun EnterTaskTimeRecordDialogStateless(
    state: EnterTaskTimeRecordScreenState,
    onEvent: (EnterTaskTimeRecordScreenEvent) -> Unit
) {
    BaseDialog(
        onDismissRequest = { onEvent(EnterTaskTimeRecordScreenEvent.OnDismissRequest) },
        actionType = BaseDialogActionType.ConfirmDismiss(
            confirmButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.confirm),
                    onClick = { onEvent(EnterTaskTimeRecordScreenEvent.OnConfirmClick) }
                )
            },
            dismissButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.cancel),
                    onClick = { onEvent(EnterTaskTimeRecordScreenEvent.OnDismissRequest) }
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
            BaseTimeInput(
                initHours = state.inputHours,
                initMinutes = state.inputMinutes,
                onHoursUpdate = { onEvent(EnterTaskTimeRecordScreenEvent.OnInputHoursUpdate(it)) },
                onMinutesUpdate = { onEvent(EnterTaskTimeRecordScreenEvent.OnInputMinutesUpdate(it)) },
                modifier = Modifier.fillMaxWidth()
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