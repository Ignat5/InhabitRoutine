package com.example.inhabitroutine.core.presentation.ui.util

import android.content.Context
import androidx.compose.ui.text.buildAnnotatedString
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.domain.model.task.content.TaskFrequency
import com.example.inhabitroutine.domain.model.task.content.TaskProgress
import com.example.inhabitroutine.domain.model.task.type.ProgressLimitType
import com.example.inhabitroutine.domain.model.task.type.TaskProgressType
import com.example.inhabitroutine.domain.model.task.type.TaskType
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month

fun TaskType.toIconId(): Int = when (this) {
    TaskType.Habit -> R.drawable.ic_habit
    TaskType.RecurringTask -> R.drawable.ic_recurring_task
    TaskType.SingleTask -> R.drawable.ic_task
}

fun TaskType.toTitleStringId(): Int = when (this) {
    TaskType.Habit -> R.string.habit_title
    TaskType.RecurringTask -> R.string.recurring_task_title
    TaskType.SingleTask -> R.string.single_task_title
}

fun TaskProgressType.toIconId(): Int = when (this) {
    TaskProgressType.YesNo -> R.drawable.ic_progress_yes_no
    TaskProgressType.Number -> R.drawable.ic_progress_number
    TaskProgressType.Time -> R.drawable.ic_progress_time
}

fun LocalDate.toDayMonthYearDisplay() = this.let { date ->
    buildString {
        append(date.dayOfMonth.insertZeroIfRequired())
        append(".")
        append(date.monthNumber.insertZeroIfRequired())
        append(".")
        append(date.year.toString().takeLast(2))
    }
}

fun LocalDate.toMonthDayYearDisplay(context: Context) = this.let { date ->
    buildString {
        append(date.month.toDisplay(context).take(3))
        append(" ")
        append(date.dayOfMonth)
        append(", ")
        append(date.year)
    }
}

fun TaskFrequency.toDisplay(context: Context) = this.let { frequency ->
    when (frequency) {
        is TaskFrequency.EveryDay -> context.getString(R.string.frequency_every_day)
        is TaskFrequency.DaysOfWeek -> frequency.daysOfWeek.toDisplay(context)
    }
}

private fun Collection<DayOfWeek>.toDisplay(
    context: Context,
    maxLettersPerDayCount: Int = 3
) = this.sortedBy { it.ordinal }.let { allDaysOfWeek ->
    buildString {
        allDaysOfWeek.forEachIndexed { index, dayOfWeek ->
            append(dayOfWeek.toDisplay(context).take(maxLettersPerDayCount))
            if (index != allDaysOfWeek.lastIndex) {
                append(" • ")
            }
        }
    }
}

private fun DayOfWeek.toDisplay(context: Context) = context.getString(
    when (this) {
        DayOfWeek.MONDAY -> R.string.day_of_week_monday
        DayOfWeek.TUESDAY -> R.string.day_of_week_tuesday
        DayOfWeek.WEDNESDAY -> R.string.day_of_week_wednesday
        DayOfWeek.THURSDAY -> R.string.day_of_week_thursday
        DayOfWeek.FRIDAY -> R.string.day_of_week_friday
        DayOfWeek.SATURDAY -> R.string.day_of_week_saturday
        DayOfWeek.SUNDAY -> R.string.day_of_week_sunday
    }
)

private fun Month.toDisplay(context: Context) = context.getString(
    when (this) {
        Month.JANUARY -> R.string.month_january
        Month.FEBRUARY -> R.string.month_february
        Month.MARCH -> R.string.month_march
        Month.APRIL -> R.string.month_april
        Month.MAY -> R.string.month_may
        Month.JUNE -> R.string.month_june
        Month.JULY -> R.string.month_july
        Month.AUGUST -> R.string.month_august
        Month.SEPTEMBER -> R.string.month_september
        Month.OCTOBER -> R.string.month_october
        Month.NOVEMBER -> R.string.month_november
        Month.DECEMBER -> R.string.month_december
    }
)

fun TaskProgress.Number.toDisplay(context: Context): String =
    this.let { progress ->
        buildString {
            append(context.getString(progress.limitType.toTitleStringId()))
            append(" ")
            append(progress.limitNumber.limitNumberToDisplay())
            if (progress.limitUnit.isNotBlank()) {
                append(" ")
                append(progress.limitUnit)
                append(" ")
            }
            append(" ")
            append(context.getString(R.string.daily_goal_suffix))
        }
    }

fun TaskProgress.Time.toDisplay(context: Context): String =
    this.let { progress ->
        buildString {
            append(context.getString(progress.limitType.toTitleStringId()))
            append(" ")
            append(progress.limitTime.toHourMinute())
            append(" ")
            append(context.getString(R.string.daily_goal_suffix))
        }
    }

fun LocalTime.toHourMinute(): String = this.let { time ->
    buildString {
        this.append(time.hour.insertZeroIfRequired())
        this.append(":")
        this.append(time.minute.insertZeroIfRequired())
    }
}

private fun Int.insertZeroIfRequired(): String = this.let { number ->
    if (number <= 9) "0$number" else "$number"
}

fun ProgressLimitType.toTitleStringId(): Int =
    when (this) {
        ProgressLimitType.AtLeast -> R.string.progress_limit_type_at_least_title
        ProgressLimitType.Exactly -> R.string.progress_limit_type_exactly_title
        ProgressLimitType.NoMoreThan -> R.string.progress_limit_type_no_more_than_title
    }

private const val DEFAULT_REMINDER: Double = 0.0
private const val DEFAULT_DELIMITER: Int = 1

fun Double.limitNumberToDisplay(): String = this.let { number ->
    if (number.rem(DEFAULT_DELIMITER) == DEFAULT_REMINDER) {
        "${number.toInt()}"
    } else "$number"
}