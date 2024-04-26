package com.example.inhabitroutine.feature.view_tasks.components

import com.example.inhabitroutine.core.presentation.components.config.ScreenConfig
import com.example.inhabitroutine.core.presentation.ui.dialog.archive_task.ArchiveTaskStateHolder
import com.example.inhabitroutine.core.presentation.ui.dialog.delete_task.DeleteTaskStateHolder
import com.example.inhabitroutine.domain.model.task.type.TaskType
import com.example.inhabitroutine.feature.view_tasks.config.view_task_actions.ViewTaskActionsStateHolder

sealed interface ViewTasksScreenConfig : ScreenConfig {
    data class ViewTaskActions(
        val stateHolder: ViewTaskActionsStateHolder
    ): ViewTasksScreenConfig

    data class ArchiveTask(
        val stateHolder: ArchiveTaskStateHolder
    ) : ViewTasksScreenConfig

    data class DeleteTask(
        val stateHolder: DeleteTaskStateHolder
    ) : ViewTasksScreenConfig

    data class PickTaskType(
        val allTaskTypes: List<TaskType>
    ) : ViewTasksScreenConfig
}