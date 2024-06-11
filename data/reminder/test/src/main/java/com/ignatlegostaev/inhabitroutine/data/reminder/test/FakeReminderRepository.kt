package com.ignatlegostaev.inhabitroutine.data.reminder.test

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.reminder.api.ReminderRepository
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate

class FakeReminderRepository : ReminderRepository {

    private val allRemindersState = MutableStateFlow<List<ReminderModel>>(emptyList())

    fun setReminders(allReminders: List<ReminderModel>) {
        allRemindersState.update { allReminders }
    }

    fun getReminders(): List<ReminderModel> = allRemindersState.value

    override fun readReminders(): Flow<List<ReminderModel>> {
        return allRemindersState
    }

    override fun readReminderIds(): Flow<List<String>> {
        return allRemindersState.map { allReminders ->
            allReminders.map { it.id }
        }
    }

    override fun readReminderById(reminderId: String): Flow<ReminderModel?> {
        return allRemindersState.map { allReminders ->
            allReminders.find { it.id == reminderId }
        }
    }

    override fun readReminderIdsByTaskId(taskId: String): Flow<List<String>> {
        return allRemindersState.map { allReminders ->
            allReminders
                .filter { it.taskId == taskId }
                .map { it.id }
        }
    }

    override fun readRemindersByDate(targetDate: LocalDate): Flow<List<ReminderModel>> {
        return allRemindersState
    }

    override fun readRemindersByTaskId(taskId: String): Flow<List<ReminderModel>> {
        return allRemindersState.map { allReminders ->
            allReminders.filter { it.taskId == taskId }
        }
    }

    override suspend fun saveReminder(reminderModel: ReminderModel): ResultModel<Unit, Throwable> {
        allRemindersState.update { oldReminders ->
            val newReminders = mutableListOf<ReminderModel>()
            newReminders.addAll(oldReminders)
            val index = newReminders.indexOfFirst { it.id == reminderModel.id }
            if (index != -1) {
                newReminders[index] = reminderModel
            } else {
                newReminders.add(reminderModel)
            }
            newReminders
        }
        return ResultModel.success(Unit)
    }

    override fun readReminderCountByTaskId(taskId: String): Flow<Int> {
        return allRemindersState.map { allReminders ->
            allReminders.count { it.taskId == taskId }
        }
    }

    override suspend fun deleteReminderById(reminderId: String): ResultModel<Unit, Throwable> {
        allRemindersState.update { oldReminders ->
            val newReminders = mutableListOf<ReminderModel>()
            newReminders.addAll(oldReminders)
            newReminders.removeIf { it.id == reminderId }
            newReminders
        }
        return ResultModel.success(Unit)
    }

}