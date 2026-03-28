package edu.cit.asia.tasktide.mobile.data

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val fname: String,
    val lname: String,
    val email: String,
    val password: String
)

data class UserDto(
    @SerializedName("user_id") val userId: Int,
    val fname: String,
    val lname: String,
    val email: String
)

data class UserRef(
    @SerializedName("user_id") val userId: Int
)

data class AddTaskRequest(
    @SerializedName("task_name") val taskName: String,
    val description: String,
    @SerializedName("energy_level") val energyLevel: String,
    val status: String,
    val user: UserRef
)

data class TaskDto(
    @SerializedName("task_id") val taskId: Int,
    @SerializedName("task_name") val taskName: String,
    val description: String?,
    @SerializedName("energy_level") val energyLevel: String?,
    val status: String?
)
