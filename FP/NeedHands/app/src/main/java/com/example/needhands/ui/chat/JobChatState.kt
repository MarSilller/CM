package com.example.needhands.ui.chat

import com.example.needhands.data.model.ChatMessage
import com.example.needhands.data.model.UserProfile

data class JobChatState(
    val targetUser: UserProfile? = null,
    val userRole: String = "recruiter",
    val recruiterName: String = "Sovereign Kitchens Ltd",
    val mapLocationUrl: String = "https://maps.google.com/?q=51.5074,-0.1278", // Westminster London
    val scheduledDate: String = "2026-06-15",
    val scheduledTime: String = "18:00 - 23:00",
    val chatMessages: List<ChatMessage> = emptyList(),
    val currentMessageText: String = "",
    val isLoading: Boolean = false,
    val isShiftConfirmed: Boolean = false
)
