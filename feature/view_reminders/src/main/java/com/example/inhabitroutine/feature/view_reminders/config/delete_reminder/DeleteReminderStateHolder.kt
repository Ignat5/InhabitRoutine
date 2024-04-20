package com.example.inhabitroutine.feature.view_reminders.config.delete_reminder

import com.example.inhabitroutine.core.presentation.base.BaseResultStateHolder
import com.example.inhabitroutine.feature.view_reminders.config.delete_reminder.components.DeleteReminderScreenEvent
import com.example.inhabitroutine.feature.view_reminders.config.delete_reminder.components.DeleteReminderScreenResult
import com.example.inhabitroutine.feature.view_reminders.config.delete_reminder.components.DeleteReminderScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DeleteReminderStateHolder(
    private val reminderId: String,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<DeleteReminderScreenEvent, DeleteReminderScreenState, DeleteReminderScreenResult>() {

    override val uiScreenState: StateFlow<DeleteReminderScreenState> =
        MutableStateFlow(DeleteReminderScreenState)

    override fun onEvent(event: DeleteReminderScreenEvent) {
        when (event) {
            is DeleteReminderScreenEvent.OnConfirmClick ->
                onConfirmClick()

            is DeleteReminderScreenEvent.OnDismissRequest ->
                onDismissRequest()
        }
    }

    private fun onConfirmClick() {
        setUpResult(DeleteReminderScreenResult.Confirm(reminderId))
    }

    private fun onDismissRequest() {
        setUpResult(DeleteReminderScreenResult.Dismiss)
    }

}