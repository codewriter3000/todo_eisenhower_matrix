# Todo Eisenhower Matrix

A modern, high-density Android application designed to help you prioritize tasks using the **Eisenhower Matrix** method, now powered by the **Carbon Design System v11**.

## Features

- **Matrix Categorization**: Scientifically prioritize tasks into four quadrants:
    - **Do First**: Urgent and Important
    - **Schedule**: Important, but not Urgent
    - **Delegate**: Urgent, but not Important
    - **Eliminate**: Neither Urgent nor Important
- **Persistent Storage**: Integrated with a **Room Database**, ensuring your tasks and settings survive app restarts and system-level clearing.
- **Resilient Reminders**: 
    - Set smart reminders with a precise date-and-time picker.
    - **Boot Support**: Alarms are automatically rescheduled when your device reboots.
    - **Lock Screen Support**: High-priority notifications designed to break through the lock screen for immediate action.
- **Productive UI**: 
    - Migrated to the **Carbon Design System v11** for a professional, information-dense aesthetic.
    - **Adaptive Theming**: Full support for system-level Light and Dark modes.
    - **Edge-to-Edge**: Optimized for modern Android devices with seamless status and navigation bar integration.

## Tech Stack

- **Design System**: Carbon Design System v11 (Manual Implementation)
- **UI Framework**: Jetpack Compose
- **Persistence**: Room Database
- **Architecture**: MVVM with Repository pattern
- **Background Tasks**: AlarmManager, BroadcastReceivers
- **Language**: Kotlin
- **Time Handling**: `java.time` (LocalDate, LocalDateTime)

## Getting Started

1. Clone the repository.
2. Open the project in **Android Studio** (Koala or newer recommended).
3. Ensure you have the **KSP** plugin active for Room code generation.
4. Build and run the app. Grant notification permissions when prompted to enable reminders.

## Testing

The project includes specialized test suites:
- **DateTimeConversionTest**: Verifies UTC-safe date handling across global time zones.
- **ReminderResilienceTest**: Instrumented tests verifying persistence and boot-completed alarm rescheduling.
- **ReminderServiceTest**: Unit tests for notification building and scheduling logic.

## Screenshots

*(Screenshots coming soon)*
