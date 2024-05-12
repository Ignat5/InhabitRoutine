package com.ignatlegostaev.inhabitroutine.feature.view_habits

import com.ignatlegostaev.inhabitroutine.core.presentation.base.BaseViewModel
import com.ignatlegostaev.inhabitroutine.core.presentation.model.UIResultModel
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.archive_task.ArchiveTaskStateHolder
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.archive_task.components.ArchiveTaskScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.delete_task.DeleteTaskStateHolder
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.delete_task.components.DeleteTaskScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_task_progress_type.PickTaskProgressTypeScreenResult
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.todayDate
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskProgressType
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ArchiveTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.DeleteTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadHabitsUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.ignatlegostaev.inhabitroutine.feature.view_habits.components.ViewHabitsScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.view_habits.components.ViewHabitsScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_habits.components.ViewHabitsScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.view_habits.components.ViewHabitsScreenState
import com.ignatlegostaev.inhabitroutine.feature.view_habits.config.view_habit_actions.ViewHabitActionsStateHolder
import com.ignatlegostaev.inhabitroutine.feature.view_habits.config.view_habit_actions.components.ViewHabitActionsScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_habits.model.HabitFilterByStatus
import com.ignatlegostaev.inhabitroutine.feature.view_habits.model.HabitSort
import com.ignatlegostaev.inhabitroutine.feature.view_habits.model.ViewHabitsMessage
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
import kotlinx.datetime.Clock

class ViewHabitsViewModel(
    readHabitsUseCase: ReadHabitsUseCase,
    private val saveTaskDraftUseCase: SaveTaskDraftUseCase,
    private val archiveTaskByIdUseCase: ArchiveTaskByIdUseCase,
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
    override val viewModelScope: CoroutineScope
) : BaseViewModel<ViewHabitsScreenEvent, ViewHabitsScreenState, ViewHabitsScreenNavigation, ViewHabitsScreenConfig>() {
    private val filterByStatusState = MutableStateFlow<HabitFilterByStatus?>(null)
    private val sortState = MutableStateFlow<HabitSort>(HabitSort.ByPriority)
    private val messageState = MutableStateFlow<ViewHabitsMessage>(ViewHabitsMessage.Idle)

    private val allHabitsState = combine(
        readHabitsUseCase(),
        filterByStatusState,
        sortState
    ) { allHabits, filterByStatus, sort ->
        if (allHabits.isNotEmpty()) {
            withContext(defaultDispatcher) {
                UIResultModel.Data(
                    allHabits
                        .let { if (filterByStatus != null) it.filterByStatus(filterByStatus) else it }
                        .sortHabits(sort)
                )
            }
        } else UIResultModel.Data(emptyList())
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        UIResultModel.Loading(emptyList())
    )

    override val uiScreenState: StateFlow<ViewHabitsScreenState> =
        combine(
            allHabitsState,
            filterByStatusState,
            sortState,
            messageState
        ) { allHabits, filterByStatus, sort, message ->
            ViewHabitsScreenState(
                allHabitsResult = allHabits,
                filterByStatus = filterByStatus,
                sort = sort,
                message = message
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ViewHabitsScreenState(
                allHabitsResult = allHabitsState.value,
                filterByStatus = filterByStatusState.value,
                sort = sortState.value,
                message = messageState.value
            )
        )

    override fun onEvent(event: ViewHabitsScreenEvent) {
        when (event) {
            is ViewHabitsScreenEvent.ResultEvent ->
                onResultEvent(event)

            is ViewHabitsScreenEvent.OnHabitClick ->
                onHabitClick(event)

            is ViewHabitsScreenEvent.OnPickFilterByStatus ->
                onPickFilterByStatus(event)

            is ViewHabitsScreenEvent.OnPickSort ->
                onPickSort(event)

            is ViewHabitsScreenEvent.OnMessageShown ->
                onMessageShown()

            is ViewHabitsScreenEvent.OnCreateHabitClick ->
                onCreateHabitClick()

            is ViewHabitsScreenEvent.OnSearchClick ->
                onSearchClick()
        }
    }

    private fun onResultEvent(event: ViewHabitsScreenEvent.ResultEvent) {
        when (event) {
            is ViewHabitsScreenEvent.ResultEvent.ViewHabitActions ->
                onViewHabitActionsResultEvent(event)

            is ViewHabitsScreenEvent.ResultEvent.PickTaskProgressType ->
                onPickTaskProgressTypeResultEvent(event)

            is ViewHabitsScreenEvent.ResultEvent.ArchiveTask ->
                onArchiveTaskResultEvent(event)

            is ViewHabitsScreenEvent.ResultEvent.DeleteTask ->
                onDeleteTaskResultEvent(event)
        }
    }

    private fun onViewHabitActionsResultEvent(event: ViewHabitsScreenEvent.ResultEvent.ViewHabitActions) {
        onIdleToAction {
            when (val result = event.result) {
                is ViewHabitActionsScreenResult.Action -> onAction(result)
                is ViewHabitActionsScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onAction(result: ViewHabitActionsScreenResult.Action) {
        when (result) {
            is ViewHabitActionsScreenResult.Action.ViewStatistics ->
                onViewStatistics(result.taskId)

            is ViewHabitActionsScreenResult.Action.Edit ->
                onEditTask(result.taskId)

            is ViewHabitActionsScreenResult.Action.Archive ->
                onArchiveTask(result.taskId)

            is ViewHabitActionsScreenResult.Action.Unarchive ->
                onUnarchiveTask(result.taskId)

            is ViewHabitActionsScreenResult.Action.Delete ->
                onDeleteTask(result.taskId)
        }
    }

    private fun onDeleteTask(taskId: String) {
        setUpConfigState(
            ViewHabitsScreenConfig.DeleteTask(
                stateHolder = DeleteTaskStateHolder(
                    taskId = taskId,
                    holderScope = provideChildScope()
                )
            )
        )
    }

    private fun onUnarchiveTask(taskId: String) {
        viewModelScope.launch {
            val resultModel = archiveTaskByIdUseCase(
                taskId = taskId,
                requestType = ArchiveTaskByIdUseCase.RequestType.Unarchive
            )
            if (resultModel is ResultModel.Success) {
                messageState.update { ViewHabitsMessage.Message.UnarchiveSuccess }
            }
        }
    }

    private fun onArchiveTask(taskId: String) {
        setUpConfigState(
            ViewHabitsScreenConfig.ArchiveTask(
                stateHolder = ArchiveTaskStateHolder(
                    taskId = taskId,
                    holderScope = provideChildScope()
                )
            )
        )
    }

    private fun onEditTask(taskId: String) {
        setUpNavigationState(ViewHabitsScreenNavigation.EditTask(taskId))
    }

    private fun onViewStatistics(taskId: String) {
        setUpNavigationState(ViewHabitsScreenNavigation.ViewStatistics(taskId))
    }

    private fun onPickTaskProgressTypeResultEvent(event: ViewHabitsScreenEvent.ResultEvent.PickTaskProgressType) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskProgressTypeScreenResult.Confirm -> onConfirmPickTaskProgressType(result)
                is PickTaskProgressTypeScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickTaskProgressType(result: PickTaskProgressTypeScreenResult.Confirm) {
        viewModelScope.launch {
            val resultModel = saveTaskDraftUseCase(
                requestType = SaveTaskDraftUseCase.RequestType.CreateHabit(
                    progressType = result.taskProgressType
                )
            )
            if (resultModel is ResultModel.Success) {
                val taskId = resultModel.value
                setUpNavigationState(
                    ViewHabitsScreenNavigation.CreateTask(taskId = taskId)
                )
            }
        }
    }

    private fun onArchiveTaskResultEvent(event: ViewHabitsScreenEvent.ResultEvent.ArchiveTask) {
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
                messageState.update { ViewHabitsMessage.Message.ArchiveSuccess }
            }
        }
    }

    private fun onDeleteTaskResultEvent(event: ViewHabitsScreenEvent.ResultEvent.DeleteTask) {
        onIdleToAction {
            when (val result = event.result) {
                is DeleteTaskScreenResult.Confirm -> onConfirmDeleteTask(result)
                is DeleteTaskScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmDeleteTask(result: DeleteTaskScreenResult.Confirm) {
        viewModelScope.launch {
            val resultModel = deleteTaskByIdUseCase(result.taskId)
            if (resultModel is ResultModel.Success) {
                messageState.update { ViewHabitsMessage.Message.DeleteSuccess }
            }
        }
    }

    private fun onHabitClick(event: ViewHabitsScreenEvent.OnHabitClick) {
        allHabitsState.value.data?.find { it.id == event.habitId }?.let { taskModel ->
            setUpConfigState(
                ViewHabitsScreenConfig.ViewHabitActions(
                    stateHolder = ViewHabitActionsStateHolder(
                        taskModel = taskModel,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onPickFilterByStatus(event: ViewHabitsScreenEvent.OnPickFilterByStatus) {
        event.filterByStatus.let { clickedFilter ->
            filterByStatusState.update { oldFilter ->
                if (clickedFilter != oldFilter) {
                    clickedFilter
                } else null
            }
        }
    }

    private fun onPickSort(event: ViewHabitsScreenEvent.OnPickSort) {
        sortState.update { event.sort }
    }

    private fun onMessageShown() {
        messageState.update { ViewHabitsMessage.Idle }
    }

    private fun onCreateHabitClick() {
        setUpConfigState(
            ViewHabitsScreenConfig.PickTaskProgressType(
                allProgressTypes = TaskProgressType.entries
            )
        )
    }

    private fun onSearchClick() {
        setUpNavigationState(ViewHabitsScreenNavigation.SearchTasks)
    }

    private fun List<TaskModel.Habit>.filterByStatus(filterByStatus: HabitFilterByStatus) =
        this.let { allHabits ->
            when (filterByStatus) {
                HabitFilterByStatus.OnlyActive -> {
                    Clock.System.todayDate.let { todayDate ->
                        allHabits.filter { taskModel ->
                            if (!taskModel.isArchived) {
                                taskModel.date.endDate?.let { endDate ->
                                    todayDate in taskModel.date.startDate..endDate
                                } ?: (todayDate >= taskModel.date.startDate)
                            } else false
                        }
                    }
                }

                HabitFilterByStatus.OnlyArchived -> {
                    allHabits.filter { it.isArchived }
                }
            }
        }

    private fun List<TaskModel.Habit>.sortHabits(sort: HabitSort) = this.let { allHabits ->
        when (sort) {
            HabitSort.ByPriority -> allHabits.sortedByDescending { it.priority }
            HabitSort.ByDate -> allHabits.sortedByDescending { it.date.startDate }
            HabitSort.ByTitle -> allHabits.sortedBy { it.title }
        }
    }

}