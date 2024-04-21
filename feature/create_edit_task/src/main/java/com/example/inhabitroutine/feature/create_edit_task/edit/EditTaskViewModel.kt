package com.example.inhabitroutine.feature.create_edit_task.edit

import com.example.inhabitroutine.domain.model.task.TaskModel
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
import com.example.inhabitroutine.feature.create_edit_task.base.BaseCreateEditTaskViewModel
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenConfig
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation
import com.example.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenConfig
import com.example.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenNavigation
import com.example.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

class EditTaskViewModel(
    private val taskId: String,
    readTaskByIdUseCase: ReadTaskByIdUseCase,
    readReminderCountByTaskIdUseCase: ReadReminderCountByTaskIdUseCase,
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    updateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase,
    updateTaskProgressByIdUseCase: UpdateTaskProgressByIdUseCase,
    updateTaskFrequencyByIdUseCase: UpdateTaskFrequencyByIdUseCase,
    updateTaskDateByIdUseCase: UpdateTaskDateByIdUseCase,
    updateTaskDescriptionByIdUseCase: UpdateTaskDescriptionByIdUseCase,
    validateProgressLimitNumberUseCase: ValidateProgressLimitNumberUseCase,
    defaultDispatcher: CoroutineDispatcher,
    override val viewModelScope: CoroutineScope
) : BaseCreateEditTaskViewModel<EditTaskScreenEvent, EditTaskScreenState, EditTaskScreenNavigation, EditTaskScreenConfig>(
    updateTaskTitleByIdUseCase = updateTaskTitleByIdUseCase,
    updateTaskProgressByIdUseCase = updateTaskProgressByIdUseCase,
    updateTaskFrequencyByIdUseCase = updateTaskFrequencyByIdUseCase,
    updateTaskDateByIdUseCase = updateTaskDateByIdUseCase,
    updateTaskDescriptionByIdUseCase = updateTaskDescriptionByIdUseCase,
    validateProgressLimitNumberUseCase = validateProgressLimitNumberUseCase,
    defaultDispatcher = defaultDispatcher
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

    override val uiScreenState: StateFlow<EditTaskScreenState> =
        combine(
            taskModelState,
            allTaskConfigItemsState
        ) { taskModel, allTaskConfigItems ->
            EditTaskScreenState(
                taskModel = taskModel,
                allTaskConfigItems = allTaskConfigItems
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            EditTaskScreenState(
                taskModel = taskModelState.value,
                allTaskConfigItems = allTaskConfigItemsState.value
            )
        )

    override fun onEvent(event: EditTaskScreenEvent) {
        when (event) {
            is EditTaskScreenEvent.Base -> onBaseEvent(event.baseEvent)
            is EditTaskScreenEvent.OnBackRequest -> onBackRequest()
        }
    }

    private fun onBackRequest() {
        setUpNavigationState(EditTaskScreenNavigation.Back)
    }

    override fun setUpBaseNavigationState(baseNavigation: BaseCreateEditTaskScreenNavigation) {
        setUpNavigationState(EditTaskScreenNavigation.Base(baseNavigation))
    }

    override fun setUpBaseConfigState(baseConfig: BaseCreateEditTaskScreenConfig) {
        setUpConfigState(EditTaskScreenConfig.Base(baseConfig))
    }

}