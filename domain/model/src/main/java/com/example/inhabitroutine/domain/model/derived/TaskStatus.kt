package com.example.inhabitroutine.domain.model.derived

sealed interface TaskStatus {
    sealed interface Habit : TaskStatus
    sealed interface Task : TaskStatus

    data object Completed : Habit, Task
    sealed interface NotCompleted : TaskStatus {
        data object Pending : Habit, Task
        data object Skipped : Habit
        data object Failed : Habit
    }
}