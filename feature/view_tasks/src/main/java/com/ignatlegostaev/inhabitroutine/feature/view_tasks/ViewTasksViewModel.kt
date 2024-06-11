package com.ignatlegostaev.inhabitroutine.feature.view_tasks

import com.ignatlegostaev.inhabitroutine.core.presentation.base.BaseViewModel
import com.ignatlegostaev.inhabitroutine.core.presentation.model.UIResultModel
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.archive_task.ArchiveTaskStateHolder
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.archive_task.components.ArchiveTaskScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.delete_task.DeleteTaskStateHolder
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.delete_task.components.DeleteTaskScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_task_type.PickTaskTypeScreenResult
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskDate
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskType
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ArchiveTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.DeleteTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTasksUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.components.ViewTasksScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.components.ViewTasksScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.components.ViewTasksScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.components.ViewTasksScreenState
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.config.view_task_actions.ViewTaskActionsStateHolder
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.config.view_task_actions.components.ViewTaskActionsScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.model.TaskFilterByStatus
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.model.TaskFilterByType
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.model.TaskSort
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.model.ViewTasksMessage
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.use_case.filter_tasks_by_status.DefaultFilterTasksByStatusUseCase
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.use_case.filter_tasks_by_status.FilterTasksByStatusUseCase
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.use_case.filter_tasks_by_type.DefaultFilterTasksByTypeUseCase
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.use_case.filter_tasks_by_type.FilterTasksByTypeUseCase
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.use_case.sort_tasks.DefaultSortTasksUseCase
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.use_case.sort_tasks.SortTasksUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewTasksViewModel(
    readTasksUseCase: ReadTasksUseCase,
    private val saveTaskDraftUseCase: SaveTaskDraftUseCase,
    private val archiveTaskByIdUseCase: ArchiveTaskByIdUseCase,
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
    override val viewModelScope: CoroutineScope,
    private val filterTasksByStatusUseCase: FilterTasksByStatusUseCase = DefaultFilterTasksByStatusUseCase(),
    private val filterTasksByTypeUseCase: FilterTasksByTypeUseCase = DefaultFilterTasksByTypeUseCase(),
    private val sortTasksUseCase: SortTasksUseCase = DefaultSortTasksUseCase()
) : BaseViewModel<ViewTasksScreenEvent, ViewTasksScreenState, ViewTasksScreenNavigation, ViewTasksScreenConfig>() {
    private val filterByStatusState = MutableStateFlow<TaskFilterByStatus?>(null)
    private val filterByTypeState = MutableStateFlow<TaskFilterByType?>(null)
    private val sortState = MutableStateFlow<TaskSort>(TaskSort.ByPriority)
    private val messageState = MutableStateFlow<ViewTasksMessage>(ViewTasksMessage.Idle)
    private val allTasksState = combine(
        readTasksUseCase(),
        filterByStatusState,
        filterByTypeState,
        sortState
    ) { allTasks, filterByStatus, filterByType, sort ->
        if (allTasks.isNotEmpty()) {
            withContext(defaultDispatcher) {
                UIResultModel.Data(
                    allTasks
                        .let { if (filterByStatus != null) it.filterByStatus(filterByStatus) else it }
                        .let { if (filterByType != null) it.filterByType(filterByType) else it }
                        .sortTasks(sort)
                )
            }
        } else UIResultModel.Data(emptyList())
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        UIResultModel.Loading(emptyList())
    )

    override val uiScreenState: StateFlow<ViewTasksScreenState> =
        combine(
            allTasksState,
            filterByStatusState,
            filterByTypeState,
            sortState,
            messageState
        ) { allTasks, filterByStatus, filterByType, sort, message ->
            ViewTasksScreenState(
                allTasksResult = allTasks,
                filterByStatus = filterByStatus,
                filterByType = filterByType,
                sort = sort,
                message = message
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ViewTasksScreenState(
                allTasksResult = allTasksState.value,
                filterByStatus = filterByStatusState.value,
                filterByType = filterByTypeState.value,
                sort = sortState.value,
                message = messageState.value
            )
        )

    override fun onEvent(event: ViewTasksScreenEvent) {
        when (event) {
            is ViewTasksScreenEvent.ResultEvent ->
                onResultEvent(event)

            is ViewTasksScreenEvent.OnTaskClick ->
                onTaskClick(event)

            is ViewTasksScreenEvent.OnPickFilterByStatus ->
                onPickFilterByStatus(event)

            is ViewTasksScreenEvent.OnPickFilterByType ->
                onPickFilterByType(event)

            is ViewTasksScreenEvent.OnPickSort ->
                onPickSort(event)

            is ViewTasksScreenEvent.OnMessageShown ->
                onMessageShown()

            is ViewTasksScreenEvent.OnCreateTaskClick ->
                onCreateTaskClick()

            is ViewTasksScreenEvent.OnSearchClick ->
                onSearchClick()
        }
    }

    private fun onResultEvent(event: ViewTasksScreenEvent.ResultEvent) {
        when (event) {
            is ViewTasksScreenEvent.ResultEvent.ViewTaskActions ->
                onViewTaskActionsResultEvent(event)

            is ViewTasksScreenEvent.ResultEvent.PickTaskType ->
                onPickTaskTypeResultEvent(event)

            is ViewTasksScreenEvent.ResultEvent.ArchiveTask ->
                onArchiveTaskResultEvent(event)

            is ViewTasksScreenEvent.ResultEvent.DeleteTask ->
                onDeleteTaskResultEvent(event)
        }
    }

    private fun onDeleteTaskResultEvent(event: ViewTasksScreenEvent.ResultEvent.DeleteTask) {
        onIdleToAction {
            when (val result = event.result) {
                is DeleteTaskScreenResult.Confirm ->
                    onConfirmDeleteTask(result)

                is DeleteTaskScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmDeleteTask(result: DeleteTaskScreenResult.Confirm) {
        viewModelScope.launch {
            val resultModel = deleteTaskByIdUseCase(taskId = result.taskId)
            if (resultModel is ResultModel.Success) {
                messageState.update { ViewTasksMessage.Message.DeleteSuccess }
            }
        }
    }

    private fun onArchiveTaskResultEvent(event: ViewTasksScreenEvent.ResultEvent.ArchiveTask) {
        onIdleToAction {
            when (val result = event.result) {
                is ArchiveTaskScreenResult.Confirm ->
                    onConfirmArchiveTask(result)

                is ArchiveTaskScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmArchiveTask(result: ArchiveTaskScreenResult.Confirm) {
        viewModelScope.launch {
            val resultModel = archiveTaskByIdUseCase(
                taskId = result.taskId,
                requestType = ArchiveTaskByIdUseCase.RequestType.Archive
            )

            if (resultModel is ResultModel.Success) {
                messageState.update { ViewTasksMessage.Message.ArchiveSuccess }
            }
        }
    }

    private fun onViewTaskActionsResultEvent(event: ViewTasksScreenEvent.ResultEvent.ViewTaskActions) {
        onIdleToAction {
            when (val result = event.result) {
                is ViewTaskActionsScreenResult.Action -> {
                    when (result) {
                        is ViewTaskActionsScreenResult.Action.Edit ->
                            onEditTask(result)

                        is ViewTaskActionsScreenResult.Action.Archive ->
                            onArchiveTask(result)

                        is ViewTaskActionsScreenResult.Action.Unarchive ->
                            onUnarchiveTask(result)

                        is ViewTaskActionsScreenResult.Action.Delete ->
                            onDeleteTask(result)
                    }
                }

                is ViewTaskActionsScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onDeleteTask(result: ViewTaskActionsScreenResult.Action.Delete) {
        setUpConfigState(
            ViewTasksScreenConfig.DeleteTask(
                stateHolder = DeleteTaskStateHolder(
                    taskId = result.taskId,
                    holderScope = provideChildScope()
                )
            )
        )
    }

    private fun onUnarchiveTask(result: ViewTaskActionsScreenResult.Action.Unarchive) {
        viewModelScope.launch {
            val resultModel = archiveTaskByIdUseCase(
                taskId = result.taskId,
                requestType = ArchiveTaskByIdUseCase.RequestType.Unarchive
            )
            if (resultModel is ResultModel.Success) {
                messageState.update { ViewTasksMessage.Message.UnarchiveSuccess }
            }
        }
    }

    private fun onArchiveTask(result: ViewTaskActionsScreenResult.Action.Archive) {
        setUpConfigState(
            ViewTasksScreenConfig.ArchiveTask(
                stateHolder = ArchiveTaskStateHolder(
                    taskId = result.taskId,
                    holderScope = provideChildScope()
                )
            )
        )
    }

    private fun onEditTask(result: ViewTaskActionsScreenResult.Action.Edit) {
        setUpNavigationState(
            ViewTasksScreenNavigation.EditTask(
                taskId = result.taskId
            )
        )
    }

    private fun onPickTaskTypeResultEvent(event: ViewTasksScreenEvent.ResultEvent.PickTaskType) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskTypeScreenResult.Confirm -> onConfirmPickTaskType(result)
                is PickTaskTypeScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickTaskType(result: PickTaskTypeScreenResult.Confirm) {
        viewModelScope.launch {
            val requestType = when (result.taskType) {
                TaskType.RecurringTask -> SaveTaskDraftUseCase.RequestType.CreateRecurringTask
                TaskType.SingleTask -> SaveTaskDraftUseCase.RequestType.CreateSingleTask
                else -> return@launch
            }
            val resultModel = saveTaskDraftUseCase(requestType)
            if (resultModel is ResultModel.Success) {
                val taskId = resultModel.value
                setUpNavigationState(
                    ViewTasksScreenNavigation.CreateTask(taskId)
                )
            }
        }
    }

    private fun onTaskClick(event: ViewTasksScreenEvent.OnTaskClick) {
        allTasksState.value.data?.find { it.id == event.taskId }?.let { taskModel ->
            setUpConfigState(
                ViewTasksScreenConfig.ViewTaskActions(
                    stateHolder = ViewTaskActionsStateHolder(
                        taskModel = taskModel,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onPickFilterByStatus(event: ViewTasksScreenEvent.OnPickFilterByStatus) {
        event.filterByStatus.let { clickedFilter ->
            filterByStatusState.update { oldFilter ->
                if (clickedFilter != oldFilter) {
                    clickedFilter
                } else null
            }
        }
    }

    private fun onPickFilterByType(event: ViewTasksScreenEvent.OnPickFilterByType) {
        event.filterByType.let { clickedFilter ->
            filterByTypeState.update { oldFilter ->
                if (clickedFilter != oldFilter) {
                    clickedFilter
                } else null
            }
        }
    }

    private fun onPickSort(event: ViewTasksScreenEvent.OnPickSort) {
        sortState.update { event.taskSort }
    }

    private fun onMessageShown() {
        messageState.update { ViewTasksMessage.Idle }
    }

    private fun onCreateTaskClick() {
        val availableTaskTypes = setOf(TaskType.RecurringTask, TaskType.SingleTask)
        setUpConfigState(ViewTasksScreenConfig.PickTaskType(
            allTaskTypes = TaskType.entries.filter { it in availableTaskTypes }
        ))
    }

    private fun onSearchClick() {
        setUpNavigationState(ViewTasksScreenNavigation.SearchTasks)
    }

    private fun List<TaskModel.Task>.filterByStatus(filterByStatus: TaskFilterByStatus) =
        filterTasksByStatusUseCase(this, filterByStatus)

    private fun List<TaskModel.Task>.filterByType(filterByType: TaskFilterByType) =
        filterTasksByTypeUseCase(this, filterByType)

    private fun List<TaskModel.Task>.sortTasks(taskSort: TaskSort) =
        sortTasksUseCase(this, taskSort)

}