package com.example.inhabitroutine.presentation.edit_task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
import com.example.inhabitroutine.domain.reminder.api.ReadReminderCountByTaskIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.DeleteTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.SaveTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskDateByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskDescriptionByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskFrequencyByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskProgressByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskTitleByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.ValidateProgressLimitNumberUseCase
import com.example.inhabitroutine.feature.create_edit_task.edit.EditTaskViewModel
import com.example.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenConfig
import com.example.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenNavigation
import com.example.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenState
import com.example.inhabitroutine.navigation.AppNavDest
import com.example.inhabitroutine.presentation.base.BaseAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class AndroidEditTaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    readTaskByIdUseCase: ReadTaskByIdUseCase,
    readReminderCountByTaskIdUseCase: ReadReminderCountByTaskIdUseCase,
    deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    updateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase,
    updateTaskProgressByIdUseCase: UpdateTaskProgressByIdUseCase,
    updateTaskFrequencyByIdUseCase: UpdateTaskFrequencyByIdUseCase,
    updateTaskDateByIdUseCase: UpdateTaskDateByIdUseCase,
    updateTaskDescriptionByIdUseCase: UpdateTaskDescriptionByIdUseCase,
    validateProgressLimitNumberUseCase: ValidateProgressLimitNumberUseCase,
    @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
) : BaseAndroidViewModel<EditTaskScreenEvent, EditTaskScreenState, EditTaskScreenNavigation, EditTaskScreenConfig>() {

    override val delegateViewModel = EditTaskViewModel(
        taskId = checkNotNull(savedStateHandle.get<String>(AppNavDest.TASK_ID_KEY)),
        readTaskByIdUseCase = readTaskByIdUseCase,
        readReminderCountByTaskIdUseCase = readReminderCountByTaskIdUseCase,
        deleteTaskByIdUseCase = deleteTaskByIdUseCase,
        updateTaskTitleByIdUseCase = updateTaskTitleByIdUseCase,
        updateTaskProgressByIdUseCase = updateTaskProgressByIdUseCase,
        updateTaskFrequencyByIdUseCase = updateTaskFrequencyByIdUseCase,
        updateTaskDateByIdUseCase = updateTaskDateByIdUseCase,
        updateTaskDescriptionByIdUseCase = updateTaskDescriptionByIdUseCase,
        validateProgressLimitNumberUseCase = validateProgressLimitNumberUseCase,
        defaultDispatcher = defaultDispatcher,
        viewModelScope = viewModelScope
    )

}