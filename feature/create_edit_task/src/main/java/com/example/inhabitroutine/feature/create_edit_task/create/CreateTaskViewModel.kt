package com.example.inhabitroutine.feature.create_edit_task.create

import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.reminder.api.ReadReminderCountByTaskIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.DeleteTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskDateByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskFrequencyByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskProgressByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskTitleByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.ValidateProgressLimitNumberUseCase
import com.example.inhabitroutine.feature.create_edit_task.base.BaseCreateEditTaskViewModel
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenConfig
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation
import com.example.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenConfig
import com.example.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenNavigation
import com.example.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenState
import com.example.inhabitroutine.feature.create_edit_task.create.config.ConfirmLeavingScreenResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateTaskViewModel(
    private val taskId: String,
    private val readTaskByIdUseCase: ReadTaskByIdUseCase,
    private val readReminderCountByTaskIdUseCase: ReadReminderCountByTaskIdUseCase,
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    updateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase,
    updateTaskProgressByIdUseCase: UpdateTaskProgressByIdUseCase,
    updateTaskFrequencyByIdUseCase: UpdateTaskFrequencyByIdUseCase,
    updateTaskDateByIdUseCase: UpdateTaskDateByIdUseCase,
    validateProgressLimitNumberUseCase: ValidateProgressLimitNumberUseCase,
    defaultDispatcher: CoroutineDispatcher,
    override val viewModelScope: CoroutineScope
) : BaseCreateEditTaskViewModel<CreateTaskScreenEvent, CreateTaskScreenState, CreateTaskScreenNavigation, CreateTaskScreenConfig>(
    updateTaskTitleByIdUseCase = updateTaskTitleByIdUseCase,
    updateTaskProgressByIdUseCase = updateTaskProgressByIdUseCase,
    updateTaskFrequencyByIdUseCase = updateTaskFrequencyByIdUseCase,
    updateTaskDateByIdUseCase = updateTaskDateByIdUseCase,
    validateProgressLimitNumberUseCase = validateProgressLimitNumberUseCase,
    defaultDispatcher = defaultDispatcher,
) {

    override val taskModelState: StateFlow<TaskModel?> = readTaskByIdUseCase(taskId)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    private val allTaskConfigItemsState = combine(
        taskModelState.filterNotNull(),
        readReminderCountByTaskIdUseCase(taskId)
    ) { taskModel, reminderCount ->
        withContext(defaultDispatcher) {
            provideBaseTaskConfigItems(
                taskModel = taskModel,
                reminderCount = reminderCount
            )
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
            is CreateTaskScreenEvent.ResultEvent -> onResultEvent(event)
            is CreateTaskScreenEvent.OnLeaveRequest -> onLeaveRequest()
        }
    }

    private fun onResultEvent(event: CreateTaskScreenEvent.ResultEvent) {
        when (event) {
            is CreateTaskScreenEvent.ResultEvent.ConfirmLeaving ->
                onConfirmLeavingResultEvent(event)
        }
    }

    private fun onConfirmLeavingResultEvent(event: CreateTaskScreenEvent.ResultEvent.ConfirmLeaving) {
        onIdleToAction {
            when (event.result) {
                is ConfirmLeavingScreenResult.Confirm -> onConfirmLeaving()
                is ConfirmLeavingScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmLeaving() {
        viewModelScope.launch {
            deleteTaskByIdUseCase(taskId)
            setUpNavigationState(CreateTaskScreenNavigation.Back)
        }
    }

    private fun onLeaveRequest() {
        if (canSaveState.value) {
            setUpConfigState(CreateTaskScreenConfig.ConfirmLeaving)
        } else {
            viewModelScope.launch {
                deleteTaskByIdUseCase(taskId)
                setUpNavigationState(CreateTaskScreenNavigation.Back)
            }
        }
    }

    override fun setUpBaseConfigState(baseConfig: BaseCreateEditTaskScreenConfig) {
        setUpConfigState(CreateTaskScreenConfig.Base(baseConfig))
    }

    override fun setUpBaseNavigationState(baseNavigation: BaseCreateEditTaskScreenNavigation) {
        setUpNavigationState(CreateTaskScreenNavigation.Base(baseNavigation))
    }

}