package com.example.inhabitroutine.presentation.view_reminders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitroutine.domain.reminder.api.ReadRemindersByTaskIdUseCase
import com.example.inhabitroutine.feature.view_reminders.ViewRemindersViewModel
import com.example.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenConfig
import com.example.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenEvent
import com.example.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenNavigation
import com.example.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenState
import com.example.inhabitroutine.navigation.AppNavDest
import com.example.inhabitroutine.presentation.base.BaseAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidViewRemindersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    readRemindersByTaskIdUseCase: ReadRemindersByTaskIdUseCase
) : BaseAndroidViewModel<ViewRemindersScreenEvent, ViewRemindersScreenState, ViewRemindersScreenNavigation, ViewRemindersScreenConfig>() {

    override val delegateViewModel = ViewRemindersViewModel(
        taskId = checkNotNull(savedStateHandle.get<String>(AppNavDest.TASK_ID_KEY)),
        readRemindersByTaskIdUseCase = readRemindersByTaskIdUseCase,
        viewModelScope = viewModelScope
    )
}