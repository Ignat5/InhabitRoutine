package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create

import com.ignatlegostaev.inhabitroutine.core.test.factory.task.SingleTaskFactory
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.DeleteTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.SaveTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskDateByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskDescriptionByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskFrequencyByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskPriorityByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskProgressByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskTitleByIdUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.other.VerifyCanSaveTaskUseCase
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.fakes.FakeReadReminderCountByTaskIdUseCase
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.fakes.FakeReadTaskByIdUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class CreateTaskViewModelTest {

    private lateinit var viewModel: CreateTaskViewModel
    private lateinit var taskId: String
    private lateinit var fakeReadTaskByIdUseCase: FakeReadTaskByIdUseCase
    private lateinit var fakeReadReminderCountByTaskIdUseCase: FakeReadReminderCountByTaskIdUseCase
    private lateinit var testDispatcher: CoroutineDispatcher

    @Mock
    private lateinit var mockedSaveTaskByIdUseCase: SaveTaskByIdUseCase

    @Mock
    private lateinit var mockedDeleteTaskByIdUseCase: DeleteTaskByIdUseCase

    @Mock
    private lateinit var mockedUpdateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase

    @Mock
    private lateinit var mockedUpdateTaskProgressByIdUseCase: UpdateTaskProgressByIdUseCase

    @Mock
    private lateinit var mockedUpdateTaskFrequencyByIdUseCase: UpdateTaskFrequencyByIdUseCase

    @Mock
    private lateinit var mockedUpdateTaskDateByIdUseCase: UpdateTaskDateByIdUseCase

    @Mock
    private lateinit var mockedUpdateTaskDescriptionByIdUseCase: UpdateTaskDescriptionByIdUseCase

    @Mock
    private lateinit var mockedUpdateTaskPriorityByIdUseCase: UpdateTaskPriorityByIdUseCase

    @Mock
    private lateinit var mockedVerifyCanSaveTaskUseCase: VerifyCanSaveTaskUseCase

    @Before
    fun setUp() {
        initDependencies()
        initViewModel()
    }

    @Test
    fun `when save task event is sent, appropriate use case is invoked`() = runLocalTest {
        val targetTask = SingleTaskFactory().build().copy(id = taskId)
        fakeReadTaskByIdUseCase.taskState.update { targetTask }
        whenever(mockedVerifyCanSaveTaskUseCase.invoke(targetTask))
            .thenReturn(true)
        collectUIState()
        advanceUntilIdle()
        viewModel.onEvent(CreateTaskScreenEvent.OnSaveClick)
        advanceUntilIdle()
        verify(mockedSaveTaskByIdUseCase).invoke(targetTask.id)
    }

    private fun runLocalTest(block: suspend TestScope.() -> Unit) = runTest(testDispatcher) {
        block()
    }

    private fun TestScope.collectUIState() {
        this.backgroundScope.launch {
            viewModel.uiScreenState.launchIn(this)
        }
    }

    private fun initDependencies() {
        taskId = randomUUID()
        fakeReadTaskByIdUseCase = FakeReadTaskByIdUseCase()
        fakeReadReminderCountByTaskIdUseCase = FakeReadReminderCountByTaskIdUseCase()
        testDispatcher = StandardTestDispatcher()
    }

    private fun initViewModel() {
        viewModel = CreateTaskViewModel(
            taskId = taskId,
            readTaskByIdUseCase = fakeReadTaskByIdUseCase,
            readReminderCountByTaskIdUseCase = fakeReadReminderCountByTaskIdUseCase,
            saveTaskByIdUseCase = mockedSaveTaskByIdUseCase,
            deleteTaskByIdUseCase = mockedDeleteTaskByIdUseCase,
            updateTaskTitleByIdUseCase = mockedUpdateTaskTitleByIdUseCase,
            updateTaskProgressByIdUseCase = mockedUpdateTaskProgressByIdUseCase,
            updateTaskFrequencyByIdUseCase = mockedUpdateTaskFrequencyByIdUseCase,
            updateTaskDateByIdUseCase = mockedUpdateTaskDateByIdUseCase,
            updateTaskDescriptionByIdUseCase = mockedUpdateTaskDescriptionByIdUseCase,
            updateTaskPriorityByIdUseCase = mockedUpdateTaskPriorityByIdUseCase,
            defaultDispatcher = testDispatcher,
            viewModelScope = TestScope(testDispatcher),
            verifyCanSaveTaskUseCase = mockedVerifyCanSaveTaskUseCase
        )
    }

}