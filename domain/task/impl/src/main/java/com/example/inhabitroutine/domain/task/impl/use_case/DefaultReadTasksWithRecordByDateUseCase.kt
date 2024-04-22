package com.example.inhabitroutine.domain.task.impl.use_case

import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.model.derived.TaskExtras
import com.example.inhabitroutine.domain.model.derived.TaskStatus
import com.example.inhabitroutine.domain.model.derived.TaskWithExtrasAndRecordModel
import com.example.inhabitroutine.domain.model.derived.TaskWithExtrasModel
import com.example.inhabitroutine.domain.model.record.RecordModel
import com.example.inhabitroutine.domain.model.record.content.RecordEntry
import com.example.inhabitroutine.domain.model.reminder.ReminderModel
import com.example.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.model.task.content.TaskDate
import com.example.inhabitroutine.domain.model.task.content.TaskFrequency
import com.example.inhabitroutine.domain.model.task.type.ProgressLimitType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

internal class DefaultReadTasksWithRecordByDateUseCase(
    private val taskRepository: TaskRepository,
    private val reminderRepository: ReminderRepository,
    private val defaultDispatcher: CoroutineDispatcher
) {

    operator fun invoke(date: LocalDate) = combine(
        readTasksByDate(date),
        readRecordsByDate(date),
        readRemindersByDate(date)
    ) { allTasks, allRecords, allReminders ->
        withContext(defaultDispatcher) {
            allTasks.map { taskModel ->
                async {
                    taskModel
                        .toTaskWithExtras(allReminders.filter { it.taskId == taskModel.id })
                        .toTaskWithExtrasAndRecord(allRecords.find { it.taskId == taskModel.id })
                }
            }.awaitAll()
        }
    }

    private fun TaskModel.toTaskWithExtras(
        allReminders: List<ReminderModel>
    ): TaskWithExtrasModel = this.let { taskModel ->
        when (taskModel) {
            is TaskModel.Habit -> {
                when (taskModel) {
                    is TaskModel.Habit.HabitContinuous -> {
                        when (taskModel) {
                            is TaskModel.Habit.HabitContinuous.HabitNumber -> {
                                TaskWithExtrasModel.Habit.HabitContinuous.HabitNumber(
                                    taskModel = taskModel,
                                    taskExtras = TaskExtras.Habit.HabitContinuous.HabitNumber(
                                        allReminders = allReminders
                                    )
                                )
                            }

                            is TaskModel.Habit.HabitContinuous.HabitTime -> {
                                TaskWithExtrasModel.Habit.HabitContinuous.HabitTime(
                                    taskModel = taskModel,
                                    taskExtras = TaskExtras.Habit.HabitContinuous.HabitTime(
                                        allReminders = allReminders
                                    )
                                )
                            }
                        }
                    }

                    is TaskModel.Habit.HabitYesNo -> {
                        TaskWithExtrasModel.Habit.HabitYesNo(
                            taskModel = taskModel,
                            taskExtras = TaskExtras.Habit.HabitYesNo(
                                allReminders = allReminders
                            )
                        )
                    }
                }
            }

            is TaskModel.Task -> {
                when (taskModel) {
                    is TaskModel.Task.RecurringTask -> {
                        TaskWithExtrasModel.Task.RecurringTask(
                            taskModel = taskModel,
                            taskExtras = TaskExtras.Task.RecurringTask(
                                allReminders = allReminders
                            )
                        )
                    }

                    is TaskModel.Task.SingleTask -> {
                        TaskWithExtrasModel.Task.SingleTask(
                            taskModel = taskModel,
                            taskExtras = TaskExtras.Task.SingleTask(
                                allReminders = allReminders
                            )
                        )
                    }
                }
            }
        }
    }

    private fun TaskWithExtrasModel.toTaskWithExtrasAndRecord(
        recordModel: RecordModel?
    ): TaskWithExtrasAndRecordModel = this.let { taskWithExtrasModel ->
        when (taskWithExtrasModel) {
            is TaskWithExtrasModel.Habit -> {
                when (taskWithExtrasModel) {
                    is TaskWithExtrasModel.Habit.HabitContinuous -> {
                        when (taskWithExtrasModel) {
                            is TaskWithExtrasModel.Habit.HabitContinuous.HabitNumber -> {
                                (recordModel?.entry as? RecordEntry.HabitEntry.Continuous.Number)?.let { entry ->
                                    TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitNumber(
                                        taskWithExtrasModel = taskWithExtrasModel,
                                        recordEntry = entry,
                                        status = when (entry) {
                                            is RecordEntry.Number -> {
                                                taskWithExtrasModel.taskModel.progress.let { progressNumber ->
                                                    val isCompleted =
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
                                                    if (isCompleted) TaskStatus.Completed
                                                    else TaskStatus.NotCompleted.Pending
                                                }
                                            }

                                            is RecordEntry.Skip -> TaskStatus.NotCompleted.Skipped
                                            is RecordEntry.Fail -> TaskStatus.NotCompleted.Failed
                                        }
                                    )
                                }
                                    ?: TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitNumber(
                                        taskWithExtrasModel = taskWithExtrasModel,
                                        recordEntry = null,
                                        status = TaskStatus.NotCompleted.Pending
                                    )
                            }

                            is TaskWithExtrasModel.Habit.HabitContinuous.HabitTime -> {
                                (recordModel?.entry as? RecordEntry.HabitEntry.Continuous.Time)?.let { entry ->
                                    TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitTime(
                                        taskWithExtrasModel = taskWithExtrasModel,
                                        recordEntry = entry,
                                        status = when (entry) {
                                            is RecordEntry.Time -> {
                                                taskWithExtrasModel.taskModel.progress.let { progressTime ->
                                                    val isCompleted =
                                                        when (progressTime.limitType) {
                                                            ProgressLimitType.AtLeast -> {
                                                                entry.time >= progressTime.limitTime
                                                            }

                                                            ProgressLimitType.Exactly -> {
                                                                entry.time == progressTime.limitTime
                                                            }

                                                            ProgressLimitType.NoMoreThan -> {
                                                                entry.time <= progressTime.limitTime
                                                            }
                                                        }
                                                    if (isCompleted) TaskStatus.Completed
                                                    else TaskStatus.NotCompleted.Pending
                                                }
                                            }

                                            is RecordEntry.Skip -> TaskStatus.NotCompleted.Skipped
                                            is RecordEntry.Fail -> TaskStatus.NotCompleted.Failed
                                        }
                                    )
                                }
                                    ?: TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitTime(
                                        taskWithExtrasModel = taskWithExtrasModel,
                                        recordEntry = null,
                                        status = TaskStatus.NotCompleted.Pending
                                    )
                            }
                        }
                    }

                    is TaskWithExtrasModel.Habit.HabitYesNo -> {
                        (recordModel?.entry as? RecordEntry.HabitEntry.YesNo)?.let { entry ->
                            TaskWithExtrasAndRecordModel.Habit.HabitYesNo(
                                taskWithExtrasModel = taskWithExtrasModel,
                                recordEntry = entry,
                                status = when (entry) {
                                    is RecordEntry.Done -> TaskStatus.Completed
                                    is RecordEntry.Skip -> TaskStatus.NotCompleted.Skipped
                                    is RecordEntry.Fail -> TaskStatus.NotCompleted.Failed
                                }
                            )
                        } ?: TaskWithExtrasAndRecordModel.Habit.HabitYesNo(
                            taskWithExtrasModel = taskWithExtrasModel,
                            recordEntry = null,
                            status = TaskStatus.NotCompleted.Pending
                        )
                    }
                }
            }

            is TaskWithExtrasModel.Task -> {
                when (taskWithExtrasModel) {
                    is TaskWithExtrasModel.Task.RecurringTask -> {
                        (recordModel?.entry as? RecordEntry.TaskEntry)?.let { entry ->
                            TaskWithExtrasAndRecordModel.Task.RecurringTask(
                                taskWithExtrasModel = taskWithExtrasModel,
                                recordEntry = entry,
                                status = when (entry) {
                                    is RecordEntry.Done -> TaskStatus.Completed
                                }
                            )
                        } ?: TaskWithExtrasAndRecordModel.Task.RecurringTask(
                            taskWithExtrasModel = taskWithExtrasModel,
                            recordEntry = null,
                            status = TaskStatus.NotCompleted.Pending
                        )
                    }

                    is TaskWithExtrasModel.Task.SingleTask -> {
                        (recordModel?.entry as? RecordEntry.TaskEntry)?.let { entry ->
                            TaskWithExtrasAndRecordModel.Task.SingleTask(
                                taskWithExtrasModel = taskWithExtrasModel,
                                recordEntry = entry,
                                status = when (entry) {
                                    is RecordEntry.Done -> TaskStatus.Completed
                                }
                            )
                        } ?: TaskWithExtrasAndRecordModel.Task.SingleTask(
                            taskWithExtrasModel = taskWithExtrasModel,
                            recordEntry = null,
                            status = TaskStatus.NotCompleted.Pending
                        )
                    }
                }
            }
        }
    }

    private fun readRecordsByDate(date: LocalDate): Flow<List<RecordModel>> =
        flow { emit(emptyList()) }

    private fun readTasksByDate(date: LocalDate) =
        taskRepository.readTasksByDate(date).map { allTasks ->
            withContext(defaultDispatcher) {
                allTasks.filterTasks(date)
            }
        }

    private fun readRemindersByDate(date: LocalDate) =
        reminderRepository.readReminders().map { allReminders ->
            withContext(defaultDispatcher) {
                allReminders.filterByDate(date)
            }
        }

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