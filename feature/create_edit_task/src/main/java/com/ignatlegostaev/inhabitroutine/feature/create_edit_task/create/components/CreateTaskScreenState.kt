package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.components

import androidx.compose.runtime.Immutable
import com.ignatlegostaev.inhabitroutine.core.presentation.components.state.ScreenState
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.model.BaseItemTaskConfig

@Immutable
data class CreateTaskScreenState(
    val taskModel: TaskModel?,
    val allTaskConfigItems: List<BaseItemTaskConfig>,
    val canSave: Boolean
) : ScreenState
