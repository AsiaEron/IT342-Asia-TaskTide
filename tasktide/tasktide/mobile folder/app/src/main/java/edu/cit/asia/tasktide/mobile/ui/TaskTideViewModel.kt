package edu.cit.asia.tasktide.mobile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.cit.asia.tasktide.mobile.api.TokenManager
import edu.cit.asia.tasktide.mobile.data.RegisterRequest
import edu.cit.asia.tasktide.mobile.data.TaskDto
import edu.cit.asia.tasktide.mobile.data.TaskRepository
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
    val tasks: List<TaskDto> = emptyList()
)

class TaskTideViewModel(
    private val repository: TaskRepository,
    tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        TaskTideUiState(route = if (tokenManager.isLoggedIn()) AppRoute.DASHBOARD else AppRoute.LOGIN)
    )
    val uiState: StateFlow<TaskTideUiState> = _uiState.asStateFlow()

    private val _messages = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val messages: SharedFlow<String> = _messages.asSharedFlow()

    fun navigateToRegister() {
        _uiState.update { it.copy(route = AppRoute.REGISTER) }
    }

    fun navigateToLogin() {
        _uiState.update { it.copy(route = AppRoute.LOGIN) }
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
                _uiState.update { it.copy(route = AppRoute.DASHBOARD) }
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
                _messages.emit("Registered successfully")
                _uiState.update { it.copy(route = AppRoute.LOGIN) }
            }.onFailure { err ->
                _messages.emit(err.message ?: "Registration failed")
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

    fun logout() {
        repository.logout()
        _uiState.update { it.copy(route = AppRoute.LOGIN, tasks = emptyList(), isLoading = false) }
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
