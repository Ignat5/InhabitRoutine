package com.ignatlegostaev.inhabitroutine.feature.view_habits.config.view_habit_actions.model

sealed interface ItemHabitAction {
    data object ViewStatistics : ItemHabitAction
    data object Archive : ItemHabitAction
    data object Unarchive : ItemHabitAction
    data object Delete : ItemHabitAction
}