package com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.todayDate
import com.ignatlegostaev.inhabitroutine.data.record.api.RecordRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskDate
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ResetTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskDateByIdUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

internal class DefaultResetTaskByIdUseCase(
    private val updateTaskDateByIdUseCase: UpdateTaskDateByIdUseCase,
    private val recordRepository: RecordRepository,
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
                recordRepository.deleteRecordsByTaskId(taskId)
            }
        }
        return resultModel
    }

}