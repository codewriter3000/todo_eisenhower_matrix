package com.example.todo_eisenhower_matrix

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.todo_eisenhower_matrix.services.ReminderService
import com.example.todo_eisenhower_matrix.ui.composable.TaskAppNavigation
import com.example.todo_eisenhower_matrix.ui.theme.Todo_Eisenhower_MatrixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ReminderService.createNotificationChannel(this)
        setContent {
            Todo_Eisenhower_MatrixTheme {
                NotificationPermissionHandler()
                TaskAppNavigation()
            }
        }
    }
}

@Composable
fun NotificationPermissionHandler() {
    val context = LocalContext.current
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { _ ->
                // Permission result handled by system, or add custom logic here
            }
        )

        LaunchedEffect(Unit) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Preview
@Composable
fun TaskAppPreview() {
    Todo_Eisenhower_MatrixTheme {
        TaskAppNavigation()
    }
}
