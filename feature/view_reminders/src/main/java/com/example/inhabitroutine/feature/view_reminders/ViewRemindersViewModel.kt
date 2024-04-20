package com.example.inhabitroutine.feature.view_reminders

import com.example.inhabitroutine.core.presentation.base.BaseViewModel
import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.domain.reminder.api.DeleteReminderByIdUseCase
import com.example.inhabitroutine.domain.reminder.api.ReadRemindersByTaskIdUseCase
import com.example.inhabitroutine.domain.reminder.api.SaveReminderUseCase
import com.example.inhabitroutine.domain.reminder.api.UpdateReminderUseCase
import com.example.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenConfig
import com.example.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenEvent
import com.example.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenNavigation
import com.example.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenState
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.CreateReminderStateHolder
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.components.CreateReminderScreenResult
import com.example.inhabitroutine.feature.view_reminders.model.ViewRemindersMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewRemindersViewModel(
    private val taskId: String,
    private val readRemindersByTaskIdUseCase: ReadRemindersByTaskIdUseCase,
    private val saveReminderUseCase: SaveReminderUseCase,
    private val updateReminderUseCase: UpdateReminderUseCase,
    private val deleteReminderByIdUseCase: DeleteReminderByIdUseCase,
    override val viewModelScope: CoroutineScope
) : BaseViewModel<ViewRemindersScreenEvent, ViewRemindersScreenState, ViewRemindersScreenNavigation, ViewRemindersScreenConfig>() {

    private val allRemindersState = readRemindersByTaskIdUseCase(taskId)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    private val messageState = MutableStateFlow<ViewRemindersMessage>(ViewRemindersMessage.Idle)

    override val uiScreenState: StateFlow<ViewRemindersScreenState> =
        combine(
            allRemindersState,
            messageState
        ) { allReminders, message ->
            ViewRemindersScreenState(
                allReminders = allReminders,
                message = message
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ViewRemindersScreenState(
                allReminders = allRemindersState.value,
                message = messageState.value
            )
        )

    override fun onEvent(event: ViewRemindersScreenEvent) {
        when (event) {
            is ViewRemindersScreenEvent.ResultEvent ->
                onResultEvent(event)

            is ViewRemindersScreenEvent.OnCreateReminderClick ->
                onCreateReminderClick()

            is ViewRemindersScreenEvent.OnMessageShown ->
                onMessageShown()

            is ViewRemindersScreenEvent.OnLeaveRequest ->
                onLeaveRequest()
        }
    }

    private fun onResultEvent(event: ViewRemindersScreenEvent.ResultEvent) {
        when (event) {
            is ViewRemindersScreenEvent.ResultEvent.CreateReminder ->
                onCreateReminderResultEvent(event)
        }
    }

    private fun onCreateReminderResultEvent(event: ViewRemindersScreenEvent.ResultEvent.CreateReminder) {
        onIdleToAction {
            when (val result = event.result) {
                is CreateReminderScreenResult.Confirm ->
                    onConfirmCreateReminder(result)

                is CreateReminderScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmCreateReminder(result: CreateReminderScreenResult.Confirm) {
        viewModelScope.launch {
            val resultModel = saveReminderUseCase(
                taskId = taskId,
                time = result.reminderTime,
                type = result.reminderType,
                schedule = result.reminderSchedule
            )
            when (resultModel) {
                is ResultModel.Success -> {
                    messageState.update { ViewRemindersMessage.Message.CreateReminderSuccess }
                }

                is ResultModel.Failure -> {
                    if (resultModel.value is SaveReminderUseCase.SaveReminderFailure.Overlap) {
                        messageState.update {
                            ViewRemindersMessage.Message.CreateReminderFailureDueToOverlap
                        }
                    }
                }
            }
        }
    }

    private fun onCreateReminderClick() {
        setUpConfigState(
            ViewRemindersScreenConfig.CreateReminder(
                stateHolder = CreateReminderStateHolder(
                    holderScope = provideChildScope()
                )
            )
        )
    }

    private fun onMessageShown() {
        messageState.update { ViewRemindersMessage.Idle }
    }

    private fun onLeaveRequest() {
        setUpNavigationState(ViewRemindersScreenNavigation.Back)
    }

}