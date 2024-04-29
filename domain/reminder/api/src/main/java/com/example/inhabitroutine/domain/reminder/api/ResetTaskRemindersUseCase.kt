package com.example.inhabitroutine.domain.reminder.api

interface ResetTaskRemindersUseCase {
    suspend operator fun invoke(taskId: String)
}