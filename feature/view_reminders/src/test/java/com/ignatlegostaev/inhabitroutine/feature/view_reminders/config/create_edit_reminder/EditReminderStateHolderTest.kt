package com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder

import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.type.ReminderType
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.components.BaseCreateEditReminderScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit.EditReminderStateHolder
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit.components.EditReminderScreenEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.datetime.LocalTime
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditReminderStateHolderTest : BaseCreateEditReminderStateHolderTest() {

    override lateinit var stateHolder: EditReminderStateHolder
    override lateinit var testDispatcher: TestDispatcher
    private val initReminderModel = ReminderModel(
        id = randomUUID(),
        taskId = randomUUID(),
        time = LocalTime(hour = 0, minute = 0),
        type = ReminderType.entries.first(),
        schedule = ReminderSchedule.AlwaysEnabled,
        createdAt = 0L
    )

    @Before
    fun setUp() {
        testDispatcher = StandardTestDispatcher()
        stateHolder = EditReminderStateHolder(
            reminderModel = initReminderModel,
            holderScope = TestScope(testDispatcher)
        )
    }

    @Test
    fun `when state holder is initialised, then state matches init reminder`() = runLocalTest {
        collectUIState()
        advanceUntilIdle()
        with(stateHolder.uiScreenState.value) {
            assertThat(this.inputHours).isEqualTo(initReminderModel.time.hour)
            assertThat(this.inputMinutes).isEqualTo(initReminderModel.time.minute)
            assertThat(this.inputReminderType).isEqualTo(initReminderModel.type)
            assertThat(this.inputReminderSchedule).isEqualTo(initReminderModel.schedule)
        }
    }

    override fun onBaseEvent(event: BaseCreateEditReminderScreenEvent) {
        stateHolder.onEvent(EditReminderScreenEvent.Base(event))
    }

}