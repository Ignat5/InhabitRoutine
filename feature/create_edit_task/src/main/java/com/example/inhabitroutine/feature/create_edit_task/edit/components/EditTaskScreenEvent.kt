package com.example.inhabitroutine.feature.create_edit_task.edit.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.core.presentation.components.result.ScreenResult
import com.example.inhabitroutine.core.presentation.ui.dialog.archive_task.components.ArchiveTaskScreenResult
import com.example.inhabitroutine.core.presentation.ui.dialog.delete_task.components.DeleteTaskScreenResult
import com.example.inhabitroutine.core.presentation.ui.dialog.reset_task.components.ResetTaskScreenResult
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.edit.model.ItemTaskAction

sealed interface EditTaskScreenEvent : ScreenEvent {
    data class Base(
        val baseEvent: BaseCreateEditTaskScreenEvent
    ) : EditTaskScreenEvent

    data class OnItemActionClick(
        val item: ItemTaskAction
    ) : EditTaskScreenEvent

    data object OnMessageShown : EditTaskScreenEvent

    data object OnBackRequest : EditTaskScreenEvent

    sealed interface ResultEvent : EditTaskScreenEvent {
        val result: ScreenResult

        data class ArchiveTask(
            override val result: ArchiveTaskScreenResult
        ) : ResultEvent

        data class DeleteTask(
            override val result: DeleteTaskScreenResult
        ) : ResultEvent

        data class ResetTask(
            override val result: ResetTaskScreenResult
        ) : ResultEvent
    }
}