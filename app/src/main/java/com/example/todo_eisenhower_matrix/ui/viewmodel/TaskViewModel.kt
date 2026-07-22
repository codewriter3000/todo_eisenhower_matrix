package com.example.todo_eisenhower_matrix.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.todo_eisenhower_matrix.data.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime

class TaskViewModel : ViewModel() {
    // Holds the list of tasks. MutableStateFlow automatically updates the UI when changed.
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    fun addTask(title: String, dueDate: LocalDateTime) {
        val newTask = Task(
            title = title,
            dueDate = dueDate
        )
        // Append the new task to the existing list
        _tasks.value += newTask
    }

    fun updateTask(updatedTask: com.example.todo_eisenhower_matrix.data.Task) {
        // Find the task by ID and replace it, keeping the rest of the list intact
        _tasks.value = _tasks.value.map { task ->
            if (task.id == updatedTask.id) updatedTask else task
        }
    }
}