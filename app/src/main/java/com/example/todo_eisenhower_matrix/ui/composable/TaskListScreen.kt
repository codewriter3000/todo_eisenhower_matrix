package com.example.todo_eisenhower_matrix.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo_eisenhower_matrix.data.Task

// The main Screen Composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    tasks: List<Task> = emptyList(),
    onAddTaskClick: () -> Unit = {},
    onTaskClick: (Task) -> Unit = {},
    onToggleTask: (Task) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Eisenhower Matrix") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddTaskClick() }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        TaskList(
            tasks = tasks,
            modifier = Modifier.padding(paddingValues),
            onTaskClick = onTaskClick,
            onToggleTask = onToggleTask
        )
    }
}

// 3. The List Composable
@Composable
fun TaskList(
    tasks: List<Task>,
    modifier: Modifier = Modifier,
    onTaskClick: (Task) -> Unit = {},
    onToggleTask: (Task) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = tasks,
            key = { task -> task.id }
        ) { task ->
            TaskItem(task = task, onTaskClick = onTaskClick, onToggleTask = onToggleTask)
        }
    }
}

// 4. The individual Task Row Composable
@Composable
fun TaskItem(
    task: Task,
    onTaskClick: (Task) -> Unit,
    onToggleTask: (Task) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTaskClick(task) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isComplete,
                onCheckedChange = { onToggleTask(task) }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.isComplete) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (task.isComplete) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Displaying the Due Date
                Text(
                    text = "Due: ${task.getFormattedDueDate()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Conditionally displaying the Reminder Time
                task.getFormattedReminder()?.let { reminder ->
                    Text(
                        text = "Reminder: $reminder",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                MatrixLabel(task.isUrgent, task.isImportant)
            }
        }
    }
}

// 5. Helper UI component
@Composable
fun MatrixLabel(isUrgent: Boolean, isImportant: Boolean) {
    val labelText = when {
        isUrgent && isImportant -> "Urgent and Important"
        !isUrgent && isImportant -> "Important"
        isUrgent && !isImportant -> "Urgent"
        else -> ""
    }

    Text(
        text = labelText,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.primary
    )
}

@Preview
@Composable
fun TaskListScreenPreview() {
    TaskListScreen()
}