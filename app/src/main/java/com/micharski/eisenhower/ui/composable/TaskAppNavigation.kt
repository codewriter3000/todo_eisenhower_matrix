package com.micharski.eisenhower.ui.composable

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.micharski.eisenhower.data.Task
import com.micharski.eisenhower.ui.viewmodel.TaskViewModel
import java.time.LocalDate
import androidx.compose.ui.tooling.preview.Preview
import com.micharski.eisenhower.ui.theme.CarbonTheme

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
    
    val tasks by viewModel.tasks.collectAsState()
    var selectedTask by remember { mutableStateOf<Task?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    if (isEditing && selectedTask != null) {
        EditTaskScreen(
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
        TaskListScreen(
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

@Preview(showBackground = true)
@Composable
fun TaskAppNavigationPreview() {
    CarbonTheme {
        TaskAppNavigation()
    }
}
