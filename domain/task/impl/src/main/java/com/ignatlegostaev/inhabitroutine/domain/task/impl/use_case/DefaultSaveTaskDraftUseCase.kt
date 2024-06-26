package com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.mapSuccess
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.core.util.todayDate
import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskDate
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskFrequency
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskProgress
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.ProgressLimitType
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskProgressType
import com.ignatlegostaev.inhabitroutine.domain.model.util.DomainConst
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
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
        val title = DEFAULT_TASK_TITLE
        val description = DEFAULT_TASK_DESCRIPTION
        val priority = DomainConst.DEFAULT_TASK_PRIORITY
        val isArchived = DEFAULT_TASK_IS_ARCHIVED
        val isDraft = DEFAULT_TASK_IS_DRAFT
        val nowInstant = Clock.System.now()
        val startDate = nowInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val createdAt = nowInstant.toEpochMilliseconds()
        return when (requestType) {
            is SaveTaskDraftUseCase.RequestType.CreateHabit -> {
                val date = TaskDate.Period(
                    startDate = startDate,
                    endDate = null
                )
                val frequency = DEFAULT_TASK_FREQUENCY
                when (requestType.progressType) {
                    TaskProgressType.YesNo -> {
                        TaskModel.Habit.HabitYesNo(
                            id = taskId,
                            title = title,
                            description = description,
                            date = date,
                            priority = priority,
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
                            priority = priority,
                            progress = TaskProgress.Number(
                                limitType = DEFAULT_LIMIT_TYPE,
                                limitNumber = DEFAULT_LIMIT_NUMBER,
                                limitUnit = DEFAULT_LIMIT_UNIT
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
                            priority = priority,
                            progress = TaskProgress.Time(
                                limitType = DEFAULT_LIMIT_TYPE,
                                limitTime = DEFAULT_LIMIT_TIME
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
                    priority = priority,
                    frequency = DEFAULT_TASK_FREQUENCY,
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
                    priority = priority,
                    isArchived = isArchived,
                    versionStartDate = versionStartDate,
                    isDraft = isDraft,
                    createdAt = createdAt
                )
            }
        }
    }

    companion object {
        const val DEFAULT_TASK_TITLE = ""
        const val DEFAULT_TASK_DESCRIPTION = ""
        const val DEFAULT_TASK_IS_ARCHIVED = false
        const val DEFAULT_TASK_IS_DRAFT = true
        const val DEFAULT_LIMIT_NUMBER: Double = 0.0
        val DEFAULT_LIMIT_TYPE get() = ProgressLimitType.AtLeast
        const val DEFAULT_LIMIT_UNIT = ""
        val DEFAULT_LIMIT_TIME get() = LocalTime(hour = 0, minute = 0, second = 0)
        val DEFAULT_TASK_FREQUENCY get() = TaskFrequency.EveryDay
    }

}