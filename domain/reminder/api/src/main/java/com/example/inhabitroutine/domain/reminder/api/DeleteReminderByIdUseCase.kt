package com.example.inhabitroutine.domain.reminder.api

import com.example.inhabitroutine.core.util.ResultModel

interface DeleteReminderByIdUseCase {
    suspend operator fun invoke(reminderId: String): ResultModel<Unit, Throwable>
}