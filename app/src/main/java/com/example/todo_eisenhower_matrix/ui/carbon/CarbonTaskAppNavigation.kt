package com.example.todo_eisenhower_matrix.ui.carbon

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_eisenhower_matrix.data.Task
import com.example.todo_eisenhower_matrix.ui.viewmodel.TaskViewModel
import java.time.LocalDate
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CarbonTaskAppNavigation() {
    val context = LocalContext.current
    val viewModel: TaskViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return TaskViewModel(context) as T
            }
        }
    )
    
    val tasks by viewModel.tasks.collectAsState()
    var selectedTask by remember { mutableStateOf<Task?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    CarbonTheme {
        if (isEditing && selectedTask != null) {
            CarbonEditTaskScreen(
                task = selectedTask!!,
                onSave = { updatedTask ->
                    if (tasks.any { it.id == updatedTask.id }) {
                        viewModel.updateTask(context, updatedTask)
                    } else {
                        viewModel.addTask(context, updatedTask)
                    }
                    isEditing = false
                },
                onCancel = { isEditing = false },
                onDelete = { taskToDelete ->
                    viewModel.deleteTask(taskToDelete.id)
                    isEditing = false
                }
            )
        } else {
            CarbonTaskListScreen(
                tasks = tasks,
                onAddTaskClick = {
                    selectedTask = Task(title = "", dueDate = LocalDate.now())
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
}

@Preview(showBackground = true)
@Composable
fun CarbonTaskAppNavigationPreview() {
    CarbonTaskAppNavigation()
}
