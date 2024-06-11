package com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder

import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.components.BaseCreateEditReminderScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.CreateReminderStateHolder
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.components.CreateReminderScreenEvent
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import org.junit.Before

class CreateReminderStateHolderTest : BaseCreateEditReminderStateHolderTest() {

    override lateinit var stateHolder: CreateReminderStateHolder
    override lateinit var testDispatcher: TestDispatcher

    @Before
    fun setUp() {
        testDispatcher = StandardTestDispatcher()
        stateHolder = CreateReminderStateHolder(
            holderScope = TestScope(testDispatcher)
        )
    }

    override fun onBaseEvent(event: BaseCreateEditReminderScreenEvent) {
        stateHolder.onEvent(CreateReminderScreenEvent.Base(event))
    }

}