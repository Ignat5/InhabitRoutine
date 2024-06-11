package com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case

import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskType
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadHabitsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultReadHabitsUseCase(
    private val taskRepository: TaskRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : ReadHabitsUseCase {

    private val targetTaskTypes: Set<TaskType>
        get() = setOf(TaskType.Habit)

    override operator fun invoke(excludeDrafts: Boolean): Flow<List<TaskModel.Habit>> =
        taskRepository.readTasksByTaskType(targetTaskTypes).map { allTasks ->
            if (allTasks.isNotEmpty()) {
                withContext(defaultDispatcher) {
                    allTasks
                        .excludeDraftsIfRequired(excludeDrafts)
                        .filterIsInstance<TaskModel.Habit>()
                }
            } else emptyList()
        }

    private fun List<TaskModel>.excludeDraftsIfRequired(exclude: Boolean) = this.let { allHabits ->
        if (exclude) allHabits.filterNot { it.isDraft } else allHabits
    }

}