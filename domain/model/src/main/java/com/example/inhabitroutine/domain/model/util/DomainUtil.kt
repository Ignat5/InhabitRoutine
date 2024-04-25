package com.example.inhabitroutine.domain.model.util

import com.example.inhabitroutine.domain.model.record.content.RecordEntry
import com.example.inhabitroutine.domain.model.task.content.TaskDate
import com.example.inhabitroutine.domain.model.task.content.TaskFrequency
import com.example.inhabitroutine.domain.model.task.content.TaskProgress
import com.example.inhabitroutine.domain.model.task.type.ProgressLimitType
import kotlinx.datetime.LocalDate

fun TaskFrequency.checkIfMatches(date: LocalDate) = this.let { taskFrequency ->
    when (taskFrequency) {
        is TaskFrequency.EveryDay -> true
        is TaskFrequency.DaysOfWeek -> {
            date.dayOfWeek in taskFrequency.daysOfWeek
        }
    }
}

fun TaskDate.checkIfMatches(date: LocalDate): Boolean = this.let { taskDate ->
    when (taskDate) {
        is TaskDate.Period -> {
            taskDate.endDate?.let { endDate ->
                date in taskDate.startDate..endDate
            } ?: run { date >= taskDate.startDate }
        }

        is TaskDate.Day -> taskDate.date == date
    }
}

fun TaskProgress.Number.checkIfCompleted(entry: RecordEntry.Number): Boolean =
    this.let { progressNumber ->
        when (progressNumber.limitType) {
            ProgressLimitType.AtLeast -> {
                entry.number >= progressNumber.limitNumber
            }

            ProgressLimitType.Exactly -> {
                entry.number == progressNumber.limitNumber
            }

            ProgressLimitType.NoMoreThan -> {
                entry.number <= progressNumber.limitNumber
            }
        }
    }

fun TaskProgress.Time.checkIfCompleted(entry: RecordEntry.Time): Boolean =
    this.let { progressNumber ->
        when (progressNumber.limitType) {
            ProgressLimitType.AtLeast -> {
                entry.time >= progressNumber.limitTime
            }

            ProgressLimitType.Exactly -> {
                entry.time == progressNumber.limitTime
            }

            ProgressLimitType.NoMoreThan -> {
                entry.time <= progressNumber.limitTime
            }
        }
    }