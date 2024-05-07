package com.ignatlegostaev.inhabitroutine.feature.view_tasks.model

sealed interface ViewTasksMessage {
    sealed interface Message : ViewTasksMessage {
        data object ArchiveSuccess : Message
        data object UnarchiveSuccess : Message
        data object DeleteSuccess : Message
    }

    data object Idle : ViewTasksMessage
}