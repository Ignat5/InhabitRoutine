import com.ignatlegostaev.inhabitroutine.core.platform.reminder.api.ReminderManager
import com.ignatlegostaev.inhabitroutine.core.test.factory.reminder.ReminderFactory
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.RecurringTaskFactory
import com.ignatlegostaev.inhabitroutine.core.util.todayDate
import com.ignatlegostaev.inhabitroutine.data.reminder.test.FakeReminderRepository
import com.ignatlegostaev.inhabitroutine.data.task.test.FakeTaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.type.ReminderType
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskDate
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskFrequency
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SetUpNextReminderUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.impl.use_case.DefaultSetUpNextReminderUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking

@OptIn(ExperimentalCoroutinesApi::class)
class SetUpNextReminderUseCaseTest {

    private lateinit var useCase: SetUpNextReminderUseCase
    private lateinit var reminderRepository: FakeReminderRepository
    private lateinit var taskRepository: FakeTaskRepository
    private lateinit var reminderManager: ReminderManager
    private lateinit var testDispatcher: TestDispatcher

    @Rule
    fun setUpRule() = SetUpRule()

    @Test
    fun `when reminder can be set, then reminder is set`() =
        runLocalTest {
            val activeTask = buildRecurringTaskThatStartsTomorrow()
            val activeReminder = buildAlwaysActiveReminderForTask(activeTask.id)
            fillTaskRepository(activeTask)
            fillReminderRepository(activeReminder)
            useCase.invoke(activeReminder.id)
            advanceUntilIdle()
            verifyReminderIsSetAtDateTimeForReminder(
                reminderId = activeReminder.id,
                dateTime = LocalDateTime(
                    date = activeTask.date.startDate,
                    time = activeReminder.time
                )
            )
        }

    private fun verifyReminderIsSetAtDateTimeForReminder(
        reminderId: String,
        dateTime: LocalDateTime
    ) {
        verifyBlocking(reminderManager) {
            this.setReminder(
                reminderId = reminderId,
                millis = dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            )
        }
    }

    private fun buildRecurringTaskThatStartsTomorrow(): TaskModel.Task.RecurringTask {
        val tomorrow = Clock.System.todayDate.plus(1, DateTimeUnit.DAY)
        return buildAlwaysActiveRecurringTask().copy(
            date = TaskDate.Period(startDate = tomorrow, endDate = null)
        )
    }

    @Test
    fun `when trying to set reminder that doesn't overlap with frequency of its task, then no reminder is set`() =
        runLocalTest {
            val taskDaysOfWeekFrequency =
                TaskFrequency.DaysOfWeek(setOf(DayOfWeek.entries.random()))
            val reminderDaysOfWeekSchedule = ReminderSchedule.DaysOfWeek(
                DayOfWeek.entries.subtract(taskDaysOfWeekFrequency.daysOfWeek)
            )
            val task = buildAlwaysActiveRecurringTask().copy(
                frequency = taskDaysOfWeekFrequency
            )
            val reminder =
                buildAlwaysActiveReminderForTask(task.id).copy(
                    schedule = reminderDaysOfWeekSchedule
                )
            fillTaskRepository(task)
            fillReminderRepository(reminder)
            useCase.invoke(reminder.id)
            advanceUntilIdle()
            verifyNoReminderIsSet()
        }

    @Test
    fun `when trying to set reminder for reminder with type 'NoReminder', then no reminder is set`() =
        runLocalTest {
            val task = buildAlwaysActiveRecurringTask()
            val reminder =
                buildAlwaysActiveReminderForTask(task.id).copy(type = ReminderType.NoReminder)
            fillTaskRepository(task)
            fillReminderRepository(reminder)
            useCase.invoke(reminder.id)
            advanceUntilIdle()
            verifyNoReminderIsSet()
        }

    @Test
    fun `when trying to set reminder for already ended task, then no reminder is set`() =
        runLocalTest {
            val task = buildAlwaysActiveRecurringTask().copy(
                date = TaskDate.Period(
                    startDate = buildDistantPastLocalDate(),
                    endDate = buildDistantPastLocalDate()
                )
            )
            val reminder = buildAlwaysActiveReminderForTask(task.id)
            fillTaskRepository(task)
            fillReminderRepository(reminder)
            useCase.invoke(reminder.id)
            advanceUntilIdle()
            verifyNoReminderIsSet()
        }

    @Test
    fun `when trying to set reminder for task-draft, then no reminder is set`() = runLocalTest {
        val task = buildAlwaysActiveRecurringTask().copy(isDraft = true)
        val reminder = buildAlwaysActiveReminderForTask(task.id)
        fillTaskRepository(task)
        fillReminderRepository(reminder)
        useCase.invoke(reminder.id)
        advanceUntilIdle()
        verifyNoReminderIsSet()
    }

    @Test
    fun `when trying to set reminder for archived task, then no reminder is set`() = runLocalTest {
        val task = buildAlwaysActiveRecurringTask().copy(isArchived = true)
        val reminder = buildAlwaysActiveReminderForTask(task.id)
        fillTaskRepository(task)
        fillReminderRepository(reminder)
        useCase.invoke(reminder.id)
        advanceUntilIdle()
        verifyNoReminderIsSet()
    }

    private fun verifyNoReminderIsSet() {
        verifyBlocking(reminderManager, times(0)) {
            setReminder(any(), any())
        }
    }

    private fun fillReminderRepository(vararg reminders: ReminderModel) {
        reminderRepository.setReminders(reminders.toList())
    }

    private fun fillTaskRepository(vararg tasks: TaskModel) {
        taskRepository.setTasks(tasks.toList())
    }

    private fun buildAlwaysActiveRecurringTask(): TaskModel.Task.RecurringTask {
        return RecurringTaskFactory().build()
            .copy(
                date = TaskDate.Period(
                    startDate = buildDistantPastLocalDate(),
                    endDate = null
                ),
                isArchived = false,
                isDraft = false,
                frequency = TaskFrequency.EveryDay
            )
    }

    private fun buildDistantPastLocalDate(): LocalDate {
        return Instant.DISTANT_PAST.toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    private fun buildAlwaysActiveReminderForTask(taskId: String): ReminderModel {
        return buildAlwaysActiveReminder().copy(taskId = taskId)
    }

    private fun buildAlwaysActiveReminder(): ReminderModel {
        return ReminderFactory().build().copy(
            type = ReminderType.Notification,
            schedule = ReminderSchedule.AlwaysEnabled
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
            reminderRepository = FakeReminderRepository()
            taskRepository = FakeTaskRepository()
            reminderManager = mock()
            testDispatcher = StandardTestDispatcher()
        }

        private fun initUseCase() {
            useCase = DefaultSetUpNextReminderUseCase(
                reminderManager = reminderManager,
                reminderRepository = reminderRepository,
                taskRepository = taskRepository,
                defaultDispatcher = testDispatcher
            )
        }

    }

}