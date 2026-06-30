package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tentang Aplikasi", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = EcoTextDark) },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("about_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali", tint = EcoGreenPrimary)
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
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Eco Logo centered badge
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(76.dp)) {
                    drawCircle(color = EcoGreenLight, radius = size.minDimension / 2f)
                    drawCircle(color = EcoGreenPrimary, radius = (size.minDimension / 2f) - 6f, style = Stroke(width = 4f))

                    val center = Offset(size.width / 2f, size.height / 2f)
                    
                    // Center leaf outline drawing
                    val leaf = Path().apply {
                        moveTo(center.x - 5f, center.y + 20f)
                        cubicTo(
                            center.x - 30f, center.y + 5f,
                            center.x - 25f, center.y - 20f,
                            center.x - 5f, center.y - 20f
                        )
                        cubicTo(
                            center.x + 5f, center.y - 5f,
                            center.x + 5f, center.y + 10f,
                            center.x - 5f, center.y + 20f
                        )
                    }
                    drawPath(leaf, color = EcoGreenPrimary)

                    val stem = Path().apply {
                        moveTo(center.x - 3f, center.y + 22f)
                        quadraticTo(center.x, center.y + 10f, center.x - 3f, center.y - 5f)
                    }
                    drawPath(stem, color = EcoGreenPrimary, style = Stroke(width = 3f))
                }
            }

            // App details
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Eco Enzyme Connect",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = EcoGreenPrimary
                )
                Text(
                    text = "Versi 1.0.0",
                    fontSize = 13.sp,
                    color = EcoTextLight,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Tim Pengembang Card List
            Card(
                modifier = Modifier.fillMaxWidth().testTag("developers_list_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Tim Pengembang",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = EcoTextDark,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Divider(color = EcoGreenLight)

                    DeveloperRow(name = "Yuli Ekayani", nim = "230040055")
                    DeveloperRow(name = "Nama Anggota 2", nim = "230040056")
                    DeveloperRow(name = "Nama Anggota 3", nim = "230040057")
                    DeveloperRow(name = "Nama Anggota 4", nim = "230040058")
                }
            }

            // Study details footer box
            Card(
                modifier = Modifier.fillMaxWidth().testTag("itb_stikom_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = EcoGreenLight)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Program Studi Teknologi Informasi",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = EcoGreenDark,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "ITB STIKOM Bali",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = EcoGreenPrimary,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "2024",
                        fontSize = 12.sp,
                        color = EcoSecondary,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
