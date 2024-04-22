package com.example.inhabitroutine.domain.model.derived

sealed interface TaskStatus {
    sealed interface HabitStatus : TaskStatus
    sealed interface SingleRecurringTaskStatus : TaskStatus

    data object Completed : HabitStatus, SingleRecurringTaskStatus
    sealed interface NotCompleted : TaskStatus {
        data object Pending : HabitStatus, SingleRecurringTaskStatus
        data object Skipped : HabitStatus
        data object Failed : HabitStatus
    }
}