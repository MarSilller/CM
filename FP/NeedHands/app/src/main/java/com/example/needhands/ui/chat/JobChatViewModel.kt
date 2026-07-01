package com.example.needhands.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.needhands.data.model.Appointment
import com.example.needhands.data.model.ChatMessage
import com.example.needhands.data.model.Worker
import com.example.needhands.data.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class JobChatViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(JobChatState())
    val uiState: StateFlow<JobChatState> = _uiState.asStateFlow()
    private val repository = FirebaseRepository()

    fun initChat(targetUserId: String, role: String) {
        _uiState.update { it.copy(userRole = role) }
        viewModelScope.launch {
            val profile = repository.getUserProfile(targetUserId)
            if (profile != null) {
                _uiState.update { it.copy(targetUser = profile) }
                loadInitialMessages(targetUserId, role)
            }
        }
    }

    private fun loadInitialMessages(targetUserId: String, role: String) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        
        val recruiterId = if (role == "recruiter") currentUserUid else targetUserId
        val workerId = if (role == "worker") currentUserUid else targetUserId

        viewModelScope.launch {
            repository.getChatMessagesFlow(recruiterId, workerId).collect { messages ->
                _uiState.update { it.copy(chatMessages = messages) }
            }
        }
        
        viewModelScope.launch {
            repository.getAppointmentFlow(recruiterId, workerId).collect { appointment ->
                if (appointment != null) {
                    _uiState.update { 
                        it.copy(
                            scheduledDate = appointment.date,
                            scheduledTime = appointment.time,
                            isShiftConfirmed = true
                        ) 
                    }
                } else {
                    _uiState.update { it.copy(isShiftConfirmed = false) }
                }
            }
        }
    }

    fun onMessageTextChanged(text: String) {
        _uiState.update { it.copy(currentMessageText = text) }
    }

    fun sendMessage() {
        val text = _uiState.value.currentMessageText.trim()
        if (text.isEmpty()) return

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: "unknown"
        val receiverId = _uiState.value.targetUser?.id ?: "unknown"

        val newMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            senderId = currentUserUid,
            receiverId = receiverId,
            text = text,
            timestamp = System.currentTimeMillis()
        )

        _uiState.update { state ->
            state.copy(
                chatMessages = state.chatMessages + newMessage,
                currentMessageText = ""
            )
        }

        viewModelScope.launch {
            try {
                repository.saveChatMessage(newMessage)
            } catch (e: Exception) {
                // Ignore for now
            }
        }
    }

    fun sendImage(uri: android.net.Uri) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: "unknown"
        val receiverId = _uiState.value.targetUser?.id ?: "unknown"

        val tempMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            senderId = currentUserUid,
            receiverId = receiverId,
            text = "Sent an image",
            imageUrl = "loading",
            timestamp = System.currentTimeMillis()
        )

        _uiState.update { state ->
            state.copy(chatMessages = state.chatMessages + tempMessage)
        }

        viewModelScope.launch {
            try {
                val messageId = repository.saveChatMessage(tempMessage)
                val downloadUrl = repository.uploadChatImage(currentUserUid, messageId, uri)
                repository.updateChatMessageImage(messageId, downloadUrl)
            } catch (e: Exception) {
                // Ignore for now, perhaps remove the temporary message or show an error
            }
        }
    }

    fun updateSchedule(date: String, time: String) {
        _uiState.update { it.copy(scheduledDate = date, scheduledTime = time) }
    }

    fun confirmShift() {
        _uiState.update { it.copy(isShiftConfirmed = !_uiState.value.isShiftConfirmed) }
        if (_uiState.value.isShiftConfirmed) {
            viewModelScope.launch {
                val role = _uiState.value.userRole
                val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: "unknown"
                val targetUserId = _uiState.value.targetUser?.id ?: "unknown"

                val recruiterId = if (role == "recruiter") currentUserUid else targetUserId
                val workerId = if (role == "worker") currentUserUid else targetUserId

                val appointment = Appointment(
                    id = "${recruiterId}_${workerId}",
                    date = _uiState.value.scheduledDate,
                    time = _uiState.value.scheduledTime,
                    recruiterId = recruiterId,
                    workerId = workerId
                )
                try {
                    repository.saveAppointment(appointment)
                } catch (e: Exception) {
                    // Ignore for now
                }
            }
        }
    }
}
