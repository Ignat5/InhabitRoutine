package com.ignatlegostaev.inhabitroutine.core.presentation.ui.util

import android.content.Context
import com.ignatlegostaev.inhabitroutine.core.presentation.R
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.type.ReminderType
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskFrequency
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskProgress
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.ProgressLimitType
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskProgressType
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskType
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

fun TaskFrequency.toDisplay(context: Context) = this.let { frequency ->
    when (frequency) {
        is TaskFrequency.EveryDay -> context.getString(R.string.frequency_every_day)
        is TaskFrequency.DaysOfWeek -> {
            if (frequency.daysOfWeek.size != DayOfWeek.entries.size) {
                frequency.daysOfWeek.toDisplay(context)
            } else context.getString(R.string.frequency_every_day)
        }
    }
}

fun ReminderType.toIconResId() = when (this) {
    ReminderType.NoReminder -> R.drawable.ic_notification_off
    ReminderType.Notification -> R.drawable.ic_notification
}

fun ReminderType.toTitleResId() = when (this) {
    ReminderType.NoReminder -> R.string.reminder_type_no_reminder
    ReminderType.Notification -> R.string.reminder_type_notification
}

fun ReminderSchedule.toDisplay(context: Context) = when (this) {
    is ReminderSchedule.AlwaysEnabled -> context.getString(R.string.reminder_schedule_always_enabled_title)
    is ReminderSchedule.DaysOfWeek -> this.daysOfWeek.toDisplay(context)
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

fun LocalDate.toDayMonthYearFullDisplay(context: Context) = this.let { date ->
    buildString {
        append(date.toDayOfMonthDisplay(context))
        append(", ")
        append(date.year.toString())
    }
}

fun LocalDate.toMonthDayYearDisplay(context: Context) = this.let { date ->
    buildString {
//        append(date.month.toDisplay(context).take(3))
//        append(" ")
//        append(date.dayOfMonth)
        append(date.toDayOfMonthDisplay(context))
        append(", ")
        append(date.year)
    }
}

fun LocalDate.toMonthYearDisplay(context: Context) = this.let { date ->
    buildString {
        append(date.month.toDisplay(context))
        append(" ")
        append(date.year.toString())
    }
}

fun LocalDate.toDayOfWeekMonthMonthDayDisplay(context: Context) = this.let { date ->
    buildString {
        append(date.dayOfWeek.toDisplay(context))
        append(", ")
        append(date.toDayOfMonthDisplay(context))
//        append(date.month.toDisplay(context))
//        append(" ")
//        append(date.dayOfMonth.toString())
    }
}

private fun Collection<DayOfWeek>.toDisplay(
    context: Context
) = this.sortedBy { it.ordinal }.let { allDaysOfWeek ->
    buildString {
        allDaysOfWeek.forEachIndexed { index, dayOfWeek ->
            append(dayOfWeek.toDisplayShort(context))
            if (index != allDaysOfWeek.lastIndex) {
                append(" • ")
            }
        }
    }
}

fun DayOfWeek.toDisplay(context: Context) = context.getString(
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

fun DayOfWeek.toDisplayShort(context: Context) = context.getString(
    when (this) {
        DayOfWeek.MONDAY -> R.string.day_of_week_monday_short
        DayOfWeek.TUESDAY -> R.string.day_of_week_tuesday_short
        DayOfWeek.WEDNESDAY -> R.string.day_of_week_wednesday_short
        DayOfWeek.THURSDAY -> R.string.day_of_week_thursday_short
        DayOfWeek.FRIDAY -> R.string.day_of_week_friday_short
        DayOfWeek.SATURDAY -> R.string.day_of_week_saturday_short
        DayOfWeek.SUNDAY -> R.string.day_of_week_sunday_short
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

private fun LocalDate.toDayOfMonthDisplay(context: Context) = context.getString(
    when (this.month) {
        Month.JANUARY -> R.string.day_of_month_january
        Month.FEBRUARY -> R.string.day_of_month_february
        Month.MARCH -> R.string.day_of_month_march
        Month.APRIL -> R.string.day_of_month_april
        Month.MAY -> R.string.day_of_month_may
        Month.JUNE -> R.string.day_of_month_june
        Month.JULY -> R.string.day_of_month_july
        Month.AUGUST -> R.string.day_of_month_august
        Month.SEPTEMBER -> R.string.day_of_month_september
        Month.OCTOBER -> R.string.day_of_month_october
        Month.NOVEMBER -> R.string.day_of_month_november
        Month.DECEMBER -> R.string.day_of_month_december
    },
    this.dayOfMonth
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