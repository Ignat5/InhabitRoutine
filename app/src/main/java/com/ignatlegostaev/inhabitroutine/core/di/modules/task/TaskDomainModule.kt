package com.ignatlegostaev.inhabitroutine.core.di.modules.task

import com.ignatlegostaev.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
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
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskPriorityByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskProgressByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskTitleByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.calculate_statistics.CalculateTaskStatisticsUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.di.LocalTaskDomainModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
object TaskDomainModule {

    @Provides
    fun provideReadTaskByIdUseCase(
        taskRepository: TaskRepository
    ): ReadTaskByIdUseCase {
        return LocalTaskDomainModule.provideReadTaskByIdUseCase(
            taskRepository = taskRepository
        )
    }

    @Provides
    fun provideReadTasksByQueryUseCase(
        taskRepository: TaskRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): ReadTasksByQueryUseCase {
        return LocalTaskDomainModule.provideReadTasksByQuery(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideReadTasksWithExtrasAndRecordByDateUseCase(
        taskRepository: TaskRepository,
        reminderRepository: ReminderRepository,
        recordRepository: RecordRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): ReadTasksWithExtrasAndRecordByDateUseCase {
        return LocalTaskDomainModule.provideReadTasksWithExtrasAndRecordByDateUseCase(
            taskRepository = taskRepository,
            reminderRepository = reminderRepository,
            recordRepository = recordRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideReadTasksUseCase(
        taskRepository: TaskRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): ReadTasksUseCase {
        return LocalTaskDomainModule.provideReadTasksUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideReadHabitsUseCase(
        taskRepository: TaskRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): ReadHabitsUseCase {
        return LocalTaskDomainModule.provideReadHabitsUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideSaveTaskDraftUseCase(
        taskRepository: TaskRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): SaveTaskDraftUseCase {
        return LocalTaskDomainModule.provideSaveTaskDraftUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideUpdateTaskTitleByIdUseCase(
        taskRepository: TaskRepository
    ): UpdateTaskTitleByIdUseCase {
        return LocalTaskDomainModule.provideUpdateTaskTitleByIdUseCase(
            taskRepository = taskRepository
        )
    }

    @Provides
    fun provideUpdateTaskDateByIdUseCase(
        taskRepository: TaskRepository,
        recordRepository: RecordRepository,
        setUpTaskRemindersUseCase: SetUpTaskRemindersUseCase,
        externalScope: CoroutineScope
    ): UpdateTaskDateByIdUseCase {
        return LocalTaskDomainModule.provideUpdateTaskDateByIdUseCase(
            taskRepository = taskRepository,
            recordRepository = recordRepository,
            setUpTaskRemindersUseCase = setUpTaskRemindersUseCase,
            externalScope = externalScope
        )
    }

    @Provides
    fun provideUpdateTaskProgressByIdUseCase(
        taskRepository: TaskRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): UpdateTaskProgressByIdUseCase {
        return LocalTaskDomainModule.provideUpdateTaskProgressByIdUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideUpdateTaskFrequencyByIdUseCase(
        taskRepository: TaskRepository,
        setUpTaskRemindersUseCase: SetUpTaskRemindersUseCase,
        externalScope: CoroutineScope,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): UpdateTaskFrequencyByIdUseCase {
        return LocalTaskDomainModule.provideUpdateTaskFrequencyByIdUseCase(
            taskRepository = taskRepository,
            setUpTaskRemindersUseCase = setUpTaskRemindersUseCase,
            externalScope = externalScope,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideUpdateTaskDescriptionByIdUseCase(
        taskRepository: TaskRepository
    ): UpdateTaskDescriptionByIdUseCase {
        return LocalTaskDomainModule.provideUpdateTaskDescriptionByIdUseCase(
            taskRepository = taskRepository
        )
    }

    @Provides
    fun provideUpdateTaskPriorityByIdUseCase(
        taskRepository: TaskRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): UpdateTaskPriorityByIdUseCase {
        return LocalTaskDomainModule.provideUpdateTaskPriorityByIdUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideSaveTaskByIdUseCase(
        taskRepository: TaskRepository,
        setUpTaskRemindersUseCase: SetUpTaskRemindersUseCase,
        externalScope: CoroutineScope
    ): SaveTaskByIdUseCase {
        return LocalTaskDomainModule.provideSaveTaskByIdUseCase(
            taskRepository = taskRepository,
            setUpTaskRemindersUseCase = setUpTaskRemindersUseCase,
            externalScope = externalScope
        )
    }

    @Provides
    fun provideArchiveTaskByIdUseCase(
        taskRepository: TaskRepository,
        setUpTaskRemindersUseCase: SetUpTaskRemindersUseCase,
        resetTaskRemindersUseCase: ResetTaskRemindersUseCase,
        externalScope: CoroutineScope,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): ArchiveTaskByIdUseCase {
        return LocalTaskDomainModule.provideArchiveTaskByIdUseCase(
            taskRepository = taskRepository,
            setUpTaskRemindersUseCase = setUpTaskRemindersUseCase,
            resetTaskRemindersUseCase = resetTaskRemindersUseCase,
            externalScope = externalScope,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideResetTaskByIdUseCase(
        updateTaskDateByIdUseCase: UpdateTaskDateByIdUseCase,
        recordRepository: RecordRepository,
        externalScope: CoroutineScope
    ): ResetTaskByIdUseCase {
        return LocalTaskDomainModule.provideResetTaskByIdUseCase(
            updateTaskDateByIdUseCase = updateTaskDateByIdUseCase,
            recordRepository = recordRepository,
            externalScope = externalScope
        )
    }

    @Provides
    fun provideCalculateTaskStatisticsUseCase(
        taskRepository: TaskRepository,
        recordRepository: RecordRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): CalculateTaskStatisticsUseCase {
        return LocalTaskDomainModule.provideCalculateTaskStatisticsUseCase(
            taskRepository = taskRepository,
            recordRepository = recordRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideDeleteTaskByIdUseCase(
        taskRepository: TaskRepository,
        resetTaskRemindersUseCase: ResetTaskRemindersUseCase,
        externalScope: CoroutineScope
    ): DeleteTaskByIdUseCase {
        return LocalTaskDomainModule.provideDeleteTaskByIdUseCase(
            taskRepository = taskRepository,
            resetTaskRemindersUseCase = resetTaskRemindersUseCase,
            externalScope = externalScope
        )
    }

}