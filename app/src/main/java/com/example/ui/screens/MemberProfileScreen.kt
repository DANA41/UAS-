package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberProfileScreen(
    memberId: String,
    viewModel: AppViewModel,
    onBackClick: () -> Unit,
    onChatClick: (String) -> Unit
) {
    val members by viewModel.members.collectAsState()
    val member = remember(members, memberId) {
        members.find { it.id == memberId }
    }

    if (member == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Anggota tidak ditemukan")
        }
        return
    }

    val initial = member.name.firstOrNull()?.toString() ?: "M"
    val avatarBg = remember(member.id) {
        when (member.id) {
            "made" -> Color(0xFFC8E6C9)
            "ayu" -> Color(0xFFFFCDD2)
            "budi" -> Color(0xFFBBDEFB)
            "komang" -> Color(0xFFFFE082)
            else -> Color(0xFFD1C4E9)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profil Anggota", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = EcoTextDark) },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("member_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali", tint = EcoGreenPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Menu dots */ }) {
                        Icon(Icons.Default.MoreVert, "More", tint = EcoGreenPrimary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(EcoBackground)
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Large Avatar
                Box(modifier = Modifier.size(110.dp)) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(avatarBg, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initial,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = EcoGreenDark
                        )
                    }
                    
                    // Online Badge
                    if (member.isOnline) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color.White, CircleShape)
                                .align(Alignment.BottomEnd)
                                .padding(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFF4CAF50), CircleShape)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name & Location
                Text(
                    text = member.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = EcoTextDark
                )
                Text(
                    text = "${member.city}, Bali",
                    fontSize = 14.sp,
                    color = EcoTextLight,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Detail Rows card list
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("member_details_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        DetailRowItem(
                            icon = Icons.Default.Phone,
                            text = member.phoneNumber,
                            label = "Nomor Handphone"
                        )
                        DetailRowItem(
                            icon = Icons.Default.CalendarToday,
                            text = "Bergabung sejak ${member.joinedDate}",
                            label = "Keanggotaan"
                        )
                        DetailRowItem(
                            icon = Icons.Default.Eco,
                            text = "Sudah membuat Eco Enzyme selama ${member.ecoDurationYears} Tahun",
                            label = "Pengalaman"
                        )
                        DetailRowItem(
                            icon = Icons.Default.Layers,
                            text = "Total Batch: ${member.totalBatch} batch",
                            label = "Produktivitas"
                        )
                    }
                }
            }

            // Big CHAT Button
            Button(
                onClick = { onChatClick(member.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("chat_action_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EcoGreenPrimary,
                    contentColor = Color.White
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Chat, "Chat", tint = Color.White)
                    Text("CHAT", fontSize = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                }
            }
        }
    }
}

@Composable
fun DetailRowItem(
    icon: ImageVector,
    text: String,
    label: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(EcoGreenLight, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = EcoGreenPrimary,
                modifier = Modifier.size(18.dp)
            )
        }

        Column {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = EcoTextDark
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = EcoTextLight
            )
        }
    }
}
