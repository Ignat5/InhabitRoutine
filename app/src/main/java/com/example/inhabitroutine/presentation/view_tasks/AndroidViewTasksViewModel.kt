package com.example.inhabitroutine.presentation.view_tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
import com.example.inhabitroutine.domain.task.api.use_case.ReadTasksUseCase
import com.example.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.example.inhabitroutine.feature.view_tasks.ViewTasksViewModel
import com.example.inhabitroutine.feature.view_tasks.components.ViewTasksScreenConfig
import com.example.inhabitroutine.feature.view_tasks.components.ViewTasksScreenEvent
import com.example.inhabitroutine.feature.view_tasks.components.ViewTasksScreenNavigation
import com.example.inhabitroutine.feature.view_tasks.components.ViewTasksScreenState
import com.example.inhabitroutine.presentation.base.BaseAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class AndroidViewTasksViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    readTasksUseCase: ReadTasksUseCase,
    saveTaskDraftUseCase: SaveTaskDraftUseCase,
    @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
) : BaseAndroidViewModel<ViewTasksScreenEvent, ViewTasksScreenState, ViewTasksScreenNavigation, ViewTasksScreenConfig>() {

    override val delegateViewModel = ViewTasksViewModel(
        readTasksUseCase = readTasksUseCase,
        saveTaskDraftUseCase = saveTaskDraftUseCase,
        defaultDispatcher = defaultDispatcher,
        viewModelScope = viewModelScope
    )

}