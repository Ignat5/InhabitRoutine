package com.ignatlegostaev.inhabitroutine.feature.view_habits.config.view_habit_actions.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.state.ScreenState
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.feature.view_habits.config.view_habit_actions.model.ItemHabitAction

data class ViewHabitActionsScreenState(
    val taskModel: TaskModel.Habit,
    val allHabitActionItems: List<ItemHabitAction>
) : ScreenState
