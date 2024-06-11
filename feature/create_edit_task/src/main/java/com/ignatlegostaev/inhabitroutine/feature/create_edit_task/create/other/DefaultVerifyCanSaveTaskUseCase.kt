package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.other

import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel

internal class DefaultVerifyCanSaveTaskUseCase : VerifyCanSaveTaskUseCase {

    override operator fun invoke(taskModel: TaskModel): Boolean {
        return taskModel.title.isNotBlank()
    }

}