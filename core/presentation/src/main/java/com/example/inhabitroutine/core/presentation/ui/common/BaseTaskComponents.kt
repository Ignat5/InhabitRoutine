package com.example.inhabitroutine.core.presentation.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.core.presentation.ui.util.toDayMonthYearDisplay
import com.example.inhabitroutine.core.presentation.ui.util.toDisplay
import com.example.inhabitroutine.core.presentation.ui.util.toHourMinute
import com.example.inhabitroutine.core.presentation.ui.util.toIconId
import com.example.inhabitroutine.core.presentation.ui.util.toMonthDayYearDisplay
import com.example.inhabitroutine.domain.model.reminder.ReminderModel
import com.example.inhabitroutine.domain.model.task.content.TaskFrequency
import com.example.inhabitroutine.domain.model.task.type.TaskProgressType
import com.example.inhabitroutine.domain.model.task.type.TaskType
import kotlinx.datetime.LocalDate

@Composable
fun ChipTaskType(taskType: TaskType) {
    val iconId = remember(taskType) {
        taskType.toIconId()
    }
    BaseIconItem(iconResId = iconId)
}

@Composable
fun ChipTaskProgressType(taskProgressType: TaskProgressType) {
    val iconId = remember(taskProgressType) {
        taskProgressType.toIconId()
    }
    BaseIconItem(iconResId = iconId)
}

@Composable
fun ChipTaskReminders(allReminders: List<ReminderModel>) {
    if (allReminders.isEmpty()) return
    val text = remember(allReminders) {
        buildString {
            allReminders.forEachIndexed { index, item ->
                append(item.time.toHourMinute())
                if (index != allReminders.lastIndex) {
                    append(" • ")
                }
            }
        }
    }
    BaseDataItem(text = text)
}

@Composable
fun ChipTaskStartDate(date: LocalDate) {
    val dateText = remember(date) {
        date.toDayMonthYearDisplay()
    }
    BaseIconDataItem(
        iconResId = R.drawable.ic_start_date,
        text = dateText
    )
}

@Composable
fun ChipTaskEndDate(date: LocalDate) {
    val dateText = remember(date) {
        date.toDayMonthYearDisplay()
    }
    BaseIconDataItem(
        iconResId = R.drawable.ic_end_date,
        text = dateText
    )
}

@Composable
fun ChipTaskArchived() {
    BaseIconItem(
        iconResId = R.drawable.ic_archive,
    )
}

@Composable
fun ChipTaskFrequency(taskFrequency: TaskFrequency) {
    val context = LocalContext.current
    BaseIconDataItem(
        iconResId = R.drawable.ic_frequency,
        text = taskFrequency.toDisplay(context)
    )
}

@Composable
fun TaskDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(modifier = modifier.alpha(0.2f))
}

@Composable
private fun BaseDataItem(
    text: String
) {
    ItemDetailContainer {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun BaseIconDataItem(
    @DrawableRes iconResId: Int,
    text: String
) {
    ItemDetailContainer {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(iconResId),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun BaseIconItem(
    @DrawableRes iconResId: Int
) {
    ItemDetailContainer {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun ItemDetailContainer(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = MaterialTheme.shapes.extraSmall
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            ProvideContentColorTextStyle(
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                textStyle = MaterialTheme.typography.labelMedium
            ) {
                content()
            }
        }
    }
}

@Composable
private fun ProvideContentColorTextStyle(
    contentColor: Color,
    textStyle: TextStyle,
    content: @Composable () -> Unit
) {
    val mergedStyle = LocalTextStyle.current.merge(textStyle)
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalTextStyle provides mergedStyle,
        content = content
    )
}