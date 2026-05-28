package edu.cit.asia.tasktide.mobile.shared.network

import edu.cit.asia.tasktide.mobile.features.task.model.AddTaskRequest
import edu.cit.asia.tasktide.mobile.features.auth.model.AuthResponse
import edu.cit.asia.tasktide.mobile.features.auth.model.LoginRequest
import edu.cit.asia.tasktide.mobile.features.auth.model.RegisterRequest
import edu.cit.asia.tasktide.mobile.features.auth.model.RegisterResponse
import edu.cit.asia.tasktide.mobile.features.auth.model.VerifyEmailRequest
import edu.cit.asia.tasktide.mobile.features.task.model.TaskDto
import edu.cit.asia.tasktide.mobile.features.admin.model.UserSummary
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.PUT

interface ApiService {

    @POST("users/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("users/verify-email")
    suspend fun verifyEmail(@Body request: VerifyEmailRequest): Response<String>

    @GET("users/all")
    suspend fun getAllUsers(): List<UserSummary>

    @DELETE("users/{userId}")
    suspend fun deleteUser(@Path("userId") userId: Int): Response<Unit>

    @POST("users/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("api/tasks/user/{userId}")
    suspend fun getTasksByUser(@Path("userId") userId: Int): List<TaskDto>

    @POST("api/tasks")
    suspend fun addTask(@Body request: AddTaskRequest): TaskDto

    @PUT("api/tasks/{taskId}")
    suspend fun updateTask(@Path("taskId") taskId: Int, @Body request: AddTaskRequest): TaskDto

    @DELETE("api/tasks/{taskId}")
    suspend fun deleteTask(@Path("taskId") taskId: Int): Response<Unit>
}
