package com.ignatlegostaev.inhabitroutine.feature.view_habits.config.view_habit_actions

import com.ignatlegostaev.inhabitroutine.core.presentation.base.BaseResultStateHolder
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.feature.view_habits.config.view_habit_actions.components.ViewHabitActionsScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_habits.config.view_habit_actions.components.ViewHabitActionsScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_habits.config.view_habit_actions.components.ViewHabitActionsScreenState
import com.ignatlegostaev.inhabitroutine.feature.view_habits.config.view_habit_actions.model.ItemHabitAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ViewHabitActionsStateHolder(
    private val taskModel: TaskModel.Habit,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<ViewHabitActionsScreenEvent, ViewHabitActionsScreenState, ViewHabitActionsScreenResult>() {

    private val allHabitActionItems = buildList<ItemHabitAction> {
        add(ItemHabitAction.ViewStatistics)
        if (taskModel.isArchived) {
            add(ItemHabitAction.Unarchive)
        } else add(ItemHabitAction.Archive)
        add(ItemHabitAction.Delete)
    }

    override val uiScreenState: StateFlow<ViewHabitActionsScreenState> =
        MutableStateFlow(
            ViewHabitActionsScreenState(
                taskModel = taskModel,
                allHabitActionItems = allHabitActionItems
            )
        )

    override fun onEvent(event: ViewHabitActionsScreenEvent) {
        when (event) {
            is ViewHabitActionsScreenEvent.OnItemActionClick ->
                onItemActionClick(event)

            is ViewHabitActionsScreenEvent.OnEditClick ->
                onEditClick()

            is ViewHabitActionsScreenEvent.OnDismissRequest ->
                onDismissRequest()
        }
    }

    private fun onEditClick() {
        setUpResult(ViewHabitActionsScreenResult.Action.Edit(taskModel.id))
    }

    private fun onItemActionClick(event: ViewHabitActionsScreenEvent.OnItemActionClick) {
        when (event.item) {
            is ItemHabitAction.ViewStatistics ->
                onViewStatisticsClick()

            is ItemHabitAction.Archive ->
                onArchiveClick()

            is ItemHabitAction.Unarchive ->
                onUnarchiveClick()

            is ItemHabitAction.Delete ->
                onDeleteClick()
        }
    }

    private fun onViewStatisticsClick() {
        setUpResult(ViewHabitActionsScreenResult.Action.ViewStatistics(taskModel.id))
    }

    private fun onArchiveClick() {
        setUpResult(ViewHabitActionsScreenResult.Action.Archive(taskModel.id))
    }

    private fun onUnarchiveClick() {
        setUpResult(ViewHabitActionsScreenResult.Action.Unarchive(taskModel.id))
    }

    private fun onDeleteClick() {
        setUpResult(ViewHabitActionsScreenResult.Action.Delete(taskModel.id))
    }

    private fun onDismissRequest() {
        setUpResult(ViewHabitActionsScreenResult.Dismiss)
    }

}