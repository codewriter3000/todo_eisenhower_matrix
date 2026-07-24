package com.example.todo_eisenhower_matrix.services

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import com.example.todo_eisenhower_matrix.data.Task
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import java.time.LocalDateTime

class ReminderServiceTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockAlarmManager: AlarmManager

    @Mock
    private lateinit var mockNotificationManager: NotificationManager

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        whenever(mockContext.getSystemService(Context.ALARM_SERVICE)).thenReturn(mockAlarmManager)
        whenever(mockContext.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(mockNotificationManager)
        // Mock strings for notification channel
        whenever(mockContext.getString(any())).thenReturn("Mock String")
    }

    @Test
    fun testScheduleReminderNotification() {
        val reminderTime = LocalDateTime.now().plusHours(1)
        val task = Task(title = "Test Task", reminderTime = reminderTime)

        ReminderService.scheduleReminderNotification(mockContext, task)

        // Verify that setExactAndAllowWhileIdle (or setAndAllowWhileIdle depending on SDK) was called
        // Since we are running in a JVM test, Build.VERSION.SDK_INT will be 0 or similar.
        // In ReminderService.kt, it handles different SDK versions.
        
        verify(mockAlarmManager).setExactAndAllowWhileIdle(any(), any(), any())
    }
}
