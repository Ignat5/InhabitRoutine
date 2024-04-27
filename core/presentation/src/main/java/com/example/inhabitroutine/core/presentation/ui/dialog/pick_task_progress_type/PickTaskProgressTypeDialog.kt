package com.example.inhabitroutine.core.presentation.ui.dialog.pick_task_progress_type

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialog
import com.example.inhabitroutine.core.presentation.ui.util.toIconId
import com.example.inhabitroutine.domain.model.task.type.TaskProgressType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickTaskProgressTypeDialog(
    allTaskProgressTypes: List<TaskProgressType>,
    onResult: (PickTaskProgressTypeScreenResult) -> Unit
) {
    BaseDialog(
        onDismissRequest = { onResult(PickTaskProgressTypeScreenResult.Dismiss) },
        dialogPadding = PaddingValues(vertical = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.habit_progress_type_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(allTaskProgressTypes) { item ->
                    ItemTaskProgressType(
                        taskType = item,
                        onClick = {
                            onResult(PickTaskProgressTypeScreenResult.Confirm(item))
                        }
                    )
                }
            }
        }
    }

//    BaseDialog(
//        onDismissRequest = { onResult(PickTaskProgressTypeScreenResult.Dismiss) },
//    ) {
//        Column(modifier = Modifier.fillMaxWidth()) {
//            Text(
//                text = stringResource(id = R.string.habit_progress_type_title),
//                style = MaterialTheme.typography.titleLarge,
//                color = MaterialTheme.colorScheme.onSurface
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            LazyColumn(modifier = Modifier.fillMaxWidth()) {
//                items(allTaskProgressTypes) { item ->
//                    ItemTaskProgressType(
//                        taskType = item,
//                        onClick = {
//                            onResult(PickTaskProgressTypeScreenResult.Confirm(item))
//                        }
//                    )
//                }
//            }
//        }
//    }
}

@Composable
private fun ItemTaskProgressType(
    taskType: TaskProgressType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
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

private fun TaskProgressType.toTitleStringId() = when (this) {
    TaskProgressType.YesNo -> R.string.habit_progress_type_yes_no_title
    TaskProgressType.Number -> R.string.habit_progress_type_number_title
    TaskProgressType.Time -> R.string.habit_progress_type_time_title
}

private fun TaskProgressType.toDescriptionStringId() = when (this) {
    TaskProgressType.YesNo -> R.string.habit_progress_type_yes_no_description
    TaskProgressType.Number -> R.string.habit_progress_type_number_description
    TaskProgressType.Time -> R.string.habit_progress_type_time_description
}