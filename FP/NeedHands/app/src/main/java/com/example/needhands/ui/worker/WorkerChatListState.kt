package com.example.needhands.ui.worker

import com.example.needhands.data.model.UserProfile

data class WorkerChatListState(
    val recruiters: List<UserProfile> = emptyList(),
    val isLoading: Boolean = true
)
