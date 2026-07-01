package com.example.needhands.ui.chat

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.needhands.data.model.ChatMessage
import com.example.needhands.data.model.Worker
import com.example.needhands.ui.components.CustomTopBar
import com.example.needhands.ui.theme.AvailabilityActive
import com.example.needhands.ui.theme.NeedHandsTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.firebase.auth.FirebaseAuth

@Composable
fun JobChatScreen(
    viewModel: JobChatViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    
    val photoPicker = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.sendImage(uri)
        }
    }

    val listState = rememberLazyListState()

    // Scroll to the bottom of the list when messages change
    LaunchedEffect(state.chatMessages.size) {
        if (state.chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(state.chatMessages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = state.targetUser?.name ?: androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.job_chat_title),
                showBackButton = true,
                onBackClick = onNavigateBack
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // TOP AREA: Split UI - Job Coordination Details
            Card(
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.shift_coordination_details),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Confirmation toggle state
                    val confirmButtonColor by animateColorAsState(
                        targetValue = if (state.isShiftConfirmed) AvailabilityActive else MaterialTheme.colorScheme.primary,
                        label = "buttonColor"
                    )
                    Button(
                        onClick = { viewModel.confirmShift() },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = confirmButtonColor
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = if (state.isShiftConfirmed) Icons.Default.Check else Icons.Default.Check,
                            contentDescription = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.confirm),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (state.isShiftConfirmed) androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.shift_confirmed) else androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.confirm_shift),
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Date & Time Picker Banner
                    val context = androidx.compose.ui.platform.LocalContext.current
                    var dataSelecionada by remember(state.scheduledDate) { mutableStateOf(state.scheduledDate) }
                    var horaSelecionada by remember(state.scheduledTime) { mutableStateOf(state.scheduledTime) }
                    
                    val isWorker = state.userRole == "worker"

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !isWorker) {
                                val calendar = java.util.Calendar.getInstance()
                                android.app.DatePickerDialog(
                                    context,
                                    { _, year, month, day ->
                                        dataSelecionada = String.format("%02d/%02d/%04d", day, month + 1, year)
                                        
                                        // Pick Start Time
                                        val startTimeDialog = android.app.TimePickerDialog(
                                            context,
                                            { _, startHour, startMinute ->
                                                val startTime = String.format("%02d:%02d", startHour, startMinute)
                                                
                                                // Pick End Time
                                                val endTimeDialog = android.app.TimePickerDialog(
                                                    context,
                                                    { _, endHour, endMinute ->
                                                        val endTime = String.format("%02d:%02d", endHour, endMinute)
                                                        horaSelecionada = "$startTime - $endTime"
                                                        
                                                        // Update the ViewModel
                                                        viewModel.updateSchedule(dataSelecionada, horaSelecionada)
                                                    },
                                                    startHour + 1, startMinute, true
                                                )
                                                endTimeDialog.setTitle(context.getString(com.example.needhands.R.string.end_time))
                                                endTimeDialog.show()
                                                
                                            },
                                            calendar.get(java.util.Calendar.HOUR_OF_DAY),
                                            calendar.get(java.util.Calendar.MINUTE),
                                            true
                                        )
                                        startTimeDialog.setTitle(context.getString(com.example.needhands.R.string.start_time))
                                        startTimeDialog.show()
                                    },
                                    calendar.get(java.util.Calendar.YEAR),
                                    calendar.get(java.util.Calendar.MONTH),
                                    calendar.get(java.util.Calendar.DAY_OF_MONTH)
                                ).show()
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.date),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = dataSelecionada,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.time),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = horaSelecionada,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            if (!isWorker) {
                                Text(
                                    text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.edit),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            } else {
                                Text(
                                    text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.read_only),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }
            }

            // BOTTOM AREA: 1-to-1 Chat feed list
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = state.chatMessages,
                        key = { it.id }
                    ) { message ->
                        MessageBubble(message = message)
                    }
                }
            }

            // Message Input bar area
            Card(
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            photoPicker.launch(androidx.activity.result.PickVisualMediaRequest(androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Add,
                            contentDescription = "Attach image",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    OutlinedTextField(
                        value = state.currentMessageText,
                        onValueChange = { viewModel.onMessageTextChanged(it) },
                        placeholder = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.plan_shift_details)) },
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = { viewModel.sendMessage() },
                        enabled = state.currentMessageText.isNotBlank(),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.send),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi::class)
@Composable
fun MessageBubble(
    message: ChatMessage,
    modifier: Modifier = Modifier
) {
    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
    val isMe = message.senderId == currentUserUid
    val timeString = remember(message.timestamp) {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        sdf.format(Date(message.timestamp))
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isMe) 16.dp else 4.dp,
                bottomEnd = if (isMe) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                if (!message.imageUrl.isNullOrBlank()) {
                    if (message.imageUrl == "loading") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(32.dp), color = if(isMe) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary)
                        }
                    } else {
                        com.bumptech.glide.integration.compose.GlideImage(
                            model = message.imageUrl,
                            contentDescription = "Chat image",
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 250.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                if (message.text.isNotBlank() && message.text != "Sent an image") {
                    Text(
                        text = message.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isMe) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    )
                } else if (message.text.isNotBlank() && message.imageUrl.isNullOrBlank()) {
                    Text(
                        text = message.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isMe) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = timeString,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 8.sp,
                    color = if (isMe) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JobChatScreenPreview() {
    val vm = JobChatViewModel().apply {
        // Preview dummy initialization
    }
    NeedHandsTheme {
        JobChatScreen(
            viewModel = vm,
            onNavigateBack = {}
        )
    }
}
