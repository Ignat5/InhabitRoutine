package com.ignatlegostaev.inhabitroutine.domain.model.record.content

import kotlinx.datetime.LocalTime

sealed interface RecordEntry {
    data class Number(val number: Double) : HabitEntry.Continuous.Number
    data class Time(val time: LocalTime) : HabitEntry.Continuous.Time
    data object Done : HabitEntry.YesNo, TaskEntry
    data object Skip : HabitEntry.Continuous.Number, HabitEntry.Continuous.Time, HabitEntry.YesNo
    data object Fail : HabitEntry.Continuous.Number, HabitEntry.Continuous.Time, HabitEntry.YesNo


    sealed interface HabitEntry : RecordEntry {
        sealed interface Continuous : HabitEntry {
            sealed interface Number : Continuous
            sealed interface Time : Continuous
        }
        sealed interface YesNo : HabitEntry
    }
    sealed interface TaskEntry : RecordEntry
}