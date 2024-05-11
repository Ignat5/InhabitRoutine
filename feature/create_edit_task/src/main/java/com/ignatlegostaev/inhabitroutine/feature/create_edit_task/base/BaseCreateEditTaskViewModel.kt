package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base

import com.ignatlegostaev.inhabitroutine.core.presentation.base.BaseViewModel
import com.ignatlegostaev.inhabitroutine.core.presentation.components.config.ScreenConfig
import com.ignatlegostaev.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.ignatlegostaev.inhabitroutine.core.presentation.components.navigation.ScreenNavigation
import com.ignatlegostaev.inhabitroutine.core.presentation.components.state.ScreenState
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_date.PickDateStateHolder
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_date.components.PickDateScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_date.model.PickDateRequestModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskDate
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskDateByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskDescriptionByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskFrequencyByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskProgressByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskTitleByIdUseCase
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_description.PickTaskDescriptionStateHolder
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_description.components.PickTaskDescriptionScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.PickTaskFrequencyStateHolder
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.components.PickTaskFrequencyScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_number_progress.PickTaskNumberProgressStateHolder
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_number_progress.components.PickTaskNumberProgressScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_time_progress.PickTaskTimeProgressStateHolder
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_time_progress.components.PickTaskTimeProgressScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_title.PickTaskTitleStateHolder
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_title.components.PickTaskTitleScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.model.BaseItemTaskConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

abstract class BaseCreateEditTaskViewModel<SE : ScreenEvent, SS : ScreenState, SN : ScreenNavigation, SC : ScreenConfig>(
    private val updateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase,
    private val updateTaskProgressByIdUseCase: UpdateTaskProgressByIdUseCase,
    private val updateTaskFrequencyByIdUseCase: UpdateTaskFrequencyByIdUseCase,
    private val updateTaskDateByIdUseCase: UpdateTaskDateByIdUseCase,
    private val updateTaskDescriptionByIdUseCase: UpdateTaskDescriptionByIdUseCase,
    private val defaultDispatcher: CoroutineDispatcher
) : BaseViewModel<SE, SS, SN, SC>() {
    private val todayDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    protected abstract val taskModelState: StateFlow<TaskModel?>
    protected abstract fun setUpBaseConfigState(baseConfig: BaseCreateEditTaskScreenConfig)
    protected abstract fun setUpBaseNavigationState(baseNavigation: BaseCreateEditTaskScreenNavigation)

    protected fun onBaseEvent(event: BaseCreateEditTaskScreenEvent) {
        when (event) {
            is BaseCreateEditTaskScreenEvent.OnItemConfigClick ->
                onItemConfigClick(event)

            is BaseCreateEditTaskScreenEvent.ResultEvent ->
                onResultEvent(event)
        }
    }

    private fun onResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent) {
        when (event) {
            is BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskTitle ->
                onPickTaskTitleResultEvent(event)

            is BaseCreateEditTaskScreenEvent.ResultEvent.PickDate ->
                onPickDateResultEvent(event)

            is BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskFrequency ->
                onPickTaskFrequencyResultEvent(event)

            is BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskNumberProgress ->
                onPickTaskNumberProgressResultEvent(event)

            is BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskTimeProgress ->
                onPickTaskTimeProgressResultEvent(event)

            is BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskDescription ->
                onPickTaskDescriptionResultEvent(event)
        }
    }

    private fun onPickTaskDescriptionResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskDescription) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskDescriptionScreenResult.Confirm -> onConfirmPickTaskDescription(result)
                is PickTaskDescriptionScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickTaskDescription(result: PickTaskDescriptionScreenResult.Confirm) {
        taskModelState.value?.let { taskModel ->
            viewModelScope.launch {
                updateTaskDescriptionByIdUseCase(
                    taskId = taskModel.id,
                    description = result.description
                )
            }
        }
    }

    private fun onPickDateResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent.PickDate) {
        when (event) {
            is BaseCreateEditTaskScreenEvent.ResultEvent.PickDate.Date ->
                onPickTaskDateResultEvent(event)

            is BaseCreateEditTaskScreenEvent.ResultEvent.PickDate.StartDate ->
                onPickStartDateResultEvent(event)

            is BaseCreateEditTaskScreenEvent.ResultEvent.PickDate.EndDate ->
                onPickEndDateResultEvent(event)
        }
    }

    private fun onPickEndDateResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent.PickDate.EndDate) {
        onIdleToAction {
            when (val result = event.result) {
                is PickDateScreenResult.Confirm -> onConfirmPickEndDate(result)
                is PickDateScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickEndDate(result: PickDateScreenResult.Confirm) {
        taskModelState.value?.let { taskModel ->
            (taskModel.date as? TaskDate.Period)?.let { datePeriod ->
                viewModelScope.launch {
                    updateTaskDateByIdUseCase(
                        taskId = taskModel.id,
                        taskDate = datePeriod.copy(endDate = result.date)
                    )
                }
            }
        }
    }

    private fun onPickStartDateResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent.PickDate.StartDate) {
        onIdleToAction {
            when (val result = event.result) {
                is PickDateScreenResult.Confirm ->
                    onConfirmPickStartDate(result)

                is PickDateScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickStartDate(result: PickDateScreenResult.Confirm) {
        taskModelState.value?.let { taskModel ->
            (taskModel.date as? TaskDate.Period)?.copy(startDate = result.date)?.let { datePeriod ->
                viewModelScope.launch {
                    updateTaskDateByIdUseCase(
                        taskId = taskModel.id,
                        taskDate = datePeriod
                    )
                }
            }
        }
    }

    private fun onPickTaskDateResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent.PickDate.Date) {
        onIdleToAction {
            when (val result = event.result) {
                is PickDateScreenResult.Confirm ->
                    onConfirmPickTaskDate(result)

                is PickDateScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickTaskDate(result: PickDateScreenResult.Confirm) {
        taskModelState.value?.let { taskModel ->
            (taskModel.date as? TaskDate.Day)?.copy(date = result.date)?.let { date ->
                viewModelScope.launch {
                    updateTaskDateByIdUseCase(
                        taskId = taskModel.id,
                        taskDate = date,
                    )
                }
            }
        }
    }

    private fun onPickTaskFrequencyResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskFrequency) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskFrequencyScreenResult.Confirm -> onConfirmPickTaskFrequency(result)
                is PickTaskFrequencyScreenResult.Dismiss -> Unit
            }
        }
    }

    protected open fun onConfirmPickTaskFrequency(result: PickTaskFrequencyScreenResult.Confirm) {
        taskModelState.value?.let { taskModel ->
            viewModelScope.launch {
                updateTaskFrequencyByIdUseCase(
                    taskId = taskModel.id,
                    taskFrequency = result.taskFrequency
                )
            }
        }
    }

    private fun onPickTaskTimeProgressResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskTimeProgress) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskTimeProgressScreenResult.Confirm ->
                    onConfirmPickTaskTimeProgress(result)

                is PickTaskTimeProgressScreenResult.Dismiss -> Unit
            }
        }
    }

    protected open fun onConfirmPickTaskTimeProgress(result: PickTaskTimeProgressScreenResult.Confirm) {
        taskModelState.value?.let { taskModel ->
            viewModelScope.launch {
                updateTaskProgressByIdUseCase(
                    taskId = taskModel.id,
                    taskProgress = result.taskProgress
                )
            }
        }
    }

    private fun onPickTaskNumberProgressResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskNumberProgress) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskNumberProgressScreenResult.Confirm ->
                    onConfirmPickTaskNumberProgress(result)

                is PickTaskNumberProgressScreenResult.Dismiss -> Unit
            }
        }
    }

    protected open fun onConfirmPickTaskNumberProgress(result: PickTaskNumberProgressScreenResult.Confirm) {
        taskModelState.value?.let { taskModel ->
            viewModelScope.launch {
                updateTaskProgressByIdUseCase(
                    taskId = taskModel.id,
                    taskProgress = result.taskProgress
                )
            }
        }
    }

    private fun onPickTaskTitleResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskTitle) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskTitleScreenResult.Confirm ->
                    onConfirmPickTaskTitle(result)

                is PickTaskTitleScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickTaskTitle(result: PickTaskTitleScreenResult.Confirm) {
        taskModelState.value?.let { taskModel ->
            viewModelScope.launch {
                updateTaskTitleByIdUseCase(
                    taskId = taskModel.id,
                    title = result.title
                )
            }
        }
    }

    private fun onItemConfigClick(event: BaseCreateEditTaskScreenEvent.OnItemConfigClick) {
        when (val itemConfig = event.itemConfig) {
            is BaseItemTaskConfig.Title ->
                onConfigTaskTitleClick()

            is BaseItemTaskConfig.Frequency ->
                onConfigTaskFrequencyClick()

            is BaseItemTaskConfig.DateConfig ->
                onConfigDateClick(itemConfig)

            is BaseItemTaskConfig.Progress ->
                onConfigTaskProgressClick(itemConfig)

            is BaseItemTaskConfig.Reminders ->
                onConfigRemindersClick()

            is BaseItemTaskConfig.Description ->
                onConfigTaskDescriptionClick()

            is BaseItemTaskConfig.Priority ->
                onConfigTaskPriorityClick()
        }
    }

    private fun onConfigTaskPriorityClick() {
        taskModelState.value?.priority?.let { priority ->

        }
    }

    private fun onConfigTaskDescriptionClick() {
        taskModelState.value?.let { taskModel ->
            setUpBaseConfigState(
                BaseCreateEditTaskScreenConfig.PickTaskDescription(
                    stateHolder = PickTaskDescriptionStateHolder(
                        initDescription = taskModel.description,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onConfigRemindersClick() {
        taskModelState.value?.let { taskModel ->
            setUpBaseNavigationState(
                BaseCreateEditTaskScreenNavigation.ViewReminders(
                    taskModel.id
                )
            )
        }
    }

    private fun onConfigDateClick(itemConfig: BaseItemTaskConfig.DateConfig) {
        when (itemConfig) {
            is BaseItemTaskConfig.DateConfig.Date ->
                onConfigDateClick()

            is BaseItemTaskConfig.DateConfig.StartDate ->
                onConfigStartDateClick()

            is BaseItemTaskConfig.DateConfig.EndDate ->
                onConfigEndDateClick()
        }
    }

    private fun onConfigEndDateClick() {
        taskModelState.value?.let { taskModel ->
            (taskModel.date as? TaskDate.Period)?.let { datePeriod ->
                if (datePeriod.endDate != null) {
                    viewModelScope.launch {
                        updateTaskDateByIdUseCase(
                            taskId = taskModel.id,
                            taskDate = datePeriod.copy(endDate = null)
                        )
                    }
                } else {
                    setUpBaseConfigState(
                        BaseCreateEditTaskScreenConfig.PickDate.EndDate(
                            stateHolder = PickDateStateHolder(
                                requestModel = PickDateRequestModel(
                                    initDate = datePeriod.startDate,
                                    minDate = datePeriod.startDate,
                                    maxDate = maxOf(datePeriod.startDate, todayDate).plus(
                                        1,
                                        DateTimeUnit.YEAR
                                    )
                                ),
                                defaultDispatcher = defaultDispatcher,
                                holderScope = provideChildScope()
                            )
                        )
                    )
                }
            }
        }
    }

    private fun onConfigStartDateClick() {
        (taskModelState.value?.date as? TaskDate.Period)?.let { period ->
            setUpBaseConfigState(
                BaseCreateEditTaskScreenConfig.PickDate.StartDate(
                    stateHolder = PickDateStateHolder(
                        requestModel = PickDateRequestModel(
                            initDate = period.startDate,
                            minDate = minOf(period.startDate, todayDate),
                            maxDate = period.endDate ?: todayDate.plus(1, DateTimeUnit.YEAR)
                        ),
                        defaultDispatcher = defaultDispatcher,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onConfigDateClick() {
        (taskModelState.value?.date as? TaskDate.Day)?.date?.let { date ->
            setUpBaseConfigState(
                BaseCreateEditTaskScreenConfig.PickDate.Date(
                    PickDateStateHolder(
                        requestModel = PickDateRequestModel(
                            initDate = date,
                            minDate = minOf(date, todayDate),
                            maxDate = todayDate.plus(1, DateTimeUnit.YEAR)
                        ),
                        defaultDispatcher = defaultDispatcher,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onConfigTaskFrequencyClick() {
        (taskModelState.value as? TaskModel.RecurringActivity)?.let { recurringActivity ->
            setUpBaseConfigState(
                BaseCreateEditTaskScreenConfig.PickTaskFrequency(
                    stateHolder = PickTaskFrequencyStateHolder(
                        initTaskFrequency = recurringActivity.frequency,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onConfigTaskProgressClick(itemConfig: BaseItemTaskConfig.Progress) {
        when (itemConfig) {
            is BaseItemTaskConfig.Progress.Number ->
                onConfigTaskNumberProgressClick()

            is BaseItemTaskConfig.Progress.Time -> {
                onConfigTaskTimeProgressClick()
            }
        }
    }

    private fun onConfigTaskTimeProgressClick() {
        (taskModelState.value as? TaskModel.Habit.HabitContinuous.HabitTime)?.let { habitTime ->
            setUpBaseConfigState(
                BaseCreateEditTaskScreenConfig.PickTaskTimeProgress(
                    stateHolder = PickTaskTimeProgressStateHolder(
                        initProgress = habitTime.progress,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onConfigTaskNumberProgressClick() {
        (taskModelState.value as? TaskModel.Habit.HabitContinuous.HabitNumber)?.let { habitNumber ->
            setUpBaseConfigState(
                BaseCreateEditTaskScreenConfig.PickTaskNumberProgress(
                    stateHolder = PickTaskNumberProgressStateHolder(
                        initTaskProgress = habitNumber.progress,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onConfigTaskTitleClick() {
        taskModelState.value?.title?.let { title ->
            setUpBaseConfigState(
                BaseCreateEditTaskScreenConfig.PickTaskTitle(
                    stateHolder = PickTaskTitleStateHolder(
                        initTitle = title,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    protected fun provideBaseTaskConfigItems(
        taskModel: TaskModel,
        reminderCount: Int
    ): List<BaseItemTaskConfig> {
        return buildList<BaseItemTaskConfig> {
            add(BaseItemTaskConfig.Title(taskModel.title))
            add(BaseItemTaskConfig.Description(taskModel.description))
            when (taskModel) {
                is TaskModel.Habit.HabitContinuous.HabitNumber -> {
                    add(BaseItemTaskConfig.Progress.Number(taskModel.progress))
                }

                is TaskModel.Habit.HabitContinuous.HabitTime -> {
                    add(BaseItemTaskConfig.Progress.Time(taskModel.progress))
                }

                else -> Unit
            }
            if (taskModel is TaskModel.RecurringActivity) {
                add(BaseItemTaskConfig.Frequency(taskModel.frequency))
            }
            when (val taskDate = taskModel.date) {
                is TaskDate.Day -> {
                    add(BaseItemTaskConfig.DateConfig.Date(taskDate.date))
                }

                is TaskDate.Period -> {
                    add(BaseItemTaskConfig.DateConfig.StartDate(taskDate.startDate))
                    add(BaseItemTaskConfig.DateConfig.EndDate(taskDate.endDate))
                }
            }
            add(BaseItemTaskConfig.Priority(taskModel.priority))
            add(BaseItemTaskConfig.Reminders(reminderCount))
        }.sortedBy { it.key.ordinal }
    }

}