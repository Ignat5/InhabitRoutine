package com.example.inhabitroutine.core.presentation.ui.dialog.pick_task_type

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseModalBottomSheetDialog
import com.example.inhabitroutine.core.presentation.ui.util.toIconId
import com.example.inhabitroutine.core.presentation.ui.util.toTitleStringId
import com.example.inhabitroutine.domain.model.task.type.TaskType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickTaskTypeDialog(
    allTaskTypes: List<TaskType>,
    onResult: (PickTaskTypeScreenResult) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    BaseModalBottomSheetDialog(
        sheetState = sheetState,
        dragHandle = null,
        onDismissRequest = { onResult(PickTaskTypeScreenResult.Dismiss) }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(allTaskTypes) { item ->
                ItemTaskType(
                    taskType = item,
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                            onResult(PickTaskTypeScreenResult.Confirm(item))
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ItemTaskType(
    taskType: TaskType,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconId = remember {
                taskType.toIconId()
            }
            Icon(
                painter = painterResource(iconId),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                val titleId = remember {
                    taskType.toTitleStringId()
                }
                Text(
                    text = stringResource(titleId),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
                val descriptionId = remember {
                    taskType.toDescriptionStringId()
                }
                Text(
                    text = stringResource(descriptionId),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private fun TaskType.toDescriptionStringId() = when (this) {
    TaskType.Habit -> R.string.habit_description
    TaskType.RecurringTask -> R.string.recurring_task_description
    TaskType.SingleTask -> R.string.single_task_description
}