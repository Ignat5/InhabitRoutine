package com.ignatlegostaev.inhabitroutine.domain.record.api

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import kotlinx.datetime.LocalDate

interface DeleteRecordUseCase {
    suspend operator fun invoke(taskId: String, date: LocalDate): ResultModel<Unit, Throwable>
}