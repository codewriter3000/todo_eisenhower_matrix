package com.example.todo_eisenhower_matrix.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.EXTRA_NOTIFICATION_ID
import androidx.core.app.NotificationManagerCompat
import com.example.todo_eisenhower_matrix.MainActivity
import com.example.todo_eisenhower_matrix.R

object ReminderService {
    const val CHANNEL_ID = "eisenhower_matrix_reminder"
    const val NOTIFICATION_ID_BASE = 1000

    fun createNotificationChannel(context: Context) {
        val name = context.getString(R.string.channel_name)
        val descriptionText = context.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return
        notificationManager.createNotificationChannel(channel)
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
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
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
        val intent = Intent(context, ReminderReceiver::class.java).apply {
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
