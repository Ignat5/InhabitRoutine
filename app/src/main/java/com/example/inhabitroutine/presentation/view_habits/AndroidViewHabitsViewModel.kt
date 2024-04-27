package com.example.inhabitroutine.presentation.view_habits

import androidx.lifecycle.viewModelScope
import com.example.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
import com.example.inhabitroutine.core.presentation.base.BaseViewModel
import com.example.inhabitroutine.domain.task.api.use_case.ArchiveTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.DeleteTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.ReadHabitsUseCase
import com.example.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.example.inhabitroutine.feature.view_habits.ViewHabitsViewModel
import com.example.inhabitroutine.feature.view_habits.components.ViewHabitsScreenConfig
import com.example.inhabitroutine.feature.view_habits.components.ViewHabitsScreenEvent
import com.example.inhabitroutine.feature.view_habits.components.ViewHabitsScreenNavigation
import com.example.inhabitroutine.feature.view_habits.components.ViewHabitsScreenState
import com.example.inhabitroutine.presentation.base.BaseAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class AndroidViewHabitsViewModel @Inject constructor(
    readHabitsUseCase: ReadHabitsUseCase,
    saveTaskDraftUseCase: SaveTaskDraftUseCase,
    archiveTaskByIdUseCase: ArchiveTaskByIdUseCase,
    deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
): BaseAndroidViewModel<ViewHabitsScreenEvent, ViewHabitsScreenState, ViewHabitsScreenNavigation, ViewHabitsScreenConfig>() {

    override val delegateViewModel = ViewHabitsViewModel(
        readHabitsUseCase = readHabitsUseCase,
        saveTaskDraftUseCase = saveTaskDraftUseCase,
        archiveTaskByIdUseCase = archiveTaskByIdUseCase,
        deleteTaskByIdUseCase = deleteTaskByIdUseCase,
        defaultDispatcher = defaultDispatcher,
        viewModelScope = viewModelScope
    )

}