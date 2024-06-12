import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.SingleTaskFactory
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.task.test.FakeTaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.util.DomainConst
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskPriorityByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultUpdateTaskPriorityByIdUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.random.Random
import kotlin.random.nextLong

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateTaskPriorityByIdUseCaseTest {

    private lateinit var useCase: UpdateTaskPriorityByIdUseCase
    private lateinit var taskRepository: FakeTaskRepository
    private lateinit var testDispatcher: TestDispatcher

    @Rule
    fun setUpRule() = SetUpRule()

    @Test
    fun `when task with provided taskId is not present, the use case results in failure`() =
        runLocalTest {
            val nonExistingTask = buildRandomTask()
            val newPriority = getRandomPriority()
            val result = useCase.invoke(taskId = nonExistingTask.id, priority = newPriority)
            advanceUntilIdle()
            assertThat(result).isInstanceOf(ResultModel.Failure::class.java)
        }

    @Test
    fun `when use case is successful, then updated task is saved to repository`() =
        runLocalTest {
            val task = buildRandomTask()
            val newPriority = getRandomPriority()
            fillRepository(listOf(task))
            useCase.invoke(taskId = task.id, priority = newPriority)
            advanceUntilIdle()
            verifyTaskWithIdHasPriority(taskId = task.id, expectedPriority = newPriority)
        }

    private fun verifyTaskWithIdHasPriority(taskId: String, expectedPriority: Long) {
        getTaskById(taskId)?.apply {
            assertThat(this.priority).isEqualTo(expectedPriority)
        } ?: AssertionError()
    }

    private fun getTaskById(taskId: String): TaskModel? {
        return taskRepository.getTasks().find { it.id == taskId }
    }

    private fun fillRepository(tasks: List<TaskModel>) {
        taskRepository.setTasks(tasks)
    }

    private fun getRandomPriority(): Long {
        return Random.nextLong(DomainConst.MIN_PRIORITY..DomainConst.MAX_PRIORITY)
    }

    private fun buildRandomTask(): TaskModel {
        return SingleTaskFactory().build()
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
            testDispatcher = StandardTestDispatcher()
        }

        private fun initUseCase() {
            useCase = DefaultUpdateTaskPriorityByIdUseCase(
                taskRepository = taskRepository,
                defaultDispatcher = testDispatcher
            )
        }

    }

}