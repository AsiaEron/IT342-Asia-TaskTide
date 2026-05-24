package edu.cit.asia.tasktide.mobile.shared.network

import edu.cit.asia.tasktide.mobile.shared.model.AddTaskRequest
import edu.cit.asia.tasktide.mobile.shared.model.AuthResponse
import edu.cit.asia.tasktide.mobile.shared.model.RegisterRequest
import edu.cit.asia.tasktide.mobile.shared.model.RegisterResponse
import edu.cit.asia.tasktide.mobile.shared.model.TaskDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("users/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("users/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("api/tasks/user/{userId}")
    suspend fun getTasksByUser(@Path("userId") userId: Int): List<TaskDto>

    @POST("api/tasks")
    suspend fun addTask(@Body request: AddTaskRequest): TaskDto

    @DELETE("api/tasks/{taskId}")
    suspend fun deleteTask(@Path("taskId") taskId: Int): Response<Unit>
}
