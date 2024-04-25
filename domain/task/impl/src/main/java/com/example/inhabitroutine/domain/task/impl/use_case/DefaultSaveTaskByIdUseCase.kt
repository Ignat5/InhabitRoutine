package com.example.inhabitroutine.domain.task.impl.use_case

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.task.api.use_case.SaveTaskByIdUseCase
import kotlinx.coroutines.CoroutineScope

internal class DefaultSaveTaskByIdUseCase(
    private val taskRepository: TaskRepository,
    private val externalScope: CoroutineScope
) : SaveTaskByIdUseCase {

    override suspend operator fun invoke(taskId: String): ResultModel<Unit, Throwable> {
        val resultModel = taskRepository.updateTaskIsDraftById(
            taskId = taskId,
            isDraft = false
        )
        if (resultModel is ResultModel.Success) {
//            TODO("set up reminder")
        }
        return resultModel
    }

}