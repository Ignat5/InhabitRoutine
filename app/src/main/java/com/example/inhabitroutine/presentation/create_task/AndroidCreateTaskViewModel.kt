package com.example.inhabitroutine.presentation.create_task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
import com.example.inhabitroutine.domain.task.api.use_case.DeleteTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskFrequencyByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskProgressByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskTitleByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.ValidateProgressLimitNumberUseCase
import com.example.inhabitroutine.feature.create_edit_task.create.CreateTaskViewModel
import com.example.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenConfig
import com.example.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenNavigation
import com.example.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenState
import com.example.inhabitroutine.navigation.AppNavDest
import com.example.inhabitroutine.presentation.base.BaseAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class AndroidCreateTaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    readTaskByIdUseCase: ReadTaskByIdUseCase,
    deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    updateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase,
    updateTaskProgressByIdUseCase: UpdateTaskProgressByIdUseCase,
    updateTaskFrequencyByIdUseCase: UpdateTaskFrequencyByIdUseCase,
    validateProgressLimitNumberUseCase: ValidateProgressLimitNumberUseCase,
    @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
) : BaseAndroidViewModel<CreateTaskScreenEvent, CreateTaskScreenState, CreateTaskScreenNavigation, CreateTaskScreenConfig>() {

    override val delegateViewModel = CreateTaskViewModel(
        taskId = checkNotNull(savedStateHandle.get<String>(AppNavDest.TASK_ID_KEY)),
        readTaskByIdUseCase = readTaskByIdUseCase,
        deleteTaskByIdUseCase = deleteTaskByIdUseCase,
        updateTaskTitleByIdUseCase = updateTaskTitleByIdUseCase,
        updateTaskProgressByIdUseCase = updateTaskProgressByIdUseCase,
        updateTaskFrequencyByIdUseCase = updateTaskFrequencyByIdUseCase,
        validateProgressLimitNumberUseCase = validateProgressLimitNumberUseCase,
        defaultDispatcher = defaultDispatcher,
        viewModelScope = viewModelScope
    )

}