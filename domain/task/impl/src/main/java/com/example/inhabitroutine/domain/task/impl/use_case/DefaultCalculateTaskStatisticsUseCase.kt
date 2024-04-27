package com.example.inhabitroutine.domain.task.impl.use_case

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.core.util.firstDayOfMonth
import com.example.inhabitroutine.core.util.firstDayOfWeek
import com.example.inhabitroutine.core.util.firstDayOfYear
import com.example.inhabitroutine.core.util.todayDate
import com.example.inhabitroutine.data.record.api.RecordRepository
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.model.derived.TaskStatus
import com.example.inhabitroutine.domain.model.derived.TaskWithRecordModel
import com.example.inhabitroutine.domain.model.record.RecordModel
import com.example.inhabitroutine.domain.model.record.content.RecordEntry
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.model.util.checkIfMatches
import com.example.inhabitroutine.domain.task.api.use_case.calculate_statistics.CalculateTaskStatisticsUseCase
import com.example.inhabitroutine.domain.task.api.use_case.calculate_statistics.TaskCompletionCount
import com.example.inhabitroutine.domain.task.api.use_case.calculate_statistics.TaskStatisticsModel
import com.example.inhabitroutine.domain.task.api.use_case.calculate_statistics.TaskStreakModel
import com.example.inhabitroutine.domain.task.impl.util.getTaskStatusByRecordEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
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

    override suspend operator fun invoke(taskId: String): ResultModel<TaskStatisticsModel, Throwable> {
        return combine(
            readTasksById(taskId),
            recordRepository.readRecordsByTaskId(taskId)
        ) { allTasks, allRecords ->
            allTasks.firstOrNull()?.let { taskModel ->
                withContext(defaultDispatcher) {
                    val startDate = taskModel.date.startDate
                    val endDate =
                        taskModel.date.endDate?.let { minOf(it, Clock.System.todayDate) }
                            ?: Clock.System.todayDate
                    getStatusMap(
                        allTasks = allTasks,
                        allRecords = allRecords,
                        startDate = startDate,
                        endDate = endDate
                    ).let { statusMap ->
                        ResultModel.success(getTaskStatisticsModel(statusMap))
                    }
                }
            } ?: ResultModel.failure(IllegalStateException())
        }.firstOrNull() ?: ResultModel.failure(NoSuchElementException())
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
        statusMap.filterValues { it !is TaskStatus.NotCompleted.Skipped }.let { statusMap ->
            statusMap.keys.sorted().let { allDays ->
                allDays.forEach { day ->
                    statusMap.getValue(day).let { status ->
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
            return TaskStreakModel(
                currentStreak = currentStreakCount,
                bestStreak = bestStreakCount
            )
        }
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

    private suspend fun getStatusMap(
        allTasks: List<TaskModel.Habit>,
        allRecords: List<RecordModel>,
        startDate: LocalDate,
        endDate: LocalDate
    ): Map<LocalDate, TaskStatus> {
        return coroutineScope {
            startDate.until(endDate, DateTimeUnit.DAY).let { daysUntilEnd ->
                (0..daysUntilEnd).map { dayOffset ->
                    async {
                        startDate.plus(dayOffset, DateTimeUnit.DAY).let { nextDate ->
                            allTasks
                                .asSequence()
                                .filter { it.versionStartDate <= nextDate }
                                .maxByOrNull { it.versionStartDate }
                                ?.let { nextTask ->
                                    allRecords.find { it.date == nextDate }.let { nextRecord ->
                                        getDateToTaskWithRecordPair(
                                            taskModel = nextTask,
                                            entry = nextRecord?.entry,
                                            date = nextDate
                                        )
                                    }
                                }
                        }
                    }
                }.awaitAll().filterNotNull().toMap()
            }
        }
    }

    private fun getDateToTaskWithRecordPair(
        taskModel: TaskModel.Habit,
        entry: RecordEntry?,
        date: LocalDate
    ): Pair<LocalDate, TaskStatus>? {
        return if (taskModel.date.checkIfMatches(date) && taskModel.frequency.checkIfMatches(date) && !taskModel.isArchived) {
            val taskStatus =
                (taskModel.getTaskStatusByRecordEntry(entry) as? TaskStatus.Habit) ?: TaskStatus.NotCompleted.Pending
            Pair(date, taskStatus)
        } else return null
    }

//    private fun TaskModel.Habit.toTaskWithRecordModel(entry: RecordEntry?): TaskWithRecordModel.Habit {
//        return this.let { taskModel ->
//            taskModel.getTaskStatusByRecordEntry(entry).let { status ->
//                when (taskModel) {
//                    is TaskModel.Habit.HabitContinuous -> {
//                        when (taskModel) {
//                            is TaskModel.Habit.HabitContinuous.HabitNumber -> {
//                                TaskWithRecordModel.Habit.HabitContinuous.HabitNumber(
//                                    task = taskModel,
//                                    recordEntry = entry as? RecordEntry.HabitEntry.Continuous.Number,
//                                    status = (status as? TaskStatus.Habit)
//                                        ?: TaskStatus.NotCompleted.Pending
//                                )
//                            }
//
//                            is TaskModel.Habit.HabitContinuous.HabitTime -> {
//                                TaskWithRecordModel.Habit.HabitContinuous.HabitTime(
//                                    task = taskModel,
//                                    recordEntry = entry as? RecordEntry.HabitEntry.Continuous.Time,
//                                    status = (status as? TaskStatus.Habit)
//                                        ?: TaskStatus.NotCompleted.Pending
//                                )
//                            }
//                        }
//                    }
//
//                    is TaskModel.Habit.HabitYesNo -> {
//                        TaskWithRecordModel.Habit.HabitYesNo(
//                            task = taskModel,
//                            recordEntry = entry as? RecordEntry.HabitEntry.YesNo,
//                            status = (status as? TaskStatus.Habit)
//                                ?: TaskStatus.NotCompleted.Pending
//                        )
//                    }
//                }
//            }
//        }
//    }

    private fun readTasksById(taskId: String): Flow<List<TaskModel.Habit>> =
        taskRepository.readTasksById(taskId).map { allTasks ->
            allTasks.filterIsInstance(TaskModel.Habit::class.java)
        }

}