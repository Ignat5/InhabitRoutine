package com.example.inhabitroutine.feature.create_edit_task.edit.model

sealed interface EditTaskMessage {
    data object Idle : EditTaskMessage
    sealed interface Message : EditTaskMessage {
        data object ArchiveSuccess : Message
        data object UnarchiveSuccess : Message
    }
}