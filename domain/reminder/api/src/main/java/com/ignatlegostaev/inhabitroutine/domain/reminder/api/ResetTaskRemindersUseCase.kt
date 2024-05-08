package com.ignatlegostaev.inhabitroutine.domain.reminder.api

interface ResetTaskRemindersUseCase {
    suspend operator fun invoke(taskId: String)
}