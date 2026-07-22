package com.example.todo_eisenhower_matrix.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.todo_eisenhower_matrix.data.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TaskViewModel : ViewModel() {
    // Holds the list of tasks. MutableStateFlow automatically updates the UI when changed.
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    fun addTask(task: Task) {
        _tasks.value += task
    }

    fun updateTask(updatedTask: Task) {
        _tasks.value = _tasks.value.map { task ->
            if (task.id == updatedTask.id) updatedTask else task
        }
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
