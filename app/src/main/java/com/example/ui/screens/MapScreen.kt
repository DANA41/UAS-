package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Member
import com.example.ui.AppViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: AppViewModel,
    onBackClick: () -> Unit,
    onMemberProfileClick: (String) -> Unit
) {
    val members by viewModel.members.collectAsState()
    var selectedMember by remember { mutableStateOf<Member?>(null) }

    // Define pixel coordinates on our 1000x1000 canvas for each member
    val pinCoordinates = remember {
        mapOf(
            "made" to Offset(400f, 320f),
            "ayu" to Offset(620f, 480f),
            "budi" to Offset(280f, 620f),
            "komang" to Offset(750f, 260f),
            "lina" to Offset(480f, 750f)
        )
    }

    // Default to 'made' on launch to showcase UI instantly
    LaunchedEffect(members) {
        if (members.isNotEmpty() && selectedMember == null) {
            selectedMember = members.find { it.id == "made" }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lokasi Anggota", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = EcoTextDark) },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("map_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali", tint = EcoGreenPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Filter */ }) {
                        Icon(Icons.Default.FilterList, "Filter", tint = EcoGreenPrimary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(EcoBackground)
                .padding(innerPadding)
        ) {
            // Interactive Map Canvas
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("map_canvas")
                    .pointerInput(members) {
                        detectTapGestures { pressOffset ->
                            // Map coordinates logic
                            // Find closest pin within click tolerance (e.g. 50 pixels scaled)
                            val width = size.width.toFloat()
                            val height = size.height.toFloat()
                            
                            var closestMember: Member? = null
                            var minDistance = 1000000f

                            members.forEach { m ->
                                val normOffset = pinCoordinates[m.id] ?: Offset(500f, 500f)
                                // Convert 1000x1000 coordinates to actual pixel screen dimensions
                                val actualOffset = Offset(
                                    x = (normOffset.x / 1000f) * width,
                                    y = (normOffset.y / 1000f) * height
                                )

                                val dist = (pressOffset - actualOffset).getDistance()
                                if (dist < 80f && dist < minDistance) {
                                    minDistance = dist
                                    closestMember = m
                                }
                            }

                            if (closestMember != null) {
                                selectedMember = closestMember
                            }
                        }
                    }
            ) {
                val width = size.width
                val height = size.height

                // Draw eco land background (shades of soft greens and water blue)
                // 1. Water body (Bali ocean at bottom)
                drawRect(
                    color = Color(0xFFE3F2FD),
                    topLeft = Offset(0f, height * 0.75f),
                    size = Size(width, height * 0.25f)
                )

                // 2. Coastal sands outline
                drawRoundRect(
                    color = Color(0xFFF1F8E9),
                    topLeft = Offset(-50f, height * 0.65f),
                    size = Size(width + 100f, 120f),
                    cornerRadius = CornerRadius(20f, 20f)
                )

                // 3. Central land river path (curved blue line)
                val river = Path().apply {
                    moveTo(width * 0.3f, 0f)
                    cubicTo(width * 0.4f, height * 0.2f, width * 0.25f, height * 0.5f, width * 0.5f, height * 0.75f)
                }
                drawPath(
                    path = river,
                    color = Color(0xFFBBDEFB),
                    style = Stroke(width = 16f)
                )

                // 4. Stylized green community fields
                drawRect(
                    color = Color(0xFFE8F5E9),
                    topLeft = Offset(50f, 100f),
                    size = Size(width * 0.35f, height * 0.2f)
                )
                drawRect(
                    color = Color(0xFFC8E6C9),
                    topLeft = Offset(width * 0.55f, height * 0.3f),
                    size = Size(width * 0.3f, height * 0.25f)
                )

                // 5. Drawing major transit lines (roads/connections)
                val road = Path().apply {
                    moveTo(0f, height * 0.4f)
                    quadraticTo(width * 0.5f, height * 0.35f, width, height * 0.5f)
                    moveTo(width * 0.5f, 0f)
                    quadraticTo(width * 0.45f, height * 0.5f, width * 0.5f, height)
                }
                drawPath(
                    path = road,
                    color = Color.White,
                    style = Stroke(width = 8f)
                )

                // 6. Draw Member Pins
                members.forEach { m ->
                    val normOffset = pinCoordinates[m.id] ?: Offset(500f, 500f)
                    val actualOffset = Offset(
                        x = (normOffset.x / 1000f) * width,
                        y = (normOffset.y / 1000f) * height
                    )

                    val isSelected = selectedMember?.id == m.id
                    val outerColor = if (isSelected) EcoRed else EcoGreenPrimary
                    val innerColor = if (isSelected) Color.White else EcoGreenLight

                    // Draw glowing shadow circle
                    drawCircle(
                        color = outerColor.copy(alpha = 0.25f),
                        radius = if (isSelected) 35f else 25f,
                        center = actualOffset
                    )

                    // Draw pin pointer balloon
                    val balloon = Path().apply {
                        val rx = actualOffset.x
                        val ry = actualOffset.y - 12f
                        moveTo(rx, actualOffset.y)
                        cubicTo(rx - 15f, ry - 15f, rx - 18f, ry - 35f, rx, ry - 35f)
                        cubicTo(rx + 18f, ry - 35f, rx + 15f, ry - 15f, rx, actualOffset.y)
                        close()
                    }
                    drawPath(balloon, color = outerColor)

                    // Draw inner circle dot
                    drawCircle(
                        color = innerColor,
                        radius = if (isSelected) 8f else 6f,
                        center = Offset(actualOffset.x, actualOffset.y - 18f)
                    )
                }
            }

            // Floating Detail Card of selected member
            AnimatedVisibility(
                visible = selectedMember != null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp)
                    .testTag("map_floating_card")
            ) {
                selectedMember?.let { m ->
                    val initial = m.name.firstOrNull()?.toString() ?: "M"
                    val avatarBg = when (m.id) {
                        "made" -> Color(0xFFC8E6C9)
                        "ayu" -> Color(0xFFFFCDD2)
                        "budi" -> Color(0xFFBBDEFB)
                        "komang" -> Color(0xFFFFE082)
                        else -> Color(0xFFD1C4E9)
                    }

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    // Avatar representation
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .background(avatarBg, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = initial,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = EcoGreenDark
                                        )
                                    }

                                    Column {
                                        Text(
                                            text = m.name,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = EcoTextDark
                                        )
                                        Text(
                                            text = "${m.city}, Bali • ${m.distance} dari Anda",
                                            fontSize = 12.sp,
                                            color = EcoTextLight
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(Color(0xFF4CAF50), CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Online", fontSize = 11.sp, color = Color(0xFF4CAF50))
                                }
                            }

                            // LIHAT PROFIL Button
                            Button(
                                onClick = { onMemberProfileClick(m.id) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .testTag("map_view_profile_btn"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = EcoGreenPrimary)
                            ) {
                                Text("LIHAT PROFIL", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
