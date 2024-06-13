package com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.firstDayOfMonth
import com.ignatlegostaev.inhabitroutine.core.util.firstDayOfWeek
import com.ignatlegostaev.inhabitroutine.core.util.firstDayOfYear
import com.ignatlegostaev.inhabitroutine.core.util.todayDate
import com.ignatlegostaev.inhabitroutine.data.record.api.RecordRepository
import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.derived.TaskStatus
import com.ignatlegostaev.inhabitroutine.domain.model.record.RecordModel
import com.ignatlegostaev.inhabitroutine.domain.model.record.content.RecordEntry
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.util.checkIfMatches
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.calculate_statistics.CalculateTaskStatisticsUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.calculate_statistics.TaskCompletionCount
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.calculate_statistics.TaskStatisticsModel
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.calculate_statistics.TaskStreakModel
import com.ignatlegostaev.inhabitroutine.domain.task.impl.util.getTaskStatusByRecordEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlinx.datetime.until

internal class DefaultCalculateTaskStatisticsUseCase(
    private val taskRepository: TaskRepository,
    private val recordRepository: RecordRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : CalculateTaskStatisticsUseCase {

    override suspend operator fun invoke(taskId: String): ResultModel<TaskStatisticsModel, Throwable> =
        coroutineScope {
            val allTasksDeferred = async {
                readTasksById(taskId).firstOrNull()
            }
            val allRecordsDeferred = async {
                readRecordsById(taskId).firstOrNull()
            }

            allTasksDeferred.await()?.let { allTasks ->
                allRecordsDeferred.await()?.let { allRecords ->
                    withContext(defaultDispatcher) {
                        val taskStatistics = calculateStatistics(allTasks, allRecords)
                        ResultModel.success(taskStatistics)
                    }
                } ?: ResultModel.failure(IllegalStateException())
            } ?: ResultModel.failure(IllegalStateException())
        }

    private suspend fun calculateStatistics(
        allTasks: List<TaskModel.Habit>,
        allRecords: List<RecordModel>
    ): TaskStatisticsModel {
        val statusMap = buildStatusMap(allTasks, allRecords)
        return getTaskStatisticsModel(statusMap)
    }

    private suspend fun getTaskStatisticsModel(statusMap: Map<LocalDate, TaskStatus>): TaskStatisticsModel =
        coroutineScope {
            val habitScoreDef = async {
                calculateHabitScore(statusMap)
            }

            val streakDef = async {
                calculateStreak(statusMap)
            }

            val completionCountDef = async {
                calculateCompletionCount(statusMap)
            }

            val statusCount = async {
                calculateStatusCount(statusMap)
            }
            TaskStatisticsModel(
                habitScore = habitScoreDef.await(),
                streakModel = streakDef.await(),
                completionCount = completionCountDef.await(),
                statusCount = statusCount.await(),
                statusMap = statusMap
            )
        }

    private fun calculateStatusCount(statusMap: Map<LocalDate, TaskStatus>): Map<TaskStatus, Int> {
        val resultMap = mutableMapOf<TaskStatus, Int>()
        statusMap.forEach { entry ->
            val count = resultMap[entry.value] ?: 0
            resultMap[entry.value] = count + 1
        }
        return resultMap
    }

    private fun calculateCompletionCount(statusMap: Map<LocalDate, TaskStatus>): TaskCompletionCount {
        var weekCount = 0
        var monthCount = 0
        var yearCount = 0
        var allTimeCount = 0
        Clock.System.todayDate.let { todayDate ->
            val weekRange = todayDate.firstDayOfWeek.rangeTo(todayDate)
            val monthRange = todayDate.firstDayOfMonth.rangeTo(todayDate)
            val yearRange = todayDate.firstDayOfYear.rangeTo(todayDate)
            statusMap.filterValues { it is TaskStatus.Completed }.let { statusMap ->
                statusMap.keys.forEach { nextDate ->
                    allTimeCount++
                    if (nextDate in weekRange) {
                        weekCount++
                    }
                    if (nextDate in monthRange) {
                        monthCount++
                    }
                    if (nextDate in yearRange) {
                        yearCount++
                    }
                }
                return TaskCompletionCount(
                    currentWeekCompletionCount = weekCount,
                    currentMonthCompletionCount = monthCount,
                    currentYearCompletionCount = yearCount,
                    allTimeCompletionCount = allTimeCount
                )
            }
        }
    }

    private fun calculateStreak(statusMap: Map<LocalDate, TaskStatus>): TaskStreakModel {
        var currentStreakCount = 0
        var bestStreakCount = 0
        with(statusMap.filterValues { it !is TaskStatus.NotCompleted.Skipped }) {
            keys.sorted().let { allDays ->
                allDays.forEach { day ->
                    getValue(day).let { status ->
                        when (status) {
                            is TaskStatus.Completed -> {
                                currentStreakCount++
                                if (currentStreakCount > bestStreakCount) {
                                    bestStreakCount = currentStreakCount
                                }
                            }

                            is TaskStatus.NotCompleted -> {
                                currentStreakCount = 0
                            }
                        }
                    }
                }
            }
        }
        return TaskStreakModel(
            currentStreak = currentStreakCount,
            bestStreak = bestStreakCount
        )
    }

    private fun calculateHabitScore(statusMap: Map<LocalDate, TaskStatus>): Float {
        statusMap.filterValues { it !is TaskStatus.NotCompleted.Skipped }.let { statusMap ->
            if (statusMap.isEmpty()) return 0f
            val allCount = statusMap.size.toFloat()
            val completedCount =
                statusMap.values.count { it is TaskStatus.Completed }.toFloat()
            return completedCount / allCount
        }
    }

    private suspend fun buildStatusMap(
        allTasks: List<TaskModel.Habit>,
        allRecords: List<RecordModel>
    ): Map<LocalDate, TaskStatus> {
        return coroutineScope {
            allTasks.firstOrNull()?.let { taskModel ->
                val dateRange = getDateRange(taskModel)
                val startDate = dateRange.start
                val endDate = dateRange.endInclusive
                startDate.until(endDate, DateTimeUnit.DAY).let { daysUntilEnd ->
                    (0..daysUntilEnd).map { dayOffset ->
                        async {
                            startDate.plus(dayOffset, DateTimeUnit.DAY).let { nextDate ->
                                findTaskRecordPairByDate(
                                    allTasks,
                                    allRecords,
                                    nextDate
                                )?.let { taskRecordPair ->
                                    getDateToTaskStatusPair(
                                        taskModel = taskRecordPair.first,
                                        entry = taskRecordPair.second?.entry,
                                        date = nextDate
                                    )
                                }
                            }
                        }
                    }.awaitAll().filterNotNull().toMap()
                }
            } ?: emptyMap()
        }
    }

    private fun getDateRange(taskModel: TaskModel.Habit): ClosedRange<LocalDate> {
        val startDate = taskModel.date.startDate
        val endDate =
            taskModel.date.endDate?.let { minOf(it, Clock.System.todayDate) }
                ?: Clock.System.todayDate

        return startDate..endDate
    }

    private fun findTaskRecordPairByDate(
        allTasks: List<TaskModel.Habit>,
        allRecords: List<RecordModel>,
        date: LocalDate
    ): Pair<TaskModel.Habit, RecordModel?>? {
        return allTasks.findTaskByVersionStartDate(date)?.let { resultTask ->
            val resultRecord = allRecords.find { it.date == date }
            Pair(resultTask, resultRecord)
        }
    }

    private fun List<TaskModel.Habit>.findTaskByVersionStartDate(date: LocalDate): TaskModel.Habit? =
        this.let { allTasks ->
            allTasks
                .filter { it.versionStartDate <= date }
                .maxByOrNull { it.versionStartDate }
        }

    private fun getDateToTaskStatusPair(
        taskModel: TaskModel.Habit,
        entry: RecordEntry?,
        date: LocalDate
    ): Pair<LocalDate, TaskStatus>? {
        return if (taskModel.checkIfScheduled(date)) {
            val taskStatus = getTaskStatus(taskModel, entry)
            Pair(date, taskStatus)
        } else null
    }

    private fun TaskModel.Habit.checkIfScheduled(date: LocalDate): Boolean =
        this.let { taskModel ->
            return if (taskModel.isArchived) false
            else taskModel.date.checkIfMatches(date) && taskModel.frequency.checkIfMatches(date)
        }

    private fun getTaskStatus(taskModel: TaskModel.Habit, entry: RecordEntry?): TaskStatus.Habit {
        return (taskModel.getTaskStatusByRecordEntry(entry) as? TaskStatus.Habit)
            ?: TaskStatus.NotCompleted.Pending
    }


    private fun readTasksById(taskId: String): Flow<List<TaskModel.Habit>> =
        taskRepository.readTasksById(taskId).map { allTasks ->
            allTasks.filterIsInstance<TaskModel.Habit>()
        }

    private fun readRecordsById(taskId: String): Flow<List<RecordModel>> =
        recordRepository.readRecordsByTaskId(taskId)

}