import com.ignatlegostaev.inhabitroutine.core.test.factory.SingleTaskFactory
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
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
import org.mockito.kotlin.spy
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking
import org.mockito.kotlin.whenever
import org.mockito.kotlin.wheneverBlocking

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteTaskByIdUseCaseTest {

    private lateinit var useCase: DefaultDeleteTaskByIdUseCase
    private lateinit var spyFakeTaskRepository: FakeTaskRepository
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
        mockRepositoryCallToResultInFailureForTask(targetTaskId)
        useCase.invoke(targetTaskId)
        advanceUntilIdle()
        verifyResetTaskRemindersUseCaseNotCalled()
    }

    private fun verifyResetTaskRemindersUseCaseNotCalled() {
        verifyBlocking(mockResetTaskRemindersUseCase, times(0)) {
            invoke(any())
        }
    }

    private fun mockRepositoryCallToResultInFailureForTask(taskId: String) {
        wheneverBlocking {
            spyFakeTaskRepository.deleteTaskById(taskId)
        }.thenReturn(ResultModel.failure(NoSuchElementException()))
    }

    @Test
    fun `when repository call results in success, then reset task reminders is called`() = runLocalTest {
        val targetTask = buildRandomTask()
        fillRepository(listOf(targetTask))
        mockRepositoryCallToResultInSuccessForTask(targetTask.id)
        useCase.invoke(targetTask.id)
        advanceUntilIdle()
        verifyResetTaskRemindersUseCaseCalledOnceForTask(targetTask.id)
    }

    private fun mockRepositoryCallToResultInSuccessForTask(taskId: String) {
        wheneverBlocking {
            spyFakeTaskRepository.deleteTaskById(taskId)
        }.thenReturn(ResultModel.success(Unit))
    }

    private fun verifyResetTaskRemindersUseCaseCalledOnceForTask(taskId: String) {
        verifyBlocking(mockResetTaskRemindersUseCase) {
            invoke(taskId)
        }
    }

    private fun fillRepository(tasks: List<TaskModel>) {
        spyFakeTaskRepository.setTasks(tasks)
    }

    private fun buildRandomTask(): TaskModel {
        return SingleTaskFactory().build()
    }

    private fun runLocalTest(block: suspend TestScope.() -> Unit) = runTest(testDispatcher) {
        block()
    }

    private fun initDependencies() {
        spyFakeTaskRepository = spy(FakeTaskRepository())
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
            taskRepository = spyFakeTaskRepository,
            resetTaskRemindersUseCase = mockResetTaskRemindersUseCase,
            externalScope = TestScope(testDispatcher)
        )
    }

}






