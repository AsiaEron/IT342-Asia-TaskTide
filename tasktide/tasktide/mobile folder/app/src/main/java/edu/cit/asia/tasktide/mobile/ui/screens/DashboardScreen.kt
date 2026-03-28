package edu.cit.asia.tasktide.mobile.ui.screens

import android.widget.Button
import android.widget.EditText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import edu.cit.asia.tasktide.mobile.R
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
    LaunchedEffect(Unit) {
        onRefresh()
    }

    Column(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                android.view.LayoutInflater.from(context).inflate(R.layout.activity_dashboard, null)
            },
            modifier = Modifier.weight(0.5f),
            update = { view ->
                val etTaskName = view.findViewById<EditText>(R.id.etTaskName)
                val etDescription = view.findViewById<EditText>(R.id.etDescription)
                val etEnergy = view.findViewById<EditText>(R.id.etEnergy)
                val btnAddTask = view.findViewById<Button>(R.id.btnAddTask)
                val btnLogout = view.findViewById<Button>(R.id.btnLogout)

                btnAddTask.isEnabled = !isLoading
                btnAddTask.text = if (isLoading) "Adding..." else "Add task"

                btnAddTask.setOnClickListener {
                    onAddTask(
                        etTaskName.text.toString(),
                        etDescription.text.toString(),
                        etEnergy.text.toString().ifEmpty { "MEDIUM" }
                    )
                    // Clear fields
                    etTaskName.setText("")
                    etDescription.setText("")
                    etEnergy.setText("MEDIUM")
                }

                btnLogout.setOnClickListener {
                    onLogout()
                }
            }
        )

        // Compose task list below the form
        TaskListComposable(
            tasks = tasks,
            isLoading = isLoading,
            onDeleteTask = onDeleteTask,
            modifier = Modifier.weight(0.5f)
        )
    }
}
