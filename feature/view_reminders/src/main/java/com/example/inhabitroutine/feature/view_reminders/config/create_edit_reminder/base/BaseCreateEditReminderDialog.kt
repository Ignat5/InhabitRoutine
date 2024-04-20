package com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.core.presentation.ui.common.BaseDaysOfWeekInput
import com.example.inhabitroutine.core.presentation.ui.common.BaseTimePicker
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialog
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogActionType
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogDefaults
import com.example.inhabitroutine.core.presentation.ui.util.toDisplay
import com.example.inhabitroutine.core.presentation.ui.util.toTitleResId
import com.example.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.example.inhabitroutine.domain.model.reminder.type.ReminderType
import com.example.inhabitroutine.domain.model.task.content.TaskFrequency
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.components.BaseCreateEditReminderScreenEvent
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.components.BaseCreateEditReminderScreenState
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.model.ReminderScheduleType
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.model.type
import kotlinx.datetime.DayOfWeek

@Composable
internal fun BaseCreateEditReminderDialog(
    title: @Composable () -> Unit,
    state: BaseCreateEditReminderScreenState,
    onEvent: (BaseCreateEditReminderScreenEvent) -> Unit
) {
    BaseDialog(
        onDismissRequest = { onEvent(BaseCreateEditReminderScreenEvent.OnDismissRequest) },
        properties = DialogProperties(dismissOnClickOutside = false),
        title = title,
        actionType = BaseDialogActionType.ConfirmDismiss(
            confirmButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.confirm),
                    enabled = state.canConfirm,
                    onClick = {
                        onEvent(BaseCreateEditReminderScreenEvent.OnConfirmClick)
                    }
                )
            },
            dismissButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.cancel),
                    onClick = {
                        onEvent(BaseCreateEditReminderScreenEvent.OnDismissRequest)
                    }
                )
            }
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            SectionTitle(
                stringResId = R.string.reminder_time_title,
                modifier = Modifier.fillMaxWidth()
            )
            AfterTitleSpacer()
            BaseTimePicker(
                initHours = state.inputHours,
                initMinutes = state.inputMinutes,
                onHoursUpdate = {
                    onEvent(BaseCreateEditReminderScreenEvent.OnInputHoursUpdate(it))
                },
                onMinutesUpdate = {
                    onEvent(BaseCreateEditReminderScreenEvent.OnInputMinutesUpdate(it))
                },
                modifier = Modifier.fillMaxWidth()
            )
            AfterSectionSpacer()
            SectionTitle(stringResId = R.string.reminder_type_title)
            AfterTitleSpacer()
            PickReminderTypeSection(
                currentReminderType = state.inputReminderType,
                onPickReminderType = {
                    onEvent(BaseCreateEditReminderScreenEvent.OnPickReminderType(it))
                }
            )
            AfterSectionSpacer()
            SectionTitle(stringResId = R.string.reminder_schedule_title)
            AfterTitleSpacer()
            PickReminderScheduleSection(
                currentReminderSchedule = state.inputReminderSchedule,
                onPickReminderScheduleType = {
                    onEvent(BaseCreateEditReminderScreenEvent.OnPickReminderScheduleType(it))
                },
                onDayOfWeekClick = {
                    onEvent(BaseCreateEditReminderScreenEvent.OnDayOfWeekClick(it))
                }
            )
        }
    }
}

@Composable
private fun PickReminderTypeSection(
    currentReminderType: ReminderType,
    onPickReminderType: (ReminderType) -> Unit
) {
    val allReminderTypes = remember { ReminderType.entries }
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(
            items = allReminderTypes
        ) { item ->
            val isSelected = remember(currentReminderType) {
                item == currentReminderType
            }
            ItemReminderType(
                item = item,
                isSelected = isSelected,
                onClick = {
                    onPickReminderType(item)
                }
            )
        }
    }
}

@Composable
private fun PickReminderScheduleSection(
    currentReminderSchedule: ReminderSchedule,
    onPickReminderScheduleType: (ReminderScheduleType) -> Unit,
    onDayOfWeekClick: (DayOfWeek) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val allTypes = remember { ReminderScheduleType.entries }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(allTypes) { item ->
                val isSelected = remember(currentReminderSchedule) {
                    item == currentReminderSchedule.type
                }
                ItemReminderScheduleType(
                    item = item,
                    isSelected = isSelected,
                    onClick = {
                        onPickReminderScheduleType(item)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        DaysOfWeekRow(
            schedule = currentReminderSchedule,
            onDayOfWeekClick = onDayOfWeekClick
        )
    }
}

@Composable
private fun DaysOfWeekRow(
    schedule: ReminderSchedule,
    onDayOfWeekClick: (DayOfWeek) -> Unit
) {
    val scheduleDaysOfWeek = remember(schedule) {
        schedule as? ReminderSchedule.DaysOfWeek
    }
    AnimatedVisibility(
        visible = scheduleDaysOfWeek != null,
        enter = fadeIn(
            animationSpec = spring(stiffness = Spring.StiffnessHigh)
        ),
        exit = fadeOut(
            animationSpec = spring(stiffness = Spring.StiffnessHigh)
        )
    ) {
        scheduleDaysOfWeek?.daysOfWeek?.let { selectedDaysOfWeek ->
            BaseDaysOfWeekInput(
                selectedDaysOfWeek = selectedDaysOfWeek,
                onDayOfWeekClick = onDayOfWeekClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ItemReminderScheduleType(
    item: ReminderScheduleType,
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
                .padding(8.dp),
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

@Composable
private fun ItemReminderType(
    item: ReminderType,
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
                .padding(8.dp),
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

@Composable
private fun SectionTitle(
    @StringRes stringResId: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = stringResId),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

private fun ReminderScheduleType.toTitleResId() = when (this) {
    ReminderScheduleType.AlwaysEnabled -> R.string.reminder_schedule_always_enabled_title
    ReminderScheduleType.DaysOfWeek -> R.string.reminder_schedule_days_of_week
}

@Composable
private fun AfterTitleSpacer() {
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun AfterSectionSpacer() {
    Spacer(modifier = Modifier.height(16.dp))
}