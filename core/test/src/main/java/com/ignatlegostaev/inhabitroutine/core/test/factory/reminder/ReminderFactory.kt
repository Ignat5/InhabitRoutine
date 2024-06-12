package com.ignatlegostaev.inhabitroutine.core.test.factory.reminder

import com.ignatlegostaev.inhabitroutine.core.test.TestUtil
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.type.ReminderType

class ReminderFactory {

    fun build(): ReminderModel {
        return ReminderModel(
            id = randomUUID(),
            taskId = randomUUID(),
            time = TestUtil.buildRandomLocalTime(),
            type = ReminderType.entries.random(),
            schedule = ReminderSchedule.AlwaysEnabled,
            createdAt = 0L
        )
    }

}