package com.example.inhabitroutine.feature.create_edit_task.base

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_date.PickDateDialog
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_date.components.PickDateScreenResult
import com.example.inhabitroutine.core.presentation.ui.util.toDayMonthYearDisplay
import com.example.inhabitroutine.core.presentation.ui.util.toDisplay
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenConfig
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_description.PickTaskDescriptionDialog
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.PickTaskFrequencyDialog
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_number_progress.PickTaskNumberProgressDialog
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_time_progress.PickTaskTimeProgressDialog
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_title.PickTaskTitleDialog
import com.example.inhabitroutine.feature.create_edit_task.base.model.BaseItemTaskConfig

@Composable
internal fun BaseCreateEditTaskConfig(
    config: BaseCreateEditTaskScreenConfig,
    onEvent: (BaseCreateEditTaskScreenEvent) -> Unit
) {
    when (config) {
        is BaseCreateEditTaskScreenConfig.PickTaskTitle -> {
            PickTaskTitleDialog(
                stateHolder = config.stateHolder,
                onResult = {
                    onEvent(BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskTitle(it))
                }
            )
        }

        is BaseCreateEditTaskScreenConfig.PickDate -> {
            when (config) {
                is BaseCreateEditTaskScreenConfig.PickDate.Date -> {
                    PickDateDialog(stateHolder = config.stateHolder) {
                        onEvent(
                            BaseCreateEditTaskScreenEvent.ResultEvent.PickDate.Date(it)
                        )
                    }
                }

                is BaseCreateEditTaskScreenConfig.PickDate.StartDate -> {
                    PickDateDialog(stateHolder = config.stateHolder) {
                        onEvent(
                            BaseCreateEditTaskScreenEvent.ResultEvent.PickDate.StartDate(it)
                        )
                    }
                }

                is BaseCreateEditTaskScreenConfig.PickDate.EndDate -> {
                    PickDateDialog(stateHolder = config.stateHolder) {
                        onEvent(
                            BaseCreateEditTaskScreenEvent.ResultEvent.PickDate.EndDate(it)
                        )
                    }
                }
            }
        }

        is BaseCreateEditTaskScreenConfig.PickTaskFrequency -> {
            PickTaskFrequencyDialog(stateHolder = config.stateHolder) {
                onEvent(BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskFrequency(it))
            }
        }

        is BaseCreateEditTaskScreenConfig.PickTaskNumberProgress -> {
            PickTaskNumberProgressDialog(
                stateHolder = config.stateHolder,
                onResult = {
                    onEvent(BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskNumberProgress(it))
                }
            )
        }

        is BaseCreateEditTaskScreenConfig.PickTaskTimeProgress -> {
            PickTaskTimeProgressDialog(
                stateHolder = config.stateHolder,
                onResult = {
                    onEvent(BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskTimeProgress(it))
                }
            )
        }

        is BaseCreateEditTaskScreenConfig.PickTaskDescription -> {
            PickTaskDescriptionDialog(stateHolder = config.stateHolder) {
                onEvent(BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskDescription(it))
            }
        }
    }
}

internal fun LazyListScope.baseConfigItems(
    allTaskConfigItems: List<BaseItemTaskConfig>,
    onItemClick: (BaseItemTaskConfig) -> Unit
) {
    itemsIndexed(
        items = allTaskConfigItems,
        key = { _, item -> item.key },
        contentType = { _, item -> item.contentType }
    ) { index, item ->
        val onClick: () -> Unit = remember {
            val callback: () -> Unit = { onItemClick(item) }
            callback
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            when (item.key) {
                BaseItemTaskConfig.Key.Title -> {
                    ItemTitleConfig(
                        item = (item as BaseItemTaskConfig.Title),
                        onClick = onClick
                    )
                }

                BaseItemTaskConfig.Key.Description -> {
                    ItemDescriptionConfig(
                        item = (item as BaseItemTaskConfig.Description),
                        onClick = onClick
                    )
                }

                BaseItemTaskConfig.Key.Progress -> {
                    ItemProgressConfig(
                        item = (item as BaseItemTaskConfig.Progress),
                        onClick = onClick
                    )
                }

                BaseItemTaskConfig.Key.Frequency -> {
                    ItemFrequencyConfig(
                        item = (item as BaseItemTaskConfig.Frequency),
                        onClick = onClick
                    )
                }

                BaseItemTaskConfig.Key.Date -> {
                    ItemDateConfig(
                        item = (item as BaseItemTaskConfig.DateConfig.Date),
                        onClick = onClick
                    )
                }

                BaseItemTaskConfig.Key.StartDate -> {
                    ItemStartDateConfig(
                        item = (item as BaseItemTaskConfig.DateConfig.StartDate),
                        onClick = onClick
                    )
                }

                BaseItemTaskConfig.Key.EndDate -> {
                    ItemEndDateConfig(
                        item = (item as BaseItemTaskConfig.DateConfig.EndDate),
                        onClick = onClick
                    )
                }

                BaseItemTaskConfig.Key.Reminders -> {
                    ItemRemindersConfig(
                        item = (item as BaseItemTaskConfig.Reminders),
                        onClick = onClick
                    )
                }
            }
            if (index != allTaskConfigItems.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
internal fun ItemTitleConfig(
    item: BaseItemTaskConfig.Title,
    onClick: () -> Unit
) {
    BasicTextItemConfig(
        iconResId = com.example.inhabitroutine.core.presentation.R.drawable.ic_edit,
        titleResId = com.example.inhabitroutine.core.presentation.R.string.task_config_title,
        text = item.title,
        onClick = onClick
    )
}

@Composable
internal fun ItemDescriptionConfig(
    item: BaseItemTaskConfig.Description,
    onClick: () -> Unit
) {
    BasicTextItemConfig(
        iconResId = com.example.inhabitroutine.core.presentation.R.drawable.ic_description,
        titleResId = com.example.inhabitroutine.core.presentation.R.string.task_config_description,
        text = item.description,
        onClick = onClick
    )
}

@Composable
internal fun ItemProgressConfig(
    item: BaseItemTaskConfig.Progress,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val text = remember(item) {
        when (item) {
            is BaseItemTaskConfig.Progress.Number -> {
                item.taskProgress.toDisplay(context)
            }

            is BaseItemTaskConfig.Progress.Time -> {
                item.taskProgress.toDisplay(context)
            }
        }
    }
    BasicTextItemConfig(
        iconResId = com.example.inhabitroutine.core.presentation.R.drawable.ic_goal,
        titleResId = com.example.inhabitroutine.core.presentation.R.string.task_config_progress,
        text = text,
        onClick = onClick
    )
}

@Composable
internal fun ItemFrequencyConfig(
    item: BaseItemTaskConfig.Frequency,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val text = remember(item) {
        item.taskFrequency.toDisplay(context)
    }
    BasicTextItemConfig(
        iconResId = com.example.inhabitroutine.core.presentation.R.drawable.ic_frequency,
        titleResId = com.example.inhabitroutine.core.presentation.R.string.task_config_frequency,
        text = text,
        onClick = onClick
    )
}

@Composable
internal fun ItemDateConfig(
    item: BaseItemTaskConfig.DateConfig.Date,
    onClick: () -> Unit
) {
    val text = remember(item) {
        item.date.toDayMonthYearDisplay()
    }
    BasicTextItemConfig(
        iconResId = com.example.inhabitroutine.core.presentation.R.drawable.ic_start_date,
        titleResId = com.example.inhabitroutine.core.presentation.R.string.task_config_date,
        text = text,
        onClick = onClick
    )
}

@Composable
internal fun ItemStartDateConfig(
    item: BaseItemTaskConfig.DateConfig.StartDate,
    onClick: () -> Unit
) {
    val text = remember(item) {
        item.date.toDayMonthYearDisplay()
    }
    BasicTextItemConfig(
        iconResId = com.example.inhabitroutine.core.presentation.R.drawable.ic_start_date,
        titleResId = com.example.inhabitroutine.core.presentation.R.string.task_config_start_date,
        text = text,
        onClick = onClick
    )
}

@Composable
internal fun ItemEndDateConfig(
    item: BaseItemTaskConfig.DateConfig.EndDate,
    onClick: () -> Unit
) {
    val isChecked = remember (item) {
        item.date != null
    }
    val text = remember(item) {
        item.date?.toDayMonthYearDisplay() ?: ""
    }
    BasicSwitchItemConfig(
        iconResId = com.example.inhabitroutine.core.presentation.R.drawable.ic_end_date,
        titleResId = com.example.inhabitroutine.core.presentation.R.string.task_config_end_date,
        text = text,
        isChecked = isChecked,
        onClick = onClick
    )
}

@Composable
internal fun ItemRemindersConfig(
    item: BaseItemTaskConfig.Reminders,
    onClick: () -> Unit
) {
    BasicTextItemConfig(
        iconResId = com.example.inhabitroutine.core.presentation.R.drawable.ic_notification,
        titleResId = com.example.inhabitroutine.core.presentation.R.string.task_config_reminders,
        text = "${item.count}",
        onClick = onClick
    )
}

@Composable
internal fun BasicSwitchItemConfig(
    @DrawableRes iconResId: Int,
    @StringRes titleResId: Int,
    text: String,
    isChecked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseItemConfigContainer(
        iconResId = iconResId,
        titleResId = titleResId,
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                modifier = Modifier
                    .weight(1f),
                textAlign = TextAlign.End,
                overflow = TextOverflow.Ellipsis
            )
            Switch(checked = isChecked, onCheckedChange = null)
        }
    }
}

@Composable
internal fun BasicTextItemConfig(
    @DrawableRes iconResId: Int,
    @StringRes titleResId: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseItemConfigContainer(
        iconResId = iconResId,
        titleResId = titleResId,
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.End,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
internal fun BaseItemConfigContainer(
    @DrawableRes iconResId: Int,
    @StringRes titleResId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier.clickable { onClick() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
            Text(
                text = stringResource(id = titleResId),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Box(modifier = Modifier.weight(1f)) {
                content()
            }
        }
    }
}