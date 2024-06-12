import com.ignatlegostaev.inhabitroutine.core.test.factory.task.SingleTaskFactory
import com.ignatlegostaev.inhabitroutine.data.task.test.FakeTaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ResetTaskRemindersUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SetUpTaskRemindersUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ArchiveTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultArchiveTaskByIdUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking

@OptIn(ExperimentalCoroutinesApi::class)
class ArchiveTaskByIdTest {

    private lateinit var useCase: DefaultArchiveTaskByIdUseCase
    private lateinit var fakeTaskRepository: FakeTaskRepository
    private lateinit var testDispatcher: TestDispatcher
    private lateinit var mockSetUpTaskRemindersUseCase: SetUpTaskRemindersUseCase
    private lateinit var mockResetTaskRemindersUseCase: ResetTaskRemindersUseCase

    @Before
    fun setUp() {
        initDependencies()
        initUseCase()
    }

    @Test
    fun `when no task with provided taskId is present, then no calls are made`() = runLocalTest {
        fillRepository(emptyList())
        val nonExistingTaskId = randomUUID()
        archiveTask(nonExistingTaskId)
        advanceUntilIdle()
        verifyRepositoryIsEmpty()
        verifyNoCallsToSetUpRemindersWereMade()
        verifyNoCallsToResetRemindersWereMade()
    }

    private fun verifyRepositoryIsEmpty() {
        getTasksFromRepository().apply {
            assertThat(this).isEmpty()
        }
    }

    private fun verifyNoCallsToSetUpRemindersWereMade() {
        verifyBlocking(mockSetUpTaskRemindersUseCase, times(0)) {
            invoke(any())
        }
    }

    private fun verifyNoCallsToResetRemindersWereMade() {
        verifyBlocking(mockResetTaskRemindersUseCase, times(0)) {
            invoke(any())
        }
    }

    @Test
    fun `when unarchive task, then correct call to setUpReminders is invoked`() = runLocalTest {
        val targetTask = buildRandomTask(isArchived = true)
        fillRepository(listOf(targetTask))
        unarchiveTask(targetTask.id)
        advanceUntilIdle()
        verifySetUpTaskRemindersUseCaseCalledOnceForTask(targetTask.id)
    }

    private fun verifySetUpTaskRemindersUseCaseCalledOnceForTask(taskId: String) {
        verifyBlocking(mockSetUpTaskRemindersUseCase) {
            invoke(taskId)
        }
    }

    @Test
    fun `when unarchive task, then correct call to repository is invoked`() = runLocalTest {
        val targetTask = buildRandomTask(isArchived = true)
        fillRepository(listOf(targetTask))
        unarchiveTask(targetTask.id)
        advanceUntilIdle()
        getTasksFromRepository().find { it.id == targetTask.id }.apply {
            assertThat(this?.isArchived).isFalse()
        }
    }

    private suspend fun unarchiveTask(taskId: String) {
        invokeUseCaseForTask(
            taskId = taskId,
            requestType = ArchiveTaskByIdUseCase.RequestType.Unarchive
        )
    }

    @Test
    fun `when archive task, then correct call to resetReminders is invoked`() = runLocalTest {
        val targetTask = buildRandomTask(isArchived = false)
        fillRepository(listOf(targetTask))
        archiveTask(targetTask.id)
        advanceUntilIdle()
        verifyResetTaskRemindersUseCaseCalledOnceForTask(targetTask.id)
    }

    private fun verifyResetTaskRemindersUseCaseCalledOnceForTask(taskId: String) {
        verifyBlocking(mockResetTaskRemindersUseCase) {
            invoke(taskId)
        }
    }

    @Test
    fun `when archive task, then correct call to repository is invoked`() = runLocalTest {
        val targetTask = buildRandomTask(isArchived = false)
        fillRepository(listOf(targetTask))
        archiveTask(targetTask.id)
        advanceUntilIdle()
        getTasksFromRepository().find { it.id == targetTask.id }.apply {
            assertThat(this?.isArchived).isTrue()
        }
    }

    private suspend fun archiveTask(taskId: String) {
        invokeUseCaseForTask(
            taskId = taskId,
            requestType = ArchiveTaskByIdUseCase.RequestType.Archive
        )
    }

    private suspend fun invokeUseCaseForTask(
        taskId: String,
        requestType: ArchiveTaskByIdUseCase.RequestType
    ) {
        useCase.invoke(
            taskId = taskId,
            requestType = requestType
        )
    }

    private fun buildRandomTask(isArchived: Boolean): TaskModel {
        return SingleTaskFactory().build().copy(isArchived = isArchived)
    }

    private fun getTasksFromRepository(): List<TaskModel> {
        return fakeTaskRepository.getTasks()
    }

    private fun fillRepository(tasks: List<TaskModel>) {
        fakeTaskRepository.setTasks(tasks)
    }

    private fun runLocalTest(block: suspend TestScope.() -> Unit) = runTest(testDispatcher) {
        block()
    }

    private fun initDependencies() {
        fakeTaskRepository = FakeTaskRepository()
        testDispatcher = StandardTestDispatcher()
        setMockForSetUpTaskRemindersUseCase()
        setMockForResetTaskRemindersUseCase()
    }

    private fun setMockForSetUpTaskRemindersUseCase() {
        mockSetUpTaskRemindersUseCase = mock<SetUpTaskRemindersUseCase>() {
            onBlocking {
                invoke(any())
            }.thenReturn(Unit)
        }
    }

    private fun setMockForResetTaskRemindersUseCase() {
        mockResetTaskRemindersUseCase = mock<ResetTaskRemindersUseCase>() {
            onBlocking {
                invoke(any())
            }.thenReturn(Unit)
        }
    }

    private fun initUseCase() {
        useCase = DefaultArchiveTaskByIdUseCase(
            taskRepository = fakeTaskRepository,
            setUpTaskRemindersUseCase = mockSetUpTaskRemindersUseCase,
            resetTaskRemindersUseCase = mockResetTaskRemindersUseCase,
            externalScope = TestScope(testDispatcher),
            defaultDispatcher = testDispatcher
        )
    }

}