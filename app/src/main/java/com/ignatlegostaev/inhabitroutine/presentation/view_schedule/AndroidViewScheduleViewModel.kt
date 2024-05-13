package com.ignatlegostaev.inhabitroutine.presentation.view_schedule

import androidx.lifecycle.viewModelScope
import com.ignatlegostaev.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
import com.ignatlegostaev.inhabitroutine.domain.record.api.DeleteRecordUseCase
import com.ignatlegostaev.inhabitroutine.domain.record.api.SaveRecordUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTasksWithExtrasAndRecordByDateUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.ViewScheduleViewModel
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenState
import com.ignatlegostaev.inhabitroutine.presentation.base.BaseAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class AndroidViewScheduleViewModel @Inject constructor(
    readTasksWithExtrasAndRecordByDateUseCase: ReadTasksWithExtrasAndRecordByDateUseCase,
    saveTaskDraftUseCase: SaveTaskDraftUseCase,
    saveRecordUseCase: SaveRecordUseCase,
    deleteRecordUseCase: DeleteRecordUseCase,
    @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
) : BaseAndroidViewModel<ViewScheduleScreenEvent, ViewScheduleScreenState, ViewScheduleScreenNavigation, ViewScheduleScreenConfig>() {

    override val delegateViewModel: ViewScheduleViewModel = ViewScheduleViewModel(
        viewModelScope = viewModelScope,
        readTasksWithExtrasAndRecordByDateUseCase = readTasksWithExtrasAndRecordByDateUseCase,
        saveTaskDraftUseCase = saveTaskDraftUseCase,
        saveRecordUseCase = saveRecordUseCase,
        deleteRecordUseCase = deleteRecordUseCase,
        defaultDispatcher = defaultDispatcher
    )

}