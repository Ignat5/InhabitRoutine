package com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case

import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTasksByQueryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultReadTasksByQueryUseCase(
    private val taskRepository: TaskRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : ReadTasksByQueryUseCase {

    override operator fun invoke(query: String): Flow<List<TaskModel>> =
        taskRepository.readTasksByQuery(query).map { allTasks ->
            withContext(defaultDispatcher) {
                allTasks.filter { taskModel ->
                    !taskModel.isDraft
                }
            }
        }

}