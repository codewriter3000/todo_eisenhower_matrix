package com.example.todo_eisenhower_matrix.ui.composable

import android.text.format.DateFormat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(
    initialDate: LocalDate = LocalDate.now(),
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { millis ->
                    val selectedDate = Instant.ofEpochMilli(millis)
                        .atZone(ZoneOffset.UTC)
                        .toLocalDate()
                    onConfirm(selectedDate)
                }
            }) { Text("Confirm") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerDialog(
    initialDateTime: LocalDateTime = LocalDateTime.now(),
    onDismiss: () -> Unit,
    onConfirm: (LocalDateTime) -> Unit
) {
    // Remember the is24Hour boolean
    val context = LocalContext.current
    val is24Hour = remember { DateFormat.is24HourFormat(context) }

    // Track whether we are showing the Date picker or Time picker
    var isPickingTime by remember { mutableStateOf(false) }

    // 1. Date Picker State
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateTime
            .toLocalDate()
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    )

    // 2. Time Picker State configured for system settings
    val timePickerState = rememberTimePickerState(
        initialHour = initialDateTime.hour,
        initialMinute = initialDateTime.minute,
        is24Hour = is24Hour
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
        // Step 2: Pick Time
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    // Combine the selected Date and Time into a single LocalDateTime
                    val selectedMillis = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                    val selectedDate = Instant.ofEpochMilli(selectedMillis)
                        .atZone(ZoneOffset.UTC)
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
