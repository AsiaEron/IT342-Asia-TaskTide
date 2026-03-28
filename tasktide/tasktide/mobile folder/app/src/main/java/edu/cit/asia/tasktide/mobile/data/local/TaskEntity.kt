package edu.cit.asia.tasktide.mobile.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    @ColumnInfo(name = "task_id")
    val taskId: Int,
    @ColumnInfo(name = "task_name")
    val taskName: String,
    val description: String?,
    @ColumnInfo(name = "energy_level")
    val energyLevel: String?,
    val status: String?,
    @ColumnInfo(name = "owner_user_id", index = true)
    val ownerUserId: Int
)
