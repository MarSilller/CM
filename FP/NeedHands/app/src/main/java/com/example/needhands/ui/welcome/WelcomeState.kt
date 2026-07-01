package com.example.needhands.ui.welcome

import androidx.annotation.StringRes

enum class UserRole {
    WORKER, RECRUITER
}

data class WelcomeState(
    val email: String = "",
    @StringRes val emailError: Int? = null,
    val password: String = "",
    @StringRes val passwordError: Int? = null,
    val selectedRole: UserRole = UserRole.WORKER,
    val isLoading: Boolean = false,
    @StringRes val errorMessage: Int? = null,
    val isAuthenticated: Boolean = false
)
