package com.example.inhabitroutine.feature.view_schedule.config.view_task_actions

import com.example.inhabitroutine.core.presentation.base.BaseResultStateHolder
import com.example.inhabitroutine.domain.model.derived.TaskStatus
import com.example.inhabitroutine.domain.model.derived.TaskWithExtrasAndRecordModel
import com.example.inhabitroutine.feature.view_schedule.config.view_task_actions.components.ViewTaskActionsScreenEvent
import com.example.inhabitroutine.feature.view_schedule.config.view_task_actions.components.ViewTaskActionsScreenResult
import com.example.inhabitroutine.feature.view_schedule.config.view_task_actions.components.ViewTaskActionsScreenState
import com.example.inhabitroutine.feature.view_schedule.config.view_task_actions.model.ItemTaskAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDate

class ViewTaskActionsStateHolder(
    private val taskWithExtrasAndRecordModel: TaskWithExtrasAndRecordModel,
    private val date: LocalDate,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<ViewTaskActionsScreenEvent, ViewTaskActionsScreenState, ViewTaskActionsScreenResult>() {

    private val allTaskActions: List<ItemTaskAction> = when (taskWithExtrasAndRecordModel) {
        is TaskWithExtrasAndRecordModel.Habit -> provideHabitActions(taskWithExtrasAndRecordModel)
        is TaskWithExtrasAndRecordModel.Task -> provideTaskActions(taskWithExtrasAndRecordModel)
    }.sortedBy { it.type.ordinal }

    override val uiScreenState: StateFlow<ViewTaskActionsScreenState> =
        MutableStateFlow(
            ViewTaskActionsScreenState(
                taskWithExtrasAndRecordModel = taskWithExtrasAndRecordModel,
                allTaskActionItems = allTaskActions,
                date = date
            )
        )

    override fun onEvent(event: ViewTaskActionsScreenEvent) {
        when (event) {
            is ViewTaskActionsScreenEvent.OnItemActionClick -> {}
            is ViewTaskActionsScreenEvent.OnEditTaskClick -> {}
            is ViewTaskActionsScreenEvent.OnDismissRequest ->
                onDismissRequest()
        }
    }

    private fun onEditTaskClick() {
        setUpResult(
            ViewTaskActionsScreenResult.OnEditTaskClick(
                taskId = taskWithExtrasAndRecordModel.taskWithExtrasModel.taskModel.id
            )
        )
    }

    private fun onDismissRequest() {
        setUpResult(
            ViewTaskActionsScreenResult.Dismiss(
                taskId = taskWithExtrasAndRecordModel.taskWithExtrasModel.taskModel.id
            )
        )
    }

    private fun provideHabitActions(taskWithExtrasAndRecordModel: TaskWithExtrasAndRecordModel.Habit): Set<ItemTaskAction> =
        buildSet {
            when (taskWithExtrasAndRecordModel) {
                is TaskWithExtrasAndRecordModel.Habit.HabitContinuous -> {
                    when (taskWithExtrasAndRecordModel) {
                        is TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitNumber -> {
                            add(
                                ItemTaskAction.ContinuousProgress.Number(
                                    progress = taskWithExtrasAndRecordModel.taskWithExtrasModel.taskModel.progress,
                                    entry = taskWithExtrasAndRecordModel.recordEntry
                                )
                            )
                        }

                        is TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitTime -> {
                            add(
                                ItemTaskAction.ContinuousProgress.Time(
                                    progress = taskWithExtrasAndRecordModel.taskWithExtrasModel.taskModel.progress,
                                    entry = taskWithExtrasAndRecordModel.recordEntry
                                )
                            )
                        }
                    }
                }

                is TaskWithExtrasAndRecordModel.Habit.HabitYesNo -> {
                    if (taskWithExtrasAndRecordModel.status != TaskStatus.Completed) {
                        add(ItemTaskAction.Done)
                    }
                }
            }
            if (taskWithExtrasAndRecordModel.status != TaskStatus.NotCompleted.Skipped) {
                add(ItemTaskAction.Skip)
            }
            if (taskWithExtrasAndRecordModel.status != TaskStatus.NotCompleted.Failed) {
                add(ItemTaskAction.Fail)
            }
            if (taskWithExtrasAndRecordModel.status != TaskStatus.NotCompleted.Pending) {
                add(ItemTaskAction.Reset)
            }
        }

    private fun provideTaskActions(taskWithExtrasAndRecordModel: TaskWithExtrasAndRecordModel.Task): Set<ItemTaskAction> =
        buildSet {
            when (taskWithExtrasAndRecordModel.status) {
                is TaskStatus.Completed -> {
                    add(ItemTaskAction.Reset)
                }

                is TaskStatus.NotCompleted.Pending -> {
                    add(ItemTaskAction.Done)
                }
            }
        }
}