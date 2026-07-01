package com.example.needhands.ui.worker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.needhands.ui.components.CustomTopBar
import com.example.needhands.ui.theme.NeedHandsTheme

@OptIn(ExperimentalMaterial3Api::class, com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi::class, ExperimentalLayoutApi::class)
@Composable
fun WorkerProfileScreen(
    viewModel: WorkerProfileViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = androidx.compose.ui.platform.LocalContext.current

    val photoPicker = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.onProfilePictureUploaded(uri.toString())
        }
    }
    
    val qualificationsList = listOf(
        "Kitchen assistant",
        "Waiter",
        "Bartender",
        "Event cleaner",
        "Doorman/bouncer",
        "Event setup crew"
    )

    val availabilityOptions = listOf("Immediate", "Flexible", "Weekends only")

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            snackbarHostState.showSnackbar(context.getString(com.example.needhands.R.string.success_profile_saved))
            viewModel.resetSaveSuccess()
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.edit_profile),
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
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Image Upload Area
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable {
                        photoPicker.launch(androidx.activity.result.PickVisualMediaRequest(androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                contentAlignment = Alignment.Center
            ) {
                if (!state.profilePictureUri.isNullOrBlank()) {
                    com.bumptech.glide.integration.compose.GlideImage(
                        model = state.profilePictureUri,
                        contentDescription = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.profile_picture_desc),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        loading = com.bumptech.glide.integration.compose.placeholder {
                            CircularProgressIndicator(modifier = Modifier.padding(32.dp))
                        },
                        failure = com.bumptech.glide.integration.compose.placeholder {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.upload_photo_desc),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.add_photo),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    )
                } else {
                    // Upload prompt
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.upload_photo_desc),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.add_photo),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Profile info card forms
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.worker_details),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Name
                    OutlinedTextField(
                        value = state.name,
                        onValueChange = { viewModel.onNameChanged(it) },
                        label = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.name_label)) },
                        placeholder = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.example_name)) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Age
                    OutlinedTextField(
                        value = state.age,
                        onValueChange = { viewModel.onAgeChanged(it) },
                        label = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.age_label)) },
                        placeholder = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.example_age)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Gender Segmented Control
                    Text(
                        text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.gender_label),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Male", "Female").forEach { option ->
                            val isSelected = state.gender == option
                            val displayOption = when (option) {
                                "Male" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.gender_male)
                                "Female" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.gender_female)
                                else -> option
                            }
                            Button(
                                onClick = { viewModel.onGenderChanged(option) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(displayOption)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Location Dropdown Menu Selector
                    Text(
                        text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.location_label),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(6.dp))

                    var expandedLocation by remember { mutableStateOf(false) }
                    val locationOptions = listOf(
                        "Lisboa", "Porto", "Braga", "Guimarães", "Viana do Castelo", "Vila Nova de Gaia",
                        "Matosinhos", "Bragança", "Coimbra", "Aveiro", "Viseu", "Leiria", "Guarda",
                        "Castelo Branco", "Almada", "Amadora", "Setúbal", "Santarém", "Torres Novas",
                        "Évora", "Beja", "Portalegre", "Elvas", "Faro", "Portimão", "Lagos", "Albufeira", "Tavira"
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandedLocation,
                        onExpandedChange = { expandedLocation = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = state.location,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.location_label)) },
                            placeholder = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.select_location)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLocation) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedLocation,
                            onDismissRequest = { expandedLocation = false }
                        ) {
                            locationOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        viewModel.onLocationChanged(option)
                                        expandedLocation = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Availability Dropdown Menu Selector
                    Text(
                        text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.availability_label),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(6.dp))

                    var expandedAvailability by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandedAvailability,
                        onExpandedChange = { expandedAvailability = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = state.availability,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAvailability) },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface
                            ),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedAvailability,
                            onDismissRequest = { expandedAvailability = false }
                        ) {
                            availabilityOptions.forEach { option ->
                                val displayOption = when (option) {
                                    "Immediate" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.availability_immediate)
                                    "Flexible" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.availability_flexible)
                                    "Weekends only" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.availability_weekends)
                                    else -> option
                                }
                                DropdownMenuItem(
                                    text = { Text(text = displayOption) },
                                    onClick = {
                                        viewModel.onAvailabilityChanged(option)
                                        expandedAvailability = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Qualifications card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.choose_qualifications),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.choose_qualifications_desc),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Qualifications Grid list
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        qualificationsList.forEach { qual ->
                            val isSelected = state.selectedQualifications.contains(qual)
                            val displayQual = when (qual) {
                                "Kitchen assistant" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.qual_kitchen)
                                "Waiter" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.qual_waiter)
                                "Bartender" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.qual_bartender)
                                "Event cleaner" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.qual_cleaner)
                                "Doorman/bouncer" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.qual_doorman)
                                "Event setup crew" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.qual_setup)
                                else -> qual
                            }
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.toggleQualification(qual) },
                                label = { Text(displayQual) },
                                leadingIcon = {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Error display
            AnimatedVisibility(visible = state.errorMessage != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.error),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (state.errorMessage != null) context.getString(state.errorMessage!!) else "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            }
            
            // Fixed bottom area for button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Save Profile Button
                Button(
                    onClick = { viewModel.saveProfile() },
                    enabled = !state.isSaving,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.save_profile),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkerProfileScreenPreview() {
    NeedHandsTheme {
        WorkerProfileScreen(
            viewModel = WorkerProfileViewModel(),
            onNavigateBack = {}
        )
    }
}
