package com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.record.api.RecordRepository
import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskDate
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SetUpTaskRemindersUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskDateByIdUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

internal class DefaultUpdateTaskDateByIdUseCase(
    private val taskRepository: TaskRepository,
    private val recordRepository: RecordRepository,
    private val setUpTaskRemindersUseCase: SetUpTaskRemindersUseCase,
    private val externalScope: CoroutineScope
) : UpdateTaskDateByIdUseCase {

    override suspend operator fun invoke(
        taskId: String,
        taskDate: TaskDate
    ): ResultModel<Unit, Throwable> {
        return taskRepository.readTaskById(taskId).firstOrNull()?.let { taskModel ->
            taskModel.copy(date = taskDate).let { newTaskModel ->
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