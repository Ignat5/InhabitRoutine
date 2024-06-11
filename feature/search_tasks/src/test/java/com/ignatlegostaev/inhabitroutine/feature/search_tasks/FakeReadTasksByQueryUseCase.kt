package com.ignatlegostaev.inhabitroutine.feature.search_tasks

import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTasksByQueryUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow

class FakeReadTasksByQueryUseCase() : ReadTasksByQueryUseCase {

    private val allTasks = mutableListOf<TaskModel>()
    private var filterQueue: (query: String, task: TaskModel) -> Boolean = { query, task ->
        task.title.contains(other = query, ignoreCase = true)
    }

    override fun invoke(query: String, excludeDrafts: Boolean): Flow<List<TaskModel>> {
        return flow {
            emit(
                allTasks
                    .filter {
                        taskModel -> filterQueue(query, taskModel)
                    }
            )
        }
    }

    fun setTasks(allTasks: List<TaskModel>) {
        this.allTasks.clear()
        this.allTasks.addAll(allTasks)
    }

    fun setFilterQueue(filterQueue: (query: String, task: TaskModel) -> Boolean) {
        this.filterQueue = filterQueue
    }

}