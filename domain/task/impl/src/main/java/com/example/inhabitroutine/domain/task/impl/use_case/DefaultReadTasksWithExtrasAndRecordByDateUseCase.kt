package com.example.inhabitroutine.domain.task.impl.use_case

import com.example.inhabitroutine.data.record.api.RecordRepository
import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.model.derived.TaskExtras
import com.example.inhabitroutine.domain.model.derived.TaskStatus
import com.example.inhabitroutine.domain.model.derived.TaskWithExtrasAndRecordModel
import com.example.inhabitroutine.domain.model.record.RecordModel
import com.example.inhabitroutine.domain.model.record.content.RecordEntry
import com.example.inhabitroutine.domain.model.reminder.ReminderModel
import com.example.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.model.task.content.TaskDate
import com.example.inhabitroutine.domain.model.task.content.TaskFrequency
import com.example.inhabitroutine.domain.model.task.type.ProgressLimitType
import com.example.inhabitroutine.domain.task.api.use_case.ReadTasksWithExtrasAndRecordByDateUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

internal class DefaultReadTasksWithExtrasAndRecordByDateUseCase(
    private val taskRepository: TaskRepository,
    private val reminderRepository: ReminderRepository,
    private val recordRepository: RecordRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : ReadTasksWithExtrasAndRecordByDateUseCase {

    override operator fun invoke(date: LocalDate): Flow<List<TaskWithExtrasAndRecordModel>> =
        combine(
            readTasksByDate(date),
            readRemindersByDate(date),
            readRecordsByDate(date)
        ) { allTasks, allReminders, allRecords ->
            withContext(defaultDispatcher) {
                allTasks.map { taskModel ->
                    async {
                        taskModel.toTaskWithExtrasAndRecord(
                            allReminders = allReminders.filter { it.taskId == taskModel.id },
                            record = allRecords.find { it.taskId == taskModel.id }
                        )
                    }
                }.awaitAll()
            }
        }

    private fun TaskModel.toTaskWithExtrasAndRecord(
        allReminders: List<ReminderModel>,
        record: RecordModel?
    ): TaskWithExtrasAndRecordModel {
        return this.let { taskModel ->
            when (taskModel) {
                is TaskModel.Habit -> {
                    when (taskModel) {
                        is TaskModel.Habit.HabitContinuous -> {
                            when (taskModel) {
                                is TaskModel.Habit.HabitContinuous.HabitNumber -> {
                                    (record?.entry as? RecordEntry.HabitEntry.Continuous.Number).let { entry ->
                                        TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitNumber(
                                            task = taskModel,
                                            taskExtras = TaskExtras.Habit.HabitContinuous.HabitNumber(
                                                allReminders
                                            ),
                                            recordEntry = entry,
                                            status = when (entry) {
                                                null -> TaskStatus.NotCompleted.Pending
                                                is RecordEntry.Number -> {
                                                    taskModel.progress.let { progress ->
                                                        val isDone = when (progress.limitType) {
                                                            ProgressLimitType.AtLeast -> {
                                                                entry.number >= progress.limitNumber
                                                            }

                                                            ProgressLimitType.Exactly -> {
                                                                entry.number == progress.limitNumber
                                                            }

                                                            ProgressLimitType.NoMoreThan -> {
                                                                entry.number <= progress.limitNumber
                                                            }
                                                        }
                                                        if (isDone) TaskStatus.Completed
                                                        else TaskStatus.NotCompleted.Pending
                                                    }
                                                }

                                                is RecordEntry.Skip -> TaskStatus.NotCompleted.Skipped
                                                is RecordEntry.Fail -> TaskStatus.NotCompleted.Failed
                                            }
                                        )
                                    }
                                }

                                is TaskModel.Habit.HabitContinuous.HabitTime -> {
                                    (record?.entry as? RecordEntry.HabitEntry.Continuous.Time).let { entry ->
                                        TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitTime(
                                            task = taskModel,
                                            taskExtras = TaskExtras.Habit.HabitContinuous.HabitTime(
                                                allReminders
                                            ),
                                            recordEntry = entry,
                                            status = when (entry) {
                                                null -> TaskStatus.NotCompleted.Pending
                                                is RecordEntry.Time -> {
                                                    taskModel.progress.let { progress ->
                                                        val isDone = when (progress.limitType) {
                                                            ProgressLimitType.AtLeast -> {
                                                                entry.time >= progress.limitTime
                                                            }

                                                            ProgressLimitType.Exactly -> {
                                                                entry.time == progress.limitTime
                                                            }

                                                            ProgressLimitType.NoMoreThan -> {
                                                                entry.time <= progress.limitTime
                                                            }
                                                        }
                                                        if (isDone) TaskStatus.Completed
                                                        else TaskStatus.NotCompleted.Pending
                                                    }
                                                }

                                                is RecordEntry.Skip -> TaskStatus.NotCompleted.Skipped
                                                is RecordEntry.Fail -> TaskStatus.NotCompleted.Failed
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        is TaskModel.Habit.HabitYesNo -> {
                            (record?.entry as? RecordEntry.HabitEntry.YesNo).let { entry ->
                                TaskWithExtrasAndRecordModel.Habit.HabitYesNo(
                                    task = taskModel,
                                    taskExtras = TaskExtras.Habit.HabitYesNo(allReminders),
                                    recordEntry = entry,
                                    status = when (entry) {
                                        null -> TaskStatus.NotCompleted.Pending
                                        is RecordEntry.Done -> TaskStatus.Completed
                                        is RecordEntry.Skip -> TaskStatus.NotCompleted.Skipped
                                        is RecordEntry.Fail -> TaskStatus.NotCompleted.Failed
                                    }
                                )
                            }
                        }
                    }
                }

                is TaskModel.Task -> {
                    when (taskModel) {
                        is TaskModel.Task.RecurringTask -> {
                            (record?.entry as? RecordEntry.TaskEntry).let { entry ->
                                TaskWithExtrasAndRecordModel.Task.RecurringTask(
                                    task = taskModel,
                                    taskExtras = TaskExtras.Task.RecurringTask(allReminders),
                                    recordEntry = entry,
                                    status = when (entry) {
                                        null -> TaskStatus.NotCompleted.Pending
                                        is RecordEntry.Done -> TaskStatus.Completed
                                    }
                                )
                            }
                        }

                        is TaskModel.Task.SingleTask -> {
                            (record?.entry as? RecordEntry.TaskEntry).let { entry ->
                                TaskWithExtrasAndRecordModel.Task.SingleTask(
                                    task = taskModel,
                                    taskExtras = TaskExtras.Task.SingleTask(allReminders),
                                    recordEntry = entry,
                                    status = when (entry) {
                                        null -> TaskStatus.NotCompleted.Pending
                                        is RecordEntry.Done -> TaskStatus.Completed
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun readRecordsByDate(date: LocalDate): Flow<List<RecordModel>> =
        recordRepository.readRecordsByDate(date)

    private fun readTasksByDate(date: LocalDate) =
        taskRepository.readTasksByDate(date)
            .map { allTasks ->
                if (allTasks.isNotEmpty()) {
                    withContext(defaultDispatcher) {
                        allTasks.filterTasks(date)
                    }
                } else emptyList()
            }.distinctUntilChanged()

    private fun readRemindersByDate(date: LocalDate) =
        reminderRepository.readRemindersByDate(date).map { allReminders ->
            if (allReminders.isNotEmpty()) {
                withContext(defaultDispatcher) {
                    allReminders.filterByDate(date)
                }
            } else emptyList()
        }.distinctUntilChanged()

    private fun List<ReminderModel>.filterByDate(date: LocalDate) = this.let { allReminders ->
        allReminders.filter { reminderModel ->
            when (val schedule = reminderModel.schedule) {
                is ReminderSchedule.AlwaysEnabled -> true
                is ReminderSchedule.DaysOfWeek -> {
                    date.dayOfWeek in schedule.daysOfWeek
                }
            }
        }
    }

    private fun List<TaskModel>.filterTasks(date: LocalDate) = this.let { allTasks ->
        allTasks
            .asSequence()
            .filterDrafts()
            .filterArchived()
            .filterByDate(date)
            .toList()
    }

    private fun Sequence<TaskModel>.filterByDate(date: LocalDate) = this.let { allTasks ->
        allTasks.filter { taskModel ->
            taskModel.date.checkIfMatches(date) &&
                    (taskModel as? TaskModel.RecurringActivity)?.frequency?.checkIfMatches(date) ?: true
        }
    }

    private fun TaskFrequency.checkIfMatches(date: LocalDate) = this.let { taskFrequency ->
        when (taskFrequency) {
            is TaskFrequency.EveryDay -> true
            is TaskFrequency.DaysOfWeek -> {
                date.dayOfWeek in taskFrequency.daysOfWeek
            }
        }
    }

    private fun TaskDate.checkIfMatches(date: LocalDate): Boolean = this.let { taskDate ->
        when (taskDate) {
            is TaskDate.Period -> {
                taskDate.endDate?.let { endDate ->
                    date in taskDate.startDate..endDate
                } ?: run { date >= taskDate.startDate }
            }

            is TaskDate.Day -> taskDate.date == date
        }
    }

    private fun Sequence<TaskModel>.filterDrafts() = this.let { allTasks ->
        allTasks.filterNot { it.isDraft }
    }

    private fun Sequence<TaskModel>.filterArchived() = this.let { allTasks ->
        allTasks.filterNot { it.isArchived }
    }

}