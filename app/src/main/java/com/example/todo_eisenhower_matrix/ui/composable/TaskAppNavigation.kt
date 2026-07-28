package com.example.todo_eisenhower_matrix.ui.composable

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_eisenhower_matrix.data.Task
import com.example.todo_eisenhower_matrix.ui.viewmodel.TaskViewModel
import java.time.LocalDate

@Composable
fun TaskAppNavigation() {
    val context = LocalContext.current
    val viewModel: TaskViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return TaskViewModel(context) as T
            }
        }
    )
    
    // Collect the task list from the ViewModel as Compose state
    val tasks by viewModel.tasks.collectAsState()

    // State to track which screen we are on, and which task is selected
    var selectedTask by remember { mutableStateOf<Task?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    if (isEditing && (selectedTask != null)) {
        EditTaskScreen(
            task = selectedTask!!,
            onSave = { updatedTask ->
                if (tasks.any { it.id == updatedTask.id }) {
                    viewModel.updateTask(context, updatedTask)
                } else {
                    viewModel.addTask(context, updatedTask)
                }
                isEditing = false // Go back to list
            },
            onCancel = { isEditing = false },
            onDelete = { taskToDelete ->
                viewModel.deleteTask(taskToDelete.id)
                isEditing = false
            }
        )
    } else {
        // Pass callbacks to handle the Floating Action Button and List Item clicks
        TaskListScreen(
            tasks = tasks,
            onAddTaskClick = {
                // Create a temporary task and immediately open the edit screen
                val newTask = Task(title = "", dueDate = LocalDate.now())
                selectedTask = newTask
                isEditing = true
            },
            onTaskClick = { clickedTask ->
                selectedTask = clickedTask
                isEditing = true
            },
            onToggleTask = { taskToToggle ->
                viewModel.toggleTaskCompletion(taskToToggle.id)
            }
        )
    }
}
