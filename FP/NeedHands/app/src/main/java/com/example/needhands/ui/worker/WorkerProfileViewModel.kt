package com.example.needhands.ui.worker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.needhands.data.model.UserProfile
import com.example.needhands.data.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.needhands.R

class WorkerProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WorkerProfileState())
    val uiState: StateFlow<WorkerProfileState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            viewModelScope.launch {
                val profile = FirebaseRepository().getUserProfile(uid)
                if (profile != null) {
                    _uiState.update {
                        it.copy(
                            name = profile.name,
                            age = if (profile.age > 0) profile.age.toString() else "",
                            gender = if (profile.gender.isNotBlank()) profile.gender else it.gender,
                            location = if (profile.location.isNotBlank()) profile.location else it.location,
                            availability = if (profile.availability.isNotBlank()) profile.availability else it.availability,
                            selectedQualifications = if (profile.qualifications.isNotEmpty()) profile.qualifications.toSet() else it.selectedQualifications,
                            profilePictureUri = profile.profileImageUrl.takeIf { url -> url.isNotBlank() } ?: it.profilePictureUri
                        )
                    }
                }
            }
        }
    }

    fun onProfilePictureUploaded(uriStr: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            viewModelScope.launch {
                try {
                    val uri = android.net.Uri.parse(uriStr)
                    val downloadUrl = FirebaseRepository().uploadProfilePicture(uid, uri)
                    _uiState.update { it.copy(profilePictureUri = downloadUrl) }
                    
                    // Also update the profile in Firestore so it saves the download URL
                    val profile = FirebaseRepository().getUserProfile(uid)
                    if (profile != null) {
                        val updatedProfile = profile.copy(profileImageUrl = downloadUrl)
                        FirebaseRepository().saveUserProfile(updatedProfile)
                    }
                } catch (e: Exception) {
                    // Handle failure if needed
                }
            }
        } else {
            _uiState.update { it.copy(profilePictureUri = uriStr) }
        }
    }

    fun onAgeChanged(age: String) {
        if (age.isEmpty() || age.all { it.isDigit() }) {
            _uiState.update { it.copy(age = age) }
        }
    }

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onGenderChanged(gender: String) {
        _uiState.update { it.copy(gender = gender) }
    }

    fun onLocationChanged(location: String) {
        _uiState.update { it.copy(location = location) }
    }

    fun onAvailabilityChanged(availability: String) {
        _uiState.update { it.copy(availability = availability) }
    }

    fun toggleQualification(qualification: String) {
        _uiState.update { state ->
            val updated = state.selectedQualifications.toMutableSet()
            if (updated.contains(qualification)) {
                updated.remove(qualification)
            } else {
                updated.add(qualification)
            }
            state.copy(selectedQualifications = updated)
        }
    }

    fun saveProfile() {
        if (!validateFields()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, saveSuccess = false, errorMessage = null) }
            
            try {
                val auth = FirebaseAuth.getInstance()
                val uid = auth.currentUser?.uid
                if (uid != null) {
                    val repository = FirebaseRepository()
                    val existingProfile = repository.getUserProfile(uid) ?: UserProfile(id = uid, role = "worker")
                    
                    val updatedProfile = existingProfile.copy(
                        name = _uiState.value.name,
                        role = "worker", // Explicitly ensure the role is set to "worker"
                        age = _uiState.value.age.toIntOrNull() ?: 0,
                        gender = _uiState.value.gender,
                        location = _uiState.value.location,
                        availability = _uiState.value.availability,
                        qualifications = _uiState.value.selectedQualifications.toList(),
                        profileImageUrl = _uiState.value.profilePictureUri ?: ""
                    )
                    
                    repository.saveUserProfile(updatedProfile)
                    _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
                } else {
                    _uiState.update { it.copy(isSaving = false, errorMessage = R.string.error_no_user) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, errorMessage = R.string.error) }
            }
        }
    }

    fun resetSaveSuccess() {
        _uiState.update { it.copy(saveSuccess = false) }
    }

    private fun validateFields(): Boolean {
        val name = _uiState.value.name
        val age = _uiState.value.age
        val location = _uiState.value.location

        if (name.isBlank()) {
            _uiState.update { it.copy(errorMessage = R.string.error_fill_fields) }
            return false
        }
        if (age.isBlank()) {
            _uiState.update { it.copy(errorMessage = R.string.error_fill_fields) }
            return false
        }
        val ageInt = age.toIntOrNull()
        if (ageInt == null || ageInt < 16 || ageInt > 100) {
            _uiState.update { it.copy(errorMessage = R.string.error_invalid_age) }
            return false
        }
        if (location.isBlank()) {
            _uiState.update { it.copy(errorMessage = R.string.error_fill_fields) }
            return false
        }
        if (_uiState.value.selectedQualifications.isEmpty()) {
            _uiState.update { it.copy(errorMessage = R.string.error_no_qualifications) }
            return false
        }
        return true
    }
}
