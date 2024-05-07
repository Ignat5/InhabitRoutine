package com.ignatlegostaev.inhabitroutine.platform.reminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ignatlegostaev.inhabitroutine.core.di.components.PresentationComponent
import kotlinx.coroutines.launch

class BootCompletedBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val pendingResult = goAsync()
            PresentationComponent.externalScope.launch {
                PresentationComponent.setUpAllRemindersUseCase()
                pendingResult.finish()
            }
        }
    }
}