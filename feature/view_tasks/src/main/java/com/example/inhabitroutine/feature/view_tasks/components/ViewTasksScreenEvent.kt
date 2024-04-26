package com.example.inhabitroutine.feature.view_tasks.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.core.presentation.components.result.ScreenResult
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_task_type.PickTaskTypeScreenResult
import com.example.inhabitroutine.feature.view_tasks.model.TaskFilterByStatus
import com.example.inhabitroutine.feature.view_tasks.model.TaskFilterByType
import com.example.inhabitroutine.feature.view_tasks.model.TaskSort

sealed interface ViewTasksScreenEvent : ScreenEvent {
    data class OnPickFilterByStatus(
        val filterByStatus: TaskFilterByStatus
    ) : ViewTasksScreenEvent

    data class OnPickFilterByType(
        val filterByType: TaskFilterByType
    ) : ViewTasksScreenEvent

    data class OnPickSort(val taskSort: TaskSort) : ViewTasksScreenEvent

    data object OnCreateTaskClick : ViewTasksScreenEvent
    data object OnSearchClick : ViewTasksScreenEvent

    sealed interface ResultEvent : ViewTasksScreenEvent {
        val result: ScreenResult

        data class PickTaskType(
            override val result: PickTaskTypeScreenResult
        ) : ResultEvent
    }
}