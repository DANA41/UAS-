package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChatMessage
import com.example.ui.AppViewModel
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    memberId: String,
    viewModel: AppViewModel,
    onBackClick: () -> Unit
) {
    val members by viewModel.members.collectAsState()
    val member = remember(members, memberId) {
        members.find { it.id == memberId }
    }

    val chatMessages by viewModel.chatMessages.collectAsState()
    
    // Filter messages for this specific conversation (User <-> This Member)
    val conversationMessages = remember(chatMessages, member) {
        if (member == null) emptyList()
        else {
            chatMessages.filter {
                (it.senderName == "User" && it.receiverName == member.name) ||
                (it.senderName == member.name && it.receiverName == "User")
            }
        }
    }

    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Scroll to the bottom when new messages arrive
    LaunchedEffect(conversationMessages.size) {
        if (conversationMessages.isNotEmpty()) {
            listState.animateScrollToItem(conversationMessages.size - 1)
        }
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
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Small avatar in title bar
                        Box(modifier = Modifier.size(36.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(avatarBg, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = initial,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EcoGreenDark
                                )
                            }
                            // Online indicator small dot
                            if (member.isOnline) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(Color.White, CircleShape)
                                        .align(Alignment.BottomEnd)
                                        .padding(1.5.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color(0xFF4CAF50), CircleShape)
                                    )
                                }
                            }
                        }

                        Column {
                            Text(
                                text = member.name,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = EcoTextDark,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Online",
                                fontSize = 11.sp,
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("chat_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali", tint = EcoGreenPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(EcoBackground)
                .padding(innerPadding)
        ) {
            // Lazy chat messages area
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
            ) {
                // Today header
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Hari ini",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = EcoTextLight,
                            modifier = Modifier
                                .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                items(conversationMessages) { message ->
                    ChatBubble(message = message)
                }
            }

            // Bottom text typing bar
            Surface(
                tonalElevation = 8.dp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth().navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = { Text("Ketik pesan...", fontSize = 14.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field"),
                        singleLine = false,
                        maxLines = 4,
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EcoGreenPrimary,
                            focusedContainerColor = EcoBackground,
                            unfocusedContainerColor = EcoBackground,
                            cursorColor = EcoGreenPrimary
                        )
                    )

                    // Send paper-plane button
                    IconButton(
                        onClick = {
                            if (messageText.trim().isNotEmpty()) {
                                viewModel.sendMessage(member.name, messageText)
                                messageText = ""
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(EcoGreenPrimary, CircleShape)
                            .testTag("chat_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Kirim",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val df = remember(message.timestamp) {
        SimpleDateFormat("HH:mm", Locale.getDefault())
    }
    val timeStr = df.format(Date(message.timestamp))

    val bubbleShape = if (message.isFromUser) {
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 16.dp,
            bottomEnd = 0.dp
        )
    } else {
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 0.dp,
            bottomEnd = 16.dp
        )
    }

    val bubbleBg = if (message.isFromUser) {
        Color(0xFFE2F0D9) // Light green for user messages
    } else {
        Color.White // Crisp white for friend messages
    }

    val align = if (message.isFromUser) Alignment.End else Alignment.Start

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = align
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(bubbleBg, bubbleShape)
                .border(
                    width = 0.5.dp,
                    color = if (message.isFromUser) EcoGreenPrimary.copy(alpha = 0.15f) else Color(0xFFE0E0E0),
                    shape = bubbleShape
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Column {
                Text(
                    text = message.message,
                    fontSize = 14.sp,
                    color = EcoTextDark,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = timeStr,
                    fontSize = 10.sp,
                    color = EcoTextLight,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
