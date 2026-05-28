package edu.cit.asia.tasktide.mobile.features.dashboard

import android.widget.Button
import android.widget.EditText
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TextButton
import androidx.compose.material3.Button as M3Button
import androidx.compose.material3.Text as M3Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import edu.cit.asia.tasktide.mobile.R
import edu.cit.asia.tasktide.mobile.features.task.model.TaskDto

@Composable
fun DashboardScreen(
    modifier: Modifier,
    tasks: List<TaskDto>,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onAddTask: (taskName: String, description: String, energy: String) -> Unit,
    onDeleteTask: (taskId: Int) -> Unit,
    onUpdateTask: (taskId: Int, taskName: String, description: String, energy: String, status: String) -> Unit,
    onToggleTask: (task: TaskDto) -> Unit,
    onLogout: () -> Unit
) {
    LaunchedEffect(Unit) {
        onRefresh()
    }

    var showModal by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<TaskDto?>(null) }
    var formTaskName by remember { mutableStateOf("") }
    var formDescription by remember { mutableStateOf("") }
    var formEnergy by remember { mutableStateOf("Medium") }
    var formStatus by remember { mutableStateOf("ACTIVE") }
    var selectedStatusFilter by remember { mutableStateOf("") }
    var selectedEnergyFilter by remember { mutableStateOf("") }

    val filteredTasks = remember(tasks, selectedStatusFilter, selectedEnergyFilter) {
        tasks.filter { task ->
            val matchesStatus = selectedStatusFilter.isBlank() ||
                (task.status ?: "").equals(selectedStatusFilter, ignoreCase = true)
            val matchesEnergy = selectedEnergyFilter.isBlank() ||
                (task.energyLevel ?: "").equals(selectedEnergyFilter, ignoreCase = true)
            matchesStatus && matchesEnergy
        }
    }

    Column(modifier = modifier.fillMaxSize().padding(12.dp)) {
        // Top-nav from XML (only keep header & logout)
        AndroidView(
            factory = { context ->
                android.view.LayoutInflater.from(context).inflate(R.layout.activity_dashboard, null)
            },
            modifier = Modifier,
            update = { view ->
                val btnLogout = view.findViewById<Button>(R.id.btnLogout)
                btnLogout.setOnClickListener { onLogout() }
            }
        )

        // Header row and Add button
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                M3Text("My Tasks")
                M3Text("${tasks.count { !(it.status ?: "").equals("COMPLETED", true) }} active tasks")
            }

            M3Button(onClick = {
                editingTask = null
                formTaskName = ""
                formDescription = ""
                formEnergy = "Medium"
                formStatus = "ACTIVE"
                showModal = true
            }) {
                M3Text("Add new task")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Filters
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            var statusExpanded by remember { mutableStateOf(false) }
            var energyExpanded by remember { mutableStateOf(false) }

            TextButton(onClick = { statusExpanded = true }) {
                M3Text(if (selectedStatusFilter.isBlank()) "Status" else "Status: $selectedStatusFilter")
            }
            DropdownMenu(expanded = statusExpanded, onDismissRequest = { statusExpanded = false }) {
                DropdownMenuItem(text = { M3Text("All") }, onClick = {
                    selectedStatusFilter = ""
                    statusExpanded = false
                })
                DropdownMenuItem(text = { M3Text("ACTIVE") }, onClick = {
                    selectedStatusFilter = "ACTIVE"
                    statusExpanded = false
                })
                DropdownMenuItem(text = { M3Text("COMPLETED") }, onClick = {
                    selectedStatusFilter = "COMPLETED"
                    statusExpanded = false
                })
            }

            Spacer(modifier = Modifier.width(8.dp))

            TextButton(onClick = { energyExpanded = true }) {
                M3Text(if (selectedEnergyFilter.isBlank()) "Energy" else "Energy: $selectedEnergyFilter")
            }
            DropdownMenu(expanded = energyExpanded, onDismissRequest = { energyExpanded = false }) {
                DropdownMenuItem(text = { M3Text("All") }, onClick = {
                    selectedEnergyFilter = ""
                    energyExpanded = false
                })
                DropdownMenuItem(text = { M3Text("Low") }, onClick = {
                    selectedEnergyFilter = "Low"
                    energyExpanded = false
                })
                DropdownMenuItem(text = { M3Text("Medium") }, onClick = {
                    selectedEnergyFilter = "Medium"
                    energyExpanded = false
                })
                DropdownMenuItem(text = { M3Text("High") }, onClick = {
                    selectedEnergyFilter = "High"
                    energyExpanded = false
                })
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Task list
        TaskListComposable(
            tasks = filteredTasks,
            isLoading = isLoading,
            onDeleteTask = onDeleteTask,
            onEditTask = { task ->
                editingTask = task
                formTaskName = task.taskName
                formDescription = task.description.orEmpty()
                formEnergy = task.energyLevel ?: "Medium"
                formStatus = task.status ?: "ACTIVE"
                showModal = true
            },
            onToggleComplete = onToggleTask,
            modifier = Modifier
        )

        if (showModal) {
            AlertDialog(
                onDismissRequest = { showModal = false },
                confirmButton = {
                    M3Button(onClick = {
                        if (editingTask != null) {
                            onUpdateTask(editingTask!!.taskId, formTaskName, formDescription, formEnergy, formStatus)
                        } else {
                            onAddTask(formTaskName, formDescription, formEnergy)
                        }
                        showModal = false
                    }) { M3Text("Save") }
                },
                dismissButton = {
                    TextButton(onClick = { showModal = false }) { M3Text("Cancel") }
                },
                title = { M3Text(if (editingTask != null) "Edit Task" else "Add New Task") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = formTaskName,
                            onValueChange = { formTaskName = it },
                            label = { M3Text("Task name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = formDescription,
                            onValueChange = { formDescription = it },
                            label = { M3Text("Description") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Energy level selector
                        var energyExpanded by remember { mutableStateOf(false) }
                        TextButton(onClick = { energyExpanded = true }) { M3Text("Energy: ${formEnergy}") }
                        DropdownMenu(expanded = energyExpanded, onDismissRequest = { energyExpanded = false }) {
                            DropdownMenuItem(text = { M3Text("Low") }, onClick = { formEnergy = "Low"; energyExpanded = false })
                            DropdownMenuItem(text = { M3Text("Medium") }, onClick = { formEnergy = "Medium"; energyExpanded = false })
                            DropdownMenuItem(text = { M3Text("High") }, onClick = { formEnergy = "High"; energyExpanded = false })
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Status selector
                        var statusExpanded by remember { mutableStateOf(false) }
                        TextButton(onClick = { statusExpanded = true }) { M3Text("Status: ${formStatus}") }
                        DropdownMenu(expanded = statusExpanded, onDismissRequest = { statusExpanded = false }) {
                            DropdownMenuItem(text = { M3Text("ACTIVE") }, onClick = { formStatus = "ACTIVE"; statusExpanded = false })
                            DropdownMenuItem(text = { M3Text("COMPLETED") }, onClick = { formStatus = "COMPLETED"; statusExpanded = false })
                        }
                    }
                }
            )
        }
    }
}
