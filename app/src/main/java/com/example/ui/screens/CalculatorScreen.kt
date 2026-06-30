package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EcoBatch
import com.example.ui.AppViewModel
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(viewModel: AppViewModel) {
    val molase by viewModel.calcMolase.collectAsState()
    val fruitSkin by viewModel.calcFruitSkin.collectAsState()
    val water by viewModel.calcWater.collectAsState()
    val batches by viewModel.batches.collectAsState()

    val focusManager = LocalFocusManager.current

    // calculation type: 0 = Fruit skin, 1 = Water, 2 = Molase
    var selectedType by remember { mutableStateOf(0) }
    var inputAmount by remember { mutableStateOf("3") }
    
    var calculated by remember { mutableStateOf(true) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var batchNameInput by remember { mutableStateOf("") }

    // Initial calculation if amounts are not populated yet
    LaunchedEffect(Unit) {
        viewModel.calculateRecipe(0, 3.0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoBackground)
    ) {
        // App bar
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "Kalkulator Eco Enzyme",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = EcoTextDark
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.White
            )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Main Calculator panel
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("calculator_panel_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Pilih Perhitungan",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = EcoTextDark
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Radio Button options
                        val options = listOf(
                            "Berdasarkan Kulit Buah" to 0,
                            "Berdasarkan Air" to 1,
                            "Berdasarkan Molase" to 2
                        )

                        options.forEach { (label, type) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedType = type }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedType == type,
                                    onClick = { selectedType = type },
                                    colors = RadioButtonDefaults.colors(selectedColor = EcoGreenPrimary)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = label, fontSize = 14.sp, color = EcoTextDark)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Amount Input field
                        val inputLabel = when (selectedType) {
                            0 -> "Masukkan Jumlah Kulit Buah"
                            1 -> "Masukkan Jumlah Air"
                            else -> "Masukkan Jumlah Molase"
                        }
                        val suffix = if (selectedType == 1) "Liter" else "kg"

                        OutlinedTextField(
                            value = inputAmount,
                            onValueChange = { inputAmount = it },
                            label = { Text(inputLabel) },
                            suffix = { Text(suffix, color = EcoTextLight) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().testTag("calc_amount_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EcoGreenPrimary,
                                focusedLabelColor = EcoGreenPrimary,
                                cursorColor = EcoGreenPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // HITUNG Button
                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                val amount = inputAmount.toDoubleOrNull() ?: 0.0
                                viewModel.calculateRecipe(selectedType, amount)
                                calculated = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("calculate_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EcoGreenPrimary)
                        ) {
                            Text("HITUNG", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }

            // Results area
            if (calculated) {
                item {
                    Column {
                        Text(
                            text = "Hasil Perhitungan",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = EcoTextLight,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Card(
                            modifier = Modifier.fillMaxWidth().testTag("results_panel_card"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // 3 Columns for Ingredients
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // 1. Molase
                                    IngredientDisplayCol(
                                        name = "Molase",
                                        amount = String.format(Locale.US, "%.1f", molase),
                                        unit = "Kg",
                                        iconColor = EcoAmber,
                                        isLiquid = false
                                    )

                                    // Divider
                                    Box(
                                        modifier = Modifier
                                            .width(1.dp)
                                            .height(50.dp)
                                            .background(EcoGreenLight)
                                    )

                                    // 2. Kulit Buah
                                    IngredientDisplayCol(
                                        name = "Kulit Buah",
                                        amount = String.format(Locale.US, "%.1f", fruitSkin),
                                        unit = "Kg",
                                        iconColor = EcoOrange,
                                        isLiquid = false,
                                        isFruit = true
                                    )

                                    // Divider
                                    Box(
                                        modifier = Modifier
                                            .width(1.dp)
                                            .height(50.dp)
                                            .background(EcoGreenLight)
                                    )

                                    // 3. Air
                                    IngredientDisplayCol(
                                        name = "Air",
                                        amount = String.format(Locale.US, "%.1f", water),
                                        unit = "Liter",
                                        iconColor = EcoBlue,
                                        isLiquid = true
                                    )
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                // SIMPAN HASIL Button
                                Button(
                                    onClick = {
                                        batchNameInput = "Batch #${batches.size + 1} (${String.format(Locale.US, "%.1f", fruitSkin)} Kg)"
                                        showSaveDialog = true
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("save_result_button"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = EcoGreenLight, contentColor = EcoGreenPrimary)
                                ) {
                                    Text("SIMPAN HASIL", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
            }

            // History Header
            if (batches.isNotEmpty()) {
                item {
                    Text(
                        text = "Riwayat Pembuatan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = EcoTextDark,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                items(batches) { batch ->
                    HistoryItemCard(batch = batch, onDelete = { viewModel.deleteBatchHistory(batch) })
                }
            }
        }
    }

    // Save Batch Dialog
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Simpan Hasil Perhitungan", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
            text = {
                Column {
                    Text("Berikan nama untuk batch Eco Enzyme ini:", fontSize = 14.sp, color = EcoTextLight)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = batchNameInput,
                        onValueChange = { batchNameInput = it },
                        placeholder = { Text("Contoh: Batch Kulit Jeruk #1") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("save_batch_name_field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EcoGreenPrimary,
                            focusedLabelColor = EcoGreenPrimary,
                            cursorColor = EcoGreenPrimary
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (batchNameInput.isNotBlank()) {
                            viewModel.saveCalculation(batchNameInput)
                            showSaveDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EcoGreenPrimary)
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Batal", color = EcoGreenPrimary)
                }
            }
        )
    }
}

@Composable
fun IngredientDisplayCol(
    name: String,
    amount: String,
    unit: String,
    iconColor: Color,
    isLiquid: Boolean,
    isFruit: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = EcoTextLight
        )
        
        Spacer(modifier = Modifier.height(12.dp))

        // Ingredient visual vector drawing
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(EcoGreenLight, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(28.dp)) {
                if (isLiquid) {
                    // Water drop
                    val drop = Path().apply {
                        moveTo(center.x, 2f)
                        cubicTo(center.x - 14f, center.y + 8f, center.x - 14f, size.height, center.x, size.height)
                        cubicTo(center.x + 14f, size.height, center.x + 14f, center.y + 8f, center.x, 2f)
                        close()
                    }
                    drawPath(drop, color = iconColor)
                } else if (isFruit) {
                    // Sliced orange circle / wedge
                    drawCircle(color = iconColor, radius = size.minDimension / 2f)
                    drawCircle(color = Color.White, radius = (size.minDimension / 2f) - 4f, style = Stroke(width = 2f))
                    // Orange slices lines
                    for (i in 0 until 4) {
                        val angle = (i * Math.PI / 4).toFloat()
                        val dx = Math.cos(angle.toDouble()).toFloat() * (size.minDimension / 2f)
                        val dy = Math.sin(angle.toDouble()).toFloat() * (size.minDimension / 2f)
                        drawLine(
                            color = Color.White,
                            start = center,
                            end = Offset(center.x + dx, center.y + dy),
                            strokeWidth = 3f
                        )
                    }
                } else {
                    // Molase jar bottle
                    val jar = Path().apply {
                        moveTo(center.x - 6f, 2f)
                        lineTo(center.x + 6f, 2f)
                        lineTo(center.x + 6f, 8f)
                        lineTo(center.x + 12f, 10f)
                        lineTo(center.x + 12f, size.height)
                        lineTo(center.x - 12f, size.height)
                        lineTo(center.x - 12f, 10f)
                        lineTo(center.x - 6f, 8f)
                        close()
                    }
                    drawPath(jar, color = iconColor)
                    // Highlight details
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(center.x - 8f, 15f),
                        size = androidx.compose.ui.geometry.Size(16f, 16f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = amount,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = EcoTextDark
            )
            Text(
                text = unit,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = EcoTextLight,
                modifier = Modifier.padding(bottom = 3.dp)
            )
        }
    }
}

@Composable
fun HistoryItemCard(
    batch: EcoBatch,
    onDelete: () -> Unit
) {
    val dateStr = remember(batch.timestamp) {
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        sdf.format(Date(batch.timestamp))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("history_batch_item_${batch.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(EcoGreenLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = "Batch item",
                        tint = EcoGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = batch.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = EcoTextDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Formula (Molase:Skins:Water):",
                        fontSize = 11.sp,
                        color = EcoTextLight,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = "${String.format(Locale.US, "%.1f", batch.molaseKg)} Kg : ${String.format(Locale.US, "%.1f", batch.fruitSkinKg)} Kg : ${String.format(Locale.US, "%.1f", batch.waterLiters)} L",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = EcoGreenPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = dateStr,
                        fontSize = 10.sp,
                        color = EcoTextLight
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Hapus Riwayat",
                    tint = EcoRed.copy(alpha = 0.8f)
                )
            }
        }
    }
}
