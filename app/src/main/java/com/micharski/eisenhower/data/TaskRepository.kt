package com.micharski.eisenhower.data

import com.micharski.eisenhower.data.persistence.TaskDao
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

class TaskRepository(private val taskDao: TaskDao) {
    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun getTaskById(taskId: Uuid): Task? = taskDao.getTaskById(taskId)

    suspend fun insertTask(task: Task) = taskDao.insertTask(task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    suspend fun deleteTaskById(taskId: Uuid) = taskDao.deleteTaskById(taskId)

    suspend fun getPendingTasksWithReminders(): List<Task> = taskDao.getPendingTasksWithReminders()
}
