package com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskFrequency
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SetUpTaskRemindersUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskFrequencyByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.util.getTaskVersionStartDate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class DefaultUpdateTaskFrequencyByIdUseCase(
    private val taskRepository: TaskRepository,
    private val setUpTaskRemindersUseCase: SetUpTaskRemindersUseCase,
    private val externalScope: CoroutineScope,
    private val defaultDispatcher: CoroutineDispatcher
) : UpdateTaskFrequencyByIdUseCase {

    override suspend operator fun invoke(
        taskId: String,
        taskFrequency: TaskFrequency
    ): ResultModel<Unit, Throwable> = withContext(defaultDispatcher) {
        taskRepository.readTaskById(taskId).firstOrNull()?.let { taskModel ->
            taskModel.copy(
                frequency = taskFrequency,
                versionStartDate = taskModel.getTaskVersionStartDate()
            ).let { newTaskModel ->
                val resultModel = taskRepository.saveTask(newTaskModel)
                if (resultModel is ResultModel.Success) {
                    externalScope.launch {
                        setUpTaskRemindersUseCase(taskId = taskId)
                    }
                }
                resultModel
            }
        } ?: ResultModel.failure(NoSuchElementException())
    }

}