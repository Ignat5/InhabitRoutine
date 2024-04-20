package com.example.inhabitroutine.domain.task.impl.use_case

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.model.task.content.TaskDate
import com.example.inhabitroutine.domain.task.api.use_case.SaveTaskByIdUseCase
import com.example.inhabitroutine.domain.task.impl.util.todayDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull

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