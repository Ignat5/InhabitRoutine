package com.ignatlegostaev.inhabitroutine.domain.task.api.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskProgressType

interface SaveTaskDraftUseCase {
    sealed interface RequestType {
        data class CreateHabit(val progressType: TaskProgressType) : RequestType
        data object CreateRecurringTask : RequestType
        data object CreateSingleTask : RequestType
    }

    suspend operator fun invoke(requestType: SaveTaskDraftUseCase.RequestType): ResultModel<String, Throwable>

}