package com.example.inhabitroutine.domain.task.impl.use_case

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.core.util.todayDate
import com.example.inhabitroutine.domain.model.task.content.TaskDate
import com.example.inhabitroutine.domain.task.api.use_case.ResetTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskDateByIdUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

internal class DefaultResetTaskByIdUseCase(
    private val updateTaskDateByIdUseCase: UpdateTaskDateByIdUseCase,
    private val externalScope: CoroutineScope
) : ResetTaskByIdUseCase {

    override suspend operator fun invoke(taskId: String): ResultModel<Unit, Throwable> {
        val resultModel = updateTaskDateByIdUseCase(
            taskId = taskId,
            taskDate = TaskDate.Period(
                startDate = Clock.System.todayDate,
                endDate = null
            )
        )
        if (resultModel is ResultModel.Success) {
            externalScope.launch {
//                TODO(delete records)
            }
        }
        return resultModel
    }

}