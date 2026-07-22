package com.example.todo_eisenhower_matrix.ui.composable

import androidx.compose.material3.*
import androidx.compose.runtime.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerDialog(
    initialDate: LocalDateTime = LocalDateTime.now(),
    onDismiss: () -> Unit,
    onConfirm: (LocalDateTime) -> Unit
) {
    // Track whether we are showing the Date picker or Time picker
    var isPickingTime by remember { mutableStateOf(false) }

    // 1. Date Picker State
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    )

    // 2. Time Picker State configured for 24-hour clock
    val timePickerState = rememberTimePickerState(
        initialHour = initialDate.hour,
        initialMinute = initialDate.minute,
        is24Hour = true
    )

    if (!isPickingTime) {
        // Step 1: Pick Date
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = { isPickingTime = true }) { Text("Next") }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    } else {
        // Step 2: Pick Time (Material 3 doesn't have a default TimePickerDialog, so we wrap it in an AlertDialog)
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    // Combine the selected Date and Time into a single LocalDateTime
                    val selectedMillis = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                    val selectedDate = Instant.ofEpochMilli(selectedMillis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()

                    val selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)

                    onConfirm(LocalDateTime.of(selectedDate, selectedTime))
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { isPickingTime = false }) { Text("Back") }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}