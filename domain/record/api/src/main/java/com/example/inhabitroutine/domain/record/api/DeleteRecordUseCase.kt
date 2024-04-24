package com.example.inhabitroutine.domain.record.api

import com.example.inhabitroutine.core.util.ResultModel
import kotlinx.datetime.LocalDate

interface DeleteRecordUseCase {
    suspend operator fun invoke(taskId: String, date: LocalDate): ResultModel<Unit, Throwable>
}