package com.example.todo_eisenhower_matrix.data

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.uuid.Uuid

data class Task(
    val id: Uuid = Uuid.random(),
    val title: String,
    val description: String? = null,
    val isComplete: Boolean = false,
    val isUrgent: Boolean = false,
    val isImportant: Boolean = false,
    val dueDate: LocalDateTime? = null,
    val reminderTime: LocalDateTime? = null
) {
    fun getFormattedDueDate(): String? {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return dueDate?.format(formatter)
    }
    fun getFormattedReminder(): String? {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return dueDate?.format(formatter)
    }
}