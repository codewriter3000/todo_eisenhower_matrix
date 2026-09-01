package com.micharski.eisenhower.ui.composable

import android.text.format.DateFormat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import com.micharski.eisenhower.ui.theme.Carbon

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
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate()
                        onConfirm(selectedDate)
                    }
                },
                colors = ButtonDefaults.textButtonColors(contentColor = Carbon.colors.buttonPrimary)
            ) { Text("Confirm") }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = Carbon.colors.textSecondary)
            ) { Text("Cancel") }
        }
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                titleContentColor = Carbon.colors.textPrimary,
                headlineContentColor = Carbon.colors.textPrimary,
                selectedDayContainerColor = Carbon.colors.buttonPrimary,
                selectedDayContentColor = Carbon.colors.textOnColor,
                todayContentColor = Carbon.colors.buttonPrimary,
                todayDateBorderColor = Carbon.colors.buttonPrimary
            )
        )
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
                TextButton(
                    onClick = { isPickingTime = true },
                    colors = ButtonDefaults.textButtonColors(contentColor = Carbon.colors.buttonPrimary)
                ) { Text("Next") }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(contentColor = Carbon.colors.textSecondary)
                ) { Text("Cancel") }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    titleContentColor = Carbon.colors.textPrimary,
                    headlineContentColor = Carbon.colors.textPrimary,
                    selectedDayContainerColor = Carbon.colors.buttonPrimary,
                    selectedDayContentColor = Carbon.colors.textOnColor,
                    todayContentColor = Carbon.colors.buttonPrimary,
                    todayDateBorderColor = Carbon.colors.buttonPrimary
                )
            )
        }
    } else {
        // Step 2: Pick Time
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        // Combine the selected Date and Time into a single LocalDateTime
                        val selectedMillis = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                        val selectedDate = Instant.ofEpochMilli(selectedMillis)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate()

                        val selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)

                        onConfirm(LocalDateTime.of(selectedDate, selectedTime))
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Carbon.colors.buttonPrimary)
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { isPickingTime = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = Carbon.colors.textSecondary)
                ) { Text("Back") }
            },
            text = {
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = Carbon.colors.layer,
                        selectorColor = Carbon.colors.buttonPrimary,
                        periodSelectorSelectedContainerColor = Carbon.colors.buttonPrimary.copy(alpha = 0.2f),
                        periodSelectorSelectedContentColor = Carbon.colors.buttonPrimary,
                        timeSelectorSelectedContainerColor = Carbon.colors.buttonPrimary.copy(alpha = 0.2f),
                        timeSelectorSelectedContentColor = Carbon.colors.buttonPrimary
                    )
                )
            }
        )
    }
}
