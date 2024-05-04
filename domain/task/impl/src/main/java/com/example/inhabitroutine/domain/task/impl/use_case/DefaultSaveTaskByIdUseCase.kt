package com.example.inhabitroutine.domain.task.impl.use_case

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.reminder.api.SetUpTaskRemindersUseCase
import com.example.inhabitroutine.domain.task.api.use_case.SaveTaskByIdUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

internal class DefaultSaveTaskByIdUseCase(
    private val taskRepository: TaskRepository,
    private val setUpTaskRemindersUseCase: SetUpTaskRemindersUseCase,
    private val externalScope: CoroutineScope
) : SaveTaskByIdUseCase {

    override suspend operator fun invoke(taskId: String): ResultModel<Unit, Throwable> {
        return taskRepository.readTaskById(taskId).firstOrNull()?.let { taskModel ->
            taskModel.copy(isDraft = false).let { newTaskModel ->
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