package com.example.inhabitroutine.platform.reminder.receiver

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.inhabitroutine.MainActivity
import com.example.inhabitroutine.core.di.components.PresentationComponent
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.core.presentation.ui.util.toHourMinute
import com.example.inhabitroutine.core.util.todayDate
import com.example.inhabitroutine.domain.model.reminder.ReminderModel
import com.example.inhabitroutine.domain.model.reminder.type.ReminderType
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.model.util.checkIfCanSetReminderForDate
import com.example.inhabitroutine.platform.reminder.util.ReminderUtil
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class ReminderBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        intent.getStringExtra(ReminderUtil.REMINDER_ID_KEY)?.let { reminderId ->
            val pendingResult = goAsync()
            PresentationComponent.externalScope.launch {
                coroutineScope {
                    launch {
                        getReminderTaskById(reminderId)?.let { reminderTaskModel ->
                            showReminder(
                                reminderTaskModel = reminderTaskModel,
                                context = context
                            )
                        }
                    }

                    launch {
                        PresentationComponent.setUpNextReminderUseCase(reminderId)
                    }
                }
                pendingResult.finish()
            }
        }
    }

    private fun showReminder(
        reminderTaskModel: ReminderTaskModel,
        context: Context
    ) {
        NotificationManagerCompat.from(context).let { notificationManagerCompat ->
            if (notificationManagerCompat.areNotificationsEnabled()) {
                when (reminderTaskModel.reminderModel.type) {
                    ReminderType.Notification -> showNotificationReminder(
                        reminderTaskModel,
                        context
                    )

                    else -> return
                }
            }
        }
    }

    private fun showNotificationReminder(
        reminderTaskModel: ReminderTaskModel,
        context: Context
    ) {
        createNotificationReminderChannel(context).let { channel ->
            val pendingContentIntent = run {
                val contentIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val requestCode = ReminderUtil.stringToInteger(reminderTaskModel.reminderModel.id)
                PendingIntent.getActivity(
                    context,
                    requestCode,
                    contentIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }
            NotificationCompat.Builder(context, channel.id)
                .setSmallIcon(com.example.inhabitroutine.R.drawable.ic_app_notification)
                .setContentTitle(reminderTaskModel.taskModel.title)
                .setContentText(reminderTaskModel.reminderModel.time.toHourMinute())
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setCategory(Notification.CATEGORY_REMINDER)
                .setAutoCancel(true)
                .setContentIntent(pendingContentIntent)
                .build()
                .let { notification ->
                    showNotification(
                        notificationId = ReminderUtil.stringToInteger(reminderTaskModel.reminderModel.id),
                        notification = notification,
                        context = context
                    )
                }
        }
    }

    private fun showNotification(
        notificationId: Int,
        notification: Notification,
        context: Context
    ) {
        NotificationManagerCompat.from(context).let { notificationManagerCompat ->
            val isPermissionGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else notificationManagerCompat.areNotificationsEnabled()
            if (isPermissionGranted) {
                notificationManagerCompat.notify(notificationId, notification)
            }
        }
    }

    private fun createNotificationReminderChannel(context: Context): NotificationChannelCompat {
        NotificationManagerCompat.from(context).let { notificationManagerCompat ->
            NotificationChannelCompat.Builder(
                NOTIFICATION_REMINDER_CHANNEL_ID,
                NotificationManagerCompat.IMPORTANCE_HIGH
            )
                .setName(context.getString(R.string.notification_reminder_channel_name))
                .setImportance(NotificationManagerCompat.IMPORTANCE_HIGH)
                .setShowBadge(true)
                .build()
                .let { channel ->
                    notificationManagerCompat.createNotificationChannel(channel)
                    return channel
                }
        }
    }

    private suspend fun getReminderTaskById(reminderId: String): ReminderTaskModel? {
        PresentationComponent.readReminderByIdUseCase(reminderId).firstOrNull()
            ?.let { reminderModel ->
                PresentationComponent.readTaskByIdUseCase(reminderModel.taskId).firstOrNull()
                    ?.let { taskModel ->
                        Clock.System.todayDate.let { todayDate ->
                            reminderModel.checkIfCanSetReminderForDate(
                                taskModel = taskModel,
                                targetDate = todayDate
                            ).let { canSetReminder ->
                                if (canSetReminder) {
                                    return ReminderTaskModel(reminderModel, taskModel)
                                } else return null
                            }
                        }
                    } ?: return null
            } ?: return null
    }

    private data class ReminderTaskModel(
        val reminderModel: ReminderModel,
        val taskModel: TaskModel
    )

    companion object {
        private const val NOTIFICATION_REMINDER_CHANNEL_ID = "NOTIFICATION_REMINDER_CHANNEL_ID"
    }

}