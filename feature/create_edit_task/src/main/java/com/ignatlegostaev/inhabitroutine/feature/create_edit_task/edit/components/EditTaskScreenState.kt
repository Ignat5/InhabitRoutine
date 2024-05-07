package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.components

import androidx.compose.runtime.Immutable
import com.ignatlegostaev.inhabitroutine.core.presentation.components.state.ScreenState
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.model.BaseItemTaskConfig
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.model.EditTaskMessage
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.model.ItemTaskAction

@Immutable
data class EditTaskScreenState(
    val taskModel: TaskModel?,
    val allTaskConfigItems: List<BaseItemTaskConfig>,
    val allTaskActionItems: List<ItemTaskAction>,
    val message: EditTaskMessage
) : ScreenState
