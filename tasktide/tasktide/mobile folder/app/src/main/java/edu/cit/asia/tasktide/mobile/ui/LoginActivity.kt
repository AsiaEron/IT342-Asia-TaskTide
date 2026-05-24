package edu.cit.asia.tasktide.mobile.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.cit.asia.tasktide.mobile.features.auth.TaskTideViewModel
import edu.cit.asia.tasktide.mobile.features.auth.TaskTideViewModelFactory
import edu.cit.asia.tasktide.mobile.features.task.TaskRepository
import edu.cit.asia.tasktide.mobile.shared.storage.TokenManager

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = TaskRepository(this)
        val tokenManager = TokenManager(this)

        setContent {
            val vm: TaskTideViewModel = viewModel(
                factory = TaskTideViewModelFactory(repository, tokenManager)
            )
            TaskTideApp(viewModel = vm)
        }
    }
}
