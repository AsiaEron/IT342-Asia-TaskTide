package edu.cit.asia.tasktide.mobile.features.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.cit.asia.tasktide.mobile.features.auth.TaskTideViewModel

@Composable
fun AdminUserTasksScreen(
    viewModel: TaskTideViewModel,
    modifier: Modifier = Modifier
) {
    val pendingUserId by viewModel.pendingUserId.collectAsStateWithLifecycle()
    val userId = pendingUserId ?: return

    LaunchedEffect(userId) {
        viewModel.loadTasksByUser(userId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = { viewModel.navigateToDashboard() }) {
            Text("Back to users")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Tasks for user $userId")

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(uiState.tasks) { task ->
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Text(task.taskName ?: "Unnamed task")
                    Text(task.description ?: "")
                }
            }
        }
    }
}
