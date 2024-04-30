package com.example.inhabitroutine.domain.task.impl.use_case

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.record.api.RecordRepository
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.model.task.content.TaskDate
import com.example.inhabitroutine.domain.reminder.api.SetUpTaskRemindersUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskDateByIdUseCase
import kotlinx.coroutines.CoroutineScope
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
        val resultModel = taskRepository.updateTaskDateById(
            taskId = taskId,
            taskDate = taskDate
        )
        if (resultModel is ResultModel.Success) {
            externalScope.launch {
                val minDate = when (taskDate) {
                    is TaskDate.Period -> taskDate.startDate
                    is TaskDate.Day -> taskDate.date
                }
                val maxDate = when (taskDate) {
                    is TaskDate.Period -> taskDate.endDate
                    is TaskDate.Day -> taskDate.date
                }
                recordRepository.deleteRecordByTaskIdAndPeriod(
                    taskId = taskId,
                    minDate = minDate,
                    maxDate = maxDate
                )
            }
            externalScope.launch {
                setUpTaskRemindersUseCase(taskId = taskId)
            }
        }
        return resultModel
    }

}