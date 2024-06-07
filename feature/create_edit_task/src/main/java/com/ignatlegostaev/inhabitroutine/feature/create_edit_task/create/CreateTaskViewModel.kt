package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create

import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
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
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.BaseCreateEditTaskViewModel
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenState
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.config.ConfirmLeavingScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.other.DefaultVerifyCanSaveTaskUseCase
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.other.VerifyCanSaveTaskUseCase
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
    readTaskByIdUseCase: ReadTaskByIdUseCase,
    readReminderCountByTaskIdUseCase: ReadReminderCountByTaskIdUseCase,
    private val saveTaskByIdUseCase: SaveTaskByIdUseCase,
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    updateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase,
    updateTaskProgressByIdUseCase: UpdateTaskProgressByIdUseCase,
    updateTaskFrequencyByIdUseCase: UpdateTaskFrequencyByIdUseCase,
    updateTaskDateByIdUseCase: UpdateTaskDateByIdUseCase,
    updateTaskDescriptionByIdUseCase: UpdateTaskDescriptionByIdUseCase,
    updateTaskPriorityByIdUseCase: UpdateTaskPriorityByIdUseCase,
    defaultDispatcher: CoroutineDispatcher,
    override val viewModelScope: CoroutineScope,
    private val verifyCanSaveTaskUseCase: VerifyCanSaveTaskUseCase = DefaultVerifyCanSaveTaskUseCase(),
) : BaseCreateEditTaskViewModel<CreateTaskScreenEvent, CreateTaskScreenState, CreateTaskScreenNavigation, CreateTaskScreenConfig>(
    updateTaskTitleByIdUseCase = updateTaskTitleByIdUseCase,
    updateTaskProgressByIdUseCase = updateTaskProgressByIdUseCase,
    updateTaskFrequencyByIdUseCase = updateTaskFrequencyByIdUseCase,
    updateTaskDateByIdUseCase = updateTaskDateByIdUseCase,
    updateTaskDescriptionByIdUseCase = updateTaskDescriptionByIdUseCase,
    updateTaskPriorityByIdUseCase = updateTaskPriorityByIdUseCase,
    defaultDispatcher = defaultDispatcher,
) {

    override val taskModelState: StateFlow<TaskModel?> = readTaskByIdUseCase(taskId)
        .filterNotNull()
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
        verifyCanSaveTaskUseCase(taskModel)
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
            is CreateTaskScreenEvent.OnSaveClick -> onSaveClick()
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

    private fun onSaveClick() {
        if (canSaveState.value) {
            viewModelScope.launch {
                saveTaskByIdUseCase(taskId)
                setUpNavigationState(CreateTaskScreenNavigation.Back)
            }
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