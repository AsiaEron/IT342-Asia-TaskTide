package edu.cit.asia.tasktide.mobile.ui.screens

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import edu.cit.asia.tasktide.mobile.R

@Composable
fun LoginScreen(
    modifier: Modifier,
    isLoading: Boolean,
    onLogin: (email: String, password: String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    AndroidView(
        factory = { context ->
            android.view.LayoutInflater.from(context).inflate(R.layout.activity_login, null)
        },
        modifier = modifier,
        update = { view ->
            val etEmail = view.findViewById<EditText>(R.id.etEmail)
            val etPassword = view.findViewById<EditText>(R.id.etPassword)
            val btnLogin = view.findViewById<Button>(R.id.btnLogin)
            val btnGoRegister = view.findViewById<Button>(R.id.btnGoRegister)

            btnLogin.isEnabled = !isLoading
            btnLogin.text = if (isLoading) "Logging in..." else "Login"

            btnLogin.setOnClickListener {
                onLogin(etEmail.text.toString(), etPassword.text.toString())
            }

            btnGoRegister.setOnClickListener {
                onNavigateToRegister()
            }
        }
    )
}
