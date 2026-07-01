package com.example.needhands.ui.worker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.needhands.data.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkerChatListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(WorkerChatListState())
    val uiState: StateFlow<WorkerChatListState> = _uiState.asStateFlow()
    private val repository = FirebaseRepository()

    init {
        loadChats()
    }

    private fun loadChats() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            repository.getWorkerChatsFlow(uid).collect { recruiters ->
                _uiState.update { 
                    it.copy(
                        recruiters = recruiters,
                        isLoading = false
                    ) 
                }
            }
        }
    }
}
