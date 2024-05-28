package com.ignatlegostaev.inhabitroutine.feature.view_tasks

import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.archive_task.components.ArchiveTaskScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.delete_task.components.DeleteTaskScreenResult
import com.ignatlegostaev.inhabitroutine.core.test.SingleTaskFactory
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ArchiveTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.DeleteTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTasksUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.components.ViewTasksScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.config.view_task_actions.components.ViewTaskActionsScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.model.TaskFilterByStatus
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.model.TaskFilterByType
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.model.TaskSort
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.use_case.filter_tasks_by_status.FilterTasksByStatusUseCase
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.use_case.filter_tasks_by_type.FilterTasksByTypeUseCase
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.use_case.sort_tasks.SortTasksUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.not
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ViewTasksViewModelTest {
    private lateinit var viewModel: ViewTasksViewModel
    private lateinit var fakeReadTasksUseCase: FakeReadTasksUseCase
    private lateinit var mockedArchiveTaskByIdUseCase: ArchiveTaskByIdUseCase
    private lateinit var mockedDeleteTaskByIdUseCase: DeleteTaskByIdUseCase
    private lateinit var mockedSaveTaskDraftUseCase: SaveTaskDraftUseCase
    private lateinit var mockedFilterTasksByStatusUseCase: FilterTasksByStatusUseCase
    private lateinit var mockedFilterTasksByTypeUseCase: FilterTasksByTypeUseCase
    private lateinit var mockedSortTasksUseCase: SortTasksUseCase
    private lateinit var testDispatcher: TestDispatcher

    @Before
    fun setUp() {
        fakeReadTasksUseCase = FakeReadTasksUseCase()
        mockedArchiveTaskByIdUseCase = mock<ArchiveTaskByIdUseCase>()
        mockedDeleteTaskByIdUseCase = mock<DeleteTaskByIdUseCase>()
        mockedSaveTaskDraftUseCase = mock<SaveTaskDraftUseCase>()
        mockedFilterTasksByStatusUseCase = mock<FilterTasksByStatusUseCase>()
        mockedFilterTasksByTypeUseCase = mock<FilterTasksByTypeUseCase>()
        mockedSortTasksUseCase = mock<SortTasksUseCase>()
        testDispatcher = StandardTestDispatcher()
        viewModel = ViewTasksViewModel(
            readTasksUseCase = fakeReadTasksUseCase,
            saveTaskDraftUseCase = mockedSaveTaskDraftUseCase,
            archiveTaskByIdUseCase = mockedArchiveTaskByIdUseCase,
            deleteTaskByIdUseCase = mockedDeleteTaskByIdUseCase,
            defaultDispatcher = testDispatcher,
            viewModelScope = TestScope(testDispatcher),
            filterTasksByStatusUseCase = mockedFilterTasksByStatusUseCase,
            filterTasksByTypeUseCase = mockedFilterTasksByTypeUseCase,
            sortTasksUseCase = mockedSortTasksUseCase
        )
    }

    @Test
    fun `when filters are active, then state's data is filtered`() = runLocalTest {
        val matchFilterTask = getSingleDummyTask()
        val notMatchFilterTask = getSingleDummyTask()
        whenever(mockedFilterTasksByStatusUseCase.invoke(any(), any()))
            .thenReturn(listOf(matchFilterTask))
        whenever(mockedFilterTasksByTypeUseCase.invoke(any(), any()))
            .thenReturn(listOf(matchFilterTask))
        whenever(mockedSortTasksUseCase.invoke(any(), any()))
            .thenReturn(listOf(matchFilterTask))

        fillFakeDataSourceWithTasks(matchFilterTask, notMatchFilterTask)
        collectUIScreenState()
        advanceUntilIdle()
        viewModel.uiScreenState.value.allTasksResult.data?.let { data ->
            assertTrue(data.contains(matchFilterTask))
            assertTrue(!data.contains(notMatchFilterTask))
        } ?: throw AssertionError()
    }

    @Test
    fun `when no filters are picked, then no filters are applied`() = runLocalTest {
        fakeReadTasksUseCase.allTasksState.update { getDummyTasks(3) }
        collectUIScreenState()
        advanceUntilIdle()
        with(viewModel.uiScreenState.value) {
            assertTrue(this.filterByStatus == null)
            assertTrue(this.filterByType == null)
            verify(mockedFilterTasksByStatusUseCase, never()).invoke(any(), any())
            verify(mockedFilterTasksByTypeUseCase, never()).invoke(any(), any())
        }
    }

    @Test
    fun `when there are tasks, then sort is always applied`() = runLocalTest {
        fakeReadTasksUseCase.allTasksState.update { getDummyTasks(3) }
        whenever(mockedSortTasksUseCase(any(), any()))
            .thenReturn(emptyList())
        collectUIScreenState()
        advanceUntilIdle()
        verify(mockedSortTasksUseCase).invoke(any(), any())
    }

    @Test
    fun `when filter by status is picked, then filter is applied`() = runLocalTest {
        setUpDefaultFilterSortMocks()
        fillFakeDataSourceWithTasks()
        collectUIScreenState()
        TaskFilterByStatus.entries.forEach { taskFilterByStatus ->
            viewModel.onEvent(ViewTasksScreenEvent.OnPickFilterByStatus(taskFilterByStatus))
            advanceUntilIdle()
            assertTrue(viewModel.uiScreenState.value.filterByStatus == taskFilterByStatus)
        }
        verify(
            mockedFilterTasksByStatusUseCase,
            times(TaskFilterByStatus.entries.size)
        ).invoke(any(), any())
    }

    @Test
    fun `when filter by type is picked, then filter is applied`() = runLocalTest {
        setUpDefaultFilterSortMocks()
        fillFakeDataSourceWithTasks()
        collectUIScreenState()
        TaskFilterByType.entries.forEach { taskFilterByType ->
            viewModel.onEvent(ViewTasksScreenEvent.OnPickFilterByType(taskFilterByType))
            advanceUntilIdle()
            assertTrue(viewModel.uiScreenState.value.filterByType == taskFilterByType)
        }
        verify(mockedFilterTasksByTypeUseCase, times(TaskFilterByType.entries.size)).invoke(
            any(),
            any()
        )
    }

    @Test
    fun `when sort is picked, then sort is applied`() = runLocalTest {
        setUpDefaultFilterSortMocks()
        fillFakeDataSourceWithTasks()
        collectUIScreenState()
        TaskSort.entries.forEach { taskSort ->
            viewModel.onEvent(ViewTasksScreenEvent.OnPickSort(taskSort))
            advanceUntilIdle()
            assertTrue(viewModel.uiScreenState.value.sort == taskSort)
        }
        verify(mockedSortTasksUseCase, times(TaskSort.entries.size)).invoke(any(), any())
    }

    @Test
    fun `when delete task action is confirmed, then use case is invoked`() = runLocalTest {
        val targetTaskId = randomUUID()
        whenever(mockedDeleteTaskByIdUseCase(targetTaskId))
            .thenReturn(ResultModel.success(Unit))
        viewModel.onEvent(
            ViewTasksScreenEvent.ResultEvent.DeleteTask(
                result = DeleteTaskScreenResult.Confirm(targetTaskId)
            )
        )
        advanceUntilIdle()
        verify(mockedDeleteTaskByIdUseCase).invoke(targetTaskId)
    }

    @Test
    fun `when archive task action is confirmed, then use case is invoked`() = runLocalTest {
        val targetTaskId = randomUUID()
        whenever(
            mockedArchiveTaskByIdUseCase(
                taskId = targetTaskId,
                requestType = ArchiveTaskByIdUseCase.RequestType.Archive
            )
        ).thenReturn(ResultModel.success(Unit))

        viewModel.onEvent(
            ViewTasksScreenEvent.ResultEvent.ArchiveTask(
                ArchiveTaskScreenResult.Confirm(targetTaskId)
            )
        )
        advanceUntilIdle()
        verify(mockedArchiveTaskByIdUseCase).invoke(
            taskId = targetTaskId,
            requestType = ArchiveTaskByIdUseCase.RequestType.Archive
        )
    }

    @Test
    fun `when unarchive task action is confirmed, then use case is invoked`() = runLocalTest {
        val targetTaskId = randomUUID()
        whenever(
            mockedArchiveTaskByIdUseCase(
                taskId = targetTaskId,
                requestType = ArchiveTaskByIdUseCase.RequestType.Unarchive
            )
        ).thenReturn(ResultModel.success(Unit))
        viewModel.onEvent(
            ViewTasksScreenEvent.ResultEvent.ViewTaskActions(
                result = ViewTaskActionsScreenResult.Action.Unarchive(
                    taskId = targetTaskId
                )
            )
        )
        advanceUntilIdle()
        verify(mockedArchiveTaskByIdUseCase).invoke(
            taskId = targetTaskId,
            requestType = ArchiveTaskByIdUseCase.RequestType.Unarchive
        )
    }

    private fun getSingleDummyTask() = getDummyTasks(count = 1).first()

    private fun getDummyTasks(count: Int): List<TaskModel.Task> {
        val singleTaskFactory = SingleTaskFactory()
        val result = mutableListOf<TaskModel.Task>()
        repeat(count) {
            result.add(singleTaskFactory.build())
        }
        return result
    }

    private fun runLocalTest(block: suspend TestScope.() -> Unit) = runTest(testDispatcher) {
        block()
    }

    private fun fillFakeDataSourceWithTasks(count: Int = 3) {
        fakeReadTasksUseCase.allTasksState.update { getDummyTasks(count) }
    }

    private fun fillFakeDataSourceWithTasks(vararg allTasks: TaskModel.Task) {
        fakeReadTasksUseCase.allTasksState.update { allTasks.toList() }
    }

    private fun setUpDefaultFilterSortMocks() {
        whenever(mockedFilterTasksByStatusUseCase.invoke(any(), any()))
            .thenReturn(emptyList())
        whenever(mockedFilterTasksByTypeUseCase.invoke(any(), any()))
            .thenReturn(emptyList())
        whenever(mockedSortTasksUseCase.invoke(any(), any()))
            .thenReturn(emptyList())
    }

    private fun TestScope.collectUIScreenState() =
        this.backgroundScope.launch {
            viewModel.uiScreenState.launchIn(this)
        }

    private class FakeReadTasksUseCase : ReadTasksUseCase {
        val allTasksState = MutableStateFlow<List<TaskModel.Task>>(emptyList())

        override fun invoke(): Flow<List<TaskModel.Task>> {
            return allTasksState
        }
    }

}