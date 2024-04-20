package com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.core.presentation.ui.base.BaseDialogWithResult
import com.example.inhabitroutine.core.presentation.ui.common.BaseDaysOfWeekInput
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialog
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogActionType
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogDefaults
import com.example.inhabitroutine.domain.model.task.content.TaskFrequency
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.components.PickTaskFrequencyScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.components.PickTaskFrequencyScreenResult
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.components.PickTaskFrequencyScreenState
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.model.TaskFrequencyType
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.model.type
import kotlinx.datetime.DayOfWeek

@Composable
internal fun PickTaskFrequencyDialog(
    stateHolder: PickTaskFrequencyStateHolder,
    onResult: (PickTaskFrequencyScreenResult) -> Unit
) {
    BaseDialogWithResult(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        PickTaskFrequencyDialogStateless(state, onEvent)
    }
}

@Composable
private fun PickTaskFrequencyDialogStateless(
    state: PickTaskFrequencyScreenState,
    onEvent: (PickTaskFrequencyScreenEvent) -> Unit
) {
    BaseDialog(
        onDismissRequest = { onEvent(PickTaskFrequencyScreenEvent.OnDismissRequest) },
        properties = DialogProperties(dismissOnClickOutside = false),
        title = {
            BaseDialogDefaults.BaseDialogTitle(titleText = stringResource(id = R.string.task_config_frequency))
        },
        actionType = BaseDialogActionType.ConfirmDismiss(
            confirmButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.confirm),
                    enabled = state.canConfirm,
                    onClick = { onEvent(PickTaskFrequencyScreenEvent.OnConfirmClick) }
                )
            },
            dismissButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.cancel),
                    onClick = { onEvent(PickTaskFrequencyScreenEvent.OnDismissRequest) }
                )
            }
        )
    ) {
        val allItems = remember { TaskFrequencyType.entries }
        val onItemClick: (TaskFrequencyType) -> Unit = remember {
            val callback: (TaskFrequencyType) -> Unit = {
                onEvent(PickTaskFrequencyScreenEvent.OnFrequencyTypeClick(it))
            }
            callback
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(allItems) { item ->
                val onClick: () -> Unit = remember {
                    val callback: () -> Unit = {
                        onItemClick(item)
                    }
                    callback
                }
                val isSelected = remember(state.inputTaskFrequency.type) {
                    item == state.inputTaskFrequency.type
                }
                ItemFrequency(
                    item = item,
                    isSelected = isSelected,
                    onClick = onClick
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        DaysOfWeekRow(
            frequency = state.inputTaskFrequency,
            onDayOfWeekClick = {
                onEvent(PickTaskFrequencyScreenEvent.OnDayOfWeekClick(it))
            }
        )
    }
}

@Composable
private fun DaysOfWeekRow(
    frequency: TaskFrequency,
    onDayOfWeekClick: (DayOfWeek) -> Unit
) {
    val freqDaysOfWeek = remember(frequency) {
        frequency as? TaskFrequency.DaysOfWeek
    }
    AnimatedVisibility(
        visible = freqDaysOfWeek != null,
        enter = fadeIn(
            animationSpec = spring(stiffness = Spring.StiffnessHigh)
        ),
        exit = fadeOut(
            animationSpec = spring(stiffness = Spring.StiffnessHigh)
        )
    ) {
        freqDaysOfWeek?.daysOfWeek?.let { selectedDaysOfWeek ->
            BaseDaysOfWeekInput(
                selectedDaysOfWeek = selectedDaysOfWeek,
                onDayOfWeekClick = onDayOfWeekClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ItemFrequency(
    item: TaskFrequencyType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val titleResId = remember {
                item.toTitleResId()
            }
            RadioButton(
                selected = isSelected,
                onClick = null
            )
            Text(
                text = stringResource(id = titleResId),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private fun TaskFrequencyType.toTitleResId() = when (this) {
    TaskFrequencyType.EveryDay -> R.string.frequency_every_day
    TaskFrequencyType.DaysOfWeek -> R.string.frequency_days_of_week
}