package com.ignatlegostaev.inhabitroutine.data.task.test

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate

class FakeTaskRepository : TaskRepository {

    private val allTasksState = MutableStateFlow<List<TaskModel>>(emptyList())

    fun setTasks(tasks: List<TaskModel>) {
        allTasksState.update { tasks }
    }

    fun getTasks(): List<TaskModel> = allTasksState.value

    override fun readTaskById(taskId: String): Flow<TaskModel?> {
        return allTasksState
            .map { allTasks ->
                allTasks.find { it.id == taskId }
            }
    }

    override fun readTasksByDate(targetDate: LocalDate): Flow<List<TaskModel>> {
        return allTasksState
    }

    override fun readTasksByQuery(query: String): Flow<List<TaskModel>> {
        return allTasksState
    }

    override fun readTasksByTaskType(targetTaskTypes: Set<TaskType>): Flow<List<TaskModel>> {
        return allTasksState.map { allTasks ->
            allTasks.filter { it.type in targetTaskTypes }
        }
    }

    override fun readTasksById(taskId: String): Flow<List<TaskModel>> {
        return allTasksState.map { allTasks ->
            allTasks.filter { it.id == taskId }
        }
    }

    override suspend fun saveTask(taskModel: TaskModel): ResultModel<Unit, Throwable> {
        allTasksState.update { oldTasks ->
            val newTasks = mutableListOf<TaskModel>()
            newTasks.addAll(oldTasks)
            val index = newTasks.indexOfFirst { it.id == taskModel.id }
            if (index != -1) {
                newTasks[index] = taskModel
            } else {
                newTasks.add(taskModel)
            }
            newTasks
        }
        return ResultModel.success(Unit)
    }

    override suspend fun deleteTaskById(taskId: String): ResultModel<Unit, Throwable> {
        allTasksState.update { oldTasks ->
            val newTasks = mutableListOf<TaskModel>()
            newTasks.addAll(oldTasks)
            newTasks.removeIf { it.id == taskId }
            newTasks
        }
        return ResultModel.success(Unit)
    }

}