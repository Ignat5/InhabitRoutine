package com.example.inhabitroutine.presentation.view_schedule

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
import com.example.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.ReadTasksWithExtrasAndRecordByDateUseCase
import com.example.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.example.inhabitroutine.feature.view_schedule.ViewScheduleViewModel
import com.example.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenConfig
import com.example.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenEvent
import com.example.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenNavigation
import com.example.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenState
import com.example.inhabitroutine.presentation.base.BaseAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class AndroidViewScheduleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    readTasksWithExtrasAndRecordByDateUseCase: ReadTasksWithExtrasAndRecordByDateUseCase,
    saveTaskDraftUseCase: SaveTaskDraftUseCase,
    @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
) : BaseAndroidViewModel<ViewScheduleScreenEvent, ViewScheduleScreenState, ViewScheduleScreenNavigation, ViewScheduleScreenConfig>() {

    override val delegateViewModel: ViewScheduleViewModel = ViewScheduleViewModel(
        viewModelScope = viewModelScope,
        readTasksWithExtrasAndRecordByDateUseCase = readTasksWithExtrasAndRecordByDateUseCase,
        saveTaskDraftUseCase = saveTaskDraftUseCase,
        defaultDispatcher = defaultDispatcher
    )

}