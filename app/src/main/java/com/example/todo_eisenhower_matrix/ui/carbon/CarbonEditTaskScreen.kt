package com.example.todo_eisenhower_matrix.ui.carbon

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo_eisenhower_matrix.data.Task
import java.time.format.DateTimeFormatter

@Composable
fun CarbonEditTaskScreen(
    task: Task,
    onSave: (Task) -> Unit,
    onCancel: () -> Unit,
    onDelete: (Task) -> Unit = {}
) {
    var title by remember { mutableStateOf(task.title) }
    var isUrgent by remember { mutableStateOf(task.isUrgent) }
    var isImportant by remember { mutableStateOf(task.isImportant) }
    var isComplete by remember { mutableStateOf(task.isComplete) }
    var dueDate by remember { mutableStateOf(task.dueDate) }
    var reminderTime by remember { mutableStateOf(task.reminderTime) }

    var showDueDatePicker by remember { mutableStateOf(false) }
    var showReminderPicker by remember { mutableStateOf(false) }

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CarbonTheme.colors.background)
    ) {
        CarbonHeader(
            title = "Edit task",
            modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
        ) {
            IconButton(onClick = { onDelete(task) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = CarbonTheme.colors.textOnColor)
            }
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            CarbonTextInput(
                value = title,
                onValueChange = { title = it },
                label = "Task title"
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Priority", style = CarbonTheme.typography.heading01)
                CarbonToggle(label = "Urgent", checked = isUrgent, onCheckedChange = { isUrgent = it })
                CarbonToggle(label = "Important", checked = isImportant, onCheckedChange = { isImportant = it })
            }

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(text = "Scheduling", style = CarbonTheme.typography.heading01)
                
                CarbonTimeRow(
                    label = "Due date",
                    value = dueDate?.format(dateFormatter) ?: "None set",
                    icon = Icons.Default.DateRange,
                    onClick = { showDueDatePicker = true }
                )

                CarbonTimeRow(
                    label = "Reminder",
                    value = reminderTime?.format(dateTimeFormatter) ?: "None set",
                    icon = Icons.Default.Notifications,
                    onClick = { showReminderPicker = true }
                )
            }

            CarbonToggle(label = "Mark as complete", checked = isComplete, onCheckedChange = { isComplete = it })
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            CarbonButton(
                text = "Cancel",
                onClick = onCancel,
                backgroundColor = CarbonTheme.colors.buttonSecondary,
                modifier = Modifier.weight(1f)
            )
            CarbonButton(
                text = "Save",
                onClick = {
                    onSave(task.copy(
                        title = title,
                        isUrgent = isUrgent,
                        isImportant = isImportant,
                        isComplete = isComplete,
                        dueDate = dueDate,
                        reminderTime = reminderTime
                    ))
                },
                modifier = Modifier.weight(1f)
            )
        }
    }

    if (showDueDatePicker) {
        com.example.todo_eisenhower_matrix.ui.composable.MyDatePickerDialog(
            initialDate = dueDate ?: java.time.LocalDate.now(),
            onDismiss = { showDueDatePicker = false },
            onConfirm = {
                dueDate = it
                showDueDatePicker = false
            }
        )
    }

    if (showReminderPicker) {
        com.example.todo_eisenhower_matrix.ui.composable.DateTimePickerDialog(
            initialDateTime = reminderTime ?: java.time.LocalDateTime.now(),
            onDismiss = { showReminderPicker = false },
            onConfirm = {
                reminderTime = it
                showReminderPicker = false
            }
        )
    }
}

@Composable
fun CarbonTimeRow(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(CarbonTheme.colors.layer)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = CarbonTheme.colors.textPrimary)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, style = CarbonTheme.typography.label01, color = CarbonTheme.colors.textSecondary)
            Text(text = value, style = CarbonTheme.typography.bodyShort02, color = CarbonTheme.colors.textPrimary)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarbonEditTaskScreenPreview() {
    CarbonTheme {
        CarbonEditTaskScreen(
            task = Task(title = "Redesign UI", isUrgent = true, isImportant = true),
            onSave = {},
            onCancel = {}
        )
    }
}
