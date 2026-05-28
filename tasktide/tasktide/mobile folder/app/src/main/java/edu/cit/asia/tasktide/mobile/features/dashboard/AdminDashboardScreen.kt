package edu.cit.asia.tasktide.mobile.features.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.cit.asia.tasktide.mobile.features.auth.TaskTideViewModel

@Composable
fun AdminDashboardScreen(
    viewModel: TaskTideViewModel,
    modifier: Modifier = Modifier
) {
    val users by viewModel.adminUsers.collectAsStateWithLifecycle()
    val error by viewModel.adminError.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadAdminUsers()
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("TaskTide Admin")
            Button(onClick = { viewModel.logout() }) {
                Text("Logout")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Admin Dashboard")
        Text("Manage users and access the database view.")

        Spacer(modifier = Modifier.height(12.dp))

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Column
        }
        if (error.isNotEmpty()) {
            Text(text = error)
            return@Column
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(users) { user ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("${user.userId} - ${user.email}")
                        Text("${user.firstName ?: ""} ${user.lastName ?: ""}")
                    }

                    Row {
                        Button(onClick = {
                            viewModel.navigateToAdminUserTasks(user.userId)
                        }) {
                            Text("View")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(onClick = { viewModel.deleteUser(user.userId) }) {
                            Text("Delete")
                        }
                    }
                }

                Divider()
            }
        }
    }
}
