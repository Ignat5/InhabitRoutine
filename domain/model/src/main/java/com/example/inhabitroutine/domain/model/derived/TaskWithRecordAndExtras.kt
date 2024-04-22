package com.example.inhabitroutine.domain.model.derived

//data class TaskWithRecordAndExtras(
//    val taskWithRecordModel: TaskWithRecordModel,
//    val allExtras: Set<TaskExtras>
//)

sealed interface TaskWithRecordAndExtras {
    val taskWithRecordModel: TaskWithRecordModel
    val extras: TaskExtras

    data class TaskWithRecordAndReminders(
        override val taskWithRecordModel: TaskWithRecordModel,
        override val extras: TaskExtras.Reminders
    ) : TaskWithRecordAndExtras
}