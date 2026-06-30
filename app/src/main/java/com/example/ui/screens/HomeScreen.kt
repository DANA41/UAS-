package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppViewModel
import com.example.ui.theme.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainerScreen(
    viewModel: AppViewModel,
    onNavigate: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val userProfile by viewModel.userProfile.collectAsState()

    val tabs = listOf(
        TabItem("Beranda", Icons.Default.Home, Icons.Outlined.Home),
        TabItem("Kalkulator", Icons.Default.Calculate, Icons.Outlined.Calculate),
        TabItem("Komunitas", Icons.Default.Group, Icons.Outlined.Group),
        TabItem("Saya", Icons.Default.Person, Icons.Outlined.Person)
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars
            ) {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == index) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title,
                                tint = if (selectedTab == index) EcoGreenPrimary else EcoTextLight
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 11.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == index) EcoGreenPrimary else EcoTextLight
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = EcoGreenLight
                        ),
                        modifier = Modifier.testTag("nav_tab_${tab.title.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> BerandaTab(
                    userName = userProfile?.fullName ?: "Yuli",
                    onNavigate = onNavigate
                )
                1 -> CalculatorScreen(viewModel = viewModel)
                2 -> CommunityScreen(viewModel = viewModel, onNavigate = onNavigate)
                3 -> ProfileScreen(viewModel = viewModel, onNavigate = onNavigate)
            }
        }
    }
}

data class TabItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaTab(
    userName: String,
    onNavigate: (String) -> Unit
) {
    val shortName = userName.split(" ").firstOrNull() ?: userName

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoBackground)
            .padding(horizontal = 20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Header Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* Menu */ },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(Icons.Default.Menu, "Menu", tint = EcoGreenPrimary)
                }
                
                IconButton(
                    onClick = { /* Bell notification */ },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(Icons.Outlined.Notifications, "Notifikasi", tint = EcoGreenPrimary)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Greeting Banner
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Halo, $shortName ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = EcoTextDark
                    )
                    Text(text = "👋", fontSize = 20.sp)
                }
                Text(
                    text = "Selamat Datang di",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = EcoGreenPrimary
                )
                Text(
                    text = "Eco Enzyme Connect",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = EcoGreenPrimary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4 Grid Feature Cards
            val features = listOf(
                FeatureItem("Apa itu\nEco Enzyme", Icons.Outlined.MenuBook, "detail_ee", EcoGreenLight, EcoGreenPrimary),
                FeatureItem("Cara\nMembuat", Icons.Outlined.Handyman, "how_to", EcoGreenLight, EcoGreenPrimary),
                FeatureItem("Manfaat", Icons.Outlined.Eco, "detail_ee", EcoGreenLight, EcoGreenPrimary),
                FeatureItem("Sensor\nCahaya", Icons.Outlined.LightMode, "light_sensor", EcoGreenLight, EcoGreenPrimary)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    FeatureCard(features[0], onNavigate)
                    FeatureCard(features[2], onNavigate)
                }
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    FeatureCard(features[1], onNavigate)
                    FeatureCard(features[3], onNavigate)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Community active widget
            Card(
                onClick = { onNavigate("map") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("komunitas_hari_ini_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(EcoGreenLight, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Group,
                                contentDescription = "Group logo",
                                tint = EcoGreenPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        
                        Column {
                            Text(
                                text = "Komunitas Hari Ini",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = EcoTextDark
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.padding(top = 2.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Group,
                                        contentDescription = null,
                                        tint = EcoTextLight,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("235 Anggota", fontSize = 12.sp, color = EcoTextLight)
                                }
                                
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(Color(0xFF4CAF50), CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("48 Online", fontSize = 12.sp, color = EcoTextLight)
                                }
                            }
                        }
                    }
                    
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Map Location",
                        tint = EcoGreenPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Artikel Terbaru Heading
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Artikel Terbaru",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = EcoTextDark
                )
                Text(
                    text = "Lihat Semua",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = EcoGreenPrimary,
                    modifier = Modifier.clickable { /* See all articles */ }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Artikel Card "Cara Membuat Eco Enzyme"
            Card(
                onClick = { onNavigate("how_to") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .testTag("article_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Placeholder for jar article image
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(EcoGreenLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(40.dp)) {
                            // Draw simple eco jar sketch
                            drawRoundRect(
                                color = EcoGreenPrimary,
                                topLeft = Offset(10f, 15f),
                                size = androidx.compose.ui.geometry.Size(this.size.width - 20f, this.size.height - 25f),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f),
                                style = Stroke(width = 4f)
                            )
                            drawCircle(
                                color = EcoGreenPrimary,
                                radius = 8f,
                                center = Offset(this.center.x, this.center.y + 5f)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Cara Membuat Eco Enzyme",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = EcoTextDark,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Panduan lengkap pengolahan sampah organik menjadi cairan multiguna berharga.",
                                fontSize = 13.sp,
                                color = EcoTextLight,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                lineHeight = 17.sp
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "10 Mei 2024",
                            fontSize = 11.sp,
                            color = EcoTextLight,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

data class FeatureItem(
    val title: String,
    val icon: ImageVector,
    val route: String,
    val containerColor: Color,
    val iconColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureCard(
    item: FeatureItem,
    onNavigate: (String) -> Unit
) {
    Card(
        onClick = { onNavigate(item.route) },
        modifier = Modifier
            .fillMaxWidth()
            .height(115.dp)
            .testTag("feature_card_${item.title.replace("\n", "_").lowercase()}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(item.containerColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = item.iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            
            Text(
                text = item.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = EcoTextDark,
                lineHeight = 18.sp
            )
        }
    }
}
