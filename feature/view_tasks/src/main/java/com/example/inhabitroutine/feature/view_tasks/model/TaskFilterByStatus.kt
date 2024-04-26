package com.example.inhabitroutine.feature.view_tasks.model

sealed interface TaskFilterByStatus {
    data object OnlyActive : TaskFilterByStatus
    data object OnlyArchived : TaskFilterByStatus

//    sealed interface Habits : TaskFilterByStatus
//    sealed interface Tasks : TaskFilterByStatus
//
//    data object OnlyActive : Habits, Tasks
//    data object OnlyArchived : Habits, Tasks
//    data object All : Habits, Tasks
}