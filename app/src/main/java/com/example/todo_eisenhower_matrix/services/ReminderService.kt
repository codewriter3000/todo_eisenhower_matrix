package com.example.todo_eisenhower_matrix.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.EXTRA_NOTIFICATION_ID
import androidx.core.app.NotificationManagerCompat
import com.example.todo_eisenhower_matrix.MainActivity
import com.example.todo_eisenhower_matrix.R

// TODO Execute channel creation as soon as app opens
class ReminderService(content: String) {
    // Constants
    val channelId = "eisenhower_matrix_reminder"
    val notificationId = 101

    val textTitle = "Reminder"
    val textContent = content
    fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel (API 26+ only)
        val name = context.getString(R.string.channel_name)
        val descriptionText = context.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system.
        val notificationManager: NotificationManager =
            context.getSystemService(NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // 2. Build & Post the Notification
    @RequiresPermission("android.permission.POST_NOTIFICATIONS")
    fun showNotification(context: Context) {
        // Make sure the channel exists first
        createNotificationChannel(context)

        val tapAction = tapAction(context)
        val snoozeAction = bottomAction("ACTION_SNOOZE", 1, context)
        val completeAction = bottomAction("ACTION_COMPLETE", 2, context)

        // Construct the notification visual & behavior properties
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your drawable resource
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(tapAction)
            .addAction(R.drawable.ic_launcher_foreground, "Snooze", snoozeAction)
            .addAction(R.drawable.ic_launcher_foreground, "Complete", completeAction)
            .setAutoCancel(true) // Automatically dismisses notification when tapped

        // Display the notification
        with(NotificationManagerCompat.from(context)) {
            // Requires POST_NOTIFICATIONS permission check on Android 13+ (API 33+)
            notify(notificationId, builder.build())
        }
    }

    private fun tapAction(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        return pendingIntent
    }

    private fun bottomAction(myAction: String, requestCode: Int, context: Context): PendingIntent {
        val intent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = myAction
            putExtra(EXTRA_NOTIFICATION_ID, notificationId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        return pendingIntent
    }
}

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1)

        when (intent.action) {
            "ACTION_SNOOZE" -> {
                // TODO handle snooze logic

                if (notificationId == -1) {
                    NotificationManagerCompat.from(context).cancel(notificationId)
                }
            }
            "ACTION_COMPLETE" -> {
                // TODO handle complete logic

                if (notificationId == -1) {
                    NotificationManagerCompat.from(context).cancel(notificationId)
                }
            }
        }
    }
}