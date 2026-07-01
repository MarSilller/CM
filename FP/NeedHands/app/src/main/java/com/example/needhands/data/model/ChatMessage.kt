package com.example.needhands.data.model

data class ChatMessage(
    val id: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val imageUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
