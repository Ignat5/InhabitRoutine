package com.example.inhabitroutine.domain.task.impl.use_case

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.core.util.todayDate
import com.example.inhabitroutine.data.record.api.RecordRepository
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.model.task.content.TaskDate
import com.example.inhabitroutine.domain.reminder.api.SetUpTaskRemindersUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskDateByIdUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

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