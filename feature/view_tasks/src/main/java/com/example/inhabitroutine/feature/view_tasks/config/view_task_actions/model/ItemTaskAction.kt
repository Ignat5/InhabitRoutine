package com.example.inhabitroutine.feature.view_tasks.config.view_task_actions.model

sealed interface ItemTaskAction {
    data object Archive : ItemTaskAction
    data object Unarchive : ItemTaskAction
    data object Delete : ItemTaskAction
}