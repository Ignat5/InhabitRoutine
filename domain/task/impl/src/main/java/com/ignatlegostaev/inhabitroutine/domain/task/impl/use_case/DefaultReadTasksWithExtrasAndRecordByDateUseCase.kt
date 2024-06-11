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

/*
    Step #1: Define the problem
    Write high quality unit tests for the complex use case

    Step #2: Metrics
        1. readability
        2. isolation
        3. coverage (cover all possible implementation flaws)

    Step #3: Questions
        1. How do i handle private functions?
        2. Do i mock or implement repositories?
        3. What can go wrong in the use case (which tests are required)?

    Answers:
        1. Private functions
            Options:
                #1) transform private functions into 'helper' classes +
                #2) move filter processing into repository (why use case should do additional data filtering?)

        2. Write fake implementations
        3. What requires to be tested:
            #1) filter tasks
            #2) filter reminders
            #3) reminders of the result model match the task that they belong to
            #4) record matches the task that it belongs to
            #5) test getTaskStatusByRecordEntry() function in isolation
            #6) test toTaskWithExtrasAndRecord() function in isolation
            Edge cases
            #7) no tasks in repo
            #8) no reminders in repo
            #9) no records in repo

    Step #4: Divide the problem (what are the independent segments of the problem?)
        1. create fake implementations for reminderRepo and recordRepo
        2. create helper classes for the private functions and pass them as arguments to the use case
        3.



 */

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