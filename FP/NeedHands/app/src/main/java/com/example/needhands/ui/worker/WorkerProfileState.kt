package com.example.needhands.ui.worker

import androidx.annotation.StringRes

data class WorkerProfileState(
    val profilePictureUri: String? = null,
    val name: String = "",
    val age: String = "",
    val gender: String = "Male",
    val location: String = "",
    val availability: String = "Immediate", // default choice
    val selectedQualifications: Set<String> = emptySet(),
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    @StringRes val errorMessage: Int? = null
)
