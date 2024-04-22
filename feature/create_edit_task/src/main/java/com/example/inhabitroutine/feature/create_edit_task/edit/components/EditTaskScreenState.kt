package com.example.inhabitroutine.feature.create_edit_task.edit.components

import androidx.compose.runtime.Immutable
import com.example.inhabitroutine.core.presentation.components.state.ScreenState
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.feature.create_edit_task.base.model.BaseItemTaskConfig
import com.example.inhabitroutine.feature.create_edit_task.edit.model.EditTaskMessage
import com.example.inhabitroutine.feature.create_edit_task.edit.model.ItemTaskAction

@Immutable
data class EditTaskScreenState(
    val taskModel: TaskModel?,
    val allTaskConfigItems: List<BaseItemTaskConfig>,
    val allTaskActionItems: List<ItemTaskAction>,
    val message: EditTaskMessage
) : ScreenState
