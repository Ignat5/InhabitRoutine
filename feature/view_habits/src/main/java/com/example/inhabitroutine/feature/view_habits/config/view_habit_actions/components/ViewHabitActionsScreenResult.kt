package com.example.inhabitroutine.feature.view_habits.config.view_habit_actions.components

import com.example.inhabitroutine.core.presentation.components.result.ScreenResult

sealed interface ViewHabitActionsScreenResult : ScreenResult {
    sealed interface Action : ViewHabitActionsScreenResult {
        val taskId: String

        data class ViewStatistics(
            override val taskId: String
        ) : Action

        data class Archive(
            override val taskId: String
        ) : Action

        data class Unarchive(
            override val taskId: String
        ) : Action

        data class Delete(
            override val taskId: String
        ) : Action

        data class Edit(
            override val taskId: String
        ) : Action
    }

    data object Dismiss : ViewHabitActionsScreenResult
}