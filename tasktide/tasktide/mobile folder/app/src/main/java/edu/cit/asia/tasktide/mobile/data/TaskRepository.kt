package edu.cit.asia.tasktide.mobile.data

import android.content.Context
import edu.cit.asia.tasktide.mobile.api.ApiClient
import edu.cit.asia.tasktide.mobile.api.TokenManager
import edu.cit.asia.tasktide.mobile.data.local.AppDatabase
import edu.cit.asia.tasktide.mobile.data.local.TaskEntity

class TaskRepository(context: Context) {

    private val api = ApiClient.create(context)
    private val tokenManager = TokenManager(context)
    private val taskDao = AppDatabase.getInstance(context).taskDao()

    suspend fun login(email: String, password: String): Result<Unit> {
        return runCatching {
            val response = api.login(LoginRequest(email, password))
            if (!response.isSuccessful) {
                throw IllegalStateException("Login failed: ${response.code()}")
            }

            val token = response.body()?.trim().orEmpty()
            if (token.isBlank()) {
                throw IllegalStateException("Login token is empty")
            }
            tokenManager.saveToken(token)

            // Backend login returns only JWT, so resolve userId from /users/all
            val users = api.getAllUsers()
            val user = users.firstOrNull { it.email.equals(email, ignoreCase = true) }
                ?: throw IllegalStateException("User ID not found after login")
            tokenManager.saveUserId(user.userId)
        }
    }

    suspend fun register(request: RegisterRequest): Result<Unit> {
        return runCatching {
            api.register(request)
        }.map { }
    }

    suspend fun getTasks(): Result<List<TaskDto>> {
        val userId = tokenManager.getUserId()
        if (userId <= 0) {
            return Result.failure(IllegalStateException("Missing user ID. Please log in again."))
        }

        return try {
            val remote = api.getTasksByUser(userId)
            taskDao.deleteByUserId(userId)
            taskDao.upsertAll(remote.map { it.toEntity(userId) })
            Result.success(remote)
        } catch (e: Exception) {
            val cached = taskDao.getByUserId(userId).map { it.toDto() }
            if (cached.isNotEmpty()) {
                Result.success(cached)
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun addTask(taskName: String, description: String, energyLevel: String): Result<TaskDto> {
        return runCatching {
            val userId = tokenManager.getUserId()
            require(userId > 0) { "Missing user ID. Please log in again." }

            val request = AddTaskRequest(
                taskName = taskName,
                description = description,
                energyLevel = energyLevel,
                status = "PENDING",
                user = UserRef(userId)
            )
            val created = api.addTask(request)
            taskDao.upsertAll(listOf(created.toEntity(userId)))
            created
        }
    }

    suspend fun deleteTask(taskId: Int): Result<Unit> {
        return runCatching {
            val response = api.deleteTask(taskId)
            if (!response.isSuccessful) {
                throw IllegalStateException("Delete failed: ${response.code()}")
            }

            taskDao.deleteByTaskId(taskId)
        }
    }

    fun logout() {
        tokenManager.clear()
    }

    private fun TaskDto.toEntity(userId: Int): TaskEntity {
        return TaskEntity(
            taskId = taskId,
            taskName = taskName,
            description = description,
            energyLevel = energyLevel,
            status = status,
            ownerUserId = userId
        )
    }

    private fun TaskEntity.toDto(): TaskDto {
        return TaskDto(
            taskId = taskId,
            taskName = taskName,
            description = description,
            energyLevel = energyLevel,
            status = status
        )
    }
}
