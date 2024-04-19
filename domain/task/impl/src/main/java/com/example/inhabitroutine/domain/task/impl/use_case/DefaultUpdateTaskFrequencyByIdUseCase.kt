package com.example.inhabitroutine.domain.task.impl.use_case

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.model.task.content.TaskFrequency
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskFrequencyByIdUseCase
import com.example.inhabitroutine.domain.task.impl.util.todayDate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

internal class DefaultUpdateTaskFrequencyByIdUseCase(
    private val taskRepository: TaskRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : UpdateTaskFrequencyByIdUseCase {

    override suspend operator fun invoke(
        taskId: String,
        taskFrequency: TaskFrequency
    ): ResultModel<Unit, Throwable> = withContext(defaultDispatcher) {
        taskRepository.readTaskById(taskId)
            .filterIsInstance<TaskModel.RecurringActivity>()
            .firstOrNull()?.let { recurringActivity ->
                val newTaskModel = when (recurringActivity) {
                    is TaskModel.Habit -> {
                        when (recurringActivity) {
                            is TaskModel.Habit.HabitYesNo -> {
                                recurringActivity.copy(frequency = taskFrequency)
                            }

                            is TaskModel.Habit.HabitContinuous -> {
                                when (recurringActivity) {
                                    is TaskModel.Habit.HabitContinuous.HabitNumber -> {
                                        recurringActivity.copy(frequency = taskFrequency)
                                    }

                                    is TaskModel.Habit.HabitContinuous.HabitTime -> {
                                        recurringActivity.copy(frequency = taskFrequency)
                                    }
                                }
                            }
                        }
                    }

                    is TaskModel.Task.RecurringTask -> {
                        recurringActivity.copy(frequency = taskFrequency)
                    }
                }
                taskRepository.saveTask(
                    taskModel = newTaskModel,
                    versionStartDate = todayDate
                )
            } ?: ResultModel.failure(NoSuchElementException())
    }

}