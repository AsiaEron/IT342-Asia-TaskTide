package edu.cit.asia.tasktide.mobile.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks WHERE owner_user_id = :userId ORDER BY task_id DESC")
    suspend fun getByUserId(userId: Int): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(tasks: List<TaskEntity>)

    @Query("DELETE FROM tasks WHERE owner_user_id = :userId")
    suspend fun deleteByUserId(userId: Int)

    @Query("DELETE FROM tasks WHERE task_id = :taskId")
    suspend fun deleteByTaskId(taskId: Int)

    @Query("DELETE FROM tasks")
    suspend fun deleteAll()
}
