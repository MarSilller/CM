package com.example.needhands.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.needhands.ui.components.CustomTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var newPassword by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(state.successMessage, state.errorMessage) {
        if (state.successMessage != null) {
            snackbarHostState.showSnackbar(context.getString(state.successMessage!!))
            viewModel.clearMessages()
            newPassword = ""
        }
        if (state.errorMessage != null) {
            snackbarHostState.showSnackbar(context.getString(state.errorMessage!!))
            viewModel.clearMessages()
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.settings),
                showBackButton = true,
                onBackClick = onNavigateBack
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.account_details),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // Email
            OutlinedTextField(
                value = state.email,
                onValueChange = {},
                readOnly = true,
                label = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.email_colon)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password
            OutlinedTextField(
                value = "********",
                onValueChange = {},
                readOnly = true,
                label = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.password_colon)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // New Password
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.new_password_colon)) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Change Password Link
            Text(
                text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.change_password),
                color = if (newPassword.length >= 6) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                textDecoration = TextDecoration.Underline,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable(enabled = newPassword.length >= 6) {
                    showDialog = true
                }
            )
            
            if (state.isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.confirm_password_change)) },
                text = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.confirm_password_change_desc)) },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        viewModel.updatePassword(newPassword)
                    }) {
                        Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.yes))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.cancel))
                    }
                }
            )
        }
    }
}
