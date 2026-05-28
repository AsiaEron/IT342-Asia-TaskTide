package edu.cit.asia.tasktide.mobile.features.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.cit.asia.tasktide.mobile.features.task.model.TaskDto

@Composable
fun TaskListComposable(
    tasks: List<TaskDto>,
    isLoading: Boolean,
    onDeleteTask: (taskId: Int) -> Unit,
    onEditTask: (task: TaskDto) -> Unit,
    onToggleComplete: (task: TaskDto) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(8.dp)) {
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
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(task.taskName, style = MaterialTheme.typography.titleMedium)
                                if (!task.description.isNullOrEmpty()) {
                                    Text(task.description.orEmpty(), style = MaterialTheme.typography.bodyMedium)
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(task.energyLevel ?: "MEDIUM")
                                Text(task.status ?: "ACTIVE")
                            }
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = { onToggleComplete(task) }) {
                                Text(if ((task.status ?: "").equals("COMPLETED", ignoreCase = true)) "Mark Active" else "Complete")
                            }

                            TextButton(onClick = { onEditTask(task) }) {
                                Text("Edit")
                            }

                            TextButton(onClick = { onDeleteTask(task.taskId) }) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}