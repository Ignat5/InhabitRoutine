import com.ignatlegostaev.inhabitroutine.core.test.factory.SingleTaskFactory
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.data.task.test.FakeTaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ResetTaskRemindersUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultDeleteTaskByIdUseCase
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
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteTaskByIdUseCaseTest {

    private lateinit var useCase: DefaultDeleteTaskByIdUseCase
    private lateinit var fakeTaskRepository: FakeTaskRepository
    private lateinit var testDispatcher: TestDispatcher
    private lateinit var mockResetTaskRemindersUseCase: ResetTaskRemindersUseCase

    @Before
    fun setUp() {
        initDependencies()
        initUseCase()
    }

    @Test
    fun `when repository call results in failure, then reset is not called`() = runLocalTest {
        val targetTaskId = randomUUID()
        mockRepositoryCallToResultInFailure()
        useCase.invoke(targetTaskId)
        advanceUntilIdle()
        verifyResetTaskRemindersUseCaseNotCalled()
    }

    private fun verifyResetTaskRemindersUseCaseNotCalled() {
        verifyBlocking(mockResetTaskRemindersUseCase, times(0)) {
            invoke(any())
        }
    }

    private fun mockRepositoryCallToResultInFailure() {
        fakeTaskRepository.deleteTaskIsAutomaticFailure = true
    }

    @Test
    fun `when repository call results in success, then reset task reminders is called`() = runLocalTest {
        val targetTask = buildRandomTask()
        fillRepository(listOf(targetTask))
        useCase.invoke(targetTask.id)
        advanceUntilIdle()
        verifyResetTaskRemindersUseCaseCalledOnceForTask(targetTask.id)
    }

    private fun verifyResetTaskRemindersUseCaseCalledOnceForTask(taskId: String) {
        verifyBlocking(mockResetTaskRemindersUseCase) {
            invoke(taskId)
        }
    }

    private fun fillRepository(tasks: List<TaskModel>) {
        fakeTaskRepository.setTasks(tasks)
    }

    private fun buildRandomTask(): TaskModel {
        return SingleTaskFactory().build()
    }

    private fun runLocalTest(block: suspend TestScope.() -> Unit) = runTest(testDispatcher) {
        block()
    }

    private fun initDependencies() {
        fakeTaskRepository = FakeTaskRepository()
        testDispatcher = StandardTestDispatcher()
        setUpMockForResetTaskRemindersUseCase()
    }

    private fun setUpMockForResetTaskRemindersUseCase() {
        mockResetTaskRemindersUseCase = mock {
            this.onBlocking {
                this.invoke(any())
            }.thenReturn(Unit)
        }
    }

    private fun initUseCase() {
        useCase = DefaultDeleteTaskByIdUseCase(
            taskRepository = fakeTaskRepository,
            resetTaskRemindersUseCase = mockResetTaskRemindersUseCase,
            externalScope = TestScope(testDispatcher)
        )
    }

}






