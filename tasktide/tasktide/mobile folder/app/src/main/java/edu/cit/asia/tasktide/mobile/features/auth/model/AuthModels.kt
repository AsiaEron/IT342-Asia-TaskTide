package edu.cit.asia.tasktide.mobile.features.auth.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val fname: String,
    val lname: String,
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val userId: Int?,
    val email: String?,
    val role: String?
)

data class RegisterResponse(
    val userId: Int,
    val email: String,
    val message: String
)

data class VerifyEmailRequest(
    val email: String,
    val verificationCode: String
)
