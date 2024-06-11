import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.test.factory.SingleTaskFactory
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.data.task.test.FakeTaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTasksByQueryUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultReadTasksByQueryUseCase
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.kotlin.not

class ReadTasksByQueryUseCaseTest {

    private lateinit var readTasksByQueryUseCase: ReadTasksByQueryUseCase
    private lateinit var taskRepository: FakeTaskRepository
    private lateinit var testDispatcher: TestDispatcher

    @Rule
    fun setUpRule() = SetUpRule()

    @Test
    fun `when exclude drafts is set, then drafts are not read`() = runLocalTest {
        val randomQuery = randomUUID()
        val notDraftTask = buildRandomTask().copy(isDraft = false)
        val draftTask = buildRandomTask().copy(isDraft = true)
        fillRepository(notDraftTask, draftTask)
        taskRepository.queryFilter = { _ -> true }
        readTasksByQueryUseCase(query = randomQuery, excludeDrafts = true).test {
            awaitItem().apply {
                assertThat(this).containsExactly(notDraftTask)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when use case is called, then only tasks that match the query are read`() = runLocalTest {
        val randomQuery = randomUUID()
        val matchFilterTask = buildRandomTask()
        val notMatchFilterTask = buildRandomTask()
        fillRepository(matchFilterTask, notMatchFilterTask)
        taskRepository.queryFilter = { taskModel ->
            taskModel == matchFilterTask
        }
        readTasksByQueryUseCase(query = randomQuery, excludeDrafts = false).test {
            awaitItem().apply {
                assertThat(this).containsExactly(matchFilterTask)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun fillRepository(vararg tasks: TaskModel) {
        taskRepository.setTasks(tasks.toList())
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
            readTasksByQueryUseCase = DefaultReadTasksByQueryUseCase(
                taskRepository = taskRepository,
                defaultDispatcher = testDispatcher
            )
        }

    }

}