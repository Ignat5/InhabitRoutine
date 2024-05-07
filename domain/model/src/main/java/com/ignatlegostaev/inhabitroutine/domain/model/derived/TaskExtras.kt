package com.ignatlegostaev.inhabitroutine.domain.model.derived

import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel

sealed interface TaskExtras {
    val allReminders: List<ReminderModel>

    sealed interface Habit : TaskExtras {
        sealed interface HabitContinuous : Habit {
            data class HabitNumber(
                override val allReminders: List<ReminderModel>
            ) : HabitContinuous

            data class HabitTime(
                override val allReminders: List<ReminderModel>
            ) : HabitContinuous
        }

        data class HabitYesNo(
            override val allReminders: List<ReminderModel>
        ) : Habit

    }

    sealed interface Task : TaskExtras {
        data class RecurringTask(
            override val allReminders: List<ReminderModel>
        ) : Task

        data class SingleTask(
            override val allReminders: List<ReminderModel>
        ) : Task
    }

}