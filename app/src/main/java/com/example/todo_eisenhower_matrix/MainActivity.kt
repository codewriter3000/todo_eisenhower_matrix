package com.example.todo_eisenhower_matrix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.todo_eisenhower_matrix.ui.composable.TaskAppNavigation
import com.example.todo_eisenhower_matrix.ui.theme.Todo_Eisenhower_MatrixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Todo_Eisenhower_MatrixTheme {
                TaskAppNavigation()
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