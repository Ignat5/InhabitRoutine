package com.ignatlegostaev.inhabitroutine.feature.view_habits

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.DeleteTaskByIdUseCase
import kotlinx.coroutines.flow.update

class FakeDeleteTaskByIdUseCase(
    private val dataSource: FakeHabitsDataSource
) : DeleteTaskByIdUseCase {

    override suspend fun invoke(taskId: String): ResultModel<Unit, Throwable> {
        dataSource.allHabitsState.update { oldList ->
            val newList = mutableListOf<TaskModel.Habit>()
            newList.addAll(oldList)
            newList.removeIf { it.id == taskId }
            newList
        }
        return ResultModel.success(Unit)
    }

}