package edu.cit.asia.tasktide.mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import edu.cit.asia.tasktide.mobile.data.TaskDto

@Composable
fun TaskListComposable(
    tasks: List<TaskDto>,
    isLoading: Boolean,
    onDeleteTask: (taskId: Int) -> Unit,
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