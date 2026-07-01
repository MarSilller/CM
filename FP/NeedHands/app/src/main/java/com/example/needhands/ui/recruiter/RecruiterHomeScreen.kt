package com.example.needhands.ui.recruiter

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.needhands.data.model.Worker
import com.example.needhands.ui.components.CustomTopBar
import com.example.needhands.ui.components.WorkerCard
import com.example.needhands.ui.theme.NeedHandsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruiterHomeScreen(
    viewModel: RecruiterHomeViewModel,
    onWorkerSelected: (Worker) -> Unit,
    onLogout: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var isFiltersExpanded by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }

    val locations = listOf(
        "Lisboa", "Porto", "Braga", "Guimarães", "Viana do Castelo", "Vila Nova de Gaia",
        "Matosinhos", "Bragança", "Coimbra", "Aveiro", "Viseu", "Leiria", "Guarda",
        "Castelo Branco", "Almada", "Amadora", "Setúbal", "Santarém", "Torres Novas",
        "Évora", "Beja", "Portalegre", "Elvas", "Faro", "Portimão", "Lagos", "Albufeira", "Tavira"
    )
    val availabilities = listOf("Immediate", "Flexible", "Weekends only")
    val qualifications = listOf("Waiter", "Bartender", "Kitchen assistant", "Doorman/bouncer", "Event cleaner", "Event setup crew")

    Scaffold(
        topBar = {
            CustomTopBar(
                title = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.app_name),
                showBackButton = false,
                actions = {
                    val themeToggle = com.example.needhands.LocalThemeToggle.current
                    androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.themes),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
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
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.logout),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            androidx.compose.material3.FloatingActionButton(
                onClick = onSettingsClick,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Settings,
                    contentDescription = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.settings)
                )
            }
        },
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.Start,
        modifier = modifier
    ) { innerPadding ->
        if (showHelpDialog) {
            AlertDialog(
                onDismissRequest = { showHelpDialog = false },
                title = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.help_recruiter_title)) },
                text = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.help_recruiter_desc)) },
                confirmButton = {
                    TextButton(onClick = { showHelpDialog = false }) {
                        Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.close))
                    }
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Search Bar & Headline
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.find_short_term_hands),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.browse_active_workers),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = { viewModel.onSearchQueryChanged(it) },
                        placeholder = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.search_placeholder)) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (state.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.clear))
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { isFiltersExpanded = !isFiltersExpanded }) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.toggle_filters),
                            tint = if (isFiltersExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Filters Section
            AnimatedVisibility(visible = isFiltersExpanded) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Location Filters
                Text(
                    text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.location_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = state.selectedLocationFilter == null,
                            onClick = { viewModel.onLocationFilterSelected(null) },
                            label = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.all_locations)) }
                        )
                    }
                    items(locations) { loc ->
                        FilterChip(
                            selected = state.selectedLocationFilter == loc,
                            onClick = { viewModel.onLocationFilterSelected(loc) },
                            label = { Text(loc) }
                        )
                    }
                }

                // Availability Filters
                Text(
                    text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.availability_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = state.selectedAvailabilityFilter == null,
                            onClick = { viewModel.onAvailabilityFilterSelected(null) },
                            label = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.any_availability)) }
                        )
                    }
                    items(availabilities) { avail ->
                        val displayAvail = when (avail) {
                            "Immediate" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.availability_immediate)
                            "Flexible" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.availability_flexible)
                            "Weekends only" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.availability_weekends)
                            else -> avail
                        }
                        FilterChip(
                            selected = state.selectedAvailabilityFilter == avail,
                            onClick = { viewModel.onAvailabilityFilterSelected(avail) },
                            label = { Text(displayAvail) }
                        )
                    }
                }

                // Qualifications Filters
                Text(
                    text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.choose_qualifications),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = state.selectedQualificationFilter == null,
                            onClick = { viewModel.onQualificationFilterSelected(null) },
                            label = { Text(androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.any_role)) }
                        )
                    }
                    items(qualifications) { qual ->
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
                            selected = state.selectedQualificationFilter == qual,
                            onClick = { viewModel.onQualificationFilterSelected(qual) },
                            label = { Text(displayQual) }
                        )
                    }
                }
            }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Marketplace List Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (state.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (state.filteredWorkers.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.no_workers_found),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = state.filteredWorkers,
                            key = { it.id }
                        ) { worker ->
                            WorkerCard(
                                worker = worker,
                                onClick = { onWorkerSelected(worker) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecruiterHomeScreenPreview() {
    NeedHandsTheme {
        RecruiterHomeScreen(
            viewModel = RecruiterHomeViewModel(),
            onWorkerSelected = {},
            onLogout = {},
            onSettingsClick = {}
        )
    }
}
