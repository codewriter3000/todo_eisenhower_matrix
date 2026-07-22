package com.example.todo_eisenhower_matrix.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter

import com.example.todo_eisenhower_matrix.data.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    task: Task,
    onSave: (Task) -> Unit,
    onCancel: () -> Unit
) {
    // 1. Local state initialized with the existing task data
    var title by remember { mutableStateOf(task.title) }
    var isUrgent by remember { mutableStateOf(task.isUrgent) }
    var isImportant by remember { mutableStateOf(task.isImportant) }
    var isComplete by remember { mutableStateOf(task.isComplete) }

    // In a production app, updating these would open DatePicker/TimePicker dialogs
    var dueDate by remember { mutableStateOf(task.dueDate) }
    var reminderTime by remember { mutableStateOf(task.reminderTime) }

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Task") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        // Pass the modified task back
                        onSave(
                            task.copy(
                                title = title,
                                isUrgent = isUrgent,
                                isImportant = isImportant,
                                isComplete = isComplete,
                                dueDate = dueDate,
                                reminderTime = reminderTime
                            )
                        )
                    }) {
                        Text("Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 2. Title Input
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Task Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )

            // 3. Matrix Toggles (Urgent / Important)
            Text(text = "Matrix Category", style = MaterialTheme.typography.titleMedium)

            SwitchRow(
                label = "Is Urgent?",
                checked = isUrgent,
                onCheckedChange = { isUrgent = it }
            )

            SwitchRow(
                label = "Is Important?",
                checked = isImportant,
                onCheckedChange = { isImportant = it }
            )

            MatrixLabel(isUrgent = isUrgent, isImportant = isImportant)

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )

            // 4. Date and Time Settings
            Text(text = "Timing", style = MaterialTheme.typography.titleMedium)

            // Due Date Row (Simulates a clickable area to open a picker)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* TODO: Open Date/Time Picker */ }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.DateRange, contentDescription = "Due Date")
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Due Date")
                    Text(
                        text = dueDate?.format(dateFormatter) ?: "No due date set",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Reminder Time Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* TODO: Open Date/Time Picker */ }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Notifications, contentDescription = "Reminder")
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Reminder")
                    Text(
                        text = reminderTime?.format(dateFormatter) ?: "None set",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )

            // 5. Completion Status
            SwitchRow(
                label = "Mark as Complete",
                checked = isComplete,
                onCheckedChange = { isComplete = it }
            )
        }
    }
}

// Helper Composable for standardizing the Switch rows
@Composable
fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Preview
@Composable
fun EditTaskScreenPreview() {
    EditTaskScreen(
        task = Task(
            title = "Sample Task",
            description = "This is a sample task for demonstration purposes.",
            isUrgent = true,
            isImportant = false,
            isComplete = false,
            dueDate = null,
            reminderTime = null
        ),
        onSave = {},
        onCancel = {}
    )
}