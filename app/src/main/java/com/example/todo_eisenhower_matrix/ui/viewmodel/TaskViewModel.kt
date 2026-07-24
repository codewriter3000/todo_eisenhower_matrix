package com.example.todo_eisenhower_matrix.ui.viewmodel

import androidx.lifecycle.ViewModel
import android.content.Context
import com.example.todo_eisenhower_matrix.data.Task
import com.example.todo_eisenhower_matrix.services.ReminderService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TaskViewModel : ViewModel() {
    // Holds the list of tasks. MutableStateFlow automatically updates the UI when changed.
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    fun addTask(context: Context, task: Task) {
        _tasks.value += task
        ReminderService.scheduleReminderNotification(context, task)
    }

    fun updateTask(context: Context, updatedTask: Task) {
        _tasks.value = _tasks.value.map { task ->
            if (task.id == updatedTask.id) updatedTask else task
        }
        ReminderService.scheduleReminderNotification(context, updatedTask)
    }

    fun deleteTask(taskId: kotlin.uuid.Uuid) {
        _tasks.value = _tasks.value.filter { it.id != taskId }
    }

    fun toggleTaskCompletion(taskId: kotlin.uuid.Uuid) {
        _tasks.value = _tasks.value.map { task ->
            if (task.id == taskId) task.copy(isComplete = !task.isComplete) else task
        }
    }
}
