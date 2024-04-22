package com.example.inhabitroutine.domain.model.derived

import com.example.inhabitroutine.domain.model.reminder.ReminderModel

//data class TaskExtras(
//    val allReminders: List<ReminderModel> = emptyList()
//)


sealed interface TaskExtras {

    interface Reminders {
        val allReminders: List<ReminderModel>
    }

    sealed interface Habit : TaskExtras, Reminders {
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

    sealed interface Task : TaskExtras, Reminders {
        data class RecurringTask(
            override val allReminders: List<ReminderModel>
        ) : Task

        data class SingleTask(
            override val allReminders: List<ReminderModel>
        ) : Task
    }

}

//sealed interface TaskExtras {
////    data class Reminders(
////        val allReminders: List<ReminderModel>
////    ) : TaskExtras
//
//
//    interface Reminders : TaskExtras {
//        val allReminders: List<ReminderModel>
//    }
//
////    data class OnlyReminders(
////        override val allReminders: List<ReminderModel>
////    ) : Reminders
//}