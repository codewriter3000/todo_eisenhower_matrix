package com.example.todo_eisenhower_matrix.ui.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.HorizontalDivider
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
    var listView by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Carbon.colors.background)
    ) {
        CarbonSubThemeG100 {
            CarbonHeader(
                title = "Eisenhower Matrix"
            ) {
                IconButton(onClick = { listView = !listView }) {
                    Icon(
                        imageVector = if (listView) Icons.Default.GridView else Icons.AutoMirrored.Filled.List,
                        contentDescription = if (listView) "Switch to quadrant view" else "Switch to list view",
                        tint = Carbon.colors.onUiShell
                    )
                }
                IconButton(onClick = onAddTaskClick) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add task",
                        tint = Carbon.colors.onUiShell
                    )
                }
            }
        }

        if (listView) {
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
        } else {
            QuadrantMatrix(
                tasks = tasks,
                onTaskClick = onTaskClick,
                onToggleTask = onToggleTask,
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.navigationBars)
            )
        }
    }
}

@Composable
fun QuadrantMatrix(
    tasks: List<Task>,
    onTaskClick: (Task) -> Unit,
    onToggleTask: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    val doFirst = tasks.filter { it.isUrgent && it.isImportant }
    val schedule = tasks.filter { !it.isUrgent && it.isImportant }
    val delegate = tasks.filter { it.isUrgent && !it.isImportant }
    val eliminate = tasks.filter { !it.isUrgent && !it.isImportant }

    Column(modifier = modifier) {
        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            QuadrantCell(
                label = "Do First",
                accentColor = Carbon.colors.buttonDanger,
                tasks = doFirst,
                onTaskClick = onTaskClick,
                onToggleTask = onToggleTask,
                modifier = Modifier.weight(1f).fillMaxHeight()
            )
            QuadrantCell(
                label = "Schedule",
                accentColor = Carbon.colors.supportInfo,
                tasks = schedule,
                onTaskClick = onTaskClick,
                onToggleTask = onToggleTask,
                modifier = Modifier.weight(1f).fillMaxHeight()
            )
        }
        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            QuadrantCell(
                label = "Delegate",
                accentColor = Carbon.colors.supportWarning,
                tasks = delegate,
                onTaskClick = onTaskClick,
                onToggleTask = onToggleTask,
                modifier = Modifier.weight(1f).fillMaxHeight()
            )
            QuadrantCell(
                label = "Eliminate",
                accentColor = Carbon.colors.textSecondary,
                tasks = eliminate,
                onTaskClick = onTaskClick,
                onToggleTask = onToggleTask,
                modifier = Modifier.weight(1f).fillMaxHeight()
            )
        }
    }
}

@Composable
fun QuadrantCell(
    label: String,
    accentColor: Color,
    tasks: List<Task>,
    onTaskClick: (Task) -> Unit,
    onToggleTask: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .border(width = 1.dp, color = Carbon.colors.layerHover)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(accentColor.copy(alpha = 0.15f))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(accentColor, shape = androidx.compose.foundation.shape.CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$label (${tasks.size})",
                style = Carbon.typography.label01,
                color = Carbon.colors.textPrimary
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(
                items = tasks,
                key = { task -> task.id }
            ) { task ->
                CompactTaskItem(
                    task = task,
                    onTaskClick = onTaskClick,
                    onToggleTask = onToggleTask
                )
            }
        }
    }
}

@Composable
fun CompactTaskItem(
    task: Task,
    onTaskClick: (Task) -> Unit,
    onToggleTask: (Task) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTaskClick(task) }
            .background(Carbon.colors.layer)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CarbonCheckbox(
            checked = task.isComplete,
            onCheckedChange = { onToggleTask(task) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = task.title,
            style = Carbon.typography.caption01,
            textDecoration = if (task.isComplete) TextDecoration.LineThrough else TextDecoration.None,
            color = if (task.isComplete) Carbon.colors.textSecondary else Carbon.colors.textPrimary,
            maxLines = 2,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
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
