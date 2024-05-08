package com.ignatlegostaev.inhabitroutine.presentation.view_reminders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ignatlegostaev.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.type.ReminderType
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.DeleteReminderByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ReadRemindersByTaskIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SaveReminderUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.UpdateReminderUseCase
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.ViewRemindersViewModel
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenState
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.components.CreateReminderScreenResult
import com.ignatlegostaev.inhabitroutine.navigation.AppNavDest
import com.ignatlegostaev.inhabitroutine.presentation.base.BaseAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AndroidViewRemindersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    readRemindersByTaskIdUseCase: ReadRemindersByTaskIdUseCase,
    saveReminderUseCase: SaveReminderUseCase,
    updateReminderUseCase: UpdateReminderUseCase,
    deleteReminderByIdUseCase: DeleteReminderByIdUseCase,
    @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
) : BaseAndroidViewModel<ViewRemindersScreenEvent, ViewRemindersScreenState, ViewRemindersScreenNavigation, ViewRemindersScreenConfig>() {

    private val _checkNotificationPermissionChannel = Channel<Unit>()
    val checkNotificationPermissionChannel: ReceiveChannel<Unit> =
        _checkNotificationPermissionChannel

    override val delegateViewModel = ViewRemindersViewModel(
        taskId = checkNotNull(savedStateHandle.get<String>(AppNavDest.TASK_ID_KEY)),
        readRemindersByTaskIdUseCase = readRemindersByTaskIdUseCase,
        saveReminderUseCase = saveReminderUseCase,
        updateReminderUseCase = updateReminderUseCase,
        deleteReminderByIdUseCase = deleteReminderByIdUseCase,
        defaultDispatcher = defaultDispatcher,
        viewModelScope = viewModelScope
    )

    override fun onEvent(event: ViewRemindersScreenEvent) {
        super.onEvent(event)
        when (event) {
            is ViewRemindersScreenEvent.ResultEvent.CreateReminder -> {
                onCreateReminderResultEvent(event)
            }

            else -> Unit
        }
    }

    private fun onCreateReminderResultEvent(event: ViewRemindersScreenEvent.ResultEvent.CreateReminder) {
        when (val result = event.result) {
            is CreateReminderScreenResult.Confirm -> onConfirmCreateReminder(result)
            is CreateReminderScreenResult.Dismiss -> Unit
        }
    }

    private fun onConfirmCreateReminder(result: CreateReminderScreenResult.Confirm) {
        if (result.reminderType in ReminderType.allNotifiableTypes) {
            viewModelScope.launch {
                _checkNotificationPermissionChannel.send(Unit)
            }
        }
    }

}