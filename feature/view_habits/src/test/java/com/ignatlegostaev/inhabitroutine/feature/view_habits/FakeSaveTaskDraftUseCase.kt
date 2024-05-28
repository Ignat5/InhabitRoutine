package com.ignatlegostaev.inhabitroutine.feature.view_habits

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskProgressType
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase

class FakeSaveTaskDraftUseCase : SaveTaskDraftUseCase {

    private var expectedResult: ResultModel<String, Throwable> =
        ResultModel.success("")

    override suspend fun invoke(requestType: SaveTaskDraftUseCase.RequestType): ResultModel<String, Throwable> {
        return when (requestType) {
            is SaveTaskDraftUseCase.RequestType.CreateHabit -> {
                expectedResult
            }

            else -> ResultModel.failure(IllegalArgumentException())
        }
    }

    fun setExpectedResult(expectedResult: ResultModel<String, Throwable>) {
        this.expectedResult = expectedResult
    }

}