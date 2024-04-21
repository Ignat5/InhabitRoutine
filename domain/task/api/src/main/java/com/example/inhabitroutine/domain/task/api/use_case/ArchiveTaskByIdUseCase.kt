package com.example.inhabitroutine.domain.task.api.use_case

import com.example.inhabitroutine.core.util.ResultModel

interface ArchiveTaskByIdUseCase {
    sealed interface RequestType {
        data object Archive : RequestType
        data object Unarchive : RequestType
    }

    suspend operator fun invoke(
        taskId: String,
        requestType: RequestType
    ): ResultModel<Unit, Throwable>
}