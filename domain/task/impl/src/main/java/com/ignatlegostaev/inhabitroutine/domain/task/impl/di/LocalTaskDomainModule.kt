package com.ignatlegostaev.inhabitroutine.domain.task.impl.di

import com.ignatlegostaev.inhabitroutine.data.record.api.RecordRepository
import com.ignatlegostaev.inhabitroutine.data.reminder.api.ReminderRepository
import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ResetTaskRemindersUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SetUpTaskRemindersUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ArchiveTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.DeleteTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadHabitsUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTasksByQueryUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTasksUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTasksWithExtrasAndRecordByDateUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ResetTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.SaveTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskDateByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskDescriptionByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskFrequencyByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskProgressByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskTitleByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.calculate_statistics.CalculateTaskStatisticsUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultArchiveTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultCalculateTaskStatisticsUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultDeleteTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultReadHabitsUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultReadTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultReadTasksByQueryUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultReadTasksUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultReadTasksWithExtrasAndRecordByDateUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultResetTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultSaveTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultSaveTaskDraftUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultUpdateTaskDateByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultUpdateTaskDescriptionByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultUpdateTaskFrequencyByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultUpdateTaskProgressByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultUpdateTaskTitleByIdUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

object LocalTaskDomainModule {

    fun provideReadTaskByIdUseCase(
        taskRepository: TaskRepository,
    ): ReadTaskByIdUseCase {
        return DefaultReadTaskByIdUseCase(
            taskRepository = taskRepository
        )
    }

    fun provideReadTasksByQuery(
        taskRepository: TaskRepository,
        defaultDispatcher: CoroutineDispatcher
    ): ReadTasksByQueryUseCase {
        return DefaultReadTasksByQueryUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    fun provideReadTasksWithExtrasAndRecordByDateUseCase(
        taskRepository: TaskRepository,
        reminderRepository: ReminderRepository,
        recordRepository: RecordRepository,
        defaultDispatcher: CoroutineDispatcher
    ): ReadTasksWithExtrasAndRecordByDateUseCase {
        return DefaultReadTasksWithExtrasAndRecordByDateUseCase(
            taskRepository = taskRepository,
            reminderRepository = reminderRepository,
            recordRepository = recordRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    fun provideReadTasksUseCase(
        taskRepository: TaskRepository,
        defaultDispatcher: CoroutineDispatcher
    ): ReadTasksUseCase {
        return DefaultReadTasksUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    fun provideReadHabitsUseCase(
        taskRepository: TaskRepository,
        defaultDispatcher: CoroutineDispatcher
    ): ReadHabitsUseCase {
        return DefaultReadHabitsUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    fun provideSaveTaskDraftUseCase(
        taskRepository: TaskRepository,
        defaultDispatcher: CoroutineDispatcher
    ): SaveTaskDraftUseCase {
        return DefaultSaveTaskDraftUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    fun provideUpdateTaskTitleByIdUseCase(
        taskRepository: TaskRepository
    ): UpdateTaskTitleByIdUseCase {
        return DefaultUpdateTaskTitleByIdUseCase(
            taskRepository = taskRepository
        )
    }

    fun provideUpdateTaskDateByIdUseCase(
        taskRepository: TaskRepository,
        recordRepository: RecordRepository,
        setUpTaskRemindersUseCase: SetUpTaskRemindersUseCase,
        externalScope: CoroutineScope
    ): UpdateTaskDateByIdUseCase {
        return DefaultUpdateTaskDateByIdUseCase(
            taskRepository = taskRepository,
            recordRepository = recordRepository,
            setUpTaskRemindersUseCase = setUpTaskRemindersUseCase,
            externalScope = externalScope
        )
    }

    fun provideUpdateTaskProgressByIdUseCase(
        taskRepository: TaskRepository,
        defaultDispatcher: CoroutineDispatcher
    ): UpdateTaskProgressByIdUseCase {
        return DefaultUpdateTaskProgressByIdUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    fun provideUpdateTaskFrequencyByIdUseCase(
        taskRepository: TaskRepository,
        setUpTaskRemindersUseCase: SetUpTaskRemindersUseCase,
        externalScope: CoroutineScope,
        defaultDispatcher: CoroutineDispatcher
    ): UpdateTaskFrequencyByIdUseCase {
        return DefaultUpdateTaskFrequencyByIdUseCase(
            taskRepository = taskRepository,
            setUpTaskRemindersUseCase = setUpTaskRemindersUseCase,
            externalScope = externalScope,
            defaultDispatcher = defaultDispatcher
        )
    }

    fun provideUpdateTaskDescriptionByIdUseCase(
        taskRepository: TaskRepository
    ): UpdateTaskDescriptionByIdUseCase {
        return DefaultUpdateTaskDescriptionByIdUseCase(
            taskRepository = taskRepository
        )
    }

    fun provideSaveTaskByIdUseCase(
        taskRepository: TaskRepository,
        setUpTaskRemindersUseCase: SetUpTaskRemindersUseCase,
        externalScope: CoroutineScope
    ): SaveTaskByIdUseCase {
        return DefaultSaveTaskByIdUseCase(
            taskRepository = taskRepository,
            setUpTaskRemindersUseCase = setUpTaskRemindersUseCase,
            externalScope = externalScope
        )
    }

    fun provideArchiveTaskByIdUseCase(
        taskRepository: TaskRepository,
        setUpTaskRemindersUseCase: SetUpTaskRemindersUseCase,
        resetTaskRemindersUseCase: ResetTaskRemindersUseCase,
        externalScope: CoroutineScope,
        defaultDispatcher: CoroutineDispatcher
    ): ArchiveTaskByIdUseCase {
        return DefaultArchiveTaskByIdUseCase(
            taskRepository = taskRepository,
            setUpTaskRemindersUseCase = setUpTaskRemindersUseCase,
            resetTaskRemindersUseCase = resetTaskRemindersUseCase,
            externalScope = externalScope,
            defaultDispatcher = defaultDispatcher
        )
    }

    fun provideResetTaskByIdUseCase(
        updateTaskDateByIdUseCase: UpdateTaskDateByIdUseCase,
        recordRepository: RecordRepository,
        externalScope: CoroutineScope
    ): ResetTaskByIdUseCase {
        return DefaultResetTaskByIdUseCase(
            updateTaskDateByIdUseCase = updateTaskDateByIdUseCase,
            recordRepository = recordRepository,
            externalScope = externalScope
        )
    }

    fun provideCalculateTaskStatisticsUseCase(
        taskRepository: TaskRepository,
        recordRepository: RecordRepository,
        defaultDispatcher: CoroutineDispatcher
    ): CalculateTaskStatisticsUseCase {
        return DefaultCalculateTaskStatisticsUseCase(
            taskRepository = taskRepository,
            recordRepository = recordRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    fun provideDeleteTaskByIdUseCase(
        taskRepository: TaskRepository,
        resetTaskRemindersUseCase: ResetTaskRemindersUseCase,
        externalScope: CoroutineScope
    ): DeleteTaskByIdUseCase {
        return DefaultDeleteTaskByIdUseCase(
            taskRepository = taskRepository,
            resetTaskRemindersUseCase = resetTaskRemindersUseCase,
            externalScope = externalScope
        )
    }
}