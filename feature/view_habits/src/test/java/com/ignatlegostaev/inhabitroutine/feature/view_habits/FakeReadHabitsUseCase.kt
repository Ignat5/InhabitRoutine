package com.ignatlegostaev.inhabitroutine.feature.view_habits

import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadHabitsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

class FakeReadHabitsUseCase(
    private val dataSource: FakeHabitsDataSource
) : ReadHabitsUseCase {

    override fun invoke(): Flow<List<TaskModel.Habit>> = dataSource.allHabitsState

}