package com.example.inhabitroutine.feature.create_edit_task.edit.model

sealed interface ItemTaskAction {
    data object ViewStatistics : ItemTaskAction
    sealed interface ArchiveUnarchive : ItemTaskAction {
        data object Archive : ArchiveUnarchive
        data object Unarchive : ArchiveUnarchive
    }
    data object Restart : ItemTaskAction
    data object Delete : ItemTaskAction
}