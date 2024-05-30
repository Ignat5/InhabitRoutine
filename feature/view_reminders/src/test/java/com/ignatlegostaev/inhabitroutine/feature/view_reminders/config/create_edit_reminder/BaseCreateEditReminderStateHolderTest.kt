package com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder

import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.type.ReminderType
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.BaseCreateEditReminderStateHolder
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.components.BaseCreateEditReminderScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.model.ReminderScheduleType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DayOfWeek
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseCreateEditReminderStateHolderTest {

    abstract val stateHolder: BaseCreateEditReminderStateHolder<*, *, *>
    abstract val testDispatcher: TestDispatcher
    abstract fun onBaseEvent(event: BaseCreateEditReminderScreenEvent)

    @Test
    fun `when hours value is updated, state is updated correctly`() = runLocalTest {
        collectUIState()
        val event = BaseCreateEditReminderScreenEvent.OnInputHoursUpdate(1)
        onBaseEvent(event)
        advanceUntilIdle()
        assertTrue(stateHolder.uiScreenState.value.inputHours == event.hours)
    }

    @Test
    fun `when minutes value is updated, state is updated correctly`() = runLocalTest {
        collectUIState()
        val event = BaseCreateEditReminderScreenEvent.OnInputMinutesUpdate(minutes = 1)
        onBaseEvent(event)
        advanceUntilIdle()
        assertTrue(stateHolder.uiScreenState.value.inputMinutes == event.minutes)
    }

    @Test
    fun `when reminder type is updated, state is updated correctly`() = runLocalTest {
        collectUIState()
        val pickedType = ReminderType.entries.first()
        val event = BaseCreateEditReminderScreenEvent.OnPickReminderType(pickedType)
        onBaseEvent(event)
        advanceUntilIdle()
        assertTrue(stateHolder.uiScreenState.value.inputReminderType == event.type)
    }

    @Test
    fun `when day of week is clicked, then state is updated correctly`() = runLocalTest {
        collectUIState()
        resetToDaysOfWeekReminderSchedule()
        val clickedDayOfWeek = DayOfWeek.MONDAY
        pickDayOfWeek(clickedDayOfWeek)
        checkIfReminderScheduleMatches(ReminderSchedule.DaysOfWeek(setOf(clickedDayOfWeek)))
        pickDayOfWeek(clickedDayOfWeek)
        checkIfReminderScheduleMatches(ReminderSchedule.DaysOfWeek(emptySet()))
    }

    private fun TestScope.resetToDaysOfWeekReminderSchedule() {
        val event =
            BaseCreateEditReminderScreenEvent.OnPickReminderScheduleType(ReminderScheduleType.DaysOfWeek)
        onBaseEvent(event)
        advanceUntilIdle()
        checkIfReminderScheduleMatches(ReminderSchedule.DaysOfWeek(emptySet()))
    }

    private fun TestScope.pickDayOfWeek(dayOfWeek: DayOfWeek) {
        onBaseEvent(BaseCreateEditReminderScreenEvent.OnDayOfWeekClick(dayOfWeek))
        advanceUntilIdle()
    }

    private fun checkIfReminderScheduleMatches(targetReminderScheduler: ReminderSchedule) {
        with(stateHolder.uiScreenState.value) {
            assertThat(this.inputReminderSchedule).isEqualTo(targetReminderScheduler)
        }
    }

    protected fun TestScope.collectUIState() {
        this.backgroundScope.launch {
            stateHolder.uiScreenState.launchIn(this)
        }
    }

    protected fun runLocalTest(block: suspend TestScope.() -> Unit) = runTest(testDispatcher) {
        block()
    }

}