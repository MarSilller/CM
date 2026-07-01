package com.example.needhands.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.needhands.data.model.Worker
import com.example.needhands.ui.theme.AvailabilityActive

@OptIn(ExperimentalLayoutApi::class, com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi::class)
@Composable
fun WorkerCard(
    worker: Worker,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Profile Circle with Initials
                val initials = worker.name.split(" ")
                    .mapNotNull { it.firstOrNull() }
                    .take(2)
                    .joinToString("")
                    .uppercase()

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    if (!worker.profileImageUrl.isNullOrBlank()) {
                        com.bumptech.glide.integration.compose.GlideImage(
                            model = worker.profileImageUrl,
                            contentDescription = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.profile_picture_desc),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            failure = com.bumptech.glide.integration.compose.placeholder {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = initials,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        )
                    } else {
                        Text(
                            text = initials,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Name, Rating & Location
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = worker.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(2.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.location_label),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = worker.location,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Availability Badge Indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (worker.isAvailableNow) AvailabilityActive 
                                else MaterialTheme.colorScheme.outline
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    val displayAvailability = when (worker.availability) {
                        "Immediate" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.availability_immediate)
                        "Flexible" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.availability_flexible)
                        "Weekends only" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.availability_weekends)
                        else -> worker.availability
                    }
                    Text(
                        text = displayAvailability,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = if (worker.isAvailableNow) AvailabilityActive else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Rating Bar
            RatingBar(rating = worker.rating)

            Spacer(modifier = Modifier.height(10.dp))

            // Qualifications Badge list
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                worker.qualifications.forEach { qual ->
                    val displayQual = when (qual) {
                        "Kitchen assistant" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.qual_kitchen)
                        "Waiter" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.qual_waiter)
                        "Bartender" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.qual_bartender)
                        "Event cleaner" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.qual_cleaner)
                        "Doorman/bouncer" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.qual_doorman)
                        "Event setup crew" -> androidx.compose.ui.res.stringResource(id = com.example.needhands.R.string.qual_setup)
                        else -> qual
                    }
                    SuggestionChip(
                        onClick = {},
                        label = {
                            Text(
                                text = displayQual,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                            labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        border = null
                    )
                }
            }
        }
    }
}
