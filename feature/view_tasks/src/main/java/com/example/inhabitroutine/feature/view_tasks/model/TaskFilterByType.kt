package com.example.inhabitroutine.feature.view_tasks.model

sealed interface TaskFilterByType {
    data object OnlyRecurring : TaskFilterByType
    data object OnlySingle : TaskFilterByType

//    data object All : TaskFilterByType
//    data object OnlyRecurring : TaskFilterByType
//    data object OnlySingle : TaskFilterByType
}