package com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ResetTaskRemindersUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SetUpTaskRemindersUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ArchiveTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.util.getTaskVersionStartDate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class DefaultArchiveTaskByIdUseCase(
    private val taskRepository: TaskRepository,
    private val setUpTaskRemindersUseCase: SetUpTaskRemindersUseCase,
    private val resetTaskRemindersUseCase: ResetTaskRemindersUseCase,
    private val externalScope: CoroutineScope,
    private val defaultDispatcher: CoroutineDispatcher
) : ArchiveTaskByIdUseCase {

    override suspend operator fun invoke(
        taskId: String,
        requestType: ArchiveTaskByIdUseCase.RequestType
    ): ResultModel<Unit, Throwable> = withContext(defaultDispatcher) {
        taskRepository.readTaskById(taskId).firstOrNull()?.let { taskModel ->
            val isArchived = requestType is ArchiveTaskByIdUseCase.RequestType.Archive
            taskModel.copy(
                isArchived = isArchived,
                versionStartDate = taskModel.getTaskVersionStartDate()
            ).let { newTaskModel ->
                val resultModel = taskRepository.saveTask(newTaskModel)
                if (resultModel is ResultModel.Success) {
                    externalScope.launch {
                        when (requestType) {
                            is ArchiveTaskByIdUseCase.RequestType.Archive -> {
                                resetTaskRemindersUseCase(taskId = taskId)
                            }

                            is ArchiveTaskByIdUseCase.RequestType.Unarchive -> {
                                setUpTaskRemindersUseCase(taskId = taskId)
                            }
                        }
                    }
                }
                resultModel
            }
        } ?: ResultModel.failure(NoSuchElementException())
    }

}