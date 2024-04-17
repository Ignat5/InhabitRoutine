package com.example.inhabitroutine.feature.create_edit_task.create

import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskTitleByIdUseCase
import com.example.inhabitroutine.feature.create_edit_task.base.BaseCreateEditTaskViewModel
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenConfig
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation
import com.example.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenConfig
import com.example.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenNavigation
import com.example.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

class CreateTaskViewModel(
    private val taskId: String,
    private val readTaskByIdUseCase: ReadTaskByIdUseCase,
    updateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase,
    defaultDispatcher: CoroutineDispatcher,
    override val viewModelScope: CoroutineScope
) : BaseCreateEditTaskViewModel<CreateTaskScreenEvent, CreateTaskScreenState, CreateTaskScreenNavigation, CreateTaskScreenConfig>(
    updateTaskTitleByIdUseCase = updateTaskTitleByIdUseCase
) {

    override val taskModelState: StateFlow<TaskModel?> = readTaskByIdUseCase(taskId)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    private val allTaskConfigItemsState = taskModelState.filterNotNull().map { taskModel ->
        withContext(defaultDispatcher) {
            provideBaseTaskConfigItems(taskModel)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    private val canSaveState = taskModelState.filterNotNull().map { taskModel ->
        taskModel.title.isNotBlank()
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        false
    )

    override val uiScreenState: StateFlow<CreateTaskScreenState> =
        combine(
            taskModelState,
            allTaskConfigItemsState,
            canSaveState
        ) { taskModel, allTaskConfigItems, canSaveState ->
            CreateTaskScreenState(
                taskModel = taskModel,
                allTaskConfigItems = allTaskConfigItems,
                canSave = canSaveState
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            CreateTaskScreenState(
                taskModel = taskModelState.value,
                allTaskConfigItems = allTaskConfigItemsState.value,
                canSave = canSaveState.value
            )
        )

    override fun onEvent(event: CreateTaskScreenEvent) {
        when (event) {
            is CreateTaskScreenEvent.Base -> onBaseEvent(event.baseEvent)
        }
    }

    override fun setUpBaseConfigState(baseConfig: BaseCreateEditTaskScreenConfig) {
        setUpConfigState(CreateTaskScreenConfig.Base(baseConfig))
    }

    override fun setUpBaseNavigationState(baseNavigation: BaseCreateEditTaskScreenNavigation) {
        setUpNavigationState(CreateTaskScreenNavigation.Base(baseNavigation))
    }

}