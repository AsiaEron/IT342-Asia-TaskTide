package edu.cit.asia.tasktide.mobile.features.auth

import android.widget.Button
import android.widget.EditText
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import edu.cit.asia.tasktide.mobile.R

@Composable
fun VerifyEmailScreen(
    modifier: Modifier,
    isLoading: Boolean,
    onVerify: (verificationCode: String) -> Unit,
    onBackToLogin: () -> Unit
) {
    AndroidView(
        factory = { context ->
            android.view.LayoutInflater.from(context).inflate(R.layout.activity_verify_email, null)
        },
        modifier = modifier.fillMaxSize(),
        update = { view ->
            val etVerificationCode = view.findViewById<EditText>(R.id.etVerificationCode)
            val btnVerifyEmail = view.findViewById<Button>(R.id.btnVerifyEmail)
            val btnBackToLogin = view.findViewById<Button>(R.id.btnBackToLogin)

            btnVerifyEmail.isEnabled = !isLoading
            btnVerifyEmail.text = if (isLoading) "Verifying..." else "Verify Email"

            btnVerifyEmail.setOnClickListener {
                onVerify(etVerificationCode.text.toString())
            }

            btnBackToLogin.setOnClickListener {
                onBackToLogin()
            }
        }
    )
}
