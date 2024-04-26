package com.example.inhabitroutine.feature.view_tasks

import com.example.inhabitroutine.core.presentation.base.BaseViewModel
import com.example.inhabitroutine.core.util.todayDate
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.model.task.content.TaskDate
import com.example.inhabitroutine.domain.model.task.type.TaskType
import com.example.inhabitroutine.domain.task.api.use_case.ReadTasksUseCase
import com.example.inhabitroutine.feature.view_tasks.components.ViewTasksScreenConfig
import com.example.inhabitroutine.feature.view_tasks.components.ViewTasksScreenEvent
import com.example.inhabitroutine.feature.view_tasks.components.ViewTasksScreenNavigation
import com.example.inhabitroutine.feature.view_tasks.components.ViewTasksScreenState
import com.example.inhabitroutine.feature.view_tasks.model.TaskFilterByStatus
import com.example.inhabitroutine.feature.view_tasks.model.TaskFilterByType
import com.example.inhabitroutine.feature.view_tasks.model.TaskSort
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate

class ViewTasksViewModel(
    readTasksUseCase: ReadTasksUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
    override val viewModelScope: CoroutineScope
) : BaseViewModel<ViewTasksScreenEvent, ViewTasksScreenState, ViewTasksScreenNavigation, ViewTasksScreenConfig>() {

    private val filterByStatusState = MutableStateFlow<TaskFilterByStatus?>(null)
    private val filterByTypeState = MutableStateFlow<TaskFilterByType?>(null)
    private val sortState = MutableStateFlow<TaskSort>(TaskSort.ByDate)

    private val allTasksState = combine(
        readTasksUseCase(),
        filterByStatusState,
        filterByTypeState,
        sortState
    ) { allTasks, filterByStatus, filterByType, sort ->
        if (allTasks.isNotEmpty()) {
            withContext(defaultDispatcher) {
                allTasks
                    .let { if (filterByStatus != null) it.filterByStatus(filterByStatus) else it }
                    .let { if (filterByType != null) it.filterByType(filterByType) else it }
                    .sortTasks(sort)
            }
        } else emptyList()
    }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    override val uiScreenState: StateFlow<ViewTasksScreenState> =
        combine(
            allTasksState,
            filterByStatusState,
            filterByTypeState,
            sortState
        ) { allTasks, filterByStatus, filterByType, sort ->
            ViewTasksScreenState(
                allTasks = allTasks,
                filterByStatus = filterByStatus,
                filterByType = filterByType,
                sort = sort
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ViewTasksScreenState(
                allTasks = allTasksState.value,
                filterByStatus = filterByStatusState.value,
                filterByType = filterByTypeState.value,
                sort = sortState.value
            )
        )

    override fun onEvent(event: ViewTasksScreenEvent) {
        when (event) {
            is ViewTasksScreenEvent.OnPickFilterByStatus ->
                onPickFilterByStatus(event)

            is ViewTasksScreenEvent.OnPickFilterByType ->
                onPickFilterByType(event)

            is ViewTasksScreenEvent.OnPickSort ->
                onPickSort(event)
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

    private fun List<TaskModel.Task>.filterByStatus(filterByStatus: TaskFilterByStatus) =
        this.let { allTasks ->
            when (filterByStatus) {
                is TaskFilterByStatus.OnlyActive -> {
                    Clock.System.todayDate.let { todayDate ->
                        allTasks.filter { taskModel ->
                            if (!taskModel.isArchived) {
                                when (val taskDate = taskModel.date) {
                                    is TaskDate.Period -> {
                                        taskDate.endDate?.let { endDate ->
                                            todayDate <= endDate
                                        } ?: true
                                    }

                                    is TaskDate.Day -> taskDate.date >= todayDate
                                }
                            } else false
                        }
                    }
                }

                is TaskFilterByStatus.OnlyArchived -> allTasks.filter { it.isArchived }
            }
        }

    private fun List<TaskModel.Task>.filterByType(filterByType: TaskFilterByType) =
        this.let { allTasks ->
            when (filterByType) {
                is TaskFilterByType.OnlyRecurring -> allTasks.filter { it.type == TaskType.RecurringTask }
                is TaskFilterByType.OnlySingle -> allTasks.filter { it.type == TaskType.SingleTask }
            }
        }

    private fun List<TaskModel.Task>.sortTasks(taskSort: TaskSort) =
        this.let { allTasks ->
            when (taskSort) {
                is TaskSort.ByDate -> {
                    allTasks.sortedByDescending { taskModel ->
                        when (val date = taskModel.date) {
                            is TaskDate.Period -> date.startDate
                            is TaskDate.Day -> date.date
                        }
                    }
                }

                is TaskSort.ByTitle -> {
                    allTasks.sortedBy { it.title }
                }
            }
        }

}