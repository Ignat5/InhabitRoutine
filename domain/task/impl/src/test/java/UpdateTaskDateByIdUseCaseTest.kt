import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.test.TestUtil
import com.ignatlegostaev.inhabitroutine.core.test.factory.SingleTaskFactory
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.data.record.api.RecordRepository
import com.ignatlegostaev.inhabitroutine.data.task.test.FakeTaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskDate
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SetUpTaskRemindersUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskDateByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultUpdateTaskDateByIdUseCase
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
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verifyBlocking
import org.mockito.kotlin.wheneverBlocking

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateTaskDateByIdUseCaseTest {

    private lateinit var useCase: UpdateTaskDateByIdUseCase
    private lateinit var taskRepository: FakeTaskRepository
    private lateinit var recordRepository: RecordRepository
    private lateinit var setUpTaskRemindersUseCase: SetUpTaskRemindersUseCase
    private lateinit var testDispatcher: TestDispatcher

    @Rule
    fun setUpRule() = LocalSetUpRule()

    @Test
    fun `when task with provided taskId is not present, then use case results in failure`() = runLocalTest {
        val nonExistingTaskId = randomUUID()
        val anyTaskDate = TaskDate.Day(TestUtil.buildRandomDate())
        val result = useCase.invoke(taskId = nonExistingTaskId, taskDate = anyTaskDate)
        advanceUntilIdle()
        assertThat(result).isInstanceOf(ResultModel.Failure::class.java)
    }

    @Test
    fun `when task is successfully updated, then deleteRecords is invoked`() =
        runLocalTest {
            val task = buildRandomSingleTask()
            val taskDate = task.date.copy(TestUtil.buildRandomDate())
            fillRepository(listOf(task))
            mockRecordRepositoryDeleteRecordsToSuccess()
            mockSetUpTaskRemindersUseCaseToSuccess()
            useCase.invoke(taskId = task.id, taskDate = taskDate)
            advanceUntilIdle()
            verifyDeleteRecordsInvoked()
        }

    private fun verifyDeleteRecordsInvoked() {
        verifyBlocking(recordRepository) {
            deleteRecordsByTaskIdAndPeriod(any(), any(), any())
        }
    }

    @Test
    fun `when task is successfully updated, then setUpTaskRemindersUseCase is invoked`() =
        runLocalTest {
            val task = buildRandomSingleTask()
            val taskDate = task.date.copy(TestUtil.buildRandomDate())
            fillRepository(listOf(task))
            mockRecordRepositoryDeleteRecordsToSuccess()
            mockSetUpTaskRemindersUseCaseToSuccess()
            useCase.invoke(taskId = task.id, taskDate = taskDate)
            advanceUntilIdle()
            verifySetUpTaskRemindersInvokedForTask(task.id)
        }

    private fun verifySetUpTaskRemindersInvokedForTask(taskId: String) {
        verifyBlocking(setUpTaskRemindersUseCase) {
            invoke(taskId)
        }
    }


    @Test
    fun `when use case is success, then repository contains updated task`() = runLocalTest {
        val task = buildRandomSingleTask()
        val taskDate = task.date.copy(TestUtil.buildRandomDate())
        fillRepository(listOf(task))
        mockRecordRepositoryDeleteRecordsToSuccess()
        mockSetUpTaskRemindersUseCaseToSuccess()
        useCase.invoke(taskId = task.id, taskDate = taskDate)
        advanceUntilIdle()
        verifyTaskWithIdHasExpectedTaskDate(taskId = task.id, expectedTaskDate = taskDate)
    }

    private fun verifyTaskWithIdHasExpectedTaskDate(taskId: String, expectedTaskDate: TaskDate) {
        getTaskFromRepositoryById(taskId)?.apply {
            assertThat(this.date).isEqualTo(expectedTaskDate)
        } ?: throw AssertionError()
    }

    private fun getTaskFromRepositoryById(taskId: String): TaskModel? {
        return getAllTasksFromRepository().find { it.id == taskId }
    }

    private fun getAllTasksFromRepository(): List<TaskModel> {
        return taskRepository.getTasks()
    }

    private fun mockRecordRepositoryDeleteRecordsToSuccess() {
        wheneverBlocking {
            recordRepository.deleteRecordsByTaskId(any())
        }.thenReturn(ResultModel.success(Unit))
    }

    private fun mockSetUpTaskRemindersUseCaseToSuccess() {
        wheneverBlocking {
            setUpTaskRemindersUseCase(any())
        }.thenReturn(Unit)
    }

    private fun fillRepository(tasks: List<TaskModel>) {
        taskRepository.setTasks(tasks)
    }

    private fun buildRandomSingleTask(): TaskModel.Task.SingleTask {
        return SingleTaskFactory().build()
    }

    private fun runLocalTest(block: suspend TestScope.() -> Unit) = runTest(testDispatcher) {
        block()
    }

    inner class LocalSetUpRule : TestWatcher() {

        override fun starting(description: Description?) {
            super.starting(description)
            initDependencies()
            initUseCase()
        }

        private fun initDependencies() {
            taskRepository = FakeTaskRepository()
            recordRepository = mock()
            setUpTaskRemindersUseCase = mock()
            testDispatcher = StandardTestDispatcher()
        }

        private fun initUseCase() {
            useCase = DefaultUpdateTaskDateByIdUseCase(
                taskRepository = taskRepository,
                recordRepository = recordRepository,
                setUpTaskRemindersUseCase = setUpTaskRemindersUseCase,
                externalScope = TestScope(testDispatcher)
            )
        }
    }

}