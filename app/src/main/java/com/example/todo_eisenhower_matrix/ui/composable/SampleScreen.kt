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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// 1. Updated Data Model
data class Task(
    val id: Int, // Retained for LazyColumn key tracking
    val title: String, // Retained for main UI display
    val isComplete: Boolean = false,
    val isUrgent: Boolean = false,
    val isImportant: Boolean = false,
    val dueDate: LocalDateTime,
    val reminderTime: LocalDateTime? = null
) {
    fun getFormattedDueDate(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return dueDate.format(formatter)
    }

    // Helper function added to format the reminder using the same 24-hour pattern
    fun getFormattedReminder(): String? {
        if (reminderTime == null) return null
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return reminderTime.format(formatter)
    }
}

// 2. The main Screen Composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen() {
    val tasks = remember {
        listOf(
            Task(
                id = 1,
                title = "Fix production crash",
                isComplete = false,
                isUrgent = true,
                isImportant = true,
                dueDate = LocalDateTime.now().plusHours(2),
                reminderTime = LocalDateTime.now().plusHours(1)
            ),
            Task(
                id = 2,
                title = "Plan Q3 roadmap",
                isComplete = false,
                isUrgent = false,
                isImportant = true,
                dueDate = LocalDateTime.now().plusDays(3),
                reminderTime = null // No reminder set
            ),
            Task(
                id = 3,
                title = "Reply to team emails",
                isComplete = true,
                isUrgent = true,
                isImportant = false,
                dueDate = LocalDateTime.now().minusDays(1),
                reminderTime = null
            )
        )
    }

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
            FloatingActionButton(onClick = { /* TODO: Add Task */ }) {
                Text("+")
            }
        }
    ) { paddingValues ->
        TaskList(
            tasks = tasks,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

// 3. The List Composable
@Composable
fun TaskList(tasks: List<Task>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = tasks,
            key = { task -> task.id }
        ) { task ->
            TaskItem(task = task)
        }
    }
}

// 4. The individual Task Row Composable
@Composable
fun TaskItem(task: Task) {
    var isChecked by remember { mutableStateOf(task.isComplete) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: Open task details */ },
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
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (isChecked) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
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
    // TODO change these labels to be more descriptive
    val labelText = when {
        isUrgent && isImportant -> "Do First"
        !isUrgent && isImportant -> "Schedule"
        isUrgent && !isImportant -> "Delegate"
        else -> "Eliminate"
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