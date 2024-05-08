package com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base

import com.ignatlegostaev.inhabitroutine.core.presentation.base.BaseResultStateHolder
import com.ignatlegostaev.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.ignatlegostaev.inhabitroutine.core.presentation.components.result.ScreenResult
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.type.ReminderType
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.components.BaseCreateEditReminderScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.components.BaseCreateEditReminderScreenState
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.model.ReminderScheduleType
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.model.type
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

abstract class BaseCreateEditReminderStateHolder<SE : ScreenEvent, SS : BaseCreateEditReminderScreenState, SR : ScreenResult>(
    final override val holderScope: CoroutineScope,
    initReminderTime: LocalTime? = null,
    initReminderType: ReminderType? = null,
    initReminderSchedule: ReminderSchedule? = null
) : BaseResultStateHolder<SE, SS, SR>() {

    protected abstract fun onConfirm()
    protected abstract fun onDismiss()

    protected val inputHoursState = MutableStateFlow(initReminderTime?.hour ?: DEFAULT_HOURS)
    protected val inputMinutesState = MutableStateFlow(initReminderTime?.minute ?: DEFAULT_MINUTES)
    protected val inputReminderTypeState = MutableStateFlow(initReminderType ?: defaultReminderType)
    protected val inputReminderScheduleState =
        MutableStateFlow(initReminderSchedule ?: defaultReminderSchedule)
    protected val canConfirmState = inputReminderScheduleState.map { inputSchedule ->
        when (inputSchedule) {
            is ReminderSchedule.AlwaysEnabled -> true
            is ReminderSchedule.DaysOfWeek -> inputSchedule.daysOfWeek.isNotEmpty()
        }
    }.stateIn(
        holderScope,
        SharingStarted.Eagerly,
        false
    )

    protected fun onBaseEvent(event: BaseCreateEditReminderScreenEvent) {
        when (event) {
            is BaseCreateEditReminderScreenEvent.OnInputHoursUpdate ->
                onInputHoursUpdate(event)

            is BaseCreateEditReminderScreenEvent.OnInputMinutesUpdate ->
                onInputMinutesUpdate(event)

            is BaseCreateEditReminderScreenEvent.OnPickReminderType ->
                onPickReminderType(event)

            is BaseCreateEditReminderScreenEvent.OnPickReminderScheduleType ->
                onPickReminderScheduleType(event)

            is BaseCreateEditReminderScreenEvent.OnDayOfWeekClick ->
                onDayOfWeekClick(event)

            is BaseCreateEditReminderScreenEvent.OnConfirmClick ->
                onConfirmClick()

            is BaseCreateEditReminderScreenEvent.OnDismissRequest ->
                onDismissRequest()
        }
    }

    private fun onInputHoursUpdate(event: BaseCreateEditReminderScreenEvent.OnInputHoursUpdate) {
        inputHoursState.update { event.hours }
    }

    private fun onInputMinutesUpdate(event: BaseCreateEditReminderScreenEvent.OnInputMinutesUpdate) {
        inputMinutesState.update { event.minutes }
    }

    private fun onPickReminderType(event: BaseCreateEditReminderScreenEvent.OnPickReminderType) {
        inputReminderTypeState.update { event.type }
    }

    private fun onPickReminderScheduleType(event: BaseCreateEditReminderScreenEvent.OnPickReminderScheduleType) {
        event.type.let { clickedType ->
            inputReminderScheduleState.value.type.let { currentType ->
                if (clickedType != currentType) {
                    inputReminderScheduleState.update {
                        when (clickedType) {
                            ReminderScheduleType.AlwaysEnabled -> ReminderSchedule.AlwaysEnabled
                            ReminderScheduleType.DaysOfWeek -> ReminderSchedule.DaysOfWeek(emptySet())
                        }
                    }
                }
            }
        }
    }

    private fun onDayOfWeekClick(event: BaseCreateEditReminderScreenEvent.OnDayOfWeekClick) {
        inputReminderScheduleState.update { old ->
            (old as? ReminderSchedule.DaysOfWeek)?.daysOfWeek?.let { oldSet ->
                event.dayOfWeek.let { clickedDayOfWeek ->
                    val newSet = mutableSetOf<DayOfWeek>()
                    newSet.addAll(oldSet)
                    if (newSet.contains(clickedDayOfWeek)) newSet.remove(clickedDayOfWeek)
                    else newSet.add(clickedDayOfWeek)
                    ReminderSchedule.DaysOfWeek(newSet)
                }
            } ?: old
        }
    }

    private fun onConfirmClick() {
        if (canConfirmState.value) {
            onConfirm()
        }
    }

    private fun onDismissRequest() = onDismiss()

    companion object {
        private const val DEFAULT_HOURS = 12
        private const val DEFAULT_MINUTES = 0
        private val defaultReminderType: ReminderType
            get() = ReminderType.NoReminder
        private val defaultReminderSchedule: ReminderSchedule
            get() = ReminderSchedule.AlwaysEnabled
    }

}