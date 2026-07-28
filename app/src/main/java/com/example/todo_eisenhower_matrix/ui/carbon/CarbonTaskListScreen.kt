package com.example.todo_eisenhower_matrix.ui.carbon

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
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

@Composable
fun CarbonTaskListScreen(
    tasks: List<Task> = emptyList(),
    onAddTaskClick: () -> Unit = {},
    onTaskClick: (Task) -> Unit = {},
    onToggleTask: (Task) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CarbonTheme.colors.background)
    ) {
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
                CarbonTaskItem(
                    task = task,
                    onTaskClick = onTaskClick,
                    onToggleTask = onToggleTask
                )
                HorizontalDivider(color = CarbonTheme.colors.layerHover)
            }
        }
    }
}

@Composable
fun CarbonTaskItem(
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
                style = CarbonTheme.typography.bodyShort02,
                fontWeight = FontWeight.SemiBold,
                textDecoration = if (task.isComplete) TextDecoration.LineThrough else TextDecoration.None,
                color = if (task.isComplete) CarbonTheme.colors.textSecondary else CarbonTheme.colors.textPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Due: ${task.getFormattedDueDate() ?: "None"}",
                style = CarbonTheme.typography.caption01,
                color = CarbonTheme.colors.textSecondary
            )
        }

        CarbonMatrixTag(isUrgent = task.isUrgent, isImportant = task.isImportant)
    }
}

@Composable
fun CarbonMatrixTag(isUrgent: Boolean, isImportant: Boolean) {
    val (text, color) = when {
        isUrgent && isImportant -> "Do First" to CarbonTheme.colors.buttonDanger
        !isUrgent && isImportant -> "Schedule" to CarbonTheme.colors.supportInfo
        isUrgent && !isImportant -> "Delegate" to CarbonTheme.colors.supportWarning
        else -> "Eliminate" to CarbonTheme.colors.textSecondary
    }

    CarbonTag(
        text = text,
        backgroundColor = color.copy(alpha = 0.2f),
        contentColor = color
    )
}

@Preview(showBackground = true)
@Composable
fun CarbonTaskListScreenPreview() {
    CarbonTheme {
        CarbonTaskListScreen(
            tasks = listOf(
                Task(title = "High Priority Task", isUrgent = true, isImportant = true),
                Task(title = "Scheduled Task", isUrgent = false, isImportant = true, isComplete = true),
                Task(title = "Delegate Task", isUrgent = true, isImportant = false)
            )
        )
    }
}
