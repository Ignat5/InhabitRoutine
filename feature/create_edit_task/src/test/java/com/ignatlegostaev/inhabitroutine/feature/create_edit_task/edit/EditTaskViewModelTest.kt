package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit

import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.archive_task.components.ArchiveTaskScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.delete_task.components.DeleteTaskScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.reset_task.components.ResetTaskScreenEvent
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.reset_task.components.ResetTaskScreenResult
import com.ignatlegostaev.inhabitroutine.core.test.factory.HabitYesNoFactory
import com.ignatlegostaev.inhabitroutine.core.test.factory.SingleTaskFactory
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ArchiveTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.DeleteTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ResetTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskDateByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskDescriptionByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskFrequencyByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskPriorityByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskProgressByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskTitleByIdUseCase
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.model.ItemTaskAction
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.fakes.FakeReadReminderCountByTaskIdUseCase
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.fakes.FakeReadTaskByIdUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class EditTaskViewModelTest {

    private lateinit var viewModel: EditTaskViewModel
    private lateinit var fakeReadTaskByIdUseCase: FakeReadTaskByIdUseCase
    private lateinit var fakeReadReminderCountByTaskIdUseCase: FakeReadReminderCountByTaskIdUseCase
    private lateinit var taskId: String
    private lateinit var testDispatcher: TestDispatcher

    @Mock
    private lateinit var archiveTaskByIdUseCase: ArchiveTaskByIdUseCase

    @Mock
    private lateinit var deleteTaskByIdUseCase: DeleteTaskByIdUseCase

    @Mock
    private lateinit var resetTaskByIdUseCase: ResetTaskByIdUseCase

    @Mock
    private lateinit var updateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase

    @Mock
    private lateinit var updateTaskProgressByIdUseCase: UpdateTaskProgressByIdUseCase

    @Mock
    private lateinit var updateTaskFrequencyByIdUseCase: UpdateTaskFrequencyByIdUseCase

    @Mock
    private lateinit var updateTaskDateByIdUseCase: UpdateTaskDateByIdUseCase

    @Mock
    private lateinit var updateTaskDescriptionByIdUseCase: UpdateTaskDescriptionByIdUseCase

    @Mock
    private lateinit var updateTaskPriorityByIdUseCase: UpdateTaskPriorityByIdUseCase

    @Before
    fun setUp() {
        initDependencies()
        initViewModel()
    }

    @Test
    fun `when task is unarchived, then archive action is provided`() = runLocalTest {
        val targetTask = buildSingleTask().copy(isArchived = false)
        fakeReadTaskByIdUseCase.taskState.update { targetTask }
        collectUIState()
        advanceUntilIdle()
        assertThat(viewModel.uiScreenState.value.allTaskActionItems).contains(
            ItemTaskAction.ArchiveUnarchive.Archive
        )
    }

    @Test
    fun `when task is archived, then unarchive action is provided`() = runLocalTest {
        val targetTask = buildSingleTask().copy(isArchived = true)
        fakeReadTaskByIdUseCase.taskState.update { targetTask }
        collectUIState()
        advanceUntilIdle()
        assertThat(viewModel.uiScreenState.value.allTaskActionItems).contains(
            ItemTaskAction.ArchiveUnarchive.Unarchive
        )
    }

    @Test
    fun `when task is not habit, then habit-specific actions are not provided`() = runLocalTest {
        val targetTask = buildSingleTask()
        fakeReadTaskByIdUseCase.taskState.update { targetTask }
        collectUIState()
        advanceUntilIdle()
        assertThat(viewModel.uiScreenState.value.allTaskActionItems).containsNoneIn(
            getHabitSpecificActions()
        )
    }

    @Test
    fun `when task is habit, then habit-specific actions are provided`() = runLocalTest {
        val targetTask = buildHabitYesNo()
        fakeReadTaskByIdUseCase.taskState.update { targetTask }
        collectUIState()
        advanceUntilIdle()
        assertThat(viewModel.uiScreenState.value.allTaskActionItems).containsAtLeastElementsIn(
            getHabitSpecificActions()
        )
    }

    private fun getHabitSpecificActions() = setOf(
        ItemTaskAction.ViewStatistics,
        ItemTaskAction.Reset,
    )

    @Test
    fun `when delete action is confirmed, then appropriate use case is invoked`() =
        runLocalTest {
            val targetTask = buildSingleTask()
            fakeReadTaskByIdUseCase.taskState.update { targetTask }
            whenever(deleteTaskByIdUseCase(targetTask.id))
                .thenReturn(ResultModel.success(Unit))
            collectUIState()
            advanceUntilIdle()
            viewModel.onEvent(
                EditTaskScreenEvent.ResultEvent.DeleteTask(
                    DeleteTaskScreenResult.Confirm(targetTask.id)
                )
            )
            advanceUntilIdle()
            verify(deleteTaskByIdUseCase).invoke(targetTask.id)
        }

    @Test
    fun `when reset action is confirmed, then appropriate use case is invoked`() = runLocalTest {
        val targetTask = buildHabitYesNo()
        fakeReadTaskByIdUseCase.taskState.update { targetTask }
        whenever(
            resetTaskByIdUseCase(targetTask.id)
        ).thenReturn(ResultModel.success(Unit))
        collectUIState()
        advanceUntilIdle()
        viewModel.onEvent(
            EditTaskScreenEvent.ResultEvent.ResetTask(
                ResetTaskScreenResult.Confirm(targetTask.id)
            )
        )
        advanceUntilIdle()
        verify(resetTaskByIdUseCase).invoke(targetTask.id)
    }

    @Test
    fun `when unarchive action is confirmed, then appropriate use case is invoked`() =
        runLocalTest {
            val targetTask = buildSingleTask()
            fakeReadTaskByIdUseCase.taskState.update { targetTask }
            whenever(
                archiveTaskByIdUseCase(
                    taskId = targetTask.id,
                    requestType = ArchiveTaskByIdUseCase.RequestType.Unarchive
                )
            ).thenReturn(ResultModel.success(Unit))
            collectUIState()
            advanceUntilIdle()
            viewModel.onEvent(
                EditTaskScreenEvent.OnItemActionClick(ItemTaskAction.ArchiveUnarchive.Unarchive)
            )
            advanceUntilIdle()
            verify(archiveTaskByIdUseCase).invoke(
                taskId = targetTask.id,
                requestType = ArchiveTaskByIdUseCase.RequestType.Unarchive
            )
        }

    @Test
    fun `when archive action is confirmed, then appropriate use case is invoked`() =
        runLocalTest {
            val targetTask = buildSingleTask()
            fakeReadTaskByIdUseCase.taskState.update { targetTask }
            whenever(
                archiveTaskByIdUseCase(
                    taskId = targetTask.id,
                    requestType = ArchiveTaskByIdUseCase.RequestType.Archive
                )
            ).thenReturn(ResultModel.success(Unit))
            collectUIState()
            advanceUntilIdle()
            viewModel.onEvent(
                EditTaskScreenEvent.ResultEvent.ArchiveTask(
                    ArchiveTaskScreenResult.Confirm(targetTask.id)
                )
            )
            advanceUntilIdle()
            verify(archiveTaskByIdUseCase).invoke(
                taskId = targetTask.id,
                requestType = ArchiveTaskByIdUseCase.RequestType.Archive
            )
        }

    private fun buildSingleTask(): TaskModel.Task.SingleTask {
        return SingleTaskFactory().build().copy(id = taskId)
    }

    private fun buildHabitYesNo(): TaskModel.Habit.HabitYesNo {
        return HabitYesNoFactory().build().copy(id = taskId)
    }

    private fun TestScope.collectUIState() {
        this.backgroundScope.launch {
            viewModel.uiScreenState.launchIn(this)
        }
    }

    private fun runLocalTest(block: suspend TestScope.() -> Unit) = runTest(testDispatcher) {
        block()
    }

    private fun initDependencies() {
        taskId = randomUUID()
        fakeReadTaskByIdUseCase = FakeReadTaskByIdUseCase()
        fakeReadReminderCountByTaskIdUseCase = FakeReadReminderCountByTaskIdUseCase()
        testDispatcher = StandardTestDispatcher()
    }

    private fun initViewModel() {
        viewModel = EditTaskViewModel(
            taskId = taskId,
            readTaskByIdUseCase = fakeReadTaskByIdUseCase,
            readReminderCountByTaskIdUseCase = fakeReadReminderCountByTaskIdUseCase,
            archiveTaskByIdUseCase = archiveTaskByIdUseCase,
            deleteTaskByIdUseCase = deleteTaskByIdUseCase,
            resetTaskByIdUseCase = resetTaskByIdUseCase,
            updateTaskTitleByIdUseCase = updateTaskTitleByIdUseCase,
            updateTaskProgressByIdUseCase = updateTaskProgressByIdUseCase,
            updateTaskFrequencyByIdUseCase = updateTaskFrequencyByIdUseCase,
            updateTaskDateByIdUseCase = updateTaskDateByIdUseCase,
            updateTaskDescriptionByIdUseCase = updateTaskDescriptionByIdUseCase,
            updateTaskPriorityByIdUseCase = updateTaskPriorityByIdUseCase,
            defaultDispatcher = testDispatcher,
            viewModelScope = TestScope(testDispatcher)
        )
    }

}