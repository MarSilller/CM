package com.example.needhands.data.model

data class UserProfile(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "", // "recruiter" or "worker"
    val age: Int = 0,
    val gender: String = "",
    val location: String = "",
    val rating: Float = 5.0f,
    val availability: String = "",
    val qualifications: List<String> = emptyList(),
    @get:com.google.firebase.firestore.PropertyName("isBoosted")
    @set:com.google.firebase.firestore.PropertyName("isBoosted")
    var isBoosted: Boolean = false,
    var profileImageUrl: String = ""
)
