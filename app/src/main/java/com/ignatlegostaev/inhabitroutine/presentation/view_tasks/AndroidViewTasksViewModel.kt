package com.ignatlegostaev.inhabitroutine.presentation.view_tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ignatlegostaev.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ArchiveTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.DeleteTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTasksUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.ViewTasksViewModel
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.components.ViewTasksScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.components.ViewTasksScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.components.ViewTasksScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.components.ViewTasksScreenState
import com.ignatlegostaev.inhabitroutine.presentation.base.BaseAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class AndroidViewTasksViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    readTasksUseCase: ReadTasksUseCase,
    saveTaskDraftUseCase: SaveTaskDraftUseCase,
    archiveTaskByIdUseCase: ArchiveTaskByIdUseCase,
    deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
) : BaseAndroidViewModel<ViewTasksScreenEvent, ViewTasksScreenState, ViewTasksScreenNavigation, ViewTasksScreenConfig>() {

    override val delegateViewModel = ViewTasksViewModel(
        readTasksUseCase = readTasksUseCase,
        saveTaskDraftUseCase = saveTaskDraftUseCase,
        archiveTaskByIdUseCase = archiveTaskByIdUseCase,
        deleteTaskByIdUseCase = deleteTaskByIdUseCase,
        defaultDispatcher = defaultDispatcher,
        viewModelScope = viewModelScope
    )

}