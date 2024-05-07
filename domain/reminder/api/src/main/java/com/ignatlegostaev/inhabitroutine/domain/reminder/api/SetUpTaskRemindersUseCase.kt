package com.ignatlegostaev.inhabitroutine.domain.reminder.api

interface SetUpTaskRemindersUseCase {
    suspend operator fun invoke(taskId: String)
}