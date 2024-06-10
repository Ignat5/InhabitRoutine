import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.test.factory.SingleTaskFactory
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.data.task.test.FakeTaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SetUpTaskRemindersUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.SaveTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultSaveTaskByIdUseCase
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
import org.mockito.kotlin.wheneverBlocking

@OptIn(ExperimentalCoroutinesApi::class)
class SaveTaskByIdUseCaseTest {

    private lateinit var useCase: SaveTaskByIdUseCase
    private lateinit var fakeTaskRepository: FakeTaskRepository
    private lateinit var testDispatcher: TestDispatcher
    private lateinit var mockSetUpTaskRemindersUseCase: SetUpTaskRemindersUseCase

    @Before
    fun setUp() {
        initDependencies()
        initUseCase()
    }

    @Test
    fun `when task with provided taskId is not present, then use case call results in failure`() =
        runLocalTest {
            val nonExitingTaskId = randomUUID()
            val result = useCase.invoke(nonExitingTaskId)
            advanceUntilIdle()
            assertThat(result).isInstanceOf(ResultModel.Failure::class.java)
        }

    @Test
    fun `when save to repository is failure, then setUpTaskRemindersUseCase is not invoked`() =
        runLocalTest {
            val targetTask = buildRandomTask(isDraft = true)
            fillRepository(listOf(targetTask))
            mockRepositoryCallToResultInFailure()
            useCase.invoke(targetTask.id)
            advanceUntilIdle()
            verifySetUpTaskRemindersUseCaseNotCalled()
        }

    private fun mockRepositoryCallToResultInFailure() {
        fakeTaskRepository.saveTaskIsAutomaticFailure = true
    }

    private fun verifySetUpTaskRemindersUseCaseNotCalled() {
        verifyBlocking(mockSetUpTaskRemindersUseCase, times(0)) {
            invoke(any())
        }
    }

    @Test
    fun `when save to repository is success, then setUpTaskRemindersUseCase is invoked`() =
        runLocalTest {
            val targetTask = buildRandomTask(isDraft = true)
            fillRepository(listOf(targetTask))
            useCase.invoke(targetTask.id)
            advanceUntilIdle()
            verifySetUpTaskRemindersUseCaseCalledOnceForTask(targetTask.id)
        }

    private fun verifySetUpTaskRemindersUseCaseCalledOnceForTask(taskId: String) {
        verifyBlocking(mockSetUpTaskRemindersUseCase) {
            invoke(taskId)
        }
    }

    @Test
    fun `when task is saved, then it is no longer a draft`() = runLocalTest {
        val targetTask = buildRandomTask(isDraft = true)
        fillRepository(listOf(targetTask))
        useCase.invoke(targetTask.id)
        advanceUntilIdle()
        getRepositoryTasks().find { it.id == targetTask.id }.apply {
            assertThat(this?.isDraft).isFalse()
        }
    }

    private fun buildRandomTask(isDraft: Boolean): TaskModel {
        return SingleTaskFactory().build().copy(isDraft = isDraft)
    }

    private fun getRepositoryTasks(): List<TaskModel> {
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
    }

    private fun setMockForSetUpTaskRemindersUseCase() {
        mockSetUpTaskRemindersUseCase = mock {
            onBlocking {
                invoke(any())
            }.thenReturn(Unit)
        }
    }

    private fun initUseCase() {
        useCase = DefaultSaveTaskByIdUseCase(
            taskRepository = fakeTaskRepository,
            setUpTaskRemindersUseCase = mockSetUpTaskRemindersUseCase,
            externalScope = TestScope(testDispatcher)
        )
    }

}