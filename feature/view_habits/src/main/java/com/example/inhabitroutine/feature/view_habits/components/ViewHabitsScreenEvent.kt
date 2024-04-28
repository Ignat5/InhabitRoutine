package com.example.inhabitroutine.feature.view_habits.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.core.presentation.components.result.ScreenResult
import com.example.inhabitroutine.core.presentation.ui.dialog.archive_task.components.ArchiveTaskScreenResult
import com.example.inhabitroutine.core.presentation.ui.dialog.delete_task.components.DeleteTaskScreenResult
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_task_progress_type.PickTaskProgressTypeScreenResult
import com.example.inhabitroutine.feature.view_habits.config.view_habit_actions.components.ViewHabitActionsScreenResult
import com.example.inhabitroutine.feature.view_habits.model.HabitFilterByStatus
import com.example.inhabitroutine.feature.view_habits.model.HabitSort

sealed interface ViewHabitsScreenEvent : ScreenEvent {
    data class OnHabitClick(
        val habitId: String
    ) : ViewHabitsScreenEvent

    data class OnPickFilterByStatus(
        val filterByStatus: HabitFilterByStatus
    ) : ViewHabitsScreenEvent

    data class OnPickSort(
        val sort: HabitSort
    ) : ViewHabitsScreenEvent

    data object OnMessageShown : ViewHabitsScreenEvent
    data object OnCreateHabitClick : ViewHabitsScreenEvent
    data object OnSearchClick : ViewHabitsScreenEvent

    sealed interface ResultEvent : ViewHabitsScreenEvent {
        val result: ScreenResult

        data class ViewHabitActions(
            override val result: ViewHabitActionsScreenResult
        ) : ResultEvent

        data class PickTaskProgressType(
            override val result: PickTaskProgressTypeScreenResult
        ) : ResultEvent

        data class ArchiveTask(
            override val result: ArchiveTaskScreenResult
        ) : ResultEvent

        data class DeleteTask(
            override val result: DeleteTaskScreenResult
        ) : ResultEvent
    }
}