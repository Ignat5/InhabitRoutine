package com.example.inhabitroutine.feature.view_tasks.model

enum class TaskSort {
    ByDate,
    ByTitle
}

//sealed class TaskSort(val type: Type) {
//    enum class Type { ByDate, ByTitle }
//
//    data object ByDate : TaskSort(Type.ByDate)
//    data object ByTitle : TaskSort(Type.ByTitle)
//
//}