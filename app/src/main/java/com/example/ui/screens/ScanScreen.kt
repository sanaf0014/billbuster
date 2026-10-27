package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AuditScanStage
import com.example.model.Receipt
import com.example.ui.theme.BorderDark
import com.example.ui.theme.OrangePrimary
import com.example.ui.theme.StatusFlagged
import com.example.ui.theme.StatusVerified
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceElevatedDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.BillBusterViewModel

@Composable
fun ScanScreen(
    viewModel: BillBusterViewModel,
    onAuditReady: (Receipt) -> Unit
) {
    val scanState by viewModel.scanState.collectAsState()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var loadedReceiptName by remember { mutableStateOf("Trader Joe's Receipt (Sample)") }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            loadedReceiptName = "Uploaded Receipt (${uri.lastPathSegment?.takeLast(14) ?: "image"})"
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Header
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                Text(
                    text = "Scan Receipt",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "AI Optical Character Recognition & Automated Math Audit Pipeline",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        // Viewfinder Frame & Preview Area
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderDark, RoundedCornerShape(18.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top Viewfinder Status Pill
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(Color(0xFF0F3A2E), RoundedCornerShape(20.dp))
                                .border(1.dp, Color(0xFF1D6853), RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(StatusVerified, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Lighting Good • Alignment Frame Ready",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StatusVerified
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = "Flash indicator",
                            tint = OrangePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Camera Framing Target Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(230.dp)
                            .background(SurfaceElevatedDark, RoundedCornerShape(12.dp))
                            .border(1.5.dp, if (scanState.isScanning) OrangePrimary else BorderDark, RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Simulated Receipt Paper Preview
                        Column(
                            modifier = Modifier
                                .width(220.dp)
                                .background(Color(0xFF13161C), RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFF2E3544), RoundedCornerShape(8.dp))
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "TRADER JOE'S",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "Sep 18, 2026 • 4:12 PM",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(BorderDark)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Org Bananas (1)",
                                    fontSize = 10.sp,
                                    color = TextSecondary,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "$2.49",
                                    fontSize = 10.sp,
                                    color = TextPrimary,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Almond Milk (2)",
                                    fontSize = 10.sp,
                                    color = TextSecondary,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "$7.98",
                                    fontSize = 10.sp,
                                    color = TextPrimary,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Sourdough Bread",
                                    fontSize = 10.sp,
                                    color = StatusFlagged,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "$14.99",
                                    fontSize = 10.sp,
                                    color = StatusFlagged,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Sea Salt (1)",
                                    fontSize = 10.sp,
                                    color = TextSecondary,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "$3.30",
                                    fontSize = 10.sp,
                                    color = TextPrimary,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Greek Yogurt (1)",
                                    fontSize = 10.sp,
                                    color = TextSecondary,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "$5.00",
                                    fontSize = 10.sp,
                                    color = TextPrimary,
                                    fontFamily = FontFamily.Monospace
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(BorderDark)
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "TOTAL:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "$23.76",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = OrangePrimary,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        // Scanning Laser overlay when running
                        if (scanState.isScanning) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(2.dp)
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color.Transparent, OrangePrimary, Color.Transparent)
                                        )
                                    )
                                    .align(Alignment.Center)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Current: $loadedReceiptName",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Buttons Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { photoPickerLauncher.launch("image/*") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = TextPrimary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Browse Photo", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                loadedReceiptName = "Trader Joe's Receipt (Demo Worked Example)"
                                viewModel.startScanningSimulation(loadedReceiptName) {
                                    // Scan finished
                                }
                            },
                            enabled = !scanState.isScanning,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceElevatedDark,
                                contentColor = OrangePrimary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Receipt,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Use Demo Receipt", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Primary Analyze Button
                    Button(
                        onClick = {
                            viewModel.startScanningSimulation(loadedReceiptName) {
                                // Scan complete
                            }
                        },
                        enabled = !scanState.isScanning,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangePrimary,
                            contentColor = Color.White
                        )
                    ) {
                        if (scanState.isScanning) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Analyzing Pipeline Active...", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Analyze Receipt & Check Math", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Multi-Stage Progress Animation (When active or complete)
        if (scanState.isScanning || scanState.isComplete) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderDark, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Automated Audit Pipeline",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${(scanState.progress * 100).toInt()}% Complete",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = OrangePrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { scanState.progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = OrangePrimary,
                            trackColor = SurfaceElevatedDark
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 4 Stages
                        val stages = AuditScanStage.entries.toTypedArray()
                        stages.forEachIndexed { index, stage ->
                            val isDone = scanState.progress >= ((index + 1) * 0.25f)
                            val isCurrent = stage == scanState.currentStage && scanState.isScanning

                            StageProgressRow(
                                stageNumber = index + 1,
                                stage = stage,
                                isDone = isDone,
                                isCurrent = isCurrent
                            )

                            if (index < stages.size - 1) {
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }

                        // Completed Action Button
                        if (scanState.isComplete && !scanState.isScanning) {
                            Spacer(modifier = Modifier.height(18.dp))
                            Button(
                                onClick = {
                                    scanState.scannedReceipt?.let { onAuditReady(it) }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = OrangePrimary,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Inspect Audit Results & Fix Items", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Method Explanation (Academic Internship Context)
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderDark, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = OrangePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "How BillBuster Auditing Works",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "1. Preprocessing deskews image and evaluates contrast.\n" +
                                "2. Pytesseract OCR extracts line strings, quantities, and prices.\n" +
                                "3. Regex parsing structures line items and computes checksum sum(items) vs printed total.\n" +
                                "4. Discrepancies trigger an audit flag to protect users from billing errors.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun StageProgressRow(
    stageNumber: Int,
    stage: AuditScanStage,
    isDone: Boolean,
    isCurrent: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(
                    when {
                        isDone -> StatusVerified
                        isCurrent -> OrangePrimary
                        else -> SurfaceElevatedDark
                    },
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isDone) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Text(
                    text = "$stageNumber",
                    color = if (isCurrent) Color.White else TextMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stage.label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isCurrent || isDone) FontWeight.Bold else FontWeight.Normal,
                color = if (isCurrent) OrangePrimary else if (isDone) TextPrimary else TextMuted
            )
            Text(
                text = stage.detail,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontSize = 11.sp
            )
        }
    }
}
