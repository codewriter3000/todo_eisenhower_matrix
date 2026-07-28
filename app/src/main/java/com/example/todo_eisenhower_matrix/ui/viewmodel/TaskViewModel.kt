package com.example.todo_eisenhower_matrix.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.content.Context
import com.example.todo_eisenhower_matrix.data.Task
import com.example.todo_eisenhower_matrix.data.TaskRepository
import com.example.todo_eisenhower_matrix.data.persistence.TaskDatabase
import com.example.todo_eisenhower_matrix.services.ReminderScheduler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(context: Context) : ViewModel() {
    private val repository: TaskRepository

    init {
        val database = TaskDatabase.getDatabase(context)
        repository = TaskRepository(database.taskDao())
    }

    // Holds the list of tasks. MutableStateFlow automatically updates the UI when changed.
    val tasks: StateFlow<List<Task>> = repository.getAllTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTask(context: Context, task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
            ReminderScheduler.scheduleReminder(context, task)
        }
    }

    fun updateTask(context: Context, updatedTask: Task) {
        viewModelScope.launch {
            repository.updateTask(updatedTask)
            ReminderScheduler.scheduleReminder(context, updatedTask)
        }
    }

    fun deleteTask(taskId: kotlin.uuid.Uuid) {
        viewModelScope.launch {
            repository.deleteTaskById(taskId)
        }
    }

    fun toggleTaskCompletion(taskId: kotlin.uuid.Uuid) {
        viewModelScope.launch {
            val task = repository.getTaskById(taskId)
            task?.let {
                val updatedTask = it.copy(isComplete = !it.isComplete)
                repository.updateTask(updatedTask)
            }
        }
    }
}
