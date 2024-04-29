package com.example.inhabitroutine.domain.reminder.api

interface SetUpTaskRemindersUseCase {
    suspend operator fun invoke(taskId: String)
}