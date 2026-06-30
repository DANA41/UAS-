package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EcoGreenPrimary
import com.example.ui.theme.EcoGreenLight
import com.example.ui.theme.EcoTextLight
import com.example.ui.theme.EcoTextDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginClick: (name: String, phone: String, city: String) -> Unit
) {
    var name by remember { mutableStateOf("Yuli Ekayani") }
    var phone by remember { mutableStateOf("0812 3456 7890") }
    var city by remember { mutableStateOf("Denpasar") }

    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Heading
            Text(
                text = "Selamat Datang!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = EcoGreenPrimary,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Masuk untuk mulai terhubung\ndengan komunitas.",
                fontSize = 15.sp,
                color = EcoTextLight,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Input Nama Lengkap
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama Lengkap") },
                placeholder = { Text("Yuli Ekayani") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Name",
                        tint = EcoGreenPrimary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EcoGreenPrimary,
                    focusedLabelColor = EcoGreenPrimary,
                    cursorColor = EcoGreenPrimary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input Nomor HP
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Nomor HP") },
                placeholder = { Text("0812 3456 7890") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone",
                        tint = EcoGreenPrimary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EcoGreenPrimary,
                    focusedLabelColor = EcoGreenPrimary,
                    cursorColor = EcoGreenPrimary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input Kota
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Kota") },
                placeholder = { Text("Denpasar") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "City",
                        tint = EcoGreenPrimary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EcoGreenPrimary,
                    focusedLabelColor = EcoGreenPrimary,
                    cursorColor = EcoGreenPrimary
                )
            )

            if (showError) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Semua bidang harus diisi",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Button Masuk
            Button(
                onClick = {
                    if (name.isNotBlank() && phone.isNotBlank() && city.isNotBlank()) {
                        onLoginClick(name, phone, city)
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EcoGreenPrimary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "MASUK",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        // Terms and conditions footer
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            val annotatedText = buildAnnotatedString {
                append("Dengan masuk, Anda setuju dengan\n")
                withStyle(style = SpanStyle(color = EcoGreenPrimary, fontWeight = FontWeight.SemiBold)) {
                    append("Kebijakan Privasi")
                }
                append(" dan ")
                withStyle(style = SpanStyle(color = EcoGreenPrimary, fontWeight = FontWeight.SemiBold)) {
                    append("Ketentuan")
                }
                append(".")
            }

            Text(
                text = annotatedText,
                fontSize = 12.sp,
                color = EcoTextLight,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}
