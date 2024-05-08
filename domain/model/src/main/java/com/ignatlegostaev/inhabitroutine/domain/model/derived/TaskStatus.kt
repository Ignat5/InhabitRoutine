package com.ignatlegostaev.inhabitroutine.domain.model.derived

sealed interface TaskStatus {
    sealed interface Habit : TaskStatus
    sealed interface Task : TaskStatus

    data object Completed : Habit, Task
    sealed interface NotCompleted : TaskStatus {
        data object Pending : NotCompleted, Habit, Task
        data object Skipped : NotCompleted, Habit
        data object Failed : NotCompleted, Habit
    }
}