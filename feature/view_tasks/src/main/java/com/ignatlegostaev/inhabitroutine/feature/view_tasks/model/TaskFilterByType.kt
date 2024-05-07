package com.ignatlegostaev.inhabitroutine.feature.view_tasks.model

enum class TaskFilterByType {
    OnlyRecurring,
    OnlySingle
}

//sealed interface TaskFilterByType {
//    data object OnlyRecurring : TaskFilterByType
//    data object OnlySingle : TaskFilterByType
//
////    data object All : TaskFilterByType
////    data object OnlyRecurring : TaskFilterByType
////    data object OnlySingle : TaskFilterByType
//}