package com.ignatlegostaev.inhabitroutine.feature.view_habits

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ArchiveTaskByIdUseCase
import kotlinx.coroutines.flow.update

class FakeArchiveTaskByIdUseCase(
    private val dataSource: FakeHabitsDataSource
) : ArchiveTaskByIdUseCase {

    override suspend fun invoke(
        taskId: String,
        requestType: ArchiveTaskByIdUseCase.RequestType
    ): ResultModel<Unit, Throwable> {
        dataSource.allHabitsState.update { oldList ->
            val newList = mutableListOf<TaskModel.Habit>()
            newList.addAll(oldList)
            val targetIndex = newList.indexOfFirst { it.id == taskId }
            if (targetIndex != -1) {
                newList[targetIndex] =
                    newList[targetIndex].copy(isArchived = requestType is ArchiveTaskByIdUseCase.RequestType.Archive) as TaskModel.Habit
            } else return ResultModel.failure(NoSuchElementException())
            newList
        }
        return ResultModel.success(Unit)
    }
}