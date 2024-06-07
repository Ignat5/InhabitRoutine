package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.fakes

import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeReadTaskByIdUseCase : ReadTaskByIdUseCase {
    val taskState = MutableStateFlow<TaskModel?>(null)

    override fun invoke(taskId: String): Flow<TaskModel?> {
        return taskState
    }
}