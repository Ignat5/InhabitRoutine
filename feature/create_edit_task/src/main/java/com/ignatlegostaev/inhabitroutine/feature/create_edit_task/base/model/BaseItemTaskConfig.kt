package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.model

import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskFrequency
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskProgress
import kotlinx.datetime.LocalDate

sealed class BaseItemTaskConfig(val key: Key, val contentType: ContentType) {
    enum class Key { Title, Description, Progress, Frequency, Date, StartDate, EndDate, Reminders }
    enum class ContentType { Basic, Switch }

    data class Title(
        val title: String
    ) : BaseItemTaskConfig(Key.Title, ContentType.Basic)

    data class Description(
        val description: String
    ) : BaseItemTaskConfig(Key.Description, ContentType.Basic)

    data class Frequency(
        val taskFrequency: TaskFrequency
    ) : BaseItemTaskConfig(Key.Frequency, ContentType.Basic)

    data class Reminders(
        val count: Int
    ) : BaseItemTaskConfig(Key.Reminders, ContentType.Basic)

    sealed class DateConfig(
        key: Key, contentType: ContentType
    ) : BaseItemTaskConfig(key, contentType) {
        abstract val date: LocalDate?

        data class Date(
            override val date: LocalDate
        ) : DateConfig(Key.Date, ContentType.Basic)

        data class StartDate(
            override val date: LocalDate
        ) : DateConfig(Key.StartDate, ContentType.Basic)

        data class EndDate(
            override val date: LocalDate?
        ) : DateConfig(Key.EndDate, ContentType.Switch)
    }

    sealed class Progress : BaseItemTaskConfig(Key.Progress, ContentType.Basic) {
        abstract val taskProgress: TaskProgress

        data class Number(
            override val taskProgress: TaskProgress.Number
        ) : Progress()

        data class Time(
            override val taskProgress: TaskProgress.Time
        ) : Progress()
    }

}