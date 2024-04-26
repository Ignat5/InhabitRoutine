package com.example.inhabitroutine.feature.view_tasks.model

sealed interface TaskSort {
    data object ByDate : TaskSort
    data object ByTitle : TaskSort
}