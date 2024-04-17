package com.example.inhabitroutine.feature.create_edit_task.base

import com.example.inhabitroutine.core.presentation.base.BaseViewModel
import com.example.inhabitroutine.core.presentation.components.config.ScreenConfig
import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.core.presentation.components.navigation.ScreenNavigation
import com.example.inhabitroutine.core.presentation.components.state.ScreenState
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.model.task.content.TaskDate
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskTitleByIdUseCase
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenConfig
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_title.PickTaskTitleStateHolder
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_title.components.PickTaskTitleScreenResult
import com.example.inhabitroutine.feature.create_edit_task.base.model.BaseItemTaskConfig
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

abstract class BaseCreateEditTaskViewModel<SE : ScreenEvent, SS : ScreenState, SN : ScreenNavigation, SC : ScreenConfig>(
    private val updateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase
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
        when (event.itemConfig) {
            is BaseItemTaskConfig.Title ->
                onConfigTaskTitleClick()

            else -> Unit
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

    protected fun provideBaseTaskConfigItems(taskModel: TaskModel): List<BaseItemTaskConfig> {
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
            add(BaseItemTaskConfig.Reminders(0))
        }.sortedBy { it.key.ordinal }
    }

}