package com.example.noteskeeper.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM task_table")
    fun getTasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM task_table order by created desc")
    fun getTasksSortByDate(): Flow<List<Task>>

    @Query("SELECT * FROM task_table order by completed desc")
    fun getTasksSortByStatus(): Flow<List<Task>>
}