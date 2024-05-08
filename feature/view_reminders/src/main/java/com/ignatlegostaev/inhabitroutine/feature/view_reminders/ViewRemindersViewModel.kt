package com.ignatlegostaev.inhabitroutine.feature.view_reminders

import com.ignatlegostaev.inhabitroutine.core.presentation.base.BaseViewModel
import com.ignatlegostaev.inhabitroutine.core.presentation.model.UIResultModel
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.DeleteReminderByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ReadRemindersByTaskIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SaveReminderUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.UpdateReminderUseCase
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenState
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.CreateReminderStateHolder
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.components.CreateReminderScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit.EditReminderStateHolder
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit.components.EditReminderScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.delete_reminder.DeleteReminderStateHolder
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.delete_reminder.components.DeleteReminderScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.model.ViewRemindersMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewRemindersViewModel(
    private val taskId: String,
    private val readRemindersByTaskIdUseCase: ReadRemindersByTaskIdUseCase,
    private val saveReminderUseCase: SaveReminderUseCase,
    private val updateReminderUseCase: UpdateReminderUseCase,
    private val deleteReminderByIdUseCase: DeleteReminderByIdUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
    override val viewModelScope: CoroutineScope
) : BaseViewModel<ViewRemindersScreenEvent, ViewRemindersScreenState, ViewRemindersScreenNavigation, ViewRemindersScreenConfig>() {

    private val allRemindersState = readRemindersByTaskIdUseCase(taskId)
        .map { allReminders ->
            UIResultModel.Data(
                if (allReminders.isNotEmpty()) {
                    withContext(defaultDispatcher) {
                        allReminders.sortedBy { it.time }
                    }
                } else emptyList()
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            UIResultModel.Loading(emptyList())
        )

    private val messageState = MutableStateFlow<ViewRemindersMessage>(ViewRemindersMessage.Idle)

    override val uiScreenState: StateFlow<ViewRemindersScreenState> =
        combine(
            allRemindersState,
            messageState
        ) { allReminders, message ->
            ViewRemindersScreenState(
                allRemindersResult = allReminders,
                message = message
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ViewRemindersScreenState(
                allRemindersResult = allRemindersState.value,
                message = messageState.value
            )
        )

    override fun onEvent(event: ViewRemindersScreenEvent) {
        when (event) {
            is ViewRemindersScreenEvent.ResultEvent ->
                onResultEvent(event)

            is ViewRemindersScreenEvent.OnReminderClick ->
                onReminderClick(event)

            is ViewRemindersScreenEvent.OnCreateReminderClick ->
                onCreateReminderClick()

            is ViewRemindersScreenEvent.OnDeleteReminderClick ->
                onDeleteReminder(event)

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

            is ViewRemindersScreenEvent.ResultEvent.EditReminder ->
                onEditReminderResultEvent(event)

            is ViewRemindersScreenEvent.ResultEvent.DeleteReminder ->
                onDeleteReminderResultEvent(event)
        }
    }

    private fun onDeleteReminderResultEvent(event: ViewRemindersScreenEvent.ResultEvent.DeleteReminder) {
        onIdleToAction {
            when (val result = event.result) {
                is DeleteReminderScreenResult.Confirm -> onConfirmDeleteReminder(result)
                is DeleteReminderScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmDeleteReminder(result: DeleteReminderScreenResult.Confirm) {
        viewModelScope.launch {
            val resultModel = deleteReminderByIdUseCase(result.reminderId)
            if (resultModel is ResultModel.Success) {
                messageState.update { ViewRemindersMessage.Message.DeleteReminderSuccess }
            }
        }
    }

    private fun onEditReminderResultEvent(event: ViewRemindersScreenEvent.ResultEvent.EditReminder) {
        onIdleToAction {
            when (val result = event.result) {
                is EditReminderScreenResult.Confirm ->
                    onConfirmEditReminder(result)

                is EditReminderScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmEditReminder(result: EditReminderScreenResult.Confirm) {
        viewModelScope.launch {
            val resultModel = updateReminderUseCase(
                reminderModel = result.reminderModel
            )
            when (resultModel) {
                is ResultModel.Success -> {
                    messageState.update { ViewRemindersMessage.Message.EditReminderSuccess }
                }

                is ResultModel.Failure -> {
                    if (resultModel.value is UpdateReminderUseCase.UpdateReminderFailure.Overlap) {
                        messageState.update { ViewRemindersMessage.Message.FailureDueToOverlap }
                    }
                }
            }
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
                            ViewRemindersMessage.Message.FailureDueToOverlap
                        }
                    }
                }
            }
        }
    }

    private fun onReminderClick(event: ViewRemindersScreenEvent.OnReminderClick) {
        allRemindersState.value.data?.find { it.id == event.reminderId }?.let { reminderModel ->
            setUpConfigState(
                ViewRemindersScreenConfig.EditReminder(
                    stateHolder = EditReminderStateHolder(
                        reminderModel = reminderModel,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onDeleteReminder(event: ViewRemindersScreenEvent.OnDeleteReminderClick) {
        setUpConfigState(
            ViewRemindersScreenConfig.DeleteReminder(
                stateHolder = DeleteReminderStateHolder(
                    reminderId = event.reminderId,
                    holderScope = provideChildScope()
                )
            )
        )
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