package edu.cit.asia.tasktide.mobile.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import edu.cit.asia.tasktide.mobile.features.auth.TaskTideViewModel
import edu.cit.asia.tasktide.mobile.features.auth.model.RegisterRequest
import edu.cit.asia.tasktide.mobile.features.auth.LoginScreen
import edu.cit.asia.tasktide.mobile.features.auth.RegisterScreen
import edu.cit.asia.tasktide.mobile.features.auth.VerifyEmailScreen
import edu.cit.asia.tasktide.mobile.features.dashboard.DashboardScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TaskTideApp(viewModel: TaskTideViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(viewModel, lifecycle) {
        viewModel.messages
            .flowWithLifecycle(lifecycle)
            .collectLatest { message ->
                snackbarHostState.showSnackbar(message)
            }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        when (uiState.route) {
            AppRoute.LOGIN -> LoginScreen(
                modifier = Modifier.padding(padding),
                isLoading = uiState.isLoading,
                onLogin = viewModel::login,
                onNavigateToRegister = viewModel::navigateToRegister
            )

            AppRoute.REGISTER -> RegisterScreen(
                modifier = Modifier.padding(padding),
                isLoading = uiState.isLoading,
                onRegister = { firstName, lastName, email, password ->
                    viewModel.register(
                        RegisterRequest(
                            fname = firstName,
                            lname = lastName,
                            email = email,
                            password = password
                        )
                    )
                },
                onBackToLogin = viewModel::navigateToLogin
            )

            AppRoute.VERIFY_EMAIL -> VerifyEmailScreen(
                modifier = Modifier.padding(padding),
                isLoading = uiState.isLoading,
                onVerify = viewModel::verifyEmail,
                onBackToLogin = viewModel::navigateToLogin
            )

            AppRoute.DASHBOARD -> {
                if (uiState.isAdmin) {
                    edu.cit.asia.tasktide.mobile.features.dashboard.AdminDashboardScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(padding)
                    )
                } else {
                    DashboardScreen(
                        modifier = Modifier.padding(padding),
                        tasks = uiState.tasks,
                        isLoading = uiState.isLoading,
                        onRefresh = viewModel::loadTasks,
                        onAddTask = viewModel::addTask,
                        onDeleteTask = viewModel::deleteTask,
                        onUpdateTask = viewModel::updateTask,
                        onToggleTask = viewModel::toggleTaskComplete,
                        onLogout = viewModel::logout
                    )
                }
            }

            AppRoute.ADMIN_USER_TASKS -> {
                edu.cit.asia.tasktide.mobile.features.dashboard.AdminUserTasksScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}
