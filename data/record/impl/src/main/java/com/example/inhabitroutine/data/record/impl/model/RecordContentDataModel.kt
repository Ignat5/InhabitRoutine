package com.example.inhabitroutine.data.record.impl.model

import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("RecordContent")
sealed interface RecordContentDataModel {
    @Serializable
    @SerialName("EntryContent")
    sealed interface EntryContent : RecordContentDataModel {
        @Serializable
        @SerialName("Done")
        data object Done : EntryContent

        @Serializable
        @SerialName("Skip")
        data object Skip : EntryContent

        @Serializable
        @SerialName("Fail")
        data object Fail : EntryContent

        @Serializable
        @SerialName("Number")
        data class Number(val number: Double) : EntryContent

        @Serializable
        @SerialName("Time")
        data class Time(val time: LocalTime) : EntryContent
    }
}