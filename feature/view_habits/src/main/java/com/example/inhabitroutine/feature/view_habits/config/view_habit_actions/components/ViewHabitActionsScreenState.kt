package com.example.inhabitroutine.feature.view_habits.config.view_habit_actions.components

import com.example.inhabitroutine.core.presentation.components.state.ScreenState
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.feature.view_habits.config.view_habit_actions.model.ItemHabitAction

data class ViewHabitActionsScreenState(
    val taskModel: TaskModel.Habit,
    val allHabitActionItems: List<ItemHabitAction>
) : ScreenState
