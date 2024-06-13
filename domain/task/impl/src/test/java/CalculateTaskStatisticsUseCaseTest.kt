import com.google.common.truth.FloatSubject
import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.test.factory.record.RecordFactory
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.HabitYesNoFactory
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.core.util.todayDate
import com.ignatlegostaev.inhabitroutine.data.record.api.RecordRepository
import com.ignatlegostaev.inhabitroutine.data.record.test.FakeRecordRepository
import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.data.task.test.FakeTaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.derived.TaskStatus
import com.ignatlegostaev.inhabitroutine.domain.model.record.RecordModel
import com.ignatlegostaev.inhabitroutine.domain.model.record.content.RecordEntry
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskDate
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskFrequency
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.calculate_statistics.CalculateTaskStatisticsUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.calculate_statistics.TaskStatisticsModel
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultCalculateTaskStatisticsUseCase
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.until
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.math.IEEErem
import kotlin.math.nextUp
import kotlin.math.roundToInt
import kotlin.math.ulp

class CalculateTaskStatisticsUseCaseTest {

    private lateinit var useCase: CalculateTaskStatisticsUseCase
    private lateinit var taskRepository: FakeTaskRepository
    private lateinit var recordRepository: FakeRecordRepository
    private lateinit var testDispatcher: TestDispatcher

    @Rule
    fun setUpRule() = SetUpRule()

    /*
    Naming convention:
        D - done
        P - pending
        S - skipped
        F - failed
     */

    @Test
    fun `when record history -D-F-S-P-, then status map is correct`() = runLocalTest {
        runFillInRecords(RecordEntry.Done, RecordEntry.Fail, RecordEntry.Skip, null) {
            assertThat(statusMap.count()).isEqualTo(4)
            assertThat(statusMap.count { it.value is TaskStatus.Completed }).isEqualTo(1)
            assertThat(statusMap.count { it.value is TaskStatus.NotCompleted.Failed }).isEqualTo(1)
            assertThat(statusMap.count { it.value is TaskStatus.NotCompleted.Skipped }).isEqualTo(1)
            assertThat(statusMap.count { it.value is TaskStatus.NotCompleted.Pending }).isEqualTo(1)
        }
    }

    @Test
    fun `when record history -D-, then status map is correct`() = runLocalTest {
        runFillInRecords(RecordEntry.Done) {
            assertThat(statusMap.count()).isEqualTo(1)
            assertThat(statusMap.count { it.value is TaskStatus.Completed }).isEqualTo(1)
        }
    }

    @Test
    fun `when record history --, then status map is correct`() = runLocalTest {
        runFillInRecords(null) {
            assertThat(statusMap.count()).isEqualTo(1)
            assertThat(statusMap.count { it.value is TaskStatus.NotCompleted.Pending }).isEqualTo(1)
        }
    }

    @Test
    fun `when record history -D-P-D-, then status count is correct`() = runLocalTest {
        runFillInRecords(RecordEntry.Done, null, RecordEntry.Done) {
            assertThat(statusCount.size).isEqualTo(2)
            assertThat(statusCount[TaskStatus.Completed]).isEqualTo(2)
            assertThat(statusCount[TaskStatus.NotCompleted.Pending]).isEqualTo(1)
        }
    }

    @Test
    fun `when record history -D-, then status count is correct`() = runLocalTest {
        runFillInRecords(RecordEntry.Done) {
            assertThat(statusCount.size).isEqualTo(1)
            assertThat(statusCount[TaskStatus.Completed]).isEqualTo(1)
        }
    }

    @Test
    fun `when record history --, then status count is correct`() = runLocalTest {
        runFillInRecords(null) {
            assertThat(statusCount.size).isEqualTo(1)
            assertThat(statusCount[TaskStatus.NotCompleted.Pending]).isEqualTo(1)
        }
    }

    @Test
    fun `when record history -D-P-D-, then completion count is correct`() = runLocalTest {
        runFillInRecords(RecordEntry.Done, null, RecordEntry.Done) {
            assertThat(completionCount.allTimeCompletionCount).isEqualTo(2)
        }
    }

    @Test
    fun `when record history -D-, then completion count is correct`() = runLocalTest {
        runFillInRecords(RecordEntry.Done) {
            assertThat(completionCount.allTimeCompletionCount).isEqualTo(1)
        }
    }

    @Test
    fun `when record history --, then completion count is correct`() = runLocalTest {
        runFillInRecords {
            assertThat(completionCount.allTimeCompletionCount).isEqualTo(0)
        }
    }

    @Test
    fun `when record history -D-D-P-D-, then streak is correct`() = runLocalTest {
        runFillInRecords(RecordEntry.Done, RecordEntry.Done, null, RecordEntry.Done) {
            assertThat(streakModel.currentStreak).isEqualTo(1)
            assertThat(streakModel.bestStreak).isEqualTo(2)
        }
    }

    @Test
    fun `when record history -D-S-D-, then streak is correct`() = runLocalTest {
        runFillInRecords(RecordEntry.Done, RecordEntry.Skip, RecordEntry.Done) {
            assertThat(streakModel.currentStreak).isEqualTo(2)
            assertThat(streakModel.bestStreak).isEqualTo(2)
        }
    }

    @Test
    fun `when record history -D-P-D-, then streak is correct`() = runLocalTest {
        runFillInRecords(RecordEntry.Done, null, RecordEntry.Done) {
            assertThat(streakModel.currentStreak).isEqualTo(1)
            assertThat(streakModel.bestStreak).isEqualTo(1)
        }
    }

    @Test
    fun `when record history -D-, then streak is correct`() = runLocalTest {
        runFillInRecords(RecordEntry.Done) {
            assertThat(streakModel.currentStreak).isEqualTo(1)
            assertThat(streakModel.bestStreak).isEqualTo(1)
        }
    }

    @Test
    fun `when record history --, then streak is correct`() = runLocalTest {
        runFillInRecords {
            assertThat(streakModel.currentStreak).isEqualTo(0)
            assertThat(streakModel.bestStreak).isEqualTo(0)
        }
    }

    @Test
    fun `when record history -D-P-D-, then habit score is correct`() = runLocalTest {
        runFillInRecords(RecordEntry.Done, null, RecordEntry.Done) {
            val expectedPercent: Int = (habitScore * 100).toInt()
            assertThat(expectedPercent).isEqualTo(66)
        }
    }

    @Test
    fun `when record history -D-S-D-, then habit score is correct`() = runLocalTest {
        runFillInRecords(RecordEntry.Done, RecordEntry.Skip, RecordEntry.Done) {
            assertThat(habitScore).isEqualTo(1f)
        }
    }

    @Test
    fun `when record history -D-P-, then habit score is correct`() = runLocalTest {
        runFillInRecords(RecordEntry.Done, null) {
            assertThat(habitScore).isEqualTo(0.5f)
        }
    }

    @Test
    fun `when record history -D-, then habit score is correct`() = runLocalTest {
        runFillInRecords(RecordEntry.Done) {
            assertThat(habitScore).isEqualTo(1f)
        }
    }

    @Test
    fun `when record history --, then habit score is correct`() = runLocalTest {
        runFillInRecords {
            assertThat(habitScore).isZero()
        }
    }

    private suspend fun runFillInRecords(
        vararg allEntries: RecordEntry?,
        block: TaskStatisticsModel.() -> Unit
    ) {
        val todayDate = Clock.System.todayDate
        val diff = maxOf(0, allEntries.size - 1)
        val testStartDate = todayDate.minus(diff, DateTimeUnit.DAY)
        val testHabit = buildYesNoHabit(startDate = testStartDate)
        val untilToday = testStartDate.until(todayDate, DateTimeUnit.DAY)
        val allRecords = (0..untilToday).mapNotNull { dayOffset ->
            allEntries.getOrNull(dayOffset)?.let { entry ->
                buildRecordForHabit(testHabit.id).copy(
                    date = testStartDate.plus(dayOffset, DateTimeUnit.DAY),
                    entry = entry
                )
            }
        }
        fillTaskRepository(listOf(testHabit))
        fillRecordRepository(allRecords)
        runCalculateStatisticsUseCase(testHabit.id) {
            block()
        }
    }


    private suspend fun runCalculateStatisticsUseCase(
        taskId: String,
        block: TaskStatisticsModel.() -> Unit
    ) {
        with(useCase.invoke(taskId)) {
            with(this as ResultModel.Success) {
                with(this.value) {
                    block()
                }
            }
        }
    }

    private fun fillRecordRepository(records: List<RecordModel>) {
        recordRepository.setRecords(records)
    }

    private fun fillTaskRepository(tasks: List<TaskModel>) {
        taskRepository.setTasks(tasks)
    }

    private fun buildRecordForHabit(taskId: String): RecordModel {
        return RecordFactory().build().copy(taskId = taskId)
    }

    private fun buildYesNoHabit(
        startDate: LocalDate = Clock.System.todayDate,
        endDate: LocalDate? = null,
        isArchived: Boolean = false
    ): TaskModel.Habit.HabitYesNo {
        return HabitYesNoFactory().build().copy(
            date = TaskDate.Period(startDate, endDate),
            isArchived = isArchived,
            isDraft = false,
            versionStartDate = startDate,
            frequency = TaskFrequency.EveryDay
        )
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
            recordRepository = FakeRecordRepository()
            testDispatcher = StandardTestDispatcher()
        }

        private fun initUseCase() {
            useCase = DefaultCalculateTaskStatisticsUseCase(
                taskRepository = taskRepository,
                recordRepository = recordRepository,
                defaultDispatcher = testDispatcher
            )
        }

    }

}