package com.example.todo_eisenhower_matrix.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat.EXTRA_NOTIFICATION_ID
import androidx.core.app.NotificationManagerCompat
import com.example.todo_eisenhower_matrix.data.persistence.TaskDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1)

        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_LOCKED_BOOT_COMPLETED -> {
                rescheduleAlarms(context)
            }
            "ACTION_SNOOZE" -> {
                if (notificationId != -1) {
                    NotificationManagerCompat.from(context).cancel(notificationId)
                }
            }
            "ACTION_COMPLETE" -> {
                if (notificationId != -1) {
                    NotificationManagerCompat.from(context).cancel(notificationId)
                }
            }
            else -> {
                // Handle the alarm trigger
                val taskTitle = intent.getStringExtra("TASK_TITLE") ?: "Task Reminder"
                val taskIdString = intent.getStringExtra("TASK_ID")
                val notificationId = taskIdString?.let { kotlin.uuid.Uuid.parse(it).hashCode() } 
                    ?: ReminderService.NOTIFICATION_ID_BASE

                ReminderService.showNotification(context, taskTitle, notificationId)
            }
        }
    }

    private fun rescheduleAlarms(context: Context) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val database = TaskDatabase.getDatabase(context)
                val pendingTasks = database.taskDao().getPendingTasksWithReminders()
                pendingTasks.forEach { task ->
                    ReminderScheduler.scheduleReminder(context, task)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
