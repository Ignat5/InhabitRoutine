package com.example.inhabitroutine.feature.view_tasks.config.view_task_actions

import com.example.inhabitroutine.core.presentation.base.BaseResultStateHolder
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.feature.view_tasks.config.view_task_actions.components.ViewTaskActionsScreenEvent
import com.example.inhabitroutine.feature.view_tasks.config.view_task_actions.components.ViewTaskActionsScreenResult
import com.example.inhabitroutine.feature.view_tasks.config.view_task_actions.components.ViewTaskActionsScreenState
import com.example.inhabitroutine.feature.view_tasks.config.view_task_actions.model.ItemTaskAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ViewTaskActionsStateHolder(
    private val taskModel: TaskModel.Task,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<ViewTaskActionsScreenEvent, ViewTaskActionsScreenState, ViewTaskActionsScreenResult>() {

    private val allTaskActionItems: List<ItemTaskAction> = buildList {
        if (taskModel.isArchived) {
            add(ItemTaskAction.Unarchive)
        } else add(ItemTaskAction.Archive)

        add(ItemTaskAction.Delete)
    }

    override val uiScreenState: StateFlow<ViewTaskActionsScreenState> =
        MutableStateFlow(
            ViewTaskActionsScreenState(
                taskModel = taskModel,
                allTaskActionItems = allTaskActionItems
            )
        )

    override fun onEvent(event: ViewTaskActionsScreenEvent) {
        when (event) {
            is ViewTaskActionsScreenEvent.OnItemActionClick ->
                onItemActionClick(event)

            is ViewTaskActionsScreenEvent.OnEditTaskClick ->
                onEditTaskClick()

            is ViewTaskActionsScreenEvent.OnDismissRequest ->
                onDismissRequest()
        }
    }

    private fun onItemActionClick(event: ViewTaskActionsScreenEvent.OnItemActionClick) {
        when (event.item) {
            is ItemTaskAction.Archive -> onArchiveClick()
            is ItemTaskAction.Unarchive -> onUnarchiveClick()
            is ItemTaskAction.Delete -> onDeleteClick()
        }
    }

    private fun onArchiveClick() {
        setUpResult(
            ViewTaskActionsScreenResult.Action.Archive(
                taskId = taskModel.id
            )
        )
    }

    private fun onUnarchiveClick() {
        setUpResult(
            ViewTaskActionsScreenResult.Action.Unarchive(
                taskId = taskModel.id
            )
        )
    }

    private fun onDeleteClick() {
        setUpResult(
            ViewTaskActionsScreenResult.Action.Delete(
                taskId = taskModel.id
            )
        )
    }

    private fun onEditTaskClick() {
        setUpResult(
            ViewTaskActionsScreenResult.Action.Edit(
                taskId = taskModel.id
            )
        )
    }

    private fun onDismissRequest() {
        setUpResult(ViewTaskActionsScreenResult.Dismiss)
    }

}