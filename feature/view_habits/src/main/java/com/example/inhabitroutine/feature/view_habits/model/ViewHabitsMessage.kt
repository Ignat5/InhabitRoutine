package com.example.inhabitroutine.feature.view_habits.model

sealed interface ViewHabitsMessage {
    sealed interface Message : ViewHabitsMessage {
        data object ArchiveSuccess : Message
        data object UnarchiveSuccess : Message
        data object DeleteSuccess : Message
    }
    data object Idle : ViewHabitsMessage
}