package edu.cit.asia.tasktide.mobile.features.task

import android.content.Context
import edu.cit.asia.tasktide.mobile.shared.local.AppDatabase
import edu.cit.asia.tasktide.mobile.shared.local.TaskEntity
import edu.cit.asia.tasktide.mobile.features.task.model.AddTaskRequest
import edu.cit.asia.tasktide.mobile.features.auth.model.LoginRequest
import edu.cit.asia.tasktide.mobile.features.auth.model.RegisterRequest
import edu.cit.asia.tasktide.mobile.features.task.model.TaskDto
import edu.cit.asia.tasktide.mobile.features.auth.model.VerifyEmailRequest
import edu.cit.asia.tasktide.mobile.features.task.model.UserRef
import edu.cit.asia.tasktide.mobile.shared.network.ApiClient
import edu.cit.asia.tasktide.mobile.shared.storage.TokenManager

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

            val auth = response.body() ?: throw IllegalStateException("Login response is empty")
            val token = auth.token
            if (token.isBlank()) {
                throw IllegalStateException("Login token is empty")
            }
            val userId = auth.userId ?: throw IllegalStateException("Login response missing user ID")

            tokenManager.saveToken(token)
            tokenManager.saveUserId(userId)
            // save optional role
            val role = try { auth.role } catch (ex: Exception) { null }
            tokenManager.saveUserRole(role)
        }
    }

    suspend fun register(request: RegisterRequest): Result<Unit> {
        return runCatching {
            api.register(request)
        }.map { }
    }

    suspend fun verifyEmail(email: String, verificationCode: String): Result<Unit> {
        return runCatching {
            val response = api.verifyEmail(VerifyEmailRequest(email = email, verificationCode = verificationCode))
            if (!response.isSuccessful) {
                throw IllegalStateException("Verification failed: ${response.code()}")
            }
        }
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
                status = "ACTIVE",
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

    suspend fun updateTask(taskId: Int, taskName: String, description: String, energyLevel: String, status: String): Result<TaskDto> {
        return runCatching {
            val userId = tokenManager.getUserId()
            require(userId > 0) { "Missing user ID. Please log in again." }

            val request = AddTaskRequest(
                taskName = taskName,
                description = description,
                energyLevel = energyLevel,
                status = status,
                user = UserRef(userId)
            )

            val updated = api.updateTask(taskId, request)
            taskDao.upsertAll(listOf(updated.toEntity(userId)))
            updated
        }
    }

    // Admin: list users (non-admins) and delete user
    suspend fun getAllUsers(): Result<List<edu.cit.asia.tasktide.mobile.features.admin.model.UserSummary>> {
        return runCatching {
            api.getAllUsers()
        }
    }

    suspend fun deleteUser(userId: Int): Result<Unit> {
        return runCatching {
            val response = api.deleteUser(userId)
            if (!response.isSuccessful) {
                throw IllegalStateException("Delete user failed: ${response.code()}")
            }
        }
    }

    fun logout() {
        tokenManager.clear()
    }

    suspend fun getTasksByUser(userId: Int): Result<List<TaskDto>> {
        return runCatching {
            api.getTasksByUser(userId)
        }
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
