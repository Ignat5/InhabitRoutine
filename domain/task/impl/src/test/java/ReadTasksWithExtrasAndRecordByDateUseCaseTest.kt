import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.test.TestUtil
import com.ignatlegostaev.inhabitroutine.core.test.factory.record.RecordFactory
import com.ignatlegostaev.inhabitroutine.core.test.factory.reminder.ReminderFactory
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.HabitNumberFactory
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.HabitTimeFactory
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.HabitYesNoFactory
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.RecurringTaskFactory
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.SingleTaskFactory
import com.ignatlegostaev.inhabitroutine.data.record.test.FakeRecordRepository
import com.ignatlegostaev.inhabitroutine.data.reminder.test.FakeReminderRepository
import com.ignatlegostaev.inhabitroutine.data.task.test.FakeTaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.derived.TaskStatus
import com.ignatlegostaev.inhabitroutine.domain.model.derived.TaskWithExtrasAndRecordModel
import com.ignatlegostaev.inhabitroutine.domain.model.record.RecordModel
import com.ignatlegostaev.inhabitroutine.domain.model.record.content.RecordEntry
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskProgress
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.ProgressLimitType
import com.ignatlegostaev.inhabitroutine.domain.model.util.DomainConst
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTasksWithExtrasAndRecordByDateUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultReadTasksWithExtrasAndRecordByDateUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class ReadTasksWithExtrasAndRecordByDateUseCaseTest {

    private lateinit var useCase: ReadTasksWithExtrasAndRecordByDateUseCase
    private lateinit var taskRepository: FakeTaskRepository
    private lateinit var reminderRepository: FakeReminderRepository
    private lateinit var recordRepository: FakeRecordRepository
    private lateinit var testDispatcher: TestDispatcher

    @Rule
    fun setUpRule() = SetUpRule()

    /*
    Naming (YX)
        Yx: y > x
        yX: y < x
        YX, yx: y == X
     */

    @Test
    fun `when archived are excluded, then no archived tasks are present in end result`() = runLocalTest {
        val notArchivedTask = buildRandomTask().copy(isArchived = false)
        val archivedTask = buildRandomTask().copy(isArchived = true)
        fillTaskRepository(listOf(notArchivedTask, archivedTask))
        invokeUseCase(excludeArchived = true).test {
            with(awaitItem()) {
                assertThat(this.map { it.task }).containsExactly(notArchivedTask)
            }
        }
    }

    @Test
    fun `when drafts are excluded, then no drafts are present in end result`() = runLocalTest {
        val notDraftTask = buildRandomTask().copy(isDraft = false)
        val draftTask = buildRandomTask().copy(isDraft = true)
        fillTaskRepository(listOf(notDraftTask, draftTask))
        invokeUseCase(excludeDrafts = true).test {
            with(awaitItem()) {
                assertThat(this.map { it.task }).containsExactly(notDraftTask)
            }
        }
    }

    @Test
    fun `when HabitTime (no more than x) with 'time' record (y, YX) is present, then end result reflects that correctly`() {
        runHabitTimeStatusCheck(
            taskProgress = TaskProgress.Time(
                limitType = ProgressLimitType.NoMoreThan,
                limitTime = buildMaxLocalTime()
            ),
            recordEntry = RecordEntry.Time(buildMaxLocalTime()),
            expectedStatus = TaskStatus.Completed
        )
    }

    @Test
    fun `when HabitTime (no more than x) with 'time' record (y, yX) is present, then end result reflects that correctly`() {
        runHabitTimeStatusCheck(
            taskProgress = TaskProgress.Time(
                limitType = ProgressLimitType.NoMoreThan,
                limitTime = buildMaxLocalTime()
            ),
            recordEntry = RecordEntry.Time(buildMinLocalTime()),
            expectedStatus = TaskStatus.Completed
        )
    }

    @Test
    fun `when HabitTime (no more than x) with 'time' record (y, Yx) is present, then end result reflects that correctly`() {
        runHabitTimeStatusCheck(
            taskProgress = TaskProgress.Time(
                limitType = ProgressLimitType.NoMoreThan,
                limitTime = buildMinLocalTime()
            ),
            recordEntry = RecordEntry.Time(buildMaxLocalTime()),
            expectedStatus = TaskStatus.NotCompleted.Pending
        )
    }

    @Test
    fun `when HabitTime (exactly x) with 'time' record (y, YX) is present, then end result reflects that correctly`() {
        runHabitTimeStatusCheck(
            taskProgress = TaskProgress.Time(
                limitType = ProgressLimitType.Exactly,
                limitTime = buildMaxLocalTime()
            ),
            recordEntry = RecordEntry.Time(buildMaxLocalTime()),
            expectedStatus = TaskStatus.Completed
        )
    }

    @Test
    fun `when HabitTime (exactly x) with 'time' record (y, yX) is present, then end result reflects that correctly`() {
        runHabitTimeStatusCheck(
            taskProgress = TaskProgress.Time(
                limitType = ProgressLimitType.Exactly,
                limitTime = buildMaxLocalTime()
            ),
            recordEntry = RecordEntry.Time(buildMinLocalTime()),
            expectedStatus = TaskStatus.NotCompleted.Pending
        )
    }

    @Test
    fun `when HabitTime (exactly x) with 'time' record (y, Yx) is present, then end result reflects that correctly`() {
        runHabitTimeStatusCheck(
            taskProgress = TaskProgress.Time(
                limitType = ProgressLimitType.Exactly,
                limitTime = buildMinLocalTime()
            ),
            recordEntry = RecordEntry.Time(buildMaxLocalTime()),
            expectedStatus = TaskStatus.NotCompleted.Pending
        )
    }

    @Test
    fun `when HabitTime (at least x) with 'time' record (y, YX) is present, then end result reflects that correctly`() {
        runHabitTimeStatusCheck(
            taskProgress = TaskProgress.Time(
                limitType = ProgressLimitType.AtLeast,
                limitTime = buildMaxLocalTime()
            ),
            recordEntry = RecordEntry.Time(buildMaxLocalTime()),
            expectedStatus = TaskStatus.Completed
        )
    }

    @Test
    fun `when HabitTime (at least x) with 'time' record (y, yX) is present, then end result reflects that correctly`() {
        runHabitTimeStatusCheck(
            taskProgress = TaskProgress.Time(
                limitType = ProgressLimitType.AtLeast,
                limitTime = buildMaxLocalTime()
            ),
            recordEntry = RecordEntry.Time(buildMinLocalTime()),
            expectedStatus = TaskStatus.NotCompleted.Pending
        )
    }

    @Test
    fun `when HabitTime (at least x) with 'time' record (y, Yx) is present, then end result reflects that correctly`() {
        runHabitTimeStatusCheck(
            taskProgress = TaskProgress.Time(
                limitType = ProgressLimitType.AtLeast,
                limitTime = buildMinLocalTime()
            ),
            recordEntry = RecordEntry.Time(buildMaxLocalTime()),
            expectedStatus = TaskStatus.Completed
        )
    }

    private fun buildMaxLocalTime() = LocalTime(hour = 23, minute = 59, second = 59)
    private fun buildMinLocalTime() = LocalTime(hour = 0, minute = 0, second = 0)

    private fun runHabitTimeStatusCheck(
        taskProgress: TaskProgress.Time,
        recordEntry: RecordEntry.Time,
        expectedStatus: TaskStatus
    ) = runLocalTest {
        val task = buildHabitTime().copy(progress = taskProgress)
        val record = buildRecordForTask(task.id).copy(entry = recordEntry)
        fillTaskRepository(listOf(task))
        fillRecordRepository(listOf(record))
        with(awaitFirstItem()) {
            assertThat(this).isInstanceOf(TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitTime::class.java)
            assertThat(this.task).isEqualTo(task)
            assertThat(this.recordEntry).isEqualTo(record.entry)
            assertThat(this.status).isEqualTo(expectedStatus)
        }
    }

    @Test
    fun `when HabitNumber (no more than x) with 'number' record (y, YX) is present, then end result reflects that correctly`() {
        runHabitNumberStatusCheck(
            taskProgress = TaskProgress.Number(
                limitType = ProgressLimitType.NoMoreThan,
                limitNumber = DomainConst.MAX_LIMIT_NUMBER,
                limitUnit = ""
            ),
            recordEntry = RecordEntry.Number(DomainConst.MAX_LIMIT_NUMBER),
            expectedStatus = TaskStatus.Completed
        )
    }

    @Test
    fun `when HabitNumber (no more than x) with 'number' record (y, yX) is present, then end result reflects that correctly`() {
        runHabitNumberStatusCheck(
            taskProgress = TaskProgress.Number(
                limitType = ProgressLimitType.NoMoreThan,
                limitNumber = DomainConst.MAX_LIMIT_NUMBER,
                limitUnit = ""
            ),
            recordEntry = RecordEntry.Number(DomainConst.MIN_LIMIT_NUMBER),
            expectedStatus = TaskStatus.Completed
        )
    }

    @Test
    fun `when HabitNumber (no more than x) with 'number' record (y, Yx) is present, then end result reflects that correctly`() {
        runHabitNumberStatusCheck(
            taskProgress = TaskProgress.Number(
                limitType = ProgressLimitType.NoMoreThan,
                limitNumber = DomainConst.MIN_LIMIT_NUMBER,
                limitUnit = ""
            ),
            recordEntry = RecordEntry.Number(DomainConst.MAX_LIMIT_NUMBER),
            expectedStatus = TaskStatus.NotCompleted.Pending
        )
    }

    @Test
    fun `when HabitNumber (exactly x) with 'number' record (y, yx) is present, then end result reflects that correctly`() {
        runHabitNumberStatusCheck(
            taskProgress = TaskProgress.Number(
                limitType = ProgressLimitType.Exactly,
                limitNumber = DomainConst.MAX_LIMIT_NUMBER,
                limitUnit = ""
            ),
            recordEntry = RecordEntry.Number(DomainConst.MAX_LIMIT_NUMBER),
            expectedStatus = TaskStatus.Completed
        )
    }

    @Test
    fun `when HabitNumber (exactly x) with 'number' record (y, yX) is present, then end result reflects that correctly`() {
        runHabitNumberStatusCheck(
            taskProgress = TaskProgress.Number(
                limitType = ProgressLimitType.Exactly,
                limitNumber = DomainConst.MAX_LIMIT_NUMBER,
                limitUnit = ""
            ),
            recordEntry = RecordEntry.Number(DomainConst.MIN_LIMIT_NUMBER),
            expectedStatus = TaskStatus.NotCompleted.Pending
        )
    }

    @Test
    fun `when HabitNumber (exactly x) with 'number' record (y, Yx) is present, then end result reflects that correctly`() {
        runHabitNumberStatusCheck(
            taskProgress = TaskProgress.Number(
                limitType = ProgressLimitType.Exactly,
                limitNumber = DomainConst.MIN_LIMIT_NUMBER,
                limitUnit = ""
            ),
            recordEntry = RecordEntry.Number(DomainConst.MAX_LIMIT_NUMBER),
            expectedStatus = TaskStatus.NotCompleted.Pending
        )
    }

    @Test
    fun `when HabitNumber (at least x) with 'number' record (y, YX) is present, then end result reflects that correctly`() {
        runHabitNumberStatusCheck(
            taskProgress = TaskProgress.Number(
                limitType = ProgressLimitType.AtLeast,
                limitNumber = DomainConst.MAX_LIMIT_NUMBER,
                limitUnit = ""
            ),
            recordEntry = RecordEntry.Number(DomainConst.MAX_LIMIT_NUMBER),
            expectedStatus = TaskStatus.Completed
        )
    }

    @Test
    fun `when HabitNumber (at least x) with 'number' record (y, yX) is present, then end result reflects that correctly`() {
        runHabitNumberStatusCheck(
            taskProgress = TaskProgress.Number(
                limitType = ProgressLimitType.AtLeast,
                limitNumber = DomainConst.MAX_LIMIT_NUMBER,
                limitUnit = ""
            ),
            recordEntry = RecordEntry.Number(DomainConst.MIN_LIMIT_NUMBER),
            expectedStatus = TaskStatus.NotCompleted.Pending
        )
    }

    @Test
    fun `when HabitNumber (at least x) with 'number' record (y, Yx) is present, then end result reflects that correctly`() {
        runHabitNumberStatusCheck(
            taskProgress = TaskProgress.Number(
                limitType = ProgressLimitType.AtLeast,
                limitNumber = DomainConst.MIN_LIMIT_NUMBER,
                limitUnit = ""
            ),
            recordEntry = RecordEntry.Number(DomainConst.MAX_LIMIT_NUMBER),
            expectedStatus = TaskStatus.Completed
        )
    }

    private fun runHabitNumberStatusCheck(
        taskProgress: TaskProgress.Number,
        recordEntry: RecordEntry.Number,
        expectedStatus: TaskStatus
    ) = runLocalTest {
        val task = buildHabitNumber().copy(progress = taskProgress)
        val record = buildRecordForTask(task.id).copy(entry = recordEntry)
        fillTaskRepository(listOf(task))
        fillRecordRepository(listOf(record))
        with(awaitFirstItem()) {
            assertThat(this).isInstanceOf(TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitNumber::class.java)
            assertThat(this.task).isEqualTo(task)
            assertThat(this.recordEntry).isEqualTo(record.entry)
            assertThat(this.status).isEqualTo(expectedStatus)
        }
    }

    @Test
    fun `when HabitNumber with no record is present, then end result reflects that correctly`() {
        runUseCaseWithCheckTest(
            task = buildHabitNumber(),
            recordEntry = null,
            expectedResultModel = TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitNumber::class.java,
            expectedStatus = TaskStatus.NotCompleted.Pending
        )
    }

    @Test
    fun `when HabitYesNo with 'fail' record is present, then end result reflects that correctly`() {
        runUseCaseWithCheckTest(
            task = buildHabitYesNo(),
            recordEntry = RecordEntry.Fail,
            expectedResultModel = TaskWithExtrasAndRecordModel.Habit.HabitYesNo::class.java,
            expectedStatus = TaskStatus.NotCompleted.Failed
        )
    }

    @Test
    fun `when HabitYesNo with 'skip' record is present, then end result reflects that correctly`() {
        runUseCaseWithCheckTest(
            task = buildHabitYesNo(),
            recordEntry = RecordEntry.Skip,
            expectedResultModel = TaskWithExtrasAndRecordModel.Habit.HabitYesNo::class.java,
            expectedStatus = TaskStatus.NotCompleted.Skipped
        )
    }

    @Test
    fun `when HabitYesNo with 'done' record is present, then end result reflects that correctly`() {
        runUseCaseWithCheckTest(
            task = buildHabitYesNo(),
            recordEntry = RecordEntry.Done,
            expectedResultModel = TaskWithExtrasAndRecordModel.Habit.HabitYesNo::class.java,
            expectedStatus = TaskStatus.Completed
        )
    }

    @Test
    fun `when HabitYesNo with no record is present, then end result reflects that correctly`() {
        runUseCaseWithCheckTest(
            task = buildHabitYesNo(),
            recordEntry = null,
            expectedResultModel = TaskWithExtrasAndRecordModel.Habit.HabitYesNo::class.java,
            expectedStatus = TaskStatus.NotCompleted.Pending
        )
    }

    @Test
    fun `when RecurringTask with 'done' record is present, then end result reflects that correctly`() {
        runUseCaseWithCheckTest(
            task = buildRecurringTask(),
            recordEntry = RecordEntry.Done,
            expectedResultModel = TaskWithExtrasAndRecordModel.Task.RecurringTask::class.java,
            expectedStatus = TaskStatus.Completed
        )
    }

    @Test
    fun `when RecurringTask with no record is present, then end result reflects that correctly`() {
        runUseCaseWithCheckTest(
            task = buildRecurringTask(),
            recordEntry = null,
            expectedResultModel = TaskWithExtrasAndRecordModel.Task.RecurringTask::class.java,
            expectedStatus = TaskStatus.NotCompleted.Pending
        )
    }

    @Test
    fun `when SingleTask with 'done' record is present, then end result reflects that correctly`() {
        runUseCaseWithCheckTest(
            task = buildSingleTask(),
            recordEntry = RecordEntry.Done,
            expectedResultModel = TaskWithExtrasAndRecordModel.Task.SingleTask::class.java,
            expectedStatus = TaskStatus.Completed
        )
    }

    @Test
    fun `when SingleTask with no record is present, then end result reflects that correctly`() {
        runUseCaseWithCheckTest(
            task = buildSingleTask(),
            recordEntry = null,
            expectedResultModel = TaskWithExtrasAndRecordModel.Task.SingleTask::class.java,
            expectedStatus = TaskStatus.NotCompleted.Pending
        )
    }

    private fun runUseCaseWithCheckTest(
        task: TaskModel,
        recordEntry: RecordEntry?,
        expectedResultModel: Class<*>,
        expectedStatus: TaskStatus
    ) = runLocalTest {
        fillTaskRepository(listOf(task))
        recordEntry?.let { entry ->
            val record = buildRecordForTask(task.id).copy(entry = entry)
            fillRecordRepository(listOf(record))
        }
        with(awaitFirstItem()) {
            assertThat(this).isInstanceOf(expectedResultModel)
            assertThat(this.task).isEqualTo(task)
            assertThat(this.recordEntry).isEqualTo(recordEntry)
            assertThat(this.status).isEqualTo(expectedStatus)
        }
    }

    private suspend fun awaitFirstItem(): TaskWithExtrasAndRecordModel {
        with(invokeUseCase().first()) {
            return first()
        }
    }

    @Test
    fun `when task has reminder set for the date, then reminder is included into the end result`() =
        runLocalTest {
            val task = buildRandomTask()
            val reminder = buildReminderForTask(task.id)
            fillTaskRepository(listOf(task))
            fillReminderRepository(listOf(reminder))
            invokeUseCase().test {
                with(awaitItem()) {
                    this.first().apply {
                        assertThat(this.task.id).isEqualTo(task.id)
                        assertThat(this.taskExtras.allReminders.first().id).isEqualTo(reminder.id)
                    }
                }
            }
        }

    @Test
    fun `when task repository is empty, then use case emits empty list`() =
        runLocalTest {
            fillTaskRepository(emptyList())
            invokeUseCase().test {
                with(awaitItem()) {
                    assertThat(this).isEmpty()
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    private fun invokeUseCase(
        date: LocalDate = TestUtil.buildRandomDate(),
        excludeArchived: Boolean = false,
        excludeDrafts: Boolean = false
    ): Flow<List<TaskWithExtrasAndRecordModel>> = useCase.invoke(
        date = date,
        excludeArchived = excludeArchived,
        excludeDrafts = excludeDrafts
    )

    private fun fillRecordRepository(records: List<RecordModel>) {
        recordRepository.setRecords(records)
    }

    private fun fillReminderRepository(reminders: List<ReminderModel>) {
        reminderRepository.setReminders(reminders)
    }

    private fun fillTaskRepository(tasks: List<TaskModel>) {
        taskRepository.setTasks(tasks)
    }

    private fun buildRecordForTask(taskId: String): RecordModel {
        return buildRandomRecord().copy(taskId = taskId)
    }

    private fun buildRandomRecord(): RecordModel {
        return RecordFactory().build()
    }

    private fun buildReminderForTask(taskId: String): ReminderModel {
        return buildRandomReminder().copy(taskId = taskId)
    }

    private fun buildRandomReminder(): ReminderModel {
        return ReminderFactory().build()
    }

    private fun buildHabitTime(): TaskModel.Habit.HabitContinuous.HabitTime {
        return HabitTimeFactory().build()
    }

    private fun buildHabitNumber(): TaskModel.Habit.HabitContinuous.HabitNumber {
        return HabitNumberFactory().build()
    }

    private fun buildHabitYesNo(): TaskModel.Habit.HabitYesNo {
        return HabitYesNoFactory().build()
    }

    private fun buildRecurringTask(): TaskModel.Task.RecurringTask {
        return RecurringTaskFactory().build()
    }

    private fun buildSingleTask(): TaskModel.Task.SingleTask {
        return SingleTaskFactory().build()
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
            reminderRepository = FakeReminderRepository()
            recordRepository = FakeRecordRepository()
            testDispatcher = StandardTestDispatcher()
        }

        private fun initUseCase() {
            useCase = DefaultReadTasksWithExtrasAndRecordByDateUseCase(
                taskRepository = taskRepository,
                reminderRepository = reminderRepository,
                recordRepository = recordRepository,
                defaultDispatcher = testDispatcher
            )
        }

    }

}