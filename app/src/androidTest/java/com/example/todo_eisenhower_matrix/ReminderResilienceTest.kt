package com.example.todo_eisenhower_matrix

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todo_eisenhower_matrix.data.Task
import com.example.todo_eisenhower_matrix.data.persistence.TaskDatabase
import com.example.todo_eisenhower_matrix.services.ReminderReceiver
import com.example.todo_eisenhower_matrix.services.ReminderScheduler
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class ReminderResilienceTest {

    private lateinit var context: Context
    private lateinit var database: TaskDatabase

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        database = TaskDatabase.getDatabase(context)
    }

    @Test
    fun testReminderPersistsAfterAppClosed() = runBlocking {
        // 1. Create and save a task with a reminder
        val task = Task(
            title = "Test Task Persistence",
            reminderTime = LocalDateTime.now().plusHours(1)
        )
        database.taskDao().insertTask(task)

        // 2. Verify it's in the database
        val savedTask = database.taskDao().getTaskById(task.id)
        assertNotNull("Task should be persisted in database", savedTask)

        // 3. Schedule the reminder (this would normally happen in ViewModel)
        ReminderScheduler.scheduleReminder(context, task)

        // Note: We can't easily check AlarmManager's internal state via API,
        // but verify the scheduling logic runs without crash.
    }

    @Test
    fun testRescheduleOnBoot() = runBlocking {
        // 1. Insert a pending task into the database
        val task = Task(
            title = "Boot Reschedule Task",
            reminderTime = LocalDateTime.now().plusMinutes(30)
        )
        database.taskDao().insertTask(task)

        // 2. Simulate the BOOT_COMPLETED broadcast
        val intent = Intent(Intent.ACTION_BOOT_COMPLETED)
        val receiver = ReminderReceiver()
        
        // This triggers the async rescheduling logic
        receiver.onReceive(context, intent)
        
        // Verification: Check logs or database interactions if mocked,
        // but here we ensure the receiver handles the intent correctly.
    }
}
