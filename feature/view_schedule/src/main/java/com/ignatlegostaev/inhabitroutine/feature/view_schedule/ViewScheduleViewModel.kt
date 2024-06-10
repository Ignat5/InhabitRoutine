package com.ignatlegostaev.inhabitroutine.feature.view_schedule

import com.ignatlegostaev.inhabitroutine.core.presentation.base.BaseViewModel
import com.ignatlegostaev.inhabitroutine.core.presentation.model.UIResultModel
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_date.PickDateStateHolder
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_date.components.PickDateScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_date.model.PickDateRequestModel
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_task_progress_type.PickTaskProgressTypeScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_task_type.PickTaskTypeScreenResult
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.firstDayOfWeek
import com.ignatlegostaev.inhabitroutine.core.util.todayDate
import com.ignatlegostaev.inhabitroutine.domain.model.derived.TaskStatus
import com.ignatlegostaev.inhabitroutine.domain.model.derived.TaskWithExtrasAndRecordModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskProgressType
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskType
import com.ignatlegostaev.inhabitroutine.domain.record.api.DeleteRecordUseCase
import com.ignatlegostaev.inhabitroutine.domain.record.api.SaveRecordUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTasksWithExtrasAndRecordByDateUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenState
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.config.enter_number_record.EnterTaskNumberRecordStateHolder
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.config.enter_number_record.components.EnterTaskNumberRecordScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.config.enter_time_record.EnterTaskTimeRecordStateHolder
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.config.enter_time_record.components.EnterTaskTimeRecordScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.config.view_task_actions.ViewTaskActionsStateHolder
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.config.view_task_actions.components.ViewTaskActionsScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.util.DefaultSortTasksUseCase
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.util.SortTasksUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.plus

@OptIn(ExperimentalCoroutinesApi::class)
class ViewScheduleViewModel(
    override val viewModelScope: CoroutineScope,
    private val readTasksWithExtrasAndRecordByDateUseCase: ReadTasksWithExtrasAndRecordByDateUseCase,
    private val saveTaskDraftUseCase: SaveTaskDraftUseCase,
    private val saveRecordUseCase: SaveRecordUseCase,
    private val deleteRecordUseCase: DeleteRecordUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
    private val sortTasksUseCase: SortTasksUseCase = DefaultSortTasksUseCase()
) : BaseViewModel<ViewScheduleScreenEvent, ViewScheduleScreenState, ViewScheduleScreenNavigation, ViewScheduleScreenConfig>() {

    private val todayDateState = flow { emit(Clock.System.todayDate) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            Clock.System.todayDate
        )

    private val currentDateState = MutableStateFlow(todayDateState.value)
    private val startOfWeekDateState = MutableStateFlow(todayDateState.value.firstDayOfWeek)

    private val allTasksState = currentDateState.flatMapLatest { date ->
        readTasksWithExtrasAndRecordByDateUseCase(date)
            .distinctUntilChanged()
            .map { allTasks ->
                if (allTasks.isNotEmpty()) {
                    withContext(defaultDispatcher) {
                        UIResultModel.Data(
                            sortTasksUseCase(allTasks)
                        )
                    }
                } else UIResultModel.Data(emptyList())
            }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        UIResultModel.Loading(emptyList())
    )

    override val uiScreenState: StateFlow<ViewScheduleScreenState> =
        combine(
            currentDateState,
            allTasksState,
            startOfWeekDateState,
            todayDateState,
        ) { currentDate, allTasks, startOfWeekDate, todayDate ->
            ViewScheduleScreenState(
                currentDate = currentDate,
                allTasksResult = allTasks,
                startOfWeekDate = startOfWeekDate,
                todayDate = todayDate
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ViewScheduleScreenState(
                currentDate = currentDateState.value,
                allTasksResult = allTasksState.value,
                startOfWeekDate = startOfWeekDateState.value,
                todayDate = todayDateState.value,
            )
        )

    override fun onEvent(event: ViewScheduleScreenEvent) {
        when (event) {
            is ViewScheduleScreenEvent.ResultEvent ->
                onResultEvent(event)

            is ViewScheduleScreenEvent.OnTaskClick ->
                onTaskClick(event)

            is ViewScheduleScreenEvent.OnTaskLongClick ->
                onTaskLongClick(event)

            is ViewScheduleScreenEvent.OnDateClick ->
                onDateClick(event)

            is ViewScheduleScreenEvent.OnNextWeekClick ->
                onNextWeekClick()

            is ViewScheduleScreenEvent.OnPrevWeekClick ->
                onPrevWeekClick()

            is ViewScheduleScreenEvent.OnCreateTaskClick ->
                onCreateTaskClick()

            is ViewScheduleScreenEvent.OnPickDateClick ->
                onPickDateClick()

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

            is ViewScheduleScreenEvent.ResultEvent.EnterTaskNumberRecord ->
                onEnterTaskNumberRecordResultEvent(event)

            is ViewScheduleScreenEvent.ResultEvent.EnterTaskTimeRecord ->
                onEnterTaskTimeRecordResultEvent(event)

            is ViewScheduleScreenEvent.ResultEvent.ViewTaskActions ->
                onViewTaskActionsResultEvent(event)

            is ViewScheduleScreenEvent.ResultEvent.PickDate ->
                onPickDateResultEvent(event)

        }
    }

    private fun onPickDateResultEvent(event: ViewScheduleScreenEvent.ResultEvent.PickDate) {
        onIdleToAction {
            when (val result = event.result) {
                is PickDateScreenResult.Confirm -> onConfirmPickDate(result)
                is PickDateScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickDate(result: PickDateScreenResult.Confirm) {
        currentDateState.update { result.date }
        startOfWeekDateState.update { result.date.firstDayOfWeek }
    }

    private fun onViewTaskActionsResultEvent(event: ViewScheduleScreenEvent.ResultEvent.ViewTaskActions) {
        onIdleToAction {
            when (val result = event.result) {
                is ViewTaskActionsScreenResult.OnActionClick ->
                    onTaskActionClick(result)

                is ViewTaskActionsScreenResult.OnEditClick ->
                    onEditTaskClick(result)

                is ViewTaskActionsScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onTaskActionClick(result: ViewTaskActionsScreenResult.OnActionClick) {
        when (result) {
            is ViewTaskActionsScreenResult.OnActionClick.EnterProgress ->
                onEnterTaskProgress(result)

            is ViewTaskActionsScreenResult.OnActionClick.Done ->
                onTaskDoneClick(result)

            is ViewTaskActionsScreenResult.OnActionClick.Skip ->
                onTaskSkipClick(result)

            is ViewTaskActionsScreenResult.OnActionClick.Fail ->
                onTaskFailClick(result)

            is ViewTaskActionsScreenResult.OnActionClick.ResetEntry ->
                onTaskResetEntryClick(result)
        }
    }

    private fun onTaskResetEntryClick(result: ViewTaskActionsScreenResult.OnActionClick.ResetEntry) {
        viewModelScope.launch {
            deleteRecordUseCase(
                taskId = result.taskId,
                date = result.date
            )
        }
    }

    private fun onTaskFailClick(result: ViewTaskActionsScreenResult.OnActionClick.Fail) {
        viewModelScope.launch {
            saveRecordUseCase(
                taskId = result.taskId,
                date = result.date,
                requestType = SaveRecordUseCase.RequestType.EntryFail
            )
        }
    }

    private fun onTaskSkipClick(result: ViewTaskActionsScreenResult.OnActionClick.Skip) {
        viewModelScope.launch {
            saveRecordUseCase(
                taskId = result.taskId,
                date = result.date,
                requestType = SaveRecordUseCase.RequestType.EntrySkip
            )
        }
    }

    private fun onTaskDoneClick(result: ViewTaskActionsScreenResult.OnActionClick.Done) {
        viewModelScope.launch {
            saveRecordUseCase(
                taskId = result.taskId,
                date = result.date,
                requestType = SaveRecordUseCase.RequestType.EntryDone
            )
        }
    }

    private fun onEnterTaskProgress(result: ViewTaskActionsScreenResult.OnActionClick.EnterProgress) {
        allTasksState.value.data?.find { it.task.id == result.taskId }
            ?.let { taskWithExtrasAndRecordModel ->
                when (taskWithExtrasAndRecordModel) {
                    is TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitNumber -> {
                        onEnterTaskNumberProgress(taskWithExtrasAndRecordModel)
                    }

                    is TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitTime -> {
                        onEnterTaskTimeProgress(taskWithExtrasAndRecordModel)
                    }

                    else -> Unit
                }
            }
    }

    private fun onEnterTaskTimeProgress(taskWithExtrasAndRecordModel: TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitTime) {
        setUpConfigState(
            ViewScheduleScreenConfig.EnterTaskTimeRecord(
                stateHolder = EnterTaskTimeRecordStateHolder(
                    taskModel = taskWithExtrasAndRecordModel.task,
                    entry = taskWithExtrasAndRecordModel.recordEntry,
                    date = currentDateState.value,
                    holderScope = provideChildScope()
                )
            )
        )
    }

    private fun onEnterTaskNumberProgress(taskWithExtrasAndRecordModel: TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitNumber) {
        setUpConfigState(
            ViewScheduleScreenConfig.EnterTaskNumberRecord(
                stateHolder = EnterTaskNumberRecordStateHolder(
                    taskModel = taskWithExtrasAndRecordModel.task,
                    entry = taskWithExtrasAndRecordModel.recordEntry,
                    date = currentDateState.value,
                    holderScope = provideChildScope()
                )
            )
        )
    }

    private fun onEditTaskClick(result: ViewTaskActionsScreenResult.OnEditClick) {
        setUpNavigationState(
            ViewScheduleScreenNavigation.EditTask(
                taskId = result.taskId
            )
        )
    }

    private fun onEnterTaskNumberRecordResultEvent(event: ViewScheduleScreenEvent.ResultEvent.EnterTaskNumberRecord) {
        onIdleToAction {
            when (val result = event.result) {
                is EnterTaskNumberRecordScreenResult.Confirm ->
                    onConfirmEnterTaskNumberRecord(result)

                is EnterTaskNumberRecordScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onEnterTaskTimeRecordResultEvent(event: ViewScheduleScreenEvent.ResultEvent.EnterTaskTimeRecord) {
        onIdleToAction {
            when (val result = event.result) {
                is EnterTaskTimeRecordScreenResult.Confirm -> onConfirmEnterTaskTimeRecord(result)
                is EnterTaskTimeRecordScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmEnterTaskTimeRecord(event: EnterTaskTimeRecordScreenResult.Confirm) {
        viewModelScope.launch {
            saveRecordUseCase(
                taskId = event.taskId,
                date = event.date,
                requestType = SaveRecordUseCase.RequestType.EntryTime(event.time)
            )
        }
    }

    private fun onConfirmEnterTaskNumberRecord(result: EnterTaskNumberRecordScreenResult.Confirm) {
        viewModelScope.launch {
            saveRecordUseCase(
                taskId = result.taskId,
                date = result.date,
                requestType = SaveRecordUseCase.RequestType.EntryNumber(result.number)
            )
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
            saveTaskDraftUseCase(SaveTaskDraftUseCase.RequestType.CreateHabit(result.taskProgressType)).let { resultModel ->
                if (resultModel is ResultModel.Success) {
                    val taskId = resultModel.value
                    setUpNavigationState(ViewScheduleScreenNavigation.CreateTask(taskId))
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

    private fun onTaskClick(event: ViewScheduleScreenEvent.OnTaskClick) {
        allTasksState.value.data?.find { it.task.id == event.taskId }
            ?.let { taskWithExtrasAndRecord ->
                when (taskWithExtrasAndRecord) {
                    is TaskWithExtrasAndRecordModel.Habit -> {
                        when (taskWithExtrasAndRecord) {
                            is TaskWithExtrasAndRecordModel.Habit.HabitContinuous -> {
                                when (taskWithExtrasAndRecord) {
                                    is TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitNumber -> {
                                        onHabitNumberClick(taskWithExtrasAndRecord)
                                    }

                                    is TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitTime -> {
                                        onHabitTimeClick(taskWithExtrasAndRecord)
                                    }
                                }
                            }

                            is TaskWithExtrasAndRecordModel.Habit.HabitYesNo -> {
                                onHabitYesNoClick(taskWithExtrasAndRecord)
                            }
                        }
                    }

                    is TaskWithExtrasAndRecordModel.Task -> {
                        onRecurringOrSingleTaskClick(taskWithExtrasAndRecord)
                    }
                }
            }
    }

    private fun onHabitNumberClick(taskWithExtrasAndRecordModel: TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitNumber) {
        setUpConfigState(
            ViewScheduleScreenConfig.EnterTaskNumberRecord(
                stateHolder = EnterTaskNumberRecordStateHolder(
                    taskModel = taskWithExtrasAndRecordModel.task,
                    entry = taskWithExtrasAndRecordModel.recordEntry,
                    date = currentDateState.value,
                    holderScope = provideChildScope()
                )
            )
        )
    }

    private fun onHabitTimeClick(taskWithExtrasAndRecordModel: TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitTime) {
        setUpConfigState(
            ViewScheduleScreenConfig.EnterTaskTimeRecord(
                stateHolder = EnterTaskTimeRecordStateHolder(
                    taskModel = taskWithExtrasAndRecordModel.task,
                    entry = taskWithExtrasAndRecordModel.recordEntry,
                    date = currentDateState.value,
                    holderScope = provideChildScope()
                )
            )
        )
    }

    private fun onHabitYesNoClick(taskWithExtrasAndRecordModel: TaskWithExtrasAndRecordModel.Habit.HabitYesNo) {
        viewModelScope.launch {
            val taskId = taskWithExtrasAndRecordModel.task.id
            val date = currentDateState.value
            when (val status = taskWithExtrasAndRecordModel.status) {
                is TaskStatus.Completed -> {
                    deleteRecordUseCase(taskId = taskId, date = date)
                }

                is TaskStatus.NotCompleted -> {
                    when (status) {
                        is TaskStatus.NotCompleted.Pending -> {
                            saveRecordUseCase(
                                taskId = taskId,
                                date = date,
                                requestType = SaveRecordUseCase.RequestType.EntryDone
                            )
                        }

                        else -> {
                            deleteRecordUseCase(
                                taskId = taskId,
                                date = date
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onRecurringOrSingleTaskClick(taskWithExtrasAndRecordModel: TaskWithExtrasAndRecordModel.Task) {
        viewModelScope.launch {
            val taskId = taskWithExtrasAndRecordModel.task.id
            val date = currentDateState.value
            when (taskWithExtrasAndRecordModel.status) {
                is TaskStatus.NotCompleted.Pending -> {
                    saveRecordUseCase(
                        taskId = taskId,
                        date = date,
                        requestType = SaveRecordUseCase.RequestType.EntryDone
                    )
                }

                is TaskStatus.Completed -> {
                    deleteRecordUseCase(
                        taskId = taskId,
                        date = date
                    )
                }
            }
        }
    }

    private fun onTaskLongClick(event: ViewScheduleScreenEvent.OnTaskLongClick) {
        allTasksState.value.data?.find { it.task.id == event.taskId }
            ?.let { taskWithExtrasAndRecordModel ->
                setUpConfigState(
                    ViewScheduleScreenConfig.ViewTaskActions(
                        stateHolder = ViewTaskActionsStateHolder(
                            taskWithExtrasAndRecordModel = taskWithExtrasAndRecordModel,
                            date = currentDateState.value,
                            holderScope = provideChildScope()
                        )
                    )
                )
            }
    }

    private fun onDateClick(event: ViewScheduleScreenEvent.OnDateClick) {
        currentDateState.update { event.date }
    }

    private fun onNextWeekClick() {
        startOfWeekDateState.update { oldDate ->
            oldDate.plus(1, DateTimeUnit.WEEK)
        }
    }

    private fun onPrevWeekClick() {
        startOfWeekDateState.update { oldDate ->
            oldDate.minus(1, DateTimeUnit.WEEK)
        }
    }

    private fun onPickHabitTaskType() {
        setUpConfigState(ViewScheduleScreenConfig.PickTaskProgressType(allProgressTypes = TaskProgressType.entries))
    }

    private fun onPickRecurringTaskType() {
        viewModelScope.launch {
            saveTaskDraftUseCase(SaveTaskDraftUseCase.RequestType.CreateRecurringTask).let { resultModel ->
                if (resultModel is ResultModel.Success) {
                    val taskId = resultModel.value
                    setUpNavigationState(ViewScheduleScreenNavigation.CreateTask(taskId))
                }
            }
        }
    }

    private fun onPickSingleTaskType() {
        viewModelScope.launch {
            saveTaskDraftUseCase(SaveTaskDraftUseCase.RequestType.CreateSingleTask).let { resultModel ->
                if (resultModel is ResultModel.Success) {
                    val taskId = resultModel.value
                    setUpNavigationState(ViewScheduleScreenNavigation.CreateTask(taskId))
                }
            }
        }
    }

    private fun onCreateTaskClick() {
        setUpConfigState(ViewScheduleScreenConfig.PickTaskType(TaskType.entries))
    }

    private fun onPickDateClick() {
        currentDateState.value.let { currentDate ->
            setUpConfigState(
                ViewScheduleScreenConfig.PickDate(
                    stateHolder = PickDateStateHolder(
                        requestModel = PickDateRequestModel(
                            initDate = currentDate,
                            minDate = currentDate.minus(1, DateTimeUnit.YEAR),
                            maxDate = currentDate.plus(1, DateTimeUnit.YEAR)
                        ),
                        defaultDispatcher = defaultDispatcher,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onSearchClick() {
        setUpNavigationState(ViewScheduleScreenNavigation.SearchTasks)
    }
}