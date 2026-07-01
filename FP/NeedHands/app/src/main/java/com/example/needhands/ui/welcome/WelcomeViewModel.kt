package com.example.needhands.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import com.example.needhands.data.repository.FirebaseRepository
import com.example.needhands.data.model.UserProfile
import com.example.needhands.R

class WelcomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WelcomeState())
    val uiState: StateFlow<WelcomeState> = _uiState.asStateFlow()

    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,}\$".toRegex()

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, emailError = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    fun onRoleChanged(role: UserRole) {
        _uiState.update { it.copy(selectedRole = role) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun login() {
        if (!validateInputs()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val auth = FirebaseAuth.getInstance()
                auth.signInWithEmailAndPassword(_uiState.value.email, _uiState.value.password).await()
                
                val expectedRole = if (_uiState.value.selectedRole == UserRole.RECRUITER) "recruiter" else "worker"
                val profile = FirebaseRepository().getUserProfile(auth.currentUser?.uid ?: "")
                
                if (profile != null && profile.role.isNotBlank() && profile.role != expectedRole) {
                    auth.signOut()
                    val errorRes = if (profile.role == "worker") R.string.error_user_is_worker else R.string.error_user_is_recruiter
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorRes
                        )
                    }
                    return@launch
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isAuthenticated = true
                    )
                }
            } catch (e: com.google.firebase.auth.FirebaseAuthInvalidUserException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = R.string.error_invalid_credentials
                    )
                }
            } catch (e: com.google.firebase.auth.FirebaseAuthInvalidCredentialsException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = R.string.error_invalid_credentials
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = R.string.error
                    )
                }
            }
        }
    }

    fun register() {
        if (!validateInputs()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val auth = FirebaseAuth.getInstance()
                val result = auth.createUserWithEmailAndPassword(_uiState.value.email, _uiState.value.password).await()
                
                val user = result.user
                if (user != null) {
                    val profile = UserProfile(
                        id = user.uid,
                        name = _uiState.value.email.substringBefore("@"),
                        email = _uiState.value.email,
                        role = if (_uiState.value.selectedRole == UserRole.RECRUITER) "recruiter" else "worker"
                    )
                    FirebaseRepository().saveUserProfile(profile)
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isAuthenticated = true
                    )
                }
            } catch (e: com.google.firebase.auth.FirebaseAuthInvalidCredentialsException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = R.string.error_invalid_credentials
                    )
                }
            } catch (e: com.google.firebase.auth.FirebaseAuthUserCollisionException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = R.string.error
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = R.string.error
                    )
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true
        val email = _uiState.value.email
        val password = _uiState.value.password

        if (email.isBlank()) {
            _uiState.update { it.copy(emailError = R.string.error_fill_fields) }
            isValid = false
        } else if (!email.matches(emailRegex)) {
            _uiState.update { it.copy(emailError = R.string.error_invalid_email) }
            isValid = false
        }

        if (password.length < 6) {
            _uiState.update { it.copy(passwordError = R.string.error_password_length) }
            isValid = false
        }

        return isValid
    }
}
