package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    isLoggedIn: Boolean,
    onNavigateNext: (String) -> Unit
) {
    var progress by remember { mutableStateOf(0f) }
    
    // Simulate progress animation
    LaunchedEffect(Unit) {
        animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
        ) { value, _ ->
            progress = value
        }
        delay(300)
        if (isLoggedIn) {
            onNavigateNext("main")
        } else {
            onNavigateNext("login")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Decorative leaves top-left
        Canvas(modifier = Modifier.size(150.dp).align(Alignment.TopStart)) {
            // Draw stylized leafy pattern in TopStart
            val p = Path().apply {
                moveTo(0f, 0f)
                quadraticTo(60f, 20f, 100f, 0f)
                quadraticTo(40f, 80f, 0f, 100f)
                close()
            }
            drawPath(p, color = EcoGreenLight)
        }

        // Decorative leaves bottom-right
        Canvas(modifier = Modifier.size(180.dp).align(Alignment.BottomEnd)) {
            val p = Path().apply {
                moveTo(size.width, size.height)
                quadraticTo(size.width - 80f, size.height - 30f, size.width - 120f, size.height)
                quadraticTo(size.width - 50f, size.height - 100f, size.width, size.height - 140f)
                close()
            }
            drawPath(p, color = EcoGreenLight)
        }

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Eco Logo Badge drawn in Canvas
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Draw a light green background circle
                    drawCircle(
                        color = EcoGreenLight,
                        radius = size.minDimension / 2f
                    )
                    
                    // Draw outer border circle
                    drawCircle(
                        color = EcoGreenPrimary,
                        radius = (size.minDimension / 2f) - 10f,
                        style = Stroke(width = 6f)
                    )
                    
                    // Draw leaves in the center
                    val center = Offset(size.width / 2f, size.height / 2f)
                    
                    // Leaf 1 (Left)
                    val leafLeft = Path().apply {
                        moveTo(center.x - 10f, center.y + 40f)
                        cubicTo(
                            center.x - 60f, center.y + 10f,
                            center.x - 50f, center.y - 40f,
                            center.x - 10f, center.y - 40f
                        )
                        cubicTo(
                            center.x + 10f, center.y - 10f,
                            center.x + 10f, center.y + 20f,
                            center.x - 10f, center.y + 40f
                        )
                    }
                    drawPath(leafLeft, color = EcoGreenPrimary)

                    // Leaf 2 (Right smaller)
                    val leafRight = Path().apply {
                        moveTo(center.x + 10f, center.y + 35f)
                        cubicTo(
                            center.x + 50f, center.y + 15f,
                            center.x + 45f, center.y - 25f,
                            center.x + 15f, center.y - 20f
                        )
                        cubicTo(
                            center.x + 5f, center.y - 5f,
                            center.x + 5f, center.y + 20f,
                            center.x + 10f, center.y + 35f
                        )
                    }
                    drawPath(leafRight, color = EcoGreenMedium)

                    // Draw stem joining them
                    val stem = Path().apply {
                        moveTo(center.x - 5f, center.y + 45f)
                        quadraticTo(center.x, center.y + 20f, center.x - 5f, center.y - 10f)
                    }
                    drawPath(stem, color = EcoGreenPrimary, style = Stroke(width = 6f))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App Name
            Text(
                text = "Eco Enzyme",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = EcoGreenPrimary
            )
            Text(
                text = "Connect",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = EcoGreenPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle
            Text(
                text = "Belajar • Berbagi",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = EcoTextLight
            )
            Text(
                text = "Bertumbuh Bersama",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = EcoTextLight
            )
        }

        // Loading Area at Bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .width(180.dp)
                    .height(6.dp),
                color = EcoGreenPrimary,
                trackColor = EcoGreenLight,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Memuat...",
                fontSize = 14.sp,
                color = EcoTextLight,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
