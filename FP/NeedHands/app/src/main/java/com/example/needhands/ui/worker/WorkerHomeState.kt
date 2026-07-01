package com.example.needhands.ui.worker

import com.example.needhands.data.model.Worker

data class WorkerHomeState(
    val workerProfile: Worker? = null,
    val isLoading: Boolean = true
)
