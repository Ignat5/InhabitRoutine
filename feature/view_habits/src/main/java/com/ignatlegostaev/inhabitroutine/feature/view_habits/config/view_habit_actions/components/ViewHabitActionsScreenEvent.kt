package com.ignatlegostaev.inhabitroutine.feature.view_habits.config.view_habit_actions.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_habits.config.view_habit_actions.model.ItemHabitAction

sealed interface ViewHabitActionsScreenEvent : ScreenEvent {
    data class OnItemActionClick(val item: ItemHabitAction) : ViewHabitActionsScreenEvent
    data object OnEditClick : ViewHabitActionsScreenEvent
    data object OnDismissRequest : ViewHabitActionsScreenEvent
}