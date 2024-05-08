package com.ignatlegostaev.inhabitroutine.presentation.view_habits

import androidx.lifecycle.viewModelScope
import com.ignatlegostaev.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ArchiveTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.DeleteTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadHabitsUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.ignatlegostaev.inhabitroutine.feature.view_habits.ViewHabitsViewModel
import com.ignatlegostaev.inhabitroutine.feature.view_habits.components.ViewHabitsScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.view_habits.components.ViewHabitsScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_habits.components.ViewHabitsScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.view_habits.components.ViewHabitsScreenState
import com.ignatlegostaev.inhabitroutine.presentation.base.BaseAndroidViewModel
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