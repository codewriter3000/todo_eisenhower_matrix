package com.example.todo_eisenhower_matrix.services

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.EXTRA_NOTIFICATION_ID
import androidx.core.app.NotificationManagerCompat
import com.example.todo_eisenhower_matrix.MainActivity
import com.example.todo_eisenhower_matrix.R
import com.example.todo_eisenhower_matrix.data.Task
import java.time.ZoneId

object ReminderService {
    const val CHANNEL_ID = "eisenhower_matrix_reminder"
    const val NOTIFICATION_ID_BASE = 1000

    fun createNotificationChannel(context: Context) {
        val name = context.getString(R.string.channel_name)
        val descriptionText = context.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun scheduleReminderNotification(context: Context, task: Task) {
        val reminderTime = task.reminderTime ?: return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = "ACTION_SHOW_NOTIFICATION"
            putExtra("TASK_ID", task.id.toString())
            putExtra("TASK_TITLE", task.title)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerAtMillis = reminderTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            } else {
                // Fallback for devices where permission is not granted
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }

    fun showNotification(context: Context, taskTitle: String, notificationId: Int) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val tapAction = getTapAction(context)
        val snoozeAction = getBottomAction("ACTION_SNOOZE", 1, context, notificationId)
        val completeAction = getBottomAction("ACTION_COMPLETE", 2, context, notificationId)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Reminder")
            .setContentText(taskTitle)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(tapAction)
            .addAction(R.drawable.ic_launcher_foreground, "Snooze", snoozeAction)
            .addAction(R.drawable.ic_launcher_foreground, "Complete", completeAction)
            .setAutoCancel(true)

        NotificationManagerCompat.from(context).notify(notificationId, builder.build())
    }

    private fun getTapAction(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getBottomAction(
        myAction: String,
        requestCode: Int,
        context: Context,
        notificationId: Int
    ): PendingIntent {
        val intent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = myAction
            putExtra(EXTRA_NOTIFICATION_ID, notificationId)
        }
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }
}

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1)
        val taskTitle = intent.getStringExtra("TASK_TITLE") ?: "Task Reminder"

        when (intent.action) {
            "ACTION_SHOW_NOTIFICATION" -> {
                val taskId = intent.getStringExtra("TASK_ID")
                val id = taskId?.hashCode() ?: ReminderService.NOTIFICATION_ID_BASE
                ReminderService.showNotification(context, taskTitle, id)
            }
            "ACTION_SNOOZE" -> {
                // TODO handle snooze logic
                if (notificationId != -1) {
                    NotificationManagerCompat.from(context).cancel(notificationId)
                }
            }
            "ACTION_COMPLETE" -> {
                // TODO handle complete logic
                if (notificationId != -1) {
                    NotificationManagerCompat.from(context).cancel(notificationId)
                }
            }
        }
    }
}
