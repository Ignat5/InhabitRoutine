package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.other

import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel

interface VerifyCanSaveTaskUseCase {
    operator fun invoke(taskModel: TaskModel): Boolean
}