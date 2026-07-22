package com.example.todo_eisenhower_matrix.data

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Task(
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
}