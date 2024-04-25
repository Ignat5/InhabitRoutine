package com.example.inhabitroutine.domain.record.impl.use_case

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.record.api.RecordRepository
import com.example.inhabitroutine.domain.record.api.DeleteRecordUseCase
import kotlinx.datetime.LocalDate

internal class DefaultDeleteRecordUseCase(
    private val recordRepository: RecordRepository
) : DeleteRecordUseCase {

    override suspend operator fun invoke(
        taskId: String,
        date: LocalDate
    ): ResultModel<Unit, Throwable> =
        recordRepository.deleteRecordByTaskIdAndDate(
            taskId = taskId,
            targetDate = date
        )

}