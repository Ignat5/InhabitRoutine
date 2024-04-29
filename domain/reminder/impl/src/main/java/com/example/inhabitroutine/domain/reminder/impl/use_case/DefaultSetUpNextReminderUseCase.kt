package com.example.inhabitroutine.domain.reminder.impl.use_case

import com.example.inhabitroutine.core.platform.reminder.api.ReminderManager
import com.example.inhabitroutine.core.util.todayDate
import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.model.reminder.ReminderModel
import com.example.inhabitroutine.domain.model.reminder.type.ReminderType
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.model.task.content.TaskDate
import com.example.inhabitroutine.domain.model.util.checkIfMatches
import com.example.inhabitroutine.domain.reminder.api.SetUpNextReminderUseCase
import com.example.inhabitroutine.domain.reminder.impl.util.checkIfMatches
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

internal class DefaultSetUpNextReminderUseCase(
    private val reminderManager: ReminderManager,
    private val reminderRepository: ReminderRepository,
    private val taskRepository: TaskRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : SetUpNextReminderUseCase {

    private val availableTypes: Set<ReminderType>
        get() = setOf(ReminderType.Notification)

    suspend operator fun invoke(reminderId: String) =
        withContext(defaultDispatcher) {
            reminderRepository.readReminderById(reminderId).firstOrNull()?.let { reminderModel ->
                taskRepository.readTaskById(reminderModel.taskId).firstOrNull()?.let { taskModel ->
                    if (taskModel.isDraft || taskModel.isArchived) return@withContext
                    if (reminderModel.type !in availableTypes) return@withContext
                    calculateNextReminderDateTime(taskModel, reminderModel)
                        ?.toInstant(TimeZone.currentSystemDefault())
                        ?.toEpochMilliseconds()?.let { millis ->
                            reminderManager.resetReminderById(reminderModel.id)
                            reminderManager.setReminder(
                                reminderId = reminderModel.id,
                                millis = millis
                            )
                        }
                }
            }
        }

    private fun calculateNextReminderDateTime(
        taskModel: TaskModel,
        reminderModel: ReminderModel
    ): LocalDateTime? {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).let { nowDateTime ->
            val startDate = maxOf(
                when (val taskDate = taskModel.date) {
                    is TaskDate.Period -> taskDate.startDate
                    is TaskDate.Day -> taskDate.date
                }, nowDateTime.date
            )
            val endDate = when (val taskDate = taskModel.date) {
                is TaskDate.Period -> taskDate.endDate ?: startDate.plus(1, DateTimeUnit.MONTH)
                is TaskDate.Day -> taskDate.date
            }
            startDate.daysUntil(endDate).let { daysUntilEnd ->
                (0..daysUntilEnd).forEach { offset ->
                    startDate.plus(offset, DateTimeUnit.DAY).let { nextDate ->
                        val isTaskScheduled = if (taskModel is TaskModel.RecurringActivity) {
                            taskModel.frequency.checkIfMatches(nextDate)
                        } else true
                        if (isTaskScheduled) {
                            if (reminderModel.schedule.checkIfMatches(nextDate)) {
                                LocalDateTime(
                                    date = nextDate,
                                    time = reminderModel.time
                                ).let { nextLocalDateTime ->
                                    if (nextLocalDateTime > nowDateTime) {
                                        return nextLocalDateTime
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null
    }

}