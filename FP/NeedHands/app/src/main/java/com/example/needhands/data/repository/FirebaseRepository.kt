package com.example.needhands.data.repository

import com.example.needhands.data.model.Appointment
import com.example.needhands.data.model.ChatMessage
import com.example.needhands.data.model.UserProfile
import com.example.needhands.data.model.Worker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import android.net.Uri
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun saveUserProfile(profile: UserProfile) {
        firestore.collection("Users").document(profile.id).set(profile).await()
    }

    suspend fun updatePassword(newPassword: String): Result<Unit> {
        return try {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                user.updatePassword(newPassword).await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("No user logged in"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(uid: String): UserProfile? {
        return try {
            val document = firestore.collection("Users").document(uid).get().await()
            document.toObject(UserProfile::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveAppointment(appointment: Appointment) {
        val document = if (appointment.id.isBlank()) {
            firestore.collection("Appointments").document()
        } else {
            firestore.collection("Appointments").document(appointment.id)
        }
        val appointmentToSave = appointment.copy(id = document.id)
        document.set(appointmentToSave).await()
    }

    suspend fun saveChatMessage(chatMessage: ChatMessage): String {
        val document = if (chatMessage.id.isBlank()) {
            firestore.collection("Chats").document()
        } else {
            firestore.collection("Chats").document(chatMessage.id)
        }
        val chatMessageToSave = chatMessage.copy(id = document.id)
        document.set(chatMessageToSave).await()
        return document.id
    }

    suspend fun uploadProfilePicture(uid: String, uri: Uri): String {
        val storageRef = FirebaseStorage.getInstance().reference.child("profile_pictures/$uid.jpg")
        storageRef.putFile(uri).await()
        return storageRef.downloadUrl.await().toString()
    }

    suspend fun uploadChatImage(userId: String, messageId: String, uri: Uri): String {
        val storageRef = FirebaseStorage.getInstance().reference.child("images/$userId/$messageId/image.jpg")
        storageRef.putFile(uri).await()
        return storageRef.downloadUrl.await().toString()
    }

    suspend fun updateChatMessageImage(messageId: String, imageUrl: String) {
        firestore.collection("Chats").document(messageId)
            .update("imageUrl", imageUrl)
            .await()
    }

    fun getWorkersFlow(): Flow<List<Worker>> = callbackFlow {
        val listenerRegistration = firestore.collection("Users")
            .whereEqualTo("role", "worker")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val workers = snapshot?.documents?.mapNotNull { doc ->
                    val profile = doc.toObject(UserProfile::class.java)
                    if (profile != null && profile.location.isNotBlank() && profile.qualifications.isNotEmpty()) {
                        Worker(
                            id = profile.id,
                            name = profile.name,
                            age = profile.age,
                            gender = profile.gender,
                            location = profile.location,
                            rating = profile.rating,
                            availability = profile.availability,
                            qualifications = profile.qualifications,
                            isBoosted = profile.isBoosted,
                            profileImageUrl = profile.profileImageUrl
                        )
                    } else null
                } ?: emptyList()
                
                trySend(workers)
            }
        awaitClose { listenerRegistration.remove() }
    }

    fun getChatMessagesFlow(recruiterId: String, workerId: String): Flow<List<ChatMessage>> = callbackFlow {
        val listenerRegistration = firestore.collection("Chats")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val messages = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(ChatMessage::class.java)
                }?.filter { 
                    (it.senderId == recruiterId && it.receiverId == workerId) || 
                    (it.senderId == workerId && it.receiverId == recruiterId)
                } ?: emptyList()

                trySend(messages)
            }
        awaitClose { listenerRegistration.remove() }
    }

    fun getAppointmentFlow(recruiterId: String, workerId: String): Flow<Appointment?> = callbackFlow {
        val listenerRegistration = firestore.collection("Appointments")
            .document("${recruiterId}_${workerId}")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val appointment = snapshot?.toObject(Appointment::class.java)
                trySend(appointment)
            }
        awaitClose { listenerRegistration.remove() }
    }

    fun getWorkerChatsFlow(workerId: String): Flow<List<UserProfile>> = callbackFlow {
        val listenerRegistration = firestore.collection("Chats")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val recruiterIds = snapshot?.documents?.mapNotNull { doc ->
                    val chat = doc.toObject(ChatMessage::class.java)
                    if (chat != null) {
                        if (chat.receiverId == workerId) chat.senderId
                        else if (chat.senderId == workerId) chat.receiverId
                        else null
                    } else null
                }?.distinct() ?: emptyList()

                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                    val recruiters = recruiterIds.mapNotNull { id ->
                        getUserProfile(id)
                    }
                    trySend(recruiters)
                }
            }
        awaitClose { listenerRegistration.remove() }
    }
}
