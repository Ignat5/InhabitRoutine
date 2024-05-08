package com.ignatlegostaev.inhabitroutine.feature.view_habits.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.config.ScreenConfig
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.archive_task.ArchiveTaskStateHolder
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.delete_task.DeleteTaskStateHolder
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskProgressType
import com.ignatlegostaev.inhabitroutine.feature.view_habits.config.view_habit_actions.ViewHabitActionsStateHolder

sealed interface ViewHabitsScreenConfig : ScreenConfig {
    data class ViewHabitActions(
        val stateHolder: ViewHabitActionsStateHolder
    ) : ViewHabitsScreenConfig

    data class PickTaskProgressType(
        val allProgressTypes: List<TaskProgressType>
    ) : ViewHabitsScreenConfig

    data class ArchiveTask(
        val stateHolder: ArchiveTaskStateHolder
    ) : ViewHabitsScreenConfig

    data class DeleteTask(
        val stateHolder: DeleteTaskStateHolder
    ) : ViewHabitsScreenConfig
}