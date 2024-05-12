package com.ignatlegostaev.inhabitroutine.presentation.edit_task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ignatlegostaev.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ReadReminderCountByTaskIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ArchiveTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.DeleteTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ResetTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskDateByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskDescriptionByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskFrequencyByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskPriorityByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskProgressByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskTitleByIdUseCase
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.EditTaskViewModel
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenState
import com.ignatlegostaev.inhabitroutine.navigation.AppNavDest
import com.ignatlegostaev.inhabitroutine.presentation.base.BaseAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class AndroidEditTaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    readTaskByIdUseCase: ReadTaskByIdUseCase,
    readReminderCountByTaskIdUseCase: ReadReminderCountByTaskIdUseCase,
    archiveTaskByIdUseCase: ArchiveTaskByIdUseCase,
    deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    resetTaskByIdUseCase: ResetTaskByIdUseCase,
    updateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase,
    updateTaskProgressByIdUseCase: UpdateTaskProgressByIdUseCase,
    updateTaskFrequencyByIdUseCase: UpdateTaskFrequencyByIdUseCase,
    updateTaskDateByIdUseCase: UpdateTaskDateByIdUseCase,
    updateTaskDescriptionByIdUseCase: UpdateTaskDescriptionByIdUseCase,
    updateTaskPriorityByIdUseCase: UpdateTaskPriorityByIdUseCase,
    @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
) : BaseAndroidViewModel<EditTaskScreenEvent, EditTaskScreenState, EditTaskScreenNavigation, EditTaskScreenConfig>() {

    override val delegateViewModel = EditTaskViewModel(
        taskId = checkNotNull(savedStateHandle.get<String>(AppNavDest.TASK_ID_KEY)),
        readTaskByIdUseCase = readTaskByIdUseCase,
        readReminderCountByTaskIdUseCase = readReminderCountByTaskIdUseCase,
        archiveTaskByIdUseCase = archiveTaskByIdUseCase,
        deleteTaskByIdUseCase = deleteTaskByIdUseCase,
        resetTaskByIdUseCase = resetTaskByIdUseCase,
        updateTaskTitleByIdUseCase = updateTaskTitleByIdUseCase,
        updateTaskProgressByIdUseCase = updateTaskProgressByIdUseCase,
        updateTaskFrequencyByIdUseCase = updateTaskFrequencyByIdUseCase,
        updateTaskDateByIdUseCase = updateTaskDateByIdUseCase,
        updateTaskDescriptionByIdUseCase = updateTaskDescriptionByIdUseCase,
        updateTaskPriorityByIdUseCase = updateTaskPriorityByIdUseCase,
        defaultDispatcher = defaultDispatcher,
        viewModelScope = viewModelScope
    )

}