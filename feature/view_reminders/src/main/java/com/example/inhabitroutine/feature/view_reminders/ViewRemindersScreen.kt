package com.example.inhabitroutine.feature.view_reminders

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.core.presentation.ui.common.BaseSnackBar
import com.example.inhabitroutine.core.presentation.ui.util.toDisplay
import com.example.inhabitroutine.core.presentation.ui.util.toHourMinute
import com.example.inhabitroutine.core.presentation.ui.util.toIconResId
import com.example.inhabitroutine.domain.model.reminder.ReminderModel
import com.example.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenConfig
import com.example.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenEvent
import com.example.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenState
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.CreateReminderDialog
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit.EditReminderDialog
import com.example.inhabitroutine.feature.view_reminders.config.delete_reminder.ConfirmDeleteReminderDialog
import com.example.inhabitroutine.feature.view_reminders.model.ViewRemindersMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewRemindersScreen(
    state: ViewRemindersScreenState,
    onEvent: (ViewRemindersScreenEvent) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    BackHandler { onEvent(ViewRemindersScreenEvent.OnLeaveRequest) }
    Scaffold(
        topBar = {
            ScreenTopBar(
                onBackClick = {
                    onEvent(ViewRemindersScreenEvent.OnLeaveRequest)
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { data ->
                    BaseSnackBar(data)
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(
                    items = state.allReminders,
                    key = { _, item -> item.id },
                    contentType = { _, _ -> ItemType.Reminder }
                ) { index, item ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        ItemReminder(
                            item = item,
                            onClick = {
                                onEvent(ViewRemindersScreenEvent.OnReminderClick(item.id))
                            },
                            onDeleteClick = {
                                onEvent(ViewRemindersScreenEvent.OnDeleteReminderClick(item.id))
                            },
                            modifier = Modifier.animateItemPlacement()
                        )
                        if (index != state.allReminders.lastIndex) {
                            HorizontalDivider()
                        }
                    }
                }
                item(
                    key = ItemType.Create,
                    contentType = { ItemType.Create }
                ) {
                    ItemCreateReminder(
                        onClick = {
                            onEvent(ViewRemindersScreenEvent.OnCreateReminderClick)
                        }
                    )
                }
            }
        }
        SnackBarMessageHandler(
            message = state.message,
            snackBarHostState = snackBarHostState,
            onMessageShown = {
                onEvent(ViewRemindersScreenEvent.OnMessageShown)
            }
        )
    }
}

@Composable
private fun SnackBarMessageHandler(
    message: ViewRemindersMessage,
    snackBarHostState: SnackbarHostState,
    onMessageShown: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(message) {
        when (message) {
            is ViewRemindersMessage.Idle -> Unit
            is ViewRemindersMessage.Message -> {
                val messageTextResId = when (message) {
                    is ViewRemindersMessage.Message.CreateReminderSuccess ->
                        R.string.reminder_create_success_message

                    is ViewRemindersMessage.Message.EditReminderSuccess ->
                        R.string.reminder_edit_success_message

                    is ViewRemindersMessage.Message.DeleteReminderSuccess ->
                        R.string.reminder_delete_success_message

                    is ViewRemindersMessage.Message.FailureDueToOverlap ->
                        R.string.reminder_failure_overlap_message
                }
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = context.getString(messageTextResId),
                        duration = SnackbarDuration.Short
                    )
                }
                onMessageShown()
            }
        }
    }
}

@Composable
fun ViewRemindersConfig(
    config: ViewRemindersScreenConfig,
    onEvent: (ViewRemindersScreenEvent) -> Unit
) {
    when (config) {
        is ViewRemindersScreenConfig.CreateReminder -> {
            CreateReminderDialog(stateHolder = config.stateHolder) {
                onEvent(ViewRemindersScreenEvent.ResultEvent.CreateReminder(it))
            }
        }

        is ViewRemindersScreenConfig.EditReminder -> {
            EditReminderDialog(stateHolder = config.stateHolder) {
                onEvent(ViewRemindersScreenEvent.ResultEvent.EditReminder(it))
            }
        }

        is ViewRemindersScreenConfig.DeleteReminder -> {
            ConfirmDeleteReminderDialog(stateHolder = config.stateHolder) {
                onEvent(ViewRemindersScreenEvent.ResultEvent.DeleteReminder(it))
            }
        }
    }
}

@Composable
private fun ItemReminder(
    item: ReminderModel,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val iconResId = remember(item) {
                item.type.toIconResId()
            }
            Icon(
                painter = painterResource(iconResId),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                val time = remember(item) {
                    item.time.toHourMinute()
                }
                Text(
                    text = time,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                val scheduleText = remember(item) {
                    item.schedule.toDisplay(context)
                }
                Text(
                    text = scheduleText,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun ItemCreateReminder(
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(id = R.string.new_reminder_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

private enum class ItemType {
    Reminder,
    Create
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenTopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.view_reminders_screen_title))
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = null)
            }
        }
    )
}