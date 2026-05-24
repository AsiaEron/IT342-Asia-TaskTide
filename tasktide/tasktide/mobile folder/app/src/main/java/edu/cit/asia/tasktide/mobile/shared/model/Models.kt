package edu.cit.asia.tasktide.mobile.shared.model

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

data class AuthResponse(
    val token: String,
    val userId: Int?,
    val email: String?
)

data class RegisterResponse(
    val userId: Int,
    val email: String,
    val message: String
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
