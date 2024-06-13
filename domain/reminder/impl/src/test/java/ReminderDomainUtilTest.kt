import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.test.factory.reminder.ReminderFactory
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.ignatlegostaev.inhabitroutine.domain.reminder.impl.util.checkOverlap
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import org.junit.Test

class ReminderDomainUtilTest {

    @Test
    fun `when reminders do share time and share the same week day, then overlap occurs`() {
        val commonDayOfWeek = DayOfWeek.WEDNESDAY
        val sourceReminder =
            buildRandomReminder().copy(schedule = buildDaysOfWeekSchedule(DayOfWeek.MONDAY, commonDayOfWeek))
        val targetReminder = buildRandomReminder().copy(
            time = sourceReminder.time,
            schedule = buildDaysOfWeekSchedule(DayOfWeek.SUNDAY, commonDayOfWeek)
        )
        assertThat(sourceReminder.checkOverlap(listOf(targetReminder))).isTrue()
    }

    @Test
    fun `when reminders do share time and are always enabled, then overlap occurs`() {
        val sourceReminder = buildRandomReminder().copy(schedule = ReminderSchedule.AlwaysEnabled)
        val targetReminder = buildRandomReminder().copy(
            time = sourceReminder.time,
            schedule = sourceReminder.schedule
        )
        assertThat(sourceReminder.checkOverlap(listOf(targetReminder))).isTrue()
    }

    @Test
    fun `when reminders do share the same time but do not overlap in schedule, then overlap doesn't occur`() {
        val sourceReminder =
            buildRandomReminder().copy(schedule = buildDaysOfWeekSchedule(DayOfWeek.MONDAY))
        val targetReminder = buildRandomReminder().copy(
            time = sourceReminder.time,
            schedule = buildDaysOfWeekSchedule(DayOfWeek.SUNDAY)
        )
        assertThat(sourceReminder.checkOverlap(listOf(targetReminder))).isFalse()
    }

    @Test
    fun `when reminders do not share the same time, then overlap doesn't occur`() {
        val sourceReminder = buildRandomReminder().copy(schedule = ReminderSchedule.AlwaysEnabled)
        val targetReminder = buildRandomReminder().copy(
            time = sourceReminder.time.plusHour(),
            schedule = sourceReminder.schedule
        )
        assertThat(sourceReminder.checkOverlap(listOf(targetReminder))).isFalse()
    }

    private fun buildDaysOfWeekSchedule(vararg dayOfWeek: DayOfWeek): ReminderSchedule.DaysOfWeek {
        return ReminderSchedule.DaysOfWeek(dayOfWeek.toSet())
    }

    private fun LocalTime.plusHour(): LocalTime = this.let { sourceTime ->
        LocalTime(
            hour = (sourceTime.hour + 1) % 24,
            minute = sourceTime.minute
        )
    }

    private fun buildRandomReminder(): ReminderModel {
        return ReminderFactory().build()
    }

}