package com.example.inhabitroutine.domain.model.derived

import com.example.inhabitroutine.domain.model.reminder.ReminderModel

sealed interface TaskExtras {
//    data class Reminders(
//        val allReminders: List<ReminderModel>
//    ) : TaskExtras


    interface Reminders : TaskExtras {
        val allReminders: List<ReminderModel>
    }

//    data class OnlyReminders(
//        override val allReminders: List<ReminderModel>
//    ) : Reminders
}