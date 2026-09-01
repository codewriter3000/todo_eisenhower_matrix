package com.micharski.eisenhower.services

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import com.micharski.eisenhower.data.Task
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
        
        // Mock canScheduleExactAlarms for Android S+ behavior if needed
        try {
            whenever(mockAlarmManager.canScheduleExactAlarms()).thenReturn(true)
        } catch (e: Exception) {
            // may not exist in some classpath configurations
        }
    }

    @Test
    fun testScheduleReminder() {
        // Reminder must be in the future to be scheduled
        val reminderTime = LocalDateTime.now().plusHours(1)
        val task = Task(title = "Test Task", reminderTime = reminderTime)

        ReminderScheduler.scheduleReminder(mockContext, task)

        // Verify that either setExactAndAllowWhileIdle or setAndAllowWhileIdle was called
        verify(mockAlarmManager, atLeastOnce()).setExactAndAllowWhileIdle(any(), any(), anyOrNull())
    }

    @Test
    fun testShowNotification() {
        ReminderService.showNotification(mockContext, "Test Task", 1001)
    }
}
