package com.example.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.ui.geometry.Offset
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LightSensorScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var luxValue by remember { mutableStateOf(145f) }
    var isSensorAvailable by remember { mutableStateOf(false) }

    // Register Android hardware light sensor
    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null && event.values.isNotEmpty()) {
                    luxValue = event.values[0]
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        if (lightSensor != null) {
            isSensorAvailable = true
            sensorManager.registerListener(listener, lightSensor, SensorManager.SENSOR_DELAY_UI)
        }

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    // Classify Lux thresholds
    // Eco Enzyme prefers dark, shaded places (< 200 Lux is Ideal)
    val statusText = when {
        luxValue < 200f -> "Ideal"
        luxValue < 500f -> "Kurang Teduh"
        else -> "Terlalu Terang!"
    }

    val statusColor = when {
        luxValue < 200f -> Color(0xFF4CAF50)
        luxValue < 500f -> EcoAmber
        else -> EcoRed
    }

    val statusDesc = when {
        luxValue < 200f -> "Kondisi pencahayaan sangat ideal untuk proses fermentasi Eco Enzyme. Hindari menyalakan lampu terang di sekitar botol."
        luxValue < 500f -> "Tempat ini agak terang. Coba pindahkan wadah Eco Enzyme Anda ke dalam lemari atau pasang penutup kain gelap di atasnya."
        else -> "Pencahayaan terlalu kuat! Sinar matahari langsung atau lampu terang bisa merusak mikroba fermentasi. Segera pindahkan wadah ke ruangan yang teduh."
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Sensor Cahaya", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = EcoTextDark) },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("sensor_back_button")) {
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Circle display with Sun icon
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(EcoGreenLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(140.dp)) {
                    // Draw outer glowing halo depending on luxValue
                    val pulseRadius = (size.minDimension / 2f) - 10f
                    drawCircle(
                        color = statusColor.copy(alpha = 0.15f),
                        radius = pulseRadius + (luxValue.coerceAtMost(3000f) / 3000f) * 20f
                    )
                    
                    // Draw main sun circle
                    drawCircle(
                        color = EcoAmber,
                        radius = 36f,
                        center = center
                    )

                    // Draw sun rays
                    val rayCount = 8
                    val innerRay = 44f
                    val outerRay = 58f
                    for (i in 0 until rayCount) {
                        val angle = (i * 2 * Math.PI / rayCount).toFloat()
                        val startX = center.x + Math.cos(angle.toDouble()).toFloat() * innerRay
                        val startY = center.y + Math.sin(angle.toDouble()).toFloat() * innerRay
                        val endX = center.x + Math.cos(angle.toDouble()).toFloat() * outerRay
                        val endY = center.y + Math.sin(angle.toDouble()).toFloat() * outerRay
                        drawLine(
                            color = EcoAmber,
                            start = Offset(startX, startY),
                            end = Offset(endX, endY),
                            strokeWidth = 6f
                        )
                    }
                }
            }

            // Giant Lux reading
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${luxValue.toInt()} Lux",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = EcoTextDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Status:",
                        fontSize = 14.sp,
                        color = EcoTextLight
                    )
                    Text(
                        text = statusText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor,
                        modifier = Modifier.testTag("lux_status_label")
                    )
                }
            }

            // Recommendation Card
            Card(
                modifier = Modifier.fillMaxWidth().testTag("lux_recommendations_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Rekomendasi",
                            tint = EcoGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Rekomendasi Penyimpanan",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = EcoTextDark
                        )
                    }
                    
                    Text(
                        text = statusDesc,
                        fontSize = 13.sp,
                        color = EcoTextLight,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Divider(color = EcoGreenLight)
                    
                    Text(
                        text = "💡 Mengapa pencahayaan penting?\n" +
                               "Sinar UV matahari dan paparan cahaya berlebih dapat mematikan jamur baik dan mikroba fermentasi ragi yang mengolah gula menjadi Eco Enzyme.",
                        fontSize = 11.sp,
                        color = EcoSecondary,
                        lineHeight = 16.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }

            // If hardware sensor is NOT available, show a slider to simulate reading
            if (!isSensorAvailable) {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("sensor_simulation_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFECEFF1))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Text(
                            text = "Aplikasi mendeteksi sensor cahaya tidak tersedia di perangkat Anda. Gunakan slider di bawah untuk mensimulasikan intensitas cahaya:",
                            fontSize = 12.sp,
                            color = EcoTextLight,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Slider(
                            value = luxValue,
                            onValueChange = { luxValue = it },
                            valueRange = 0f..1000f,
                            colors = SliderDefaults.colors(
                                thumbColor = EcoGreenPrimary,
                                activeTrackColor = EcoGreenPrimary
                            ),
                            modifier = Modifier.testTag("sensor_slider")
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Teduh (0 Lux)", fontSize = 11.sp, color = EcoTextLight)
                            Text("Terang (1000 Lux)", fontSize = 11.sp, color = EcoTextLight)
                        }
                    }
                }
            } else {
                Text(
                    text = "Aplikasi sedang membaca sensor cahaya fisik dari perangkat Anda secara real-time.",
                    fontSize = 11.sp,
                    color = EcoGreenPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
