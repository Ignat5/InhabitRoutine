package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.config.ScreenConfig
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.archive_task.ArchiveTaskStateHolder
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.delete_task.DeleteTaskStateHolder
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.reset_task.ResetTaskStateHolder
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenConfig

sealed interface EditTaskScreenConfig : ScreenConfig {
    data class Base(
        val baseConfig: BaseCreateEditTaskScreenConfig
    ) : EditTaskScreenConfig

    data class ArchiveTask(
        val stateHolder: ArchiveTaskStateHolder
    ) : EditTaskScreenConfig

    data class DeleteTask(
        val stateHolder: DeleteTaskStateHolder
    ) : EditTaskScreenConfig

    data class ResetTask(
        val stateHolder: ResetTaskStateHolder
    ) : EditTaskScreenConfig
}