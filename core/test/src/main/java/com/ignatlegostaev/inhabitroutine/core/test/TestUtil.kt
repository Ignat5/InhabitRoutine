package com.ignatlegostaev.inhabitroutine.core.test

import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskProgress
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.ProgressLimitType
import com.ignatlegostaev.inhabitroutine.domain.model.util.DomainConst
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.random.Random
import kotlin.random.nextInt

object TestUtil {

    fun buildRandomTaskNumberProgress(): TaskProgress.Number {
        return TaskProgress.Number(
            limitType = ProgressLimitType.entries.random(),
            limitNumber = Random.nextDouble(DomainConst.MIN_LIMIT_NUMBER, DomainConst.MAX_LIMIT_NUMBER),
            limitUnit = randomUUID()
        )
    }

    fun buildRandomTaskProgressTime(): TaskProgress.Time {
        return TaskProgress.Time(
            limitType = ProgressLimitType.entries.random(),
            limitTime = buildRandomLocalTime()
        )
    }

    fun buildRandomLocalTime(): LocalTime {
        return LocalTime(hour = getRandomHour(), minute = getRandomMinute())
    }

    private fun getRandomHour(): Int {
        return Random.nextInt(0..23)
    }

    private fun getRandomMinute(): Int {
        return Random.nextInt(0..59)
    }

    fun buildRandomDate(): LocalDate {
        return LocalDate(
            year = getRandomYear(),
            monthNumber = getRandomMonthNumber(),
            dayOfMonth = getRandomDayOfMonth()
        )
    }

    private fun getRandomYear(): Int {
        return Random.nextInt(2000, 2024)
    }

    private fun getRandomMonthNumber(): Int {
        return Random.nextInt(1, 12)
    }

    private fun getRandomDayOfMonth(): Int {
        return Random.nextInt(1, 20)
    }

}