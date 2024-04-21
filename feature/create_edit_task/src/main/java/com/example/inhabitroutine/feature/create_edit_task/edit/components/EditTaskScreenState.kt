package com.example.inhabitroutine.feature.create_edit_task.edit.components

import com.example.inhabitroutine.core.presentation.components.state.ScreenState
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.feature.create_edit_task.base.model.BaseItemTaskConfig

data class EditTaskScreenState(
    val taskModel: TaskModel?,
    val allTaskConfigItems: List<BaseItemTaskConfig>
) : ScreenState
