package com.example.needhands.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.needhands.data.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import androidx.annotation.StringRes
import com.example.needhands.R

data class SettingsUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    @StringRes val successMessage: Int? = null,
    @StringRes val errorMessage: Int? = null
)

class SettingsViewModel : ViewModel() {
    private val repository = FirebaseRepository()
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        val user = FirebaseAuth.getInstance().currentUser
        _uiState.value = _uiState.value.copy(email = user?.email ?: "")
    }

    fun updatePassword(newPassword: String) {
        if (newPassword.length < 6) {
            _uiState.value = _uiState.value.copy(errorMessage = R.string.error_password_length)
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, successMessage = null)
        
        viewModelScope.launch {
            val result = repository.updatePassword(newPassword)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = R.string.success_password_updated
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = R.string.error_update_password_failed
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(errorMessage = null, successMessage = null)
    }
}
