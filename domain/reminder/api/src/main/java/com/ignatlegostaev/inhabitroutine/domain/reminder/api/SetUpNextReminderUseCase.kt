package com.ignatlegostaev.inhabitroutine.domain.reminder.api

interface SetUpNextReminderUseCase {
    suspend operator fun invoke(reminderId: String)
}