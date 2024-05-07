package com.ignatlegostaev.inhabitroutine.domain.reminder.api

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel

interface DeleteReminderByIdUseCase {
    suspend operator fun invoke(reminderId: String): ResultModel<Unit, Throwable>
}