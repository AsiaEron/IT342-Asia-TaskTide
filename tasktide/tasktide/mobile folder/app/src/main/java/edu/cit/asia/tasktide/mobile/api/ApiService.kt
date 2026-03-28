package edu.cit.asia.tasktide.mobile.api

import edu.cit.asia.tasktide.mobile.data.AddTaskRequest
import edu.cit.asia.tasktide.mobile.data.LoginRequest
import edu.cit.asia.tasktide.mobile.data.RegisterRequest
import edu.cit.asia.tasktide.mobile.data.TaskDto
import edu.cit.asia.tasktide.mobile.data.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("users/register")
    suspend fun register(@Body request: RegisterRequest): UserDto

    @POST("users/login")
    suspend fun login(@Body request: LoginRequest): Response<String>

    @GET("users/all")
    suspend fun getAllUsers(): List<UserDto>

    @GET("tasks/user/{userId}")
    suspend fun getTasksByUser(@Path("userId") userId: Int): List<TaskDto>

    @POST("tasks/add")
    suspend fun addTask(@Body request: AddTaskRequest): TaskDto

    @DELETE("tasks/delete/{taskId}")
    suspend fun deleteTask(@Path("taskId") taskId: Int): Response<Unit>
}
