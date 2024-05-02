package com.example.inhabitroutine.domain.reminder.impl.use_case

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.core.util.mapFailure
import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.domain.model.reminder.ReminderModel
import com.example.inhabitroutine.domain.reminder.api.SetUpNextReminderUseCase
import com.example.inhabitroutine.domain.reminder.api.UpdateReminderUseCase
import com.example.inhabitroutine.domain.reminder.impl.util.checkOverlap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

internal class DefaultUpdateReminderUseCase(
    private val reminderRepository: ReminderRepository,
    private val setUpNextReminderUseCase: SetUpNextReminderUseCase,
    private val externalScope: CoroutineScope,
    private val defaultDispatcher: CoroutineDispatcher
) : UpdateReminderUseCase {

    override suspend operator fun invoke(
        reminderModel: ReminderModel
    ): ResultModel<Unit, UpdateReminderUseCase.UpdateReminderFailure> =
        withContext(defaultDispatcher) {
            val doesOverlap = checkIfOverlaps(reminderModel)
            if (!doesOverlap) {
                val resultModel = reminderRepository.updateReminder(reminderModel)
                if (resultModel is ResultModel.Success) {
                    setUpNextReminderUseCase(reminderId = reminderModel.id)
                }
                resultModel.mapFailure { UpdateReminderUseCase.UpdateReminderFailure.Other(it) }
            } else {
                ResultModel.failure(UpdateReminderUseCase.UpdateReminderFailure.Overlap)
            }
        }

    private suspend fun checkIfOverlaps(reminderModel: ReminderModel): Boolean =
        reminderRepository.readRemindersByTaskId(reminderModel.taskId)
            .firstOrNull()
            ?.filterNot { it.id == reminderModel.id }
            ?.let { allReminders ->
                reminderModel.checkOverlap(allReminders)
            } ?: false

}