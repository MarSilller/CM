package com.example.needhands.data.model

data class Worker(
    val id: String,
    val name: String,
    val age: Int,
    val gender: String,
    val location: String,
    val rating: Float,
    val availability: String, // e.g., "Immediate", "Weekends only", "Flexible"
    val qualifications: List<String>, // e.g., "Kitchen assistant", "Waiter", "Doorman/bouncer"
    val isBoosted: Boolean = false,
    val profileImageUrl: String? = null,
    val isAvailableNow: Boolean = true
)
