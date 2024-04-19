package com.example.inhabitroutine.feature.view_reminders

import com.example.inhabitroutine.core.presentation.base.BaseViewModel
import com.example.inhabitroutine.domain.reminder.api.ReadRemindersByTaskIdUseCase
import com.example.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenConfig
import com.example.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenEvent
import com.example.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenNavigation
import com.example.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ViewRemindersViewModel(
    private val taskId: String,
    private val readRemindersByTaskIdUseCase: ReadRemindersByTaskIdUseCase,
    override val viewModelScope: CoroutineScope
) : BaseViewModel<ViewRemindersScreenEvent, ViewRemindersScreenState, ViewRemindersScreenNavigation, ViewRemindersScreenConfig>() {

    private val allRemindersState = readRemindersByTaskIdUseCase(taskId)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    override val uiScreenState: StateFlow<ViewRemindersScreenState> =
        allRemindersState.map { allReminders ->
            ViewRemindersScreenState(
                allReminders = allReminders
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ViewRemindersScreenState(
                allReminders = allRemindersState.value
            )
        )

    override fun onEvent(event: ViewRemindersScreenEvent) {
        TODO("Not yet implemented")
    }

}