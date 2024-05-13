package com.ignatlegostaev.inhabitroutine.presentation.create_task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ignatlegostaev.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ReadReminderCountByTaskIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.DeleteTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.SaveTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskDateByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskDescriptionByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskFrequencyByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskPriorityByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskProgressByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskTitleByIdUseCase
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.CreateTaskViewModel
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenState
import com.ignatlegostaev.inhabitroutine.navigation.AppNavDest
import com.ignatlegostaev.inhabitroutine.presentation.base.BaseAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class AndroidCreateTaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    readTaskByIdUseCase: ReadTaskByIdUseCase,
    saveTaskByIdUseCase: SaveTaskByIdUseCase,
    deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    updateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase,
    updateTaskProgressByIdUseCase: UpdateTaskProgressByIdUseCase,
    updateTaskFrequencyByIdUseCase: UpdateTaskFrequencyByIdUseCase,
    updateTaskDateByIdUseCase: UpdateTaskDateByIdUseCase,
    updateTaskDescriptionByIdUseCase: UpdateTaskDescriptionByIdUseCase,
    updateTaskPriorityByIdUseCase: UpdateTaskPriorityByIdUseCase,
    readReminderCountByTaskIdUseCase: ReadReminderCountByTaskIdUseCase,
    @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
) : BaseAndroidViewModel<CreateTaskScreenEvent, CreateTaskScreenState, CreateTaskScreenNavigation, CreateTaskScreenConfig>() {

    override val delegateViewModel = CreateTaskViewModel(
        taskId = checkNotNull(savedStateHandle.get<String>(AppNavDest.TASK_ID_KEY)),
        readTaskByIdUseCase = readTaskByIdUseCase,
        readReminderCountByTaskIdUseCase = readReminderCountByTaskIdUseCase,
        saveTaskByIdUseCase = saveTaskByIdUseCase,
        deleteTaskByIdUseCase = deleteTaskByIdUseCase,
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