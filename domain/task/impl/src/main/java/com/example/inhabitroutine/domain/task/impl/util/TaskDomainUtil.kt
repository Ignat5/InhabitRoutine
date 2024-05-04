package com.example.inhabitroutine.domain.task.impl.util

import com.example.inhabitroutine.core.util.todayDate
import com.example.inhabitroutine.domain.model.derived.TaskStatus
import com.example.inhabitroutine.domain.model.record.content.RecordEntry
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.model.util.checkIfCompleted
import kotlinx.datetime.Clock

internal fun TaskModel.getTaskStatusByRecordEntry(entry: RecordEntry?): TaskStatus =
    this.let { taskModel ->
        when (taskModel) {
            is TaskModel.Habit -> {
                when (taskModel) {
                    is TaskModel.Habit.HabitContinuous -> {
                        when (taskModel) {
                            is TaskModel.Habit.HabitContinuous.HabitNumber -> {
                                (entry as? RecordEntry.HabitEntry.Continuous.Number).let { entry ->
                                    when (entry) {
                                        null -> TaskStatus.NotCompleted.Pending
                                        is RecordEntry.Number -> {
                                            taskModel.progress.checkIfCompleted(entry)
                                                .let { isCompleted ->
                                                    if (isCompleted) TaskStatus.Completed
                                                    else TaskStatus.NotCompleted.Pending
                                                }
                                        }

                                        is RecordEntry.Skip -> TaskStatus.NotCompleted.Skipped
                                        is RecordEntry.Fail -> TaskStatus.NotCompleted.Failed
                                    }
                                }
                            }

                            is TaskModel.Habit.HabitContinuous.HabitTime -> {
                                (entry as? RecordEntry.HabitEntry.Continuous.Time).let { entry ->
                                    when (entry) {
                                        null -> TaskStatus.NotCompleted.Pending
                                        is RecordEntry.Time -> {
                                            taskModel.progress.checkIfCompleted(entry)
                                                .let { isCompleted ->
                                                    if (isCompleted) TaskStatus.Completed
                                                    else TaskStatus.NotCompleted.Pending
                                                }
                                        }

                                        is RecordEntry.Skip -> TaskStatus.NotCompleted.Skipped
                                        is RecordEntry.Fail -> TaskStatus.NotCompleted.Failed
                                    }
                                }
                            }
                        }
                    }

                    is TaskModel.Habit.HabitYesNo -> {
                        (entry as? RecordEntry.HabitEntry.YesNo).let { entry ->
                            when (entry) {
                                null -> TaskStatus.NotCompleted.Pending
                                is RecordEntry.Done -> TaskStatus.Completed
                                is RecordEntry.Skip -> TaskStatus.NotCompleted.Skipped
                                is RecordEntry.Fail -> TaskStatus.NotCompleted.Failed
                            }
                        }
                    }
                }
            }

            is TaskModel.Task -> {
                when (taskModel) {
                    is TaskModel.Task.RecurringTask -> {
                        (entry as? RecordEntry.TaskEntry).let { entry ->
                            when (entry) {
                                null -> TaskStatus.NotCompleted.Pending
                                is RecordEntry.Done -> TaskStatus.Completed
                            }
                        }
                    }

                    is TaskModel.Task.SingleTask -> {
                        (entry as? RecordEntry.TaskEntry).let { entry ->
                            when (entry) {
                                null -> TaskStatus.NotCompleted.Pending
                                is RecordEntry.Done -> TaskStatus.Completed
                            }
                        }
                    }
                }
            }
        }
    }

internal fun TaskModel.getTaskVersionStartDate() = this.let { taskModel ->
    if (taskModel.isDraft) taskModel.versionStartDate
    else Clock.System.todayDate.let { today ->
        maxOf(today, taskModel.versionStartDate)
    }
}