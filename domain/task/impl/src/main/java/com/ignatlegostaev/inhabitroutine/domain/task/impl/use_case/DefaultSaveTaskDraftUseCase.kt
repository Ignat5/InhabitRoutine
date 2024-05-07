package com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.mapSuccess
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.core.util.todayDate
import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskDate
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskProgress
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskProgressType
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.util.DomainConst
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal class DefaultSaveTaskDraftUseCase(
    private val taskRepository: TaskRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : SaveTaskDraftUseCase {

    override suspend operator fun invoke(requestType: SaveTaskDraftUseCase.RequestType): ResultModel<String, Throwable> =
        withContext(defaultDispatcher) {
            Clock.System.todayDate.let { todayDate ->
                buildTaskModel(
                    requestType = requestType,
                    versionStartDate = todayDate
                ).let { taskModel ->
                    taskRepository.saveTask(taskModel).mapSuccess {
                        taskModel.id
                    }
                }
            }
        }

    private fun buildTaskModel(
        requestType: SaveTaskDraftUseCase.RequestType,
        versionStartDate: LocalDate
    ): TaskModel {
        val taskId = randomUUID()
        val title = DomainConst.DEFAULT_TASK_TITLE
        val description = DomainConst.DEFAULT_TASK_DESCRIPTION
        val isArchived = DomainConst.DEFAULT_TASK_IS_ARCHIVED
        val isDraft = DomainConst.DEFAULT_TASK_IS_DRAFT
        val nowInstant = Clock.System.now()
        val startDate = nowInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val createdAt = nowInstant.toEpochMilliseconds()
        return when (requestType) {
            is SaveTaskDraftUseCase.RequestType.CreateHabit -> {
                val date = TaskDate.Period(
                    startDate = startDate,
                    endDate = null
                )
                val frequency = DomainConst.DEFAULT_TASK_FREQUENCY
                when (requestType.progressType) {
                    TaskProgressType.YesNo -> {
                        TaskModel.Habit.HabitYesNo(
                            id = taskId,
                            title = title,
                            description = description,
                            date = date,
                            frequency = frequency,
                            isArchived = isArchived,
                            versionStartDate = versionStartDate,
                            isDraft = isDraft,
                            createdAt = createdAt
                        )
                    }

                    TaskProgressType.Number -> {
                        TaskModel.Habit.HabitContinuous.HabitNumber(
                            id = taskId,
                            title = title,
                            description = description,
                            date = date,
                            progress = TaskProgress.Number(
                                limitType = DomainConst.DEFAULT_LIMIT_TYPE,
                                limitNumber = DomainConst.DEFAULT_LIMIT_NUMBER,
                                limitUnit = DomainConst.DEFAULT_LIMIT_UNIT
                            ),
                            frequency = frequency,
                            isArchived = isArchived,
                            versionStartDate = versionStartDate,
                            isDraft = isDraft,
                            createdAt = createdAt
                        )
                    }

                    TaskProgressType.Time -> {
                        TaskModel.Habit.HabitContinuous.HabitTime(
                            id = taskId,
                            title = title,
                            description = description,
                            date = date,
                            progress = TaskProgress.Time(
                                limitType = DomainConst.DEFAULT_LIMIT_TYPE,
                                limitTime = DomainConst.DEFAULT_LIMIT_TIME
                            ),
                            frequency = frequency,
                            isArchived = isArchived,
                            versionStartDate = versionStartDate,
                            isDraft = isDraft,
                            createdAt = createdAt
                        )
                    }
                }
            }

            is SaveTaskDraftUseCase.RequestType.CreateRecurringTask -> {
                TaskModel.Task.RecurringTask(
                    id = taskId,
                    title = title,
                    description = description,
                    date = TaskDate.Period(
                        startDate = startDate,
                        endDate = null
                    ),
                    frequency = DomainConst.DEFAULT_TASK_FREQUENCY,
                    isArchived = isArchived,
                    versionStartDate = versionStartDate,
                    isDraft = isDraft,
                    createdAt = createdAt
                )
            }

            is SaveTaskDraftUseCase.RequestType.CreateSingleTask -> {
                TaskModel.Task.SingleTask(
                    id = taskId,
                    title = title,
                    description = description,
                    date = TaskDate.Day(startDate),
                    isArchived = isArchived,
                    versionStartDate = versionStartDate,
                    isDraft = isDraft,
                    createdAt = createdAt
                )
            }
        }
    }

}