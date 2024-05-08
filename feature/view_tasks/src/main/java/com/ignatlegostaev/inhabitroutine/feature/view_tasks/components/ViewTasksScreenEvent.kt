package com.ignatlegostaev.inhabitroutine.feature.view_tasks.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.ignatlegostaev.inhabitroutine.core.presentation.components.result.ScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.archive_task.components.ArchiveTaskScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.delete_task.components.DeleteTaskScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_task_type.PickTaskTypeScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.config.view_task_actions.components.ViewTaskActionsScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.model.TaskFilterByStatus
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.model.TaskFilterByType
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.model.TaskSort

sealed interface ViewTasksScreenEvent : ScreenEvent {
    data class OnTaskClick(
        val taskId: String
    ) : ViewTasksScreenEvent

    data class OnPickFilterByStatus(
        val filterByStatus: TaskFilterByStatus
    ) : ViewTasksScreenEvent

    data class OnPickFilterByType(
        val filterByType: TaskFilterByType
    ) : ViewTasksScreenEvent

    data class OnPickSort(val taskSort: TaskSort) : ViewTasksScreenEvent
    data object OnMessageShown : ViewTasksScreenEvent
    data object OnCreateTaskClick : ViewTasksScreenEvent
    data object OnSearchClick : ViewTasksScreenEvent

    sealed interface ResultEvent : ViewTasksScreenEvent {
        val result: ScreenResult

        data class ViewTaskActions(
            override val result: ViewTaskActionsScreenResult
        ) : ResultEvent

        data class ArchiveTask(
            override val result: ArchiveTaskScreenResult
        ) : ResultEvent

        data class DeleteTask(
            override val result: DeleteTaskScreenResult
        ) : ResultEvent

        data class PickTaskType(
            override val result: PickTaskTypeScreenResult
        ) : ResultEvent
    }
}