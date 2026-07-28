package com.example.todo_eisenhower_matrix.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo_eisenhower_matrix.data.Task
import com.example.todo_eisenhower_matrix.ui.theme.Carbon
import com.example.todo_eisenhower_matrix.ui.theme.CarbonSubThemeG100
import com.example.todo_eisenhower_matrix.ui.theme.CarbonTheme

@Composable
fun TaskListScreen(
    tasks: List<Task> = emptyList(),
    onAddTaskClick: () -> Unit = {},
    onTaskClick: (Task) -> Unit = {},
    onToggleTask: (Task) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Carbon.colors.background)
    ) {
        CarbonSubThemeG100 {
            CarbonHeader(
                title = "Eisenhower Matrix",
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
            ) {
                CarbonButton(
                    text = "Add task",
                    onClick = onAddTaskClick,
                    backgroundColor = Color.Transparent, // Ghost style for header
                    icon = Icons.Default.Add
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars),
            contentPadding = PaddingValues(0.dp)
        ) {
            items(
                items = tasks,
                key = { task -> task.id }
            ) { task ->
                TaskItem(
                    task = task,
                    onTaskClick = onTaskClick,
                    onToggleTask = onToggleTask
                )
                HorizontalDivider(color = Carbon.colors.layerHover)
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onTaskClick: (Task) -> Unit,
    onToggleTask: (Task) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTaskClick(task) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CarbonCheckbox(
            checked = task.isComplete,
            onCheckedChange = { onToggleTask(task) }
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                style = Carbon.typography.bodyShort02,
                fontWeight = FontWeight.SemiBold,
                textDecoration = if (task.isComplete) TextDecoration.LineThrough else TextDecoration.None,
                color = if (task.isComplete) Carbon.colors.textSecondary else Carbon.colors.textPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Due: ${task.getFormattedDueDate() ?: "None"}",
                style = Carbon.typography.caption01,
                color = Carbon.colors.textSecondary
            )
        }

        MatrixLabel(isUrgent = task.isUrgent, isImportant = task.isImportant)
    }
}

@Composable
fun MatrixLabel(isUrgent: Boolean, isImportant: Boolean) {
    val (text, color, isDarkText) = when {
        isUrgent && isImportant -> Triple("Do First", Carbon.colors.buttonDanger, false)
        !isUrgent && isImportant -> Triple("Schedule", Carbon.colors.supportInfo, false)
        isUrgent && !isImportant -> Triple("Delegate", Carbon.colors.supportWarning, true)
        else -> Triple("Eliminate", Carbon.colors.textSecondary, false)
    }

    CarbonTag(
        text = text,
        backgroundColor = color.copy(alpha = 0.2f),
        contentColor = if (isDarkText) Color(0xFF161616) else color
    )
}

@Preview(showBackground = true)
@Composable
fun TaskListScreenPreview() {
    CarbonTheme {
        TaskListScreen(
            tasks = listOf(
                Task(title = "High Priority Task", isUrgent = true, isImportant = true),
                Task(title = "Scheduled Task", isUrgent = false, isImportant = true, isComplete = true),
                Task(title = "Delegate Task", isUrgent = true, isImportant = false)
            )
        )
    }
}
