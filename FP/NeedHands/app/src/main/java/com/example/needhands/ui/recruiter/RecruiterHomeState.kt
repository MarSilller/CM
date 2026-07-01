package com.example.needhands.ui.recruiter

import com.example.needhands.data.model.Worker

data class RecruiterHomeState(
    val searchQuery: String = "",
    val selectedLocationFilter: String? = null,
    val selectedAvailabilityFilter: String? = null,
    val selectedQualificationFilter: String? = null,
    val workers: List<Worker> = emptyList(),
    val filteredWorkers: List<Worker> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
