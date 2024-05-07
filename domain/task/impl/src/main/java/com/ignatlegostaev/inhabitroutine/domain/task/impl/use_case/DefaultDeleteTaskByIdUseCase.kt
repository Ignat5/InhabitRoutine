package com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ResetTaskRemindersUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.DeleteTaskByIdUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class DefaultDeleteTaskByIdUseCase(
    private val taskRepository: TaskRepository,
    private val resetTaskRemindersUseCase: ResetTaskRemindersUseCase,
    private val externalScope: CoroutineScope
) : DeleteTaskByIdUseCase {

    override suspend operator fun invoke(taskId: String): ResultModel<Unit, Throwable> {
        val resultModel = taskRepository.deleteTaskById(taskId)
        if (resultModel is ResultModel.Success) {
            externalScope.launch {
                resetTaskRemindersUseCase(taskId)
            }
        }
        return resultModel
    }

}