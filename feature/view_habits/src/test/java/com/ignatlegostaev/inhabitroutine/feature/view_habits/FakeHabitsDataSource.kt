package com.ignatlegostaev.inhabitroutine.feature.view_habits

import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeHabitsDataSource {
    val allHabitsState = MutableStateFlow<List<TaskModel.Habit>>(emptyList())
}