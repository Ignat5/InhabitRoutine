package com.example.inhabitroutine.domain.task.impl.use_case

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.core.util.todayDate
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.task.api.use_case.ArchiveTaskByIdUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

internal class DefaultArchiveTaskByIdUseCase(
    private val taskRepository: TaskRepository,
    private val externalScope: CoroutineScope,
    private val defaultDispatcher: CoroutineDispatcher
) : ArchiveTaskByIdUseCase {

    override suspend operator fun invoke(
        taskId: String,
        requestType: ArchiveTaskByIdUseCase.RequestType
    ): ResultModel<Unit, Throwable> = withContext(defaultDispatcher) {
        taskRepository.readTaskById(taskId).firstOrNull()?.let { taskModel ->
            val isArchived = requestType is ArchiveTaskByIdUseCase.RequestType.Archive
            val newTaskModel: TaskModel = when (taskModel) {
                is TaskModel.Habit -> {
                    when (taskModel) {
                        is TaskModel.Habit.HabitContinuous -> {
                            when (taskModel) {
                                is TaskModel.Habit.HabitContinuous.HabitNumber -> {
                                    taskModel.copy(
                                        isArchived = isArchived,
                                        versionStartDate = Clock.System.todayDate
                                    )
                                }

                                is TaskModel.Habit.HabitContinuous.HabitTime -> {
                                    taskModel.copy(
                                        isArchived = isArchived,
                                        versionStartDate = Clock.System.todayDate
                                    )
                                }
                            }
                        }

                        is TaskModel.Habit.HabitYesNo -> {
                            taskModel.copy(
                                isArchived = isArchived,
                                versionStartDate = Clock.System.todayDate
                            )
                        }
                    }
                }

                is TaskModel.Task -> {
                    when (taskModel) {
                        is TaskModel.Task.RecurringTask -> {
                            taskModel.copy(
                                isArchived = isArchived,
                                versionStartDate = Clock.System.todayDate
                            )
                        }

                        is TaskModel.Task.SingleTask -> {
                            taskModel.copy(
                                isArchived = isArchived,
                                versionStartDate = Clock.System.todayDate
                            )
                        }
                    }
                }
            }
            val resultModel = taskRepository.saveTask(newTaskModel)
            if (resultModel is ResultModel.Success) {
                externalScope.launch {
//                    TODO(set/reset reminders)
                }
            }
            resultModel
        } ?: ResultModel.failure(NoSuchElementException())
    }

}