package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailEeScreen(
    initialTab: Int = 0, // 0 = Apa itu Eco Enzyme, 1 = Cara Membuat
    onBackClick: () -> Unit
) {
    var activeTab by remember { mutableStateOf(initialTab) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (activeTab == 0) "Apa itu Eco Enzyme" else "Cara Membuat Eco Enzyme",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = EcoTextDark
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("detail_back_button")) {
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
        ) {
            // Tab row selector
            TabRow(
                selectedTabIndex = activeTab,
                containerColor = Color.White,
                contentColor = EcoGreenPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                        color = EcoGreenPrimary
                    )
                }
            ) {
                Tab(
                    selected = activeTab == 0,
                    onClick = { activeTab = 0 },
                    text = { Text("Apa itu Eco Enzyme", fontSize = 14.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = activeTab == 1,
                    onClick = { activeTab = 1 },
                    text = { Text("Cara Membuat", fontSize = 14.sp, fontWeight = FontWeight.Bold) }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (activeTab == 0) {
                    // TAB 0: DESCRIPTION & BENEFITS
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("info_image_card"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Beautiful Mason Jar illustration drawn with Canvas
                            Box(
                                modifier = Modifier
                                    .size(150.dp)
                                    .background(EcoGreenLight, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Canvas(modifier = Modifier.size(100.dp)) {
                                    val topOffset = 15f
                                    val jarWidth = size.width - 30f
                                    val jarHeight = size.height - 35f
                                    
                                    // Liquid inside jar (Amber-Orange)
                                    val liquidPath = Path().apply {
                                        moveTo(25f, 50f)
                                        lineTo(size.width - 25f, 50f)
                                        lineTo(size.width - 25f, size.height - 15f)
                                        quadraticTo(size.width - 25f, size.height - 5f, size.width - 35f, size.height - 5f)
                                        lineTo(35f, size.height - 5f)
                                        quadraticTo(25f, size.height - 5f, 25f, size.height - 15f)
                                        close()
                                    }
                                    drawPath(liquidPath, color = EcoAmber.copy(alpha = 0.85f))
                                    
                                    // Organic fruits float circles inside jar
                                    drawCircle(color = EcoOrange, radius = 8f, center = Offset(45f, 65f))
                                    drawCircle(color = EcoGreenPrimary, radius = 7f, center = Offset(65f, 80f))
                                    drawCircle(color = EcoAmber, radius = 9f, center = Offset(52f, 75f))
                                    
                                    // Glass Jar outline
                                    drawRoundRect(
                                        color = EcoGreenPrimary,
                                        topLeft = Offset(15f, topOffset + 15f),
                                        size = Size(jarWidth, jarHeight - 15f),
                                        cornerRadius = CornerRadius(15f, 15f),
                                        style = Stroke(width = 5f)
                                    )
                                    
                                    // Jar neck & cap
                                    drawRect(
                                        color = EcoGreenPrimary,
                                        topLeft = Offset(center.x - 25f, topOffset),
                                        size = Size(50f, 15f),
                                        style = Stroke(width = 5f)
                                    )
                                    drawRoundRect(
                                        color = EcoGreenPrimary,
                                        topLeft = Offset(center.x - 30f, topOffset - 6f),
                                        size = Size(60f, 10f),
                                        cornerRadius = CornerRadius(4f, 4f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = "Eco Enzyme adalah cairan hasil fermentasi dari limbah organik (seperti kulit buah dan sayur), molase, dan air.",
                                fontSize = 15.sp,
                                color = EcoTextDark,
                                textAlign = TextAlign.Center,
                                lineHeight = 22.sp
                            )
                        }
                    }

                    // Section Manfaat Eco Enzyme
                    Text(
                        text = "Manfaat Eco Enzyme",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = EcoTextDark,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    val benefits = listOf(
                        "Untuk rumah tangga (pembersih lantai, deterjen, hand sanitizer)" to "Menggantikan cairan kimia sintetis pembersih rumah tangga secara alami.",
                        "Untuk pertanian (pupuk organik alami, pembasmi hama)" to "Merangsang pertumbuhan tanaman, meningkatkan kesuburan daun dan buah.",
                        "Untuk lingkungan (pembersih udara, pemurni air parit)" to "Menetralkan polutan udara dan membersihkan ekosistem perairan terdekat.",
                        "Meningkatkan kualitas tanah" to "Membantu mengikat unsur hara dan mengembalikan sirkulasi mikroba baik tanah.",
                        "Mengurangi limbah organik di tempat pembuangan" to "Mengolah sisa kulit buah dapur menjadi cairan serbaguna ramah lingkungan."
                    )

                    benefits.forEachIndexed { index, (title, desc) ->
                        BenefitCheckCard(title = title, desc = desc, index = index)
                    }
                } else {
                    // TAB 1: HOW TO MAKE ECO ENZYME
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = EcoGreenLight)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Waktu",
                                tint = EcoGreenPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Column {
                                Text(
                                    text = "Masa Fermentasi",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = EcoGreenDark
                                )
                                Text(
                                    text = "Standar proses memakan waktu 3 bulan (90 hari)",
                                    fontSize = 13.sp,
                                    color = EcoSecondary
                                )
                            }
                        }
                    }

                    Text(
                        text = "Langkah-Langkah Pembuatan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = EcoTextDark,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    val steps = listOf(
                        "Gunakan Wadah Plastik" to "Siapkan wadah berbahan plastik yang bermulut lebar (seperti baskom atau toples). Hindari botol kaca karena gas hasil fermentasi bisa memicu ledakan tekanan.",
                        "Formula Perbandingan 1:3:10" to "Gunakan perbandingan berat: 1 bagian Molase (Gula Merah), 3 bagian Kulit Buah/Sayur segar, dan 10 bagian Air Bersih.",
                        "Potong Kecil Kulit Buah" to "Rajang kulit buah (seperti jeruk, apel, nanas, pepaya) berukuran kecil agar sirkulasi proses penguraian di air gula berjalan optimal.",
                        "Larutkan Gula & Air" to "Masukkan air bersih ke wadah, tambahkan molase sesuai takaran kalkulator, lalu aduk sampai benar-benar larut.",
                        "Masukkan Kulit Buah" to "Masukkan potongan kulit buah ke dalam larutan gula, aduk merata secara perlahan hingga seluruh buah terendam air.",
                        "Simpan di Tempat Teduh" to "Tutup rapat wadah, beri label bertuliskan tanggal pembuatan, lalu simpan di tempat yang sejuk, teduh, bersirkulasi udara baik, dan jauh dari paparan matahari langsung.",
                        "Buang Gas Fermentasi" to "Selama 1-2 minggu pertama, buka sedikit tutup wadah secara berkala (misal tiap pagi) untuk membuang gas fermentasi yang terkumpul, lalu tutup rapat kembali.",
                        "Tunggu dan Panen" to "Biarkan wadah tertutup hingga genap 3 bulan. Saring cairan Eco Enzyme yang berwarna cokelat jernih beraroma asam segar. Sisa ampas buah bisa dijadikan pupuk kompos!"
                    )

                    steps.forEachIndexed { index, (stepTitle, stepDesc) ->
                        StepGuideCard(index = index + 1, title = stepTitle, desc = stepDesc)
                    }
                }
            }
        }
    }
}

@Composable
fun BenefitCheckCard(
    title: String,
    desc: String,
    index: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("benefit_card_$index"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Checkmark",
                tint = EcoGreenPrimary,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = EcoTextDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = desc,
                    fontSize = 13.sp,
                    color = EcoTextLight,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun StepGuideCard(
    index: Int,
    title: String,
    desc: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("step_card_$index"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Visual step number indicator circle
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(EcoGreenPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = index.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Column {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = EcoTextDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = desc,
                    fontSize = 13.sp,
                    color = EcoTextLight,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
