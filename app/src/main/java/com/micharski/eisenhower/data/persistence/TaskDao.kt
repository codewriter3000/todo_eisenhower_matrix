package com.micharski.eisenhower.data.persistence

import androidx.room.*
import com.micharski.eisenhower.data.Task
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Uuid): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Uuid)

    @Query("SELECT * FROM tasks WHERE reminderTime IS NOT NULL AND isComplete = 0")
    suspend fun getPendingTasksWithReminders(): List<Task>
}
