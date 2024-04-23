package com.example.inhabitroutine.feature.view_schedule

import com.example.inhabitroutine.core.presentation.base.BaseViewModel
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_task_progress_type.PickTaskProgressTypeScreenResult
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_task_type.PickTaskTypeScreenResult
import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.domain.model.derived.TaskStatus
import com.example.inhabitroutine.domain.model.derived.TaskWithExtrasAndRecordModel
import com.example.inhabitroutine.domain.model.task.type.TaskProgressType
import com.example.inhabitroutine.domain.model.task.type.TaskType
import com.example.inhabitroutine.domain.task.api.use_case.ReadTasksWithExtrasAndRecordByDateUseCase
import com.example.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.example.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenConfig
import com.example.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenEvent
import com.example.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenNavigation
import com.example.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class ViewScheduleViewModel(
    override val viewModelScope: CoroutineScope,
    private val readTasksWithExtrasAndRecordByDateUseCase: ReadTasksWithExtrasAndRecordByDateUseCase,
    private val saveTaskDraftUseCase: SaveTaskDraftUseCase,
    private val defaultDispatcher: CoroutineDispatcher
) : BaseViewModel<ViewScheduleScreenEvent, ViewScheduleScreenState, ViewScheduleScreenNavigation, ViewScheduleScreenConfig>() {

    private val currentDateState = MutableStateFlow(todayDate)

    private val allTasksState = currentDateState.flatMapLatest { date ->
        readTasksWithExtrasAndRecordByDateUseCase(date)
            .distinctUntilChanged()
            .map { allTasks ->
                if (allTasks.isNotEmpty()) {
                    withContext(defaultDispatcher) {
                        allTasks.sortTasks()
                    }
                } else emptyList()
            }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    override val uiScreenState: StateFlow<ViewScheduleScreenState> =
        combine(
            currentDateState,
            allTasksState
        ) { currentDate, allTasks ->
            ViewScheduleScreenState(
                currentDate = currentDate,
                allTasks = allTasks
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ViewScheduleScreenState(
                currentDate = currentDateState.value,
                allTasks = allTasksState.value
            )
        )

    override fun onEvent(event: ViewScheduleScreenEvent) {
        when (event) {
            is ViewScheduleScreenEvent.ResultEvent ->
                onResultEvent(event)

            is ViewScheduleScreenEvent.OnCreateTaskClick ->
                onCreateTaskClick()

            is ViewScheduleScreenEvent.OnSearchClick ->
                onSearchClick()
        }
    }

    private fun onResultEvent(event: ViewScheduleScreenEvent.ResultEvent) {
        when (event) {
            is ViewScheduleScreenEvent.ResultEvent.PickTaskType ->
                onPickTaskTypeResultEvent(event)

            is ViewScheduleScreenEvent.ResultEvent.PickTaskProgressType ->
                onPickTaskProgressTypeResultEvent(event)
        }
    }

    private fun onPickTaskProgressTypeResultEvent(event: ViewScheduleScreenEvent.ResultEvent.PickTaskProgressType) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskProgressTypeScreenResult.Confirm ->
                    onConfirmPickTaskProgressType(result)

                is PickTaskProgressTypeScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickTaskProgressType(result: PickTaskProgressTypeScreenResult.Confirm) {
        viewModelScope.launch {
            saveTaskDraftUseCase(SaveTaskDraftUseCase.RequestType.CreateHabit(result.taskProgressType)).let { result ->
                when (result) {
                    is ResultModel.Success -> {
                        val taskId = result.value
                        setUpNavigationState(ViewScheduleScreenNavigation.CreateTask(taskId))
                    }

                    is ResultModel.Failure -> {
                        /* TODO */
                    }
                }
            }
        }
    }

    private fun onPickTaskTypeResultEvent(event: ViewScheduleScreenEvent.ResultEvent.PickTaskType) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskTypeScreenResult.Confirm -> onConfirmPickTaskType(result)
                is PickTaskTypeScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickTaskType(result: PickTaskTypeScreenResult.Confirm) {
        when (result.taskType) {
            TaskType.Habit -> onPickHabitTaskType()
            TaskType.RecurringTask -> onPickRecurringTaskType()
            TaskType.SingleTask -> onPickSingleTaskType()
        }
    }

    private fun onPickHabitTaskType() {
        setUpConfigState(ViewScheduleScreenConfig.PickTaskProgressType(allProgressTypes = TaskProgressType.entries))
    }

    private fun onPickRecurringTaskType() {
        viewModelScope.launch {
            saveTaskDraftUseCase(SaveTaskDraftUseCase.RequestType.CreateRecurringTask).let { result ->
                when (result) {
                    is ResultModel.Success -> {
                        val taskId = result.value
                        setUpNavigationState(ViewScheduleScreenNavigation.CreateTask(taskId))
                    }

                    is ResultModel.Failure -> {
                        /* TODO */
                    }
                }
            }
        }
    }

    private fun onPickSingleTaskType() {
        viewModelScope.launch {
            saveTaskDraftUseCase(SaveTaskDraftUseCase.RequestType.CreateSingleTask).let { result ->
                when (result) {
                    is ResultModel.Success -> {
                        val taskId = result.value
                        setUpNavigationState(ViewScheduleScreenNavigation.CreateTask(taskId))
                    }

                    is ResultModel.Failure -> {
                        /* TODO */
                    }
                }
            }
        }
    }

    private fun onCreateTaskClick() {
        setUpConfigState(ViewScheduleScreenConfig.PickTaskType(TaskType.entries))
    }

    private fun onSearchClick() {
        setUpNavigationState(ViewScheduleScreenNavigation.SearchTasks)
    }

    private fun List<TaskWithExtrasAndRecordModel>.sortTasks() = this.let { allTasks ->
        allTasks.sortedWith(
            compareBy<TaskWithExtrasAndRecordModel> { taskWithExtrasAndRecord ->
                when (taskWithExtrasAndRecord.status) {
                    is TaskStatus.NotCompleted.Pending -> Int.MIN_VALUE
                    else -> Int.MAX_VALUE
                }
            }.thenBy { taskWithExtrasAndRecord ->
                taskWithExtrasAndRecord.taskWithExtrasModel.taskExtras.allReminders.minByOrNull { it.time }?.time?.toMillisecondOfDay()
                    ?: Int.MAX_VALUE
            }.thenBy { taskWithExtrasAndRecord ->
                taskWithExtrasAndRecord.taskWithExtrasModel.taskModel.createdAt
            }
        )
    }

    private val todayDate: LocalDate
        get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

}