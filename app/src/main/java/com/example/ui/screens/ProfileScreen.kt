package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.foundation.BorderStroke
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
import com.example.data.UserProfile
import com.example.ui.AppViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: AppViewModel,
    onNavigate: (String) -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf("") }
    var editPhone by remember { mutableStateOf("") }
    var editCity by remember { mutableStateOf("") }

    var showDeveloperDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    val nameText = userProfile?.fullName ?: "Yuli Ekayani"
    val phoneText = userProfile?.phoneNumber ?: "0812 3456 7890"
    val cityText = userProfile?.city ?: "Denpasar"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoBackground)
    ) {
        // App Bar
        CenterAlignedTopAppBar(
            title = { Text("Profil Saya", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = EcoTextDark) },
            actions = {
                IconButton(onClick = {
                    editName = nameText
                    editPhone = phoneText
                    editCity = cityText
                    showEditDialog = true
                }) {
                    Icon(Icons.Default.Edit, "Edit Profil", tint = EcoGreenPrimary)
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Large User Avatar with camera overlay
            Box(
                modifier = Modifier.size(110.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color(0xFFE8F5E9), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = nameText.firstOrNull()?.toString() ?: "Y",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = EcoGreenPrimary
                    )
                }

                // Camera icon overlay
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(EcoGreenPrimary, CircleShape)
                        .align(Alignment.BottomEnd)
                        .clickable {
                            editName = nameText
                            editPhone = phoneText
                            editCity = cityText
                            showEditDialog = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Ganti Foto",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // Name & basic metadata
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = nameText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = EcoTextDark
                )
                Text(
                    text = phoneText,
                    fontSize = 13.sp,
                    color = EcoTextLight,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = "$cityText, Bali",
                    fontSize = 13.sp,
                    color = EcoTextLight
                )
            }

            // Options List Card
            Card(
                modifier = Modifier.fillMaxWidth().testTag("profile_options_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    ProfileMenuRow(
                        icon = Icons.Outlined.Person,
                        title = "Edit Profil",
                        onClick = {
                            editName = nameText
                            editPhone = phoneText
                            editCity = cityText
                            showEditDialog = true
                        }
                    )
                    Divider(color = EcoBackground, thickness = 1.dp)

                    ProfileMenuRow(
                        icon = Icons.Outlined.Info,
                        title = "Tentang Aplikasi",
                        onClick = { onNavigate("about") }
                    )
                    Divider(color = EcoBackground, thickness = 1.dp)

                    ProfileMenuRow(
                        icon = Icons.Outlined.Groups,
                        title = "Tim Pengembang",
                        onClick = { showDeveloperDialog = true }
                    )
                    Divider(color = EcoBackground, thickness = 1.dp)

                    ProfileMenuRow(
                        icon = Icons.Outlined.Settings,
                        title = "Pengaturan",
                        onClick = { showSettingsDialog = true }
                    )
                }
            }

            // Logout Button
            Button(
                onClick = {
                    viewModel.logout()
                    onNavigate("login")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("logout_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = EcoRed
                ),
                border = BorderStroke(1.dp, EcoRed.copy(alpha = 0.5f))
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Logout, "Keluar", tint = EcoRed)
                    Text("Keluar (Logout)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }

    // Edit Profile Modal Dialog
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Profil", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Nama Lengkap") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("edit_profile_name")
                    )

                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { editPhone = it },
                        label = { Text("Nomor HP") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("edit_profile_phone")
                    )

                    OutlinedTextField(
                        value = editCity,
                        onValueChange = { editCity = it },
                        label = { Text("Kota") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("edit_profile_city")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editName.isNotBlank() && editPhone.isNotBlank() && editCity.isNotBlank()) {
                            viewModel.login(editName, editPhone, editCity)
                            showEditDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EcoGreenPrimary)
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Batal", color = EcoGreenPrimary)
                }
            }
        )
    }

    // Developer Team Dialog (as referenced in Screen 12 of mockup)
    if (showDeveloperDialog) {
        AlertDialog(
            onDismissRequest = { showDeveloperDialog = false },
            title = { Text("Tim Pengembang", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = EcoGreenPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Program Studi Teknologi Informasi", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = EcoTextDark)
                    Text("ITB STIKOM Bali, 2024", fontSize = 13.sp, color = EcoTextLight)
                    
                    Divider(color = EcoGreenLight, modifier = Modifier.padding(vertical = 4.dp))
                    
                    DeveloperRow(name = "Yuli Ekayani", nim = "230040055")
                    DeveloperRow(name = "Nama Anggota 2", nim = "230040056")
                    DeveloperRow(name = "Nama Anggota 3", nim = "230040057")
                    DeveloperRow(name = "Nama Anggota 4", nim = "230040058")
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDeveloperDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = EcoGreenPrimary)
                ) {
                    Text("Tutup")
                }
            }
        )
    }

    // Simple Settings dialog
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("Pengaturan", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = EcoGreenPrimary) },
            text = {
                var checkedNotif by remember { mutableStateOf(true) }
                var checkedDark by remember { mutableStateOf(false) }

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Notifikasi Aplikasi", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text("Dapatkan tips mingguan fermentasi", fontSize = 11.sp, color = EcoTextLight)
                        }
                        Switch(
                            checked = checkedNotif,
                            onCheckedChange = { checkedNotif = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = EcoGreenPrimary)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Mode Gelap", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text("Gunakan tema gelap organik", fontSize = 11.sp, color = EcoTextLight)
                        }
                        Switch(
                            checked = checkedDark,
                            onCheckedChange = { checkedDark = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = EcoGreenPrimary)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showSettingsDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = EcoGreenPrimary)
                ) {
                    Text("Tutup")
                }
            }
        )
    }
}

@Composable
fun ProfileMenuRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(EcoGreenLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = EcoGreenPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = EcoTextDark
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Buka",
            tint = EcoTextLight,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun DeveloperRow(name: String, nim: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(name, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = EcoTextDark)
        Text(nim, fontSize = 13.sp, color = EcoGreenPrimary, fontWeight = FontWeight.Bold)
    }
}
