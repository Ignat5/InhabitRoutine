package com.example.inhabitroutine.domain.task.api.use_case

interface ValidateProgressLimitNumberUseCase {
    operator fun invoke(limitNumber: Double): Boolean
}