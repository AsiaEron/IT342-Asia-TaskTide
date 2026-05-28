package edu.cit.asia.tasktide.mobile.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.cit.asia.tasktide.mobile.features.task.TaskRepository
import edu.cit.asia.tasktide.mobile.features.auth.model.RegisterRequest
import edu.cit.asia.tasktide.mobile.features.task.model.TaskDto
import edu.cit.asia.tasktide.mobile.shared.storage.TokenManager
import edu.cit.asia.tasktide.mobile.ui.AppRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TaskTideUiState(
    val route: AppRoute = AppRoute.LOGIN,
    val isLoading: Boolean = false,
    val tasks: List<TaskDto> = emptyList(),
    val pendingVerificationEmail: String = "",
    val isAdmin: Boolean = false
)

class TaskTideViewModel(
    private val repository: TaskRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        TaskTideUiState(
            route = if (tokenManager.isLoggedIn()) AppRoute.DASHBOARD else AppRoute.LOGIN,
            isAdmin = tokenManager.getUserRole()?.contains("ADMIN", ignoreCase = true) == true
        )
    )
    val uiState: StateFlow<TaskTideUiState> = _uiState.asStateFlow()

    private val _messages = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val messages: SharedFlow<String> = _messages.asSharedFlow()

    private val _adminUsers = MutableStateFlow<List<edu.cit.asia.tasktide.mobile.features.admin.model.UserSummary>>(emptyList())
    val adminUsers = _adminUsers.asStateFlow()

    private val _adminError = MutableStateFlow("")
    val adminError = _adminError.asStateFlow()

    private val _pendingUserId = MutableStateFlow<Int?>(null)
    val pendingUserId = _pendingUserId.asStateFlow()

    fun navigateToRegister() {
        _uiState.update { it.copy(route = AppRoute.REGISTER, pendingVerificationEmail = "") }
    }

    fun navigateToLogin() {
        _uiState.update { it.copy(route = AppRoute.LOGIN, pendingVerificationEmail = "") }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _messages.tryEmit("Email and password are required")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.login(email.trim(), password.trim())
            _uiState.update { it.copy(isLoading = false) }

            result.onSuccess {
                // determine admin role from stored token manager
                val admin = tokenManager.getUserRole()?.contains("ADMIN", ignoreCase = true) == true
                _uiState.update { it.copy(route = AppRoute.DASHBOARD, isAdmin = admin) }
                loadTasks()
            }.onFailure { err ->
                _messages.emit(err.message ?: "Login failed")
            }
        }
    }

    fun register(request: RegisterRequest) {
        if (request.fname.isBlank() || request.lname.isBlank() || request.email.isBlank() || request.password.isBlank()) {
            _messages.tryEmit("Please complete all fields")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.register(request)
            _uiState.update { it.copy(isLoading = false) }

            result.onSuccess {
                _messages.emit("A verification code has been sent to your email")
                _uiState.update {
                    it.copy(
                        route = AppRoute.VERIFY_EMAIL,
                        pendingVerificationEmail = request.email.trim()
                    )
                }
            }.onFailure { err ->
                _messages.emit(err.message ?: "Registration failed")
            }
        }
    }

    fun verifyEmail(verificationCode: String) {
        val email = _uiState.value.pendingVerificationEmail
        if (email.isBlank() || verificationCode.isBlank()) {
            _messages.tryEmit("Email and verification code are required")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.verifyEmail(email, verificationCode)
            _uiState.update { it.copy(isLoading = false) }

            result.onSuccess {
                _messages.emit("Email verified successfully. You can now log in.")
                _uiState.update { it.copy(route = AppRoute.LOGIN, pendingVerificationEmail = "") }
            }.onFailure { err ->
                _messages.emit(err.message ?: "Verification failed")
            }
        }
    }

    fun loadTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.getTasks()
            _uiState.update { it.copy(isLoading = false) }

            result.onSuccess { tasks ->
                _uiState.update { it.copy(tasks = tasks) }
            }.onFailure { err ->
                _messages.emit(err.message ?: "Failed to load tasks")
            }
        }
    }

    fun addTask(taskName: String, description: String, energy: String) {
        if (taskName.isBlank()) {
            _messages.tryEmit("Task name is required")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.addTask(
                taskName = taskName.trim(),
                description = description.trim(),
                energyLevel = energy.trim().ifBlank { "MEDIUM" }
            )

            result.onSuccess {
                loadTasks()
            }.onFailure { err ->
                _uiState.update { it.copy(isLoading = false) }
                _messages.emit(err.message ?: "Failed to add task")
            }
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.deleteTask(taskId)
            result.onSuccess {
                loadTasks()
            }.onFailure { err ->
                _messages.emit(err.message ?: "Failed to delete task")
            }
        }
    }

    fun updateTask(taskId: Int, taskName: String, description: String, energy: String, status: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.updateTask(taskId, taskName, description, energy, status)
            _uiState.update { it.copy(isLoading = false) }

            result.onSuccess {
                loadTasks()
                _messages.emit("Task updated")
            }.onFailure { err ->
                _messages.emit(err.message ?: "Failed to update task")
            }
        }
    }

    fun toggleTaskComplete(task: TaskDto) {
        viewModelScope.launch(Dispatchers.IO) {
            val taskId = task.taskId
            if (taskId <= 0) {
                _messages.emit("Task ID missing")
                return@launch
            }

            val nextStatus = if ((task.status ?: "").equals("COMPLETED", ignoreCase = true)) "ACTIVE" else "COMPLETED"
            val result = repository.updateTask(taskId, task.taskName, task.description.orEmpty(), task.energyLevel ?: "MEDIUM", nextStatus)

            result.onSuccess {
                loadTasks()
            }.onFailure { err ->
                _messages.emit(err.message ?: "Failed to toggle task")
            }
        }
    }

    fun logout() {
        repository.logout()
        _uiState.update { it.copy(route = AppRoute.LOGIN, tasks = emptyList(), isLoading = false, pendingVerificationEmail = "") }
    }

    fun loadAdminUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            _adminError.value = ""
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.getAllUsers()
            _uiState.update { it.copy(isLoading = false) }

            result.onSuccess { users ->
                _adminUsers.value = users
            }.onFailure { err ->
                _adminError.value = err.message ?: "Failed to load users"
            }
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.deleteUser(userId)
            _uiState.update { it.copy(isLoading = false) }

            result.onSuccess {
                _messages.emit("User deleted")
                loadAdminUsers()
            }.onFailure { err ->
                _messages.emit(err.message ?: "Failed to delete user")
            }
        }
    }

    fun navigateToAdminUserTasks(userId: Int) {
        _pendingUserId.value = userId
        _uiState.update { it.copy(route = AppRoute.ADMIN_USER_TASKS) }
    }

    fun navigateToDashboard() {
        _pendingUserId.value = null
        _uiState.update { it.copy(route = AppRoute.DASHBOARD) }
    }

    fun loadTasksByUser(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.getTasksByUser(userId)
            _uiState.update { it.copy(isLoading = false) }

            result.onSuccess { tasks ->
                _uiState.update { it.copy(tasks = tasks) }
            }.onFailure { err ->
                _messages.emit(err.message ?: "Failed to load user tasks")
            }
        }
    }
}

class TaskTideViewModelFactory(
    private val repository: TaskRepository,
    private val tokenManager: TokenManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskTideViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskTideViewModel(repository, tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
