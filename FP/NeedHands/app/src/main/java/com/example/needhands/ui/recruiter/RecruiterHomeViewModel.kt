package com.example.needhands.ui.recruiter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.needhands.data.model.Worker
import com.example.needhands.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecruiterHomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RecruiterHomeState())
    val uiState: StateFlow<RecruiterHomeState> = _uiState.asStateFlow()

    private val repository = FirebaseRepository()

    init {
        loadWorkers()
    }

    private fun loadWorkers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getWorkersFlow().collect { workersList ->
                _uiState.update {
                    it.copy(
                        workers = workersList,
                        isLoading = false
                    )
                }
                applyFilters()
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun onLocationFilterSelected(location: String?) {
        _uiState.update { it.copy(selectedLocationFilter = location) }
        applyFilters()
    }

    fun onAvailabilityFilterSelected(availability: String?) {
        _uiState.update { it.copy(selectedAvailabilityFilter = availability) }
        applyFilters()
    }

    fun onQualificationFilterSelected(qualification: String?) {
        _uiState.update { it.copy(selectedQualificationFilter = qualification) }
        applyFilters()
    }

    private fun applyFilters() {
        val query = _uiState.value.searchQuery.lowercase()
        val location = _uiState.value.selectedLocationFilter
        val availability = _uiState.value.selectedAvailabilityFilter
        val qualification = _uiState.value.selectedQualificationFilter

        val filtered = _uiState.value.workers.filter { worker ->
            val matchesSearch = worker.name.lowercase().contains(query) ||
                    worker.qualifications.any { it.lowercase().contains(query) } ||
                    worker.location.lowercase().contains(query)

            val matchesLocation = location == null || worker.location == location
            val matchesAvailability = availability == null || worker.availability == availability
            val matchesQualification = qualification == null || worker.qualifications.contains(qualification)

            matchesSearch && matchesLocation && matchesAvailability && matchesQualification
        }.sortedByDescending { it.isBoosted }

        _uiState.update { it.copy(filteredWorkers = filtered) }
    }
}
