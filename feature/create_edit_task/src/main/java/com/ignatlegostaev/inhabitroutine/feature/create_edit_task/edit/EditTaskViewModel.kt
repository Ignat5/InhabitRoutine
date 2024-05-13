package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit

import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.archive_task.ArchiveTaskStateHolder
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.archive_task.components.ArchiveTaskScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.delete_task.DeleteTaskStateHolder
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.delete_task.components.DeleteTaskScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.reset_task.ResetTaskStateHolder
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.reset_task.components.ResetTaskScreenResult
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
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
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.BaseCreateEditTaskViewModel
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenState
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.model.EditTaskMessage
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.model.ItemTaskAction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditTaskViewModel(
    private val taskId: String,
    readTaskByIdUseCase: ReadTaskByIdUseCase,
    readReminderCountByTaskIdUseCase: ReadReminderCountByTaskIdUseCase,
    private val archiveTaskByIdUseCase: ArchiveTaskByIdUseCase,
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    private val resetTaskByIdUseCase: ResetTaskByIdUseCase,
    updateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase,
    updateTaskProgressByIdUseCase: UpdateTaskProgressByIdUseCase,
    updateTaskFrequencyByIdUseCase: UpdateTaskFrequencyByIdUseCase,
    updateTaskDateByIdUseCase: UpdateTaskDateByIdUseCase,
    updateTaskDescriptionByIdUseCase: UpdateTaskDescriptionByIdUseCase,
    updateTaskPriorityByIdUseCase: UpdateTaskPriorityByIdUseCase,
    defaultDispatcher: CoroutineDispatcher,
    override val viewModelScope: CoroutineScope
) : BaseCreateEditTaskViewModel<EditTaskScreenEvent, EditTaskScreenState, EditTaskScreenNavigation, EditTaskScreenConfig>(
    updateTaskTitleByIdUseCase = updateTaskTitleByIdUseCase,
    updateTaskProgressByIdUseCase = updateTaskProgressByIdUseCase,
    updateTaskFrequencyByIdUseCase = updateTaskFrequencyByIdUseCase,
    updateTaskDateByIdUseCase = updateTaskDateByIdUseCase,
    updateTaskDescriptionByIdUseCase = updateTaskDescriptionByIdUseCase,
    updateTaskPriorityByIdUseCase = updateTaskPriorityByIdUseCase,
    defaultDispatcher = defaultDispatcher
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

    private val allTaskActionItemsState = taskModelState.filterNotNull().map { taskModel ->
        buildList {
            if (taskModel is TaskModel.Habit) {
                add(ItemTaskAction.ViewStatistics)
            }
            add(
                if (taskModel.isArchived) ItemTaskAction.ArchiveUnarchive.Unarchive
                else ItemTaskAction.ArchiveUnarchive.Archive
            )
            if (taskModel is TaskModel.Habit) {
                add(ItemTaskAction.Reset)
            }
            add(ItemTaskAction.Delete)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    private val messageState = MutableStateFlow<EditTaskMessage>(EditTaskMessage.Idle)

    override val uiScreenState: StateFlow<EditTaskScreenState> =
        combine(
            taskModelState.filterNotNull(),
            allTaskConfigItemsState,
            allTaskActionItemsState,
            messageState
        ) { taskModel, allTaskConfigItems, allTaskActionItems, message ->
            EditTaskScreenState(
                taskModel = taskModel,
                allTaskConfigItems = allTaskConfigItems,
                allTaskActionItems = allTaskActionItems,
                message = message
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            EditTaskScreenState(
                taskModel = taskModelState.value,
                allTaskConfigItems = allTaskConfigItemsState.value,
                allTaskActionItems = allTaskActionItemsState.value,
                message = messageState.value
            )
        )

    override fun onEvent(event: EditTaskScreenEvent) {
        when (event) {
            is EditTaskScreenEvent.Base -> onBaseEvent(event.baseEvent)
            is EditTaskScreenEvent.OnItemActionClick -> onItemActionClick(event)
            is EditTaskScreenEvent.ResultEvent -> onResultEvent(event)
            is EditTaskScreenEvent.OnMessageShown -> onMessageShown()
            is EditTaskScreenEvent.OnBackRequest -> onBackRequest()
        }
    }

    private fun onResultEvent(event: EditTaskScreenEvent.ResultEvent) {
        when (event) {
            is EditTaskScreenEvent.ResultEvent.ArchiveTask ->
                onArchiveResultEvent(event)

            is EditTaskScreenEvent.ResultEvent.DeleteTask ->
                onDeleteTaskResultEvent(event)

            is EditTaskScreenEvent.ResultEvent.ResetTask ->
                onResetTaskResultEvent(event)
        }
    }

    private fun onResetTaskResultEvent(event: EditTaskScreenEvent.ResultEvent.ResetTask) {
        onIdleToAction {
            when (val result = event.result) {
                is ResetTaskScreenResult.Confirm -> onConfirmResetTask(result)
                is ResetTaskScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmResetTask(result: ResetTaskScreenResult.Confirm) {
        viewModelScope.launch {
            val resultModel = resetTaskByIdUseCase(taskId)
            if (resultModel is ResultModel.Success) {
                messageState.update { EditTaskMessage.Message.ResetSuccess }
            }
        }
    }

    private fun onDeleteTaskResultEvent(event: EditTaskScreenEvent.ResultEvent.DeleteTask) {
        onIdleToAction {
            when (val result = event.result) {
                is DeleteTaskScreenResult.Confirm -> onConfirmDeleteTask(result)
                is DeleteTaskScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmDeleteTask(result: DeleteTaskScreenResult.Confirm) {
        viewModelScope.launch {
            deleteTaskByIdUseCase(taskId)
            setUpNavigationState(EditTaskScreenNavigation.Back)
        }
    }

    private fun onArchiveResultEvent(event: EditTaskScreenEvent.ResultEvent.ArchiveTask) {
        onIdleToAction {
            when (val result = event.result) {
                is ArchiveTaskScreenResult.Confirm -> onConfirmArchiveTask(result)
                is ArchiveTaskScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmArchiveTask(result: ArchiveTaskScreenResult.Confirm) {
        viewModelScope.launch {
            val resultModel = archiveTaskByIdUseCase(
                taskId = taskId,
                requestType = ArchiveTaskByIdUseCase.RequestType.Archive
            )
            if (resultModel is ResultModel.Success) {
                messageState.update { EditTaskMessage.Message.ArchiveSuccess }
            }
        }
    }

    private fun onItemActionClick(event: EditTaskScreenEvent.OnItemActionClick) {
        when (val item = event.item) {
            is ItemTaskAction.ViewStatistics ->
                onViewTaskStatisticsClick()

            is ItemTaskAction.ArchiveUnarchive ->
                onArchiveUnarchiveActionClick(item)

            is ItemTaskAction.Reset -> onResetActionClick()
            is ItemTaskAction.Delete -> onDeleteActionClick()
        }
    }

    private fun onViewTaskStatisticsClick() {
        setUpNavigationState(EditTaskScreenNavigation.ViewTaskStatistics(taskId))
    }

    private fun onResetActionClick() {
        setUpConfigState(
            EditTaskScreenConfig.ResetTask(
                stateHolder = ResetTaskStateHolder(
                    taskId = taskId,
                    holderScope = provideChildScope()
                )
            )
        )
    }

    private fun onDeleteActionClick() {
        setUpConfigState(
            EditTaskScreenConfig.DeleteTask(
                stateHolder = DeleteTaskStateHolder(
                    taskId = taskId,
                    holderScope = provideChildScope()
                )
            )
        )
    }

    private fun onArchiveUnarchiveActionClick(item: ItemTaskAction.ArchiveUnarchive) {
        when (item) {
            is ItemTaskAction.ArchiveUnarchive.Archive ->
                onArchiveClick()

            is ItemTaskAction.ArchiveUnarchive.Unarchive ->
                onUnarchiveClick()
        }
    }

    private fun onArchiveClick() {
        setUpConfigState(
            EditTaskScreenConfig.ArchiveTask(
                stateHolder = ArchiveTaskStateHolder(
                    taskId = taskId,
                    holderScope = provideChildScope()
                )
            )
        )
    }

    private fun onUnarchiveClick() {
        viewModelScope.launch {
            val resultModel = archiveTaskByIdUseCase(
                taskId = taskId,
                requestType = ArchiveTaskByIdUseCase.RequestType.Unarchive
            )
            if (resultModel is ResultModel.Success) {
                messageState.update { EditTaskMessage.Message.UnarchiveSuccess }
            }
        }
    }

    private fun onMessageShown() {
        messageState.update { EditTaskMessage.Idle }
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