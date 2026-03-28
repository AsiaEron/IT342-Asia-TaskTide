package edu.cit.asia.tasktide.mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.cit.asia.tasktide.mobile.data.TaskDto

@Composable
fun DashboardScreen(
    modifier: Modifier,
    tasks: List<TaskDto>,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onAddTask: (taskName: String, description: String, energy: String) -> Unit,
    onDeleteTask: (taskId: Int) -> Unit,
    onLogout: () -> Unit
) {
    var taskName by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var energy by rememberSaveable { mutableStateOf("MEDIUM") }

    LaunchedEffect(Unit) {
        onRefresh()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Dashboard", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = onLogout) {
                Text("Logout")
            }
        }

        OutlinedTextField(
            value = taskName,
            onValueChange = { taskName = it },
            label = { Text("Task name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        OutlinedTextField(
            value = energy,
            onValueChange = { energy = it },
            label = { Text("Energy (LOW/MEDIUM/HIGH)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Button(
            onClick = {
                onAddTask(taskName, description, energy)
                taskName = ""
                description = ""
                energy = "MEDIUM"
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Add task")
        }

        if (isLoading) {
            Text(
                text = "Loading tasks...",
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks, key = { it.taskId }) { task ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(task.taskName, style = MaterialTheme.typography.titleMedium)
                        Text(task.description.orEmpty(), style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "${task.energyLevel ?: "MEDIUM"} - ${task.status ?: "PENDING"}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        TextButton(
                            onClick = { onDeleteTask(task.taskId) },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}
