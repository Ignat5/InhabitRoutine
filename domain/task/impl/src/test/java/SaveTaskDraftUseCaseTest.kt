import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.task.test.FakeTaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskProgressType
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultSaveTaskDraftUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SaveTaskDraftUseCaseTest {

    private lateinit var useCase: SaveTaskDraftUseCase
    private lateinit var fakeTaskRepository: FakeTaskRepository
    private lateinit var testDispatcher: TestDispatcher

    @Before
    fun setUp() {
        initDependencies()
        initUseCase()
    }

    @Test
    fun `when task is successfully created, then returned taskId matches saved task`() = runLocalTest {
        verifyRepositoryIsEmpty()
        val result = useCase.invoke(SaveTaskDraftUseCase.RequestType.CreateSingleTask)
        advanceUntilIdle()
        (result as? ResultModel.Success)?.value?.let { taskId ->
            verifyRepositoryContainsTaskWithId(taskId)
        } ?: throw AssertionError()
    }

    private fun verifyRepositoryContainsTaskWithId(taskId: String) {
        getRepositoryTasks().apply {
            assertThat(this.any { it.id == taskId }).isTrue()
        }
    }

    @Test
    fun `when task is created, then it is a draft`() = runLocalTest {
        verifyRepositoryIsEmpty()
        useCase.invoke(SaveTaskDraftUseCase.RequestType.CreateSingleTask)
        advanceUntilIdle()
        getFirstRepositoryTask().apply {
            assertThat(this.isDraft).isTrue()
        }
    }

    private fun getFirstRepositoryTask(): TaskModel {
        return getRepositoryTasks().first()
    }

    @Test
    fun `when request is CreateHabit with Time progress type, then created task is HabitTime`() {
        runRequestMatchesCreatedTaskTest(
            requestType = SaveTaskDraftUseCase.RequestType.CreateHabit(progressType = TaskProgressType.Time),
            expectedInstanceOf = TaskModel.Habit.HabitContinuous.HabitTime::class.java
        )
    }

    @Test
    fun `when request is CreateHabit with Number progress type, then created task is HabitNumber`() {
        runRequestMatchesCreatedTaskTest(
            requestType = SaveTaskDraftUseCase.RequestType.CreateHabit(progressType = TaskProgressType.Number),
            expectedInstanceOf = TaskModel.Habit.HabitContinuous.HabitNumber::class.java
        )
    }

    @Test
    fun `when request is CreateHabit with YesNo progress type, then created task is HabitYesNo`() {
        runRequestMatchesCreatedTaskTest(
            requestType = SaveTaskDraftUseCase.RequestType.CreateHabit(progressType = TaskProgressType.YesNo),
            expectedInstanceOf = TaskModel.Habit.HabitYesNo::class.java
        )
    }

    @Test
    fun `when request is to CreateRecurringTask, then created task is RecurringTask`() {
        runRequestMatchesCreatedTaskTest(
            requestType = SaveTaskDraftUseCase.RequestType.CreateRecurringTask,
            expectedInstanceOf = TaskModel.Task.RecurringTask::class.java
        )
    }

    @Test
    fun `when request is to CreateSingleTask, then created task is SingleTask`() {
        runRequestMatchesCreatedTaskTest(
            requestType = SaveTaskDraftUseCase.RequestType.CreateSingleTask,
            expectedInstanceOf = TaskModel.Task.SingleTask::class.java
        )
    }

    private fun <T : TaskModel> runRequestMatchesCreatedTaskTest(
        requestType: SaveTaskDraftUseCase.RequestType,
        expectedInstanceOf: Class<T>
    ) = runLocalTest {
        verifyRepositoryIsEmpty()
        useCase.invoke(requestType)
        advanceUntilIdle()
        verifyRepositoryContainsTaskOfInstance(expectedInstanceOf)
    }

    private fun <T : TaskModel> verifyRepositoryContainsTaskOfInstance(expectedInstanceOf: Class<T>) {
        getRepositoryTasks().apply {
            assertThat(this.filterIsInstance(expectedInstanceOf)).isNotEmpty()
        }
    }

    private fun verifyRepositoryIsEmpty() {
        getRepositoryTasks().apply {
            assertThat(this).isEmpty()
        }
    }

    private fun getRepositoryTasks(): List<TaskModel> {
        return fakeTaskRepository.getTasks()
    }

    private fun runLocalTest(block: suspend TestScope.() -> Unit) = runTest(testDispatcher) {
        block()
    }

    private fun initDependencies() {
        fakeTaskRepository = FakeTaskRepository()
        testDispatcher = StandardTestDispatcher()
    }

    private fun initUseCase() {
        useCase = DefaultSaveTaskDraftUseCase(
            taskRepository = fakeTaskRepository,
            defaultDispatcher = testDispatcher
        )
    }

}