import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.RecurringTaskFactory
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.data.task.test.FakeTaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskFrequency
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SetUpTaskRemindersUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskFrequencyByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultUpdateTaskFrequencyByIdUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DayOfWeek
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verifyBlocking
import org.mockito.kotlin.wheneverBlocking

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateTaskFrequencyByIdUseCaseTest {

    private lateinit var useCase: UpdateTaskFrequencyByIdUseCase
    private lateinit var taskRepository: FakeTaskRepository
    private lateinit var setUpTaskRemindersUseCase: SetUpTaskRemindersUseCase
    private lateinit var testDispatcher: TestDispatcher

    @Rule
    fun setUpRule() = SetUpRule()

    @Test
    fun `when task with provided taskId is not present, then use case results in failure`() =
        runLocalTest {
            val nonExistingTaskId = randomUUID()
            val newFrequency = buildRandomTaskFrequency()
            val result = useCase.invoke(taskId = nonExistingTaskId, taskFrequency = newFrequency)
            advanceUntilIdle()
            assertThat(result).isInstanceOf(ResultModel.Failure::class.java)
        }

    @Test
    fun `when use case is successful, then setUpTaskReminders is invoked`() = runLocalTest {
        val task = buildRandomRecurringTask()
        val newFrequency = buildRandomTaskFrequency()
        fillRepository(listOf(task))
        mockSetUpTaskRemindersToSuccess()
        useCase.invoke(taskId = task.id, taskFrequency = newFrequency)
        advanceUntilIdle()
        verifySetUpTaskReminderUseCaseCalledForTask(task.id)
    }

    private fun verifySetUpTaskReminderUseCaseCalledForTask(taskId: String) {
        verifyBlocking(setUpTaskRemindersUseCase) {
            invoke(taskId)
        }
    }

    @Test
    fun `when use case is successful, then updated task is saved to repository`() =
        runLocalTest {
            val task = buildRandomRecurringTask()
            val newFrequency = buildRandomTaskFrequency()
            fillRepository(listOf(task))
            mockSetUpTaskRemindersToSuccess()
            useCase.invoke(taskId = task.id, taskFrequency = newFrequency)
            advanceUntilIdle()
            verifyTaskWithIdHasFrequency(taskId = task.id, expectedFrequency = newFrequency)
        }

    private fun verifyTaskWithIdHasFrequency(taskId: String, expectedFrequency: TaskFrequency) {
        taskRepository.getTasks().find { it.id == taskId }?.let { taskModel ->
            (taskModel as? TaskModel.RecurringActivity)?.apply {
                assertThat(this.frequency).isEqualTo(expectedFrequency)
            } ?: throw AssertionError()
        } ?: throw AssertionError()
    }

    private fun mockSetUpTaskRemindersToSuccess() {
        wheneverBlocking {
            setUpTaskRemindersUseCase.invoke(any())
        }.thenReturn(Unit)
    }

    private fun fillRepository(tasks: List<TaskModel>) {
        taskRepository.setTasks(tasks)
    }

    private fun buildRandomTaskFrequency(): TaskFrequency {
        return TaskFrequency.DaysOfWeek(setOf(DayOfWeek.entries.random()))
    }

    private fun buildRandomRecurringTask(): TaskModel.Task.RecurringTask {
        return RecurringTaskFactory().build()
    }

    private fun runLocalTest(block: suspend TestScope.() -> Unit) = runTest(testDispatcher) {
        block()
    }

    inner class SetUpRule : TestWatcher() {
        override fun starting(description: Description?) {
            super.starting(description)
            initDependencies()
            initUseCase()
        }

        private fun initDependencies() {
            taskRepository = FakeTaskRepository()
            setUpTaskRemindersUseCase = mock()
            testDispatcher = StandardTestDispatcher()
        }

        private fun initUseCase() {
            useCase = DefaultUpdateTaskFrequencyByIdUseCase(
                taskRepository = taskRepository,
                setUpTaskRemindersUseCase = setUpTaskRemindersUseCase,
                externalScope = TestScope(testDispatcher),
                defaultDispatcher = testDispatcher
            )
        }

    }

}