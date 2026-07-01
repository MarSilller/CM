package com.example.needhands.ui.worker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.draw.scale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.needhands.ui.components.WorkerCard
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerHomeScreen(
    viewModel: WorkerHomeViewModel,
    onEditProfileClick: () -> Unit,
    onLogout: () -> Unit,
    onChatListClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var showBoostDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }

    // Reload profile every time the screen is displayed (e.g. after returning from edit)
    LaunchedEffect(Unit) {
        viewModel.loadWorkerProfile()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.worker_home_title), 
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    ) 
                },
                actions = {
                    val themeToggle = com.example.needhands.LocalThemeToggle.current
                    androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.themes),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        androidx.compose.material3.Switch(
                            checked = themeToggle.isDarkTheme,
                            onCheckedChange = { themeToggle.toggleTheme() },
                            modifier = Modifier.padding(end = 8.dp),
                            colors = androidx.compose.material3.SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                    }
                    IconButton(onClick = { showHelpDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.help),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(onClick = {
                        FirebaseAuth.getInstance().signOut()
                        onLogout()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.logout),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            Column(
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.End
            ) {
                androidx.compose.material3.FloatingActionButton(
                    onClick = onChatListClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Filled.Email,
                        contentDescription = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.chats)
                    )
                }
            }
        },
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.End
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Settings FAB on bottom left
            androidx.compose.material3.FloatingActionButton(
                onClick = onSettingsClick,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Settings,
                    contentDescription = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.settings)
                )
            }
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.worker_home_description),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    state.workerProfile?.let { worker ->
                        WorkerCard(
                            worker = worker,
                            onClick = { onEditProfileClick() }
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        val isBoosted = worker.isBoosted
                        
                        var playAnimation by remember { mutableStateOf(false) }
                        var wasBoosted by remember { mutableStateOf(isBoosted) }

                        LaunchedEffect(isBoosted) {
                            if (!wasBoosted && isBoosted) {
                                playAnimation = true
                                kotlinx.coroutines.delay(800)
                                playAnimation = false
                            }
                            wasBoosted = isBoosted
                        }

                        val buttonScale by androidx.compose.animation.core.animateFloatAsState(
                            targetValue = if (playAnimation) 1.15f else 1f,
                            animationSpec = if (playAnimation) {
                                androidx.compose.animation.core.spring(
                                    dampingRatio = androidx.compose.animation.core.Spring.DampingRatioHighBouncy,
                                    stiffness = androidx.compose.animation.core.Spring.StiffnessMedium
                                )
                            } else {
                                androidx.compose.animation.core.tween(300)
                            },
                            label = "buttonScale"
                        )
                        
                        val starScale by androidx.compose.animation.core.animateFloatAsState(
                            targetValue = if (playAnimation) 1.8f else 0.5f,
                            animationSpec = androidx.compose.animation.core.tween(600, easing = androidx.compose.animation.core.FastOutSlowInEasing),
                            label = "starScale"
                        )
                        val starAlpha by androidx.compose.animation.core.animateFloatAsState(
                            targetValue = if (playAnimation) 0f else 1f,
                            animationSpec = androidx.compose.animation.core.tween(600),
                            label = "starAlpha"
                        )

                        val buttonColor by androidx.compose.animation.animateColorAsState(
                            targetValue = if (isBoosted) androidx.compose.ui.graphics.Color.Gray else androidx.compose.ui.graphics.Color(0xFFFFC107),
                            animationSpec = androidx.compose.animation.core.tween(durationMillis = 500),
                            label = "boostButtonColor"
                        )
                        
                        Box(contentAlignment = Alignment.Center) {
                            if (playAnimation) {
                                Icon(
                                    Icons.Default.Star, contentDescription = null, 
                                    tint = androidx.compose.ui.graphics.Color(0xFFFFD700).copy(alpha = starAlpha), 
                                    modifier = Modifier.offset(x = (-80).dp, y = (-40).dp).size(24.dp).scale(starScale)
                                )
                                Icon(
                                    Icons.Default.Star, contentDescription = null, 
                                    tint = androidx.compose.ui.graphics.Color(0xFFFFD700).copy(alpha = starAlpha), 
                                    modifier = Modifier.offset(x = 80.dp, y = (-30).dp).size(16.dp).scale(starScale)
                                )
                                Icon(
                                    Icons.Default.Star, contentDescription = null, 
                                    tint = androidx.compose.ui.graphics.Color(0xFFFFD700).copy(alpha = starAlpha), 
                                    modifier = Modifier.offset(x = (-60).dp, y = 40.dp).size(20.dp).scale(starScale)
                                )
                                Icon(
                                    Icons.Default.Star, contentDescription = null, 
                                    tint = androidx.compose.ui.graphics.Color(0xFFFFD700).copy(alpha = starAlpha), 
                                    modifier = Modifier.offset(x = 60.dp, y = 30.dp).size(22.dp).scale(starScale)
                                )
                            }
                            
                            androidx.compose.material3.Button(
                                onClick = { showBoostDialog = true },
                                enabled = !isBoosted,
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                    containerColor = buttonColor,
                                    contentColor = androidx.compose.ui.graphics.Color.Black,
                                    disabledContainerColor = buttonColor,
                                    disabledContentColor = androidx.compose.ui.graphics.Color.LightGray
                                ),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                                elevation = androidx.compose.material3.ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .height(56.dp)
                                    .scale(buttonScale)
                            ) {
                                androidx.compose.material3.Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Boost Icon",
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = if (isBoosted) androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.boosted) else androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.boost),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    } ?: run {
                        Text(
                            text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.worker_profile_not_setup),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Text(
                        text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.tap_to_edit),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    if (showBoostDialog) {
        AlertDialog(
            onDismissRequest = { showBoostDialog = false },
            title = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.boost_account_title)) },
            text = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.boost_account_desc)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showBoostDialog = false
                        viewModel.buyBoost()
                    }
                ) {
                    Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.buy))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showBoostDialog = false }
                ) {
                    Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.cancel))
                }
            }
        )
    }

    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.help_worker_title)) },
            text = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.help_worker_desc)) },
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.close))
                }
            }
        )
    }
}
