package com.example.inhabitroutine.domain.reminder.api

interface SetUpNextReminderUseCase {
    suspend operator fun invoke(reminderId: String)
}