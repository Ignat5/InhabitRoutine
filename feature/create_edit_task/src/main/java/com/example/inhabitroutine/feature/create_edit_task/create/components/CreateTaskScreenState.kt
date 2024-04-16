package com.example.inhabitroutine.feature.create_edit_task.create.components

import androidx.compose.runtime.Immutable
import com.example.inhabitroutine.core.presentation.components.state.ScreenState
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.feature.create_edit_task.base.model.BaseItemTaskConfig

@Immutable
data class CreateTaskScreenState(
    val taskModel: TaskModel?,
    val allTaskConfigItems: List<BaseItemTaskConfig>,
    val canSave: Boolean
) : ScreenState
