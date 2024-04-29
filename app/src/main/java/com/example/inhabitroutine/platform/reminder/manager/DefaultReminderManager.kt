package com.example.inhabitroutine.platform.reminder.manager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.inhabitroutine.core.platform.reminder.api.ReminderManager
import com.example.inhabitroutine.platform.reminder.receiver.ReminderBroadcastReceiver
import com.example.inhabitroutine.platform.reminder.util.ReminderUtil

class DefaultReminderManager(private val context: Context) : ReminderManager {

    override fun setReminder(reminderId: String, millis: Long) {
        setUpAlarm(reminderId, millis)
    }

    override fun resetReminderById(reminderId: String) {
        cancelAlarm(reminderId)
    }

    private fun setUpAlarm(reminderId: String, epochMillis: Long) {
        runCatching {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val pendingIntent = buildPendingIntent(reminderId)
            val canScheduleExactAlarms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                alarmManager.canScheduleExactAlarms()
            } else true
            if (canScheduleExactAlarms) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    epochMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    epochMillis,
                    pendingIntent
                )
            }
        }.onFailure {
            it.printStackTrace()
        }
    }

    private fun cancelAlarm(reminderId: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = buildPendingIntent(reminderId)
        alarmManager.cancel(pendingIntent)
    }

    private fun buildPendingIntent(reminderId: String): PendingIntent {
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra(ReminderUtil.REMINDER_ID_KEY, reminderId)
        }
        val requestCode = ReminderUtil.stringToInteger(reminderId)
        return PendingIntent.getBroadcast(
            context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE
        )
    }
}