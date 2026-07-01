package com.example.needhands.ui.worker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.needhands.data.model.Worker
import com.example.needhands.data.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkerHomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WorkerHomeState())
    val uiState: StateFlow<WorkerHomeState> = _uiState.asStateFlow()

    init {
        loadWorkerProfile()
    }

    fun loadWorkerProfile() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                val profile = FirebaseRepository().getUserProfile(uid)
                if (profile != null) {
                    val worker = Worker(
                        id = profile.id,
                        name = profile.name,
                        age = profile.age,
                        gender = profile.gender,
                        location = profile.location,
                        rating = profile.rating,
                        availability = profile.availability,
                        qualifications = profile.qualifications,
                        isBoosted = profile.isBoosted,
                        profileImageUrl = profile.profileImageUrl
                    )
                    _uiState.update { it.copy(workerProfile = worker, isLoading = false) }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        } else {
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun buyBoost() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            val repository = FirebaseRepository()
            val profile = repository.getUserProfile(uid)
            if (profile != null) {
                val updatedProfile = profile.copy(isBoosted = true)
                repository.saveUserProfile(updatedProfile)
                
                // Update local UI state directly to allow animation to play
                _uiState.update { state ->
                    val currentWorker = state.workerProfile
                    if (currentWorker != null) {
                        state.copy(workerProfile = currentWorker.copy(isBoosted = true))
                    } else {
                        state
                    }
                }
            }
        }
    }
}
