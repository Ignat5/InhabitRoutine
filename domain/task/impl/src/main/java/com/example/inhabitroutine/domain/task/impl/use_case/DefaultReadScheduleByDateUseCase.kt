package com.example.inhabitroutine.domain.task.impl.use_case

import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.model.reminder.ReminderModel
import com.example.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.model.task.content.TaskDate
import com.example.inhabitroutine.domain.model.task.content.TaskFrequency
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

internal class DefaultReadScheduleByDateUseCase(
    private val taskRepository: TaskRepository,
    private val reminderRepository: ReminderRepository,
    private val defaultDispatcher: CoroutineDispatcher
) {

    operator fun invoke(date: LocalDate) {
        combine(
            readTasksByDate(date),
            readRemindersByDate(date)
        ) { allTasks, allReminders ->
            withContext(defaultDispatcher) {

            }
        }
    }

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