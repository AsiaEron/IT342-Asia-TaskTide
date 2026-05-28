package edu.cit.asia.tasktide.mobile.features.auth

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import edu.cit.asia.tasktide.mobile.R

@Composable
fun RegisterScreen(
    modifier: Modifier,
    isLoading: Boolean,
    onRegister: (firstName: String, lastName: String, email: String, password: String) -> Unit,
    onBackToLogin: () -> Unit
) {
    AndroidView(
        factory = { context ->
            android.view.LayoutInflater.from(context).inflate(R.layout.activity_register, null)
        },
        modifier = modifier.fillMaxSize(),
        update = { view ->
            val etFirstName = view.findViewById<EditText>(R.id.etFirstName)
            val etLastName = view.findViewById<EditText>(R.id.etLastName)
            val etEmail = view.findViewById<EditText>(R.id.etEmail)
            val etPassword = view.findViewById<EditText>(R.id.etPassword)
            val etConfirmPassword = view.findViewById<EditText>(R.id.etConfirmPassword)
            val btnRegister = view.findViewById<Button>(R.id.btnRegister)
            val btnBackToLogin = view.findViewById<Button>(R.id.btnBackToLogin)

            btnRegister.isEnabled = !isLoading
            btnRegister.text = if (isLoading) "Creating account..." else "Create Account"

            btnRegister.setOnClickListener {
                val password = etPassword.text.toString()
                val confirmPassword = etConfirmPassword.text.toString()

                if (password != confirmPassword) {
                    Toast.makeText(view.context, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                onRegister(
                    etFirstName.text.toString(),
                    etLastName.text.toString(),
                    etEmail.text.toString(),
                    password
                )
            }

            btnBackToLogin.setOnClickListener {
                onBackToLogin()
            }
        }
    )
}
