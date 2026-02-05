package com.eventhub.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eventhub.app.data.Event
import com.eventhub.app.data.User
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import com.eventhub.app.ui.theme.ThemeMode
import com.eventhub.app.viewmodel.ThemeViewModel
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import kotlin.math.abs
import kotlin.math.sign
import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    user: User,
    events: List<Event>,
    registeredEvents: Set<String>,
    currentEventIndex: Int,
    onLogout: () -> Unit,
    onRegisterEvent: (String) -> Unit,
    onUnregisterEvent: (String) -> Unit,
    onUpdateEventIndex: (Int) -> Unit,
    themeViewModel: ThemeViewModel,
    onShowEventDetails: (Event) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    var showThemeMenu by remember { mutableStateOf(false) }
    val currentTheme by themeViewModel.themeMode.collectAsState()
    
    val availableEvents = events.filter { it.id !in registeredEvents }
    
    // Get current event from original events list, not filtered
    val currentEvent = if (events.isNotEmpty() && currentEventIndex < events.size) {
        val eventAtIndex = events[currentEventIndex]
        if (eventAtIndex.id in registeredEvents) {
            // Current event is registered, find next available
            events.drop(currentEventIndex + 1).firstOrNull { it.id !in registeredEvents }
                ?: events.take(currentEventIndex).firstOrNull { it.id !in registeredEvents }
        } else {
            eventAtIndex
        }
    } else null

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "EventHub",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    // Theme Selector Icon
                    Box {
                        IconButton(onClick = { showThemeMenu = true }) {
                            Icon(
                                imageVector = when (currentTheme) {
                                    ThemeMode.LIGHT -> Icons.Filled.LightMode
                                    ThemeMode.DARK -> Icons.Filled.DarkMode
                                    ThemeMode.SYSTEM -> Icons.Filled.PhoneAndroid
                                },
                                contentDescription = "Change Theme",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        // Theme Dropdown Menu
                        DropdownMenu(
                            expanded = showThemeMenu,
                            onDismissRequest = { showThemeMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Icon(
                                            Icons.Filled.LightMode,
                                            contentDescription = null,
                                            tint = if (currentTheme == ThemeMode.LIGHT)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.onSurface
                                        )
                                        Text("Light")
                                        if (currentTheme == ThemeMode.LIGHT) {
                                            Icon(
                                                Icons.Filled.Check,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    themeViewModel.setThemeMode(ThemeMode.LIGHT)
                                    showThemeMenu = false
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Icon(
                                            Icons.Filled.DarkMode,
                                            contentDescription = null,
                                            tint = if (currentTheme == ThemeMode.DARK)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.onSurface
                                        )
                                        Text("Dark")
                                        if (currentTheme == ThemeMode.DARK) {
                                            Icon(
                                                Icons.Filled.Check,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    themeViewModel.setThemeMode(ThemeMode.DARK)
                                    showThemeMenu = false
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Icon(
                                            Icons.Filled.PhoneAndroid,
                                            contentDescription = null,
                                            tint = if (currentTheme == ThemeMode.SYSTEM)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.onSurface
                                        )
                                        Text("System Default")
                                        if (currentTheme == ThemeMode.SYSTEM) {
                                            Icon(
                                                Icons.Filled.Check,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    themeViewModel.setThemeMode(ThemeMode.SYSTEM)
                                    showThemeMenu = false
                                }
                            )
                        }
                    }

                    // Logout Icon
                    IconButton(onClick = onLogout) {
                        Icon(
                            Icons.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 3.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home,
                            null
                        )
                    },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            if (selectedTab == 1) Icons.Filled.Event else Icons.Outlined.Event,
                            null
                        )
                    },
                    label = { Text("My Events") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            if (selectedTab == 2) Icons.Filled.Person else Icons.Outlined.Person,
                            null
                        )
                    },
                    label = { Text("Profile") }
                )
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> {
                HomeScreen(
                    currentEvent = currentEvent,
                    allEvents = events,
                    registeredEvents = registeredEvents,
                    currentEventIndex = currentEventIndex,
                    onCurrentEventIndexChange = onUpdateEventIndex,
                    onRegisterEvent,
                    onUnregisterEvent,
                    onShowEventDetails,
                    Modifier.padding(padding)
                )
            }
            1 -> MyEventsScreen(
                events.filter { it.id in registeredEvents },
                onUnregisterEvent,
                onShowEventDetails,
                Modifier.padding(padding)
            )
            2 -> ProfileScreen(user, Modifier.padding(padding))
        }
    }
}

@Composable
fun HomeScreen(
    currentEvent: Event?,
    allEvents: List<Event>,
    registeredEvents: Set<String>,
    currentEventIndex: Int,
    onCurrentEventIndexChange: (Int) -> Unit,
    onRegisterEvent: (String) -> Unit,
    onUnregisterEvent: (String) -> Unit,
    onShowEventDetails: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategories by remember { mutableStateOf(setOf<String>()) }
    var showFilterSheet by remember { mutableStateOf(false) }
    
    val availableCategories = listOf("Technical", "Cultural", "Sports", "Workshop", "Business", "Gaming", "Social")
    
    val filteredEvents = allEvents.filter { event ->
        val matchesSearch = event.title.contains(searchQuery, ignoreCase = true) ||
                           event.description.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategories.isEmpty() || event.category in selectedCategories
        val notRegistered = event.id !in registeredEvents
        matchesSearch && matchesCategory && notRegistered
    }
    
    val displayEvent = filteredEvents.getOrNull(0)
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        // Search and Filter Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search events...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            
            Surface(
                onClick = { showFilterSheet = true },
                shape = RoundedCornerShape(12.dp),
                color = if (selectedCategories.isEmpty()) 
                    MaterialTheme.colorScheme.surface 
                else 
                    MaterialTheme.colorScheme.primaryContainer,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (selectedCategories.isEmpty())
                        MaterialTheme.colorScheme.outline
                    else
                        MaterialTheme.colorScheme.primary
                )
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.FilterList,
                        "Filter",
                        tint = if (selectedCategories.isEmpty())
                            MaterialTheme.colorScheme.onSurface
                        else
                            MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        
        // Active Filters
        if (selectedCategories.isNotEmpty()) {
            androidx.compose.foundation.lazy.LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(selectedCategories.toList()) { category ->
                    FilterChip(
                        selected = true,
                        onClick = { selectedCategories = selectedCategories - category },
                        label = { Text(category) },
                        trailingIcon = { Icon(Icons.Default.Close, null, Modifier.size(16.dp)) }
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }
        
        // Event Card
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (displayEvent == null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            if (searchQuery.isNotEmpty() || selectedCategories.isNotEmpty())
                                "No matching events"
                            else
                                "No events available",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            if (searchQuery.isNotEmpty() || selectedCategories.isNotEmpty())
                                "Try adjusting your filters"
                            else
                                "Check back later for new events",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                SwipeableEventCard(
                    event = displayEvent,
                    onSwipeLeft = {
                        val currentIndex = filteredEvents.indexOf(displayEvent)
                        if (currentIndex < filteredEvents.size - 1) {
                            // Show next filtered event
                        }
                    },
                    onSwipeRight = {
                        val currentIndex = filteredEvents.indexOf(displayEvent)
                        if (currentIndex > 0) {
                            // Show previous filtered event
                        }
                    },
                    onCardClick = {
                        onShowEventDetails(displayEvent)
                    }
                )
            }
        }
    }
    
    // Filter Bottom Sheet
    if (showFilterSheet) {
        androidx.compose.material3.ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    "Filter by Category",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                availableCategories.forEach { category ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedCategories = if (category in selectedCategories) {
                                    selectedCategories - category
                                } else {
                                    selectedCategories + category
                                }
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = category in selectedCategories,
                            onCheckedChange = null
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            category,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                
                Spacer(Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            selectedCategories = emptySet()
                            showFilterSheet = false
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Clear All")
                    }
                    Button(
                        onClick = { showFilterSheet = false },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Apply")
                    }
                }
                
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SwipeableEventCard(
    event: Event,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    onCardClick: () -> Unit
) {
    var offsetX by remember(event.id) { mutableStateOf(0f) }
    
    val density = LocalDensity.current
    val screenWidth = with(density) { 400.dp.toPx() }
    
    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = tween(300),
        label = "cardOffset"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .graphicsLayer(translationX = animatedOffsetX)
            .clickable { onCardClick() }
            .pointerInput(event.id) {
                detectDragGestures(
                    onDragEnd = {
                        val threshold = screenWidth * 0.3f
                        when {
                            offsetX > threshold -> {
                                onSwipeLeft()
                                offsetX = 0f
                            }
                            offsetX < -threshold -> {
                                onSwipeRight()
                                offsetX = 0f
                            }
                            else -> offsetX = 0f
                        }
                    }
                ) { _, dragAmount ->
                    offsetX = (offsetX + dragAmount.x).coerceIn(-screenWidth, screenWidth)
                }
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(
                        when (event.category) {
                            "Technical" -> MaterialTheme.colorScheme.primaryContainer
                            "Cultural" -> MaterialTheme.colorScheme.secondaryContainer
                            "Sports" -> MaterialTheme.colorScheme.tertiaryContainer
                            else -> MaterialTheme.colorScheme.surfaceContainer
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    when (event.category) {
                        "Technical" -> Icons.Default.Computer
                        "Cultural" -> Icons.Default.MusicNote
                        "Sports" -> Icons.Default.SportsBasketball
                        else -> Icons.Default.Event
                    },
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    event.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    event.date,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    "Tap for details • Swipe to browse",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
        
        // Swipe indicators
        if (abs(animatedOffsetX) > 80f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "NEXT EVENT",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun WelcomeHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Welcome Back!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Explore and register for events",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
            Icon(
                Icons.Filled.Celebration,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun EventStatsCard(totalEvents: Int, registeredCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            icon = Icons.Filled.Event,
            value = totalEvents.toString(),
            label = "Total Events",
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = Icons.Filled.CheckCircle,
            value = registeredCount.toString(),
            label = "Registered",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun MyEventsScreen(
    events: List<Event>,
    onUnregisterEvent: (String) -> Unit,
    onShowEventDetails: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    "My Events",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${events.size} registered events",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (events.isEmpty()) {
            item {
                EmptyStateCard()
            }
        } else {
            items(events) { event ->
                EventCard(
                    event = event,
                    isRegistered = true,
                    onRegister = {},
                    onUnregister = { onUnregisterEvent(event.id) }
                )
            }
        }
    }
}

@Composable
fun EmptyStateCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Outlined.EventBusy,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "No Events Yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Register for events to see them here",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ProfileScreen(
    user: User,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            ProfileHeaderCard(user)
        }

        item {
            ProfileDetailsCard(user)
        }
    }
}

@Composable
fun ProfileHeaderCard(user: User) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(96.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = user.name.first().uppercase(),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = user.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun ProfileDetailsCard(user: User) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Account Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            ProfileInfoRow(Icons.Outlined.Badge, "College ID", user.collegeId)
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            ProfileInfoRow(Icons.Outlined.Email, "Email Address", user.email)
        }
    }
}

@Composable
private fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun EventCard(
    event: Event,
    isRegistered: Boolean,
    onRegister: () -> Unit,
    onUnregister: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    event.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                if (isRegistered) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "Registered",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                event.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(16.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHighest
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    InfoRow(Icons.Outlined.CalendarToday, event.date)
                    Spacer(Modifier.height(8.dp))
                    InfoRow(Icons.Outlined.Schedule, event.time)
                    Spacer(Modifier.height(8.dp))
                    InfoRow(Icons.Outlined.LocationOn, event.location)
                }
            }

            Spacer(Modifier.height(16.dp))

            if (isRegistered) {
                OutlinedButton(
                    onClick = onUnregister,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Outlined.Cancel,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Unregister")
                }
            } else {
                Button(
                    onClick = onRegister,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Register Now")
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}