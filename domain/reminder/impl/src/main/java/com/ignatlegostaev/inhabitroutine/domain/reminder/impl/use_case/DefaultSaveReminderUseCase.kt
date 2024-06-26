package com.ignatlegostaev.inhabitroutine.domain.reminder.impl.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.mapFailure
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.data.reminder.api.ReminderRepository
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.type.ReminderType
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SaveReminderUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SetUpNextReminderUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.impl.util.checkOverlap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime

internal class DefaultSaveReminderUseCase(
    private val reminderRepository: ReminderRepository,
    private val setUpNextReminderUseCase: SetUpNextReminderUseCase,
    private val externalScope: CoroutineScope,
    private val defaultDispatcher: CoroutineDispatcher
) : SaveReminderUseCase {

    override suspend operator fun invoke(
        taskId: String,
        time: LocalTime,
        type: ReminderType,
        schedule: ReminderSchedule
    ): ResultModel<Unit, SaveReminderUseCase.SaveReminderFailure> = withContext(defaultDispatcher) {
        ReminderModel(
            id = randomUUID(),
            taskId = taskId,
            time = time,
            type = type,
            schedule = schedule,
            createdAt = Clock.System.now().toEpochMilliseconds()
        ).let { reminderModel ->
            val doesOverlap = checkIfOverlaps(reminderModel)
            if (!doesOverlap) {
                val resultModel = reminderRepository.saveReminder(reminderModel)
                if (resultModel is ResultModel.Success) {
                    externalScope.launch {
                        setUpNextReminderUseCase(reminderModel.id)
                    }
                }
                resultModel.mapFailure { SaveReminderUseCase.SaveReminderFailure.Other(it) }
            } else {
                ResultModel.failure(SaveReminderUseCase.SaveReminderFailure.Overlap)
            }
        }
    }

    private suspend fun checkIfOverlaps(reminderModel: ReminderModel): Boolean =
        reminderRepository.readRemindersByTaskId(reminderModel.taskId).firstOrNull()
            ?.let { allReminders ->
                reminderModel.checkOverlap(allReminders)
            } ?: false

}