package com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case

import com.ignatlegostaev.inhabitroutine.data.record.api.RecordRepository
import com.ignatlegostaev.inhabitroutine.data.reminder.api.ReminderRepository
import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.derived.TaskExtras
import com.ignatlegostaev.inhabitroutine.domain.model.derived.TaskStatus
import com.ignatlegostaev.inhabitroutine.domain.model.derived.TaskWithExtrasAndRecordModel
import com.ignatlegostaev.inhabitroutine.domain.model.record.RecordModel
import com.ignatlegostaev.inhabitroutine.domain.model.record.content.RecordEntry
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.util.checkIfMatches
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTasksWithExtrasAndRecordByDateUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.util.getTaskStatusByRecordEntry
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
            val status = taskModel.getTaskStatusByRecordEntry(record?.entry)
            when (taskModel) {
                is TaskModel.Habit -> {
                    when (taskModel) {
                        is TaskModel.Habit.HabitContinuous -> {
                            when (taskModel) {
                                is TaskModel.Habit.HabitContinuous.HabitNumber -> {
                                    TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitNumber(
                                        task = taskModel,
                                        taskExtras = TaskExtras.Habit.HabitContinuous.HabitNumber(
                                            allReminders
                                        ),
                                        recordEntry = record?.entry as? RecordEntry.HabitEntry.Continuous.Number,
                                        status = (status as? TaskStatus.Habit)
                                            ?: TaskStatus.NotCompleted.Pending
                                    )
                                }

                                is TaskModel.Habit.HabitContinuous.HabitTime -> {
                                    TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitTime(
                                        task = taskModel,
                                        taskExtras = TaskExtras.Habit.HabitContinuous.HabitTime(
                                            allReminders
                                        ),
                                        recordEntry = record?.entry as? RecordEntry.HabitEntry.Continuous.Time,
                                        status = (status as? TaskStatus.Habit)
                                            ?: TaskStatus.NotCompleted.Pending
                                    )
                                }
                            }
                        }

                        is TaskModel.Habit.HabitYesNo -> {
                            TaskWithExtrasAndRecordModel.Habit.HabitYesNo(
                                task = taskModel,
                                taskExtras = TaskExtras.Habit.HabitYesNo(allReminders),
                                recordEntry = record?.entry as? RecordEntry.HabitEntry.YesNo,
                                status = (status as? TaskStatus.Habit)
                                    ?: TaskStatus.NotCompleted.Pending
                            )
                        }
                    }
                }

                is TaskModel.Task -> {
                    when (taskModel) {
                        is TaskModel.Task.RecurringTask -> {
                            TaskWithExtrasAndRecordModel.Task.RecurringTask(
                                task = taskModel,
                                taskExtras = TaskExtras.Task.RecurringTask(allReminders),
                                recordEntry = record?.entry as? RecordEntry.TaskEntry,
                                status = (status as? TaskStatus.Task)
                                    ?: TaskStatus.NotCompleted.Pending
                            )
                        }

                        is TaskModel.Task.SingleTask -> {
                            TaskWithExtrasAndRecordModel.Task.SingleTask(
                                task = taskModel,
                                taskExtras = TaskExtras.Task.SingleTask(allReminders),
                                recordEntry = record?.entry as? RecordEntry.TaskEntry,
                                status = (status as? TaskStatus.Task)
                                    ?: TaskStatus.NotCompleted.Pending
                            )
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

    private fun Sequence<TaskModel>.filterDrafts() = this.let { allTasks ->
        allTasks.filterNot { it.isDraft }
    }

    private fun Sequence<TaskModel>.filterArchived() = this.let { allTasks ->
        allTasks.filterNot { it.isArchived }
    }

}