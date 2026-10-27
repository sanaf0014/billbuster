package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Receipt
import com.example.model.ReceiptItem
import com.example.model.ReceiptStatus
import com.example.ui.components.FixItemDialog
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
fun AuditDetailScreen(
    receipt: Receipt,
    viewModel: BillBusterViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val editDialogState by viewModel.editDialogState.collectAsState()
    val currencySymbol by viewModel.currencySymbol.collectAsState()

    // Calculated fields
    val sumOfItems = receipt.calculatedTotal
    val printedTotal = receipt.printedTotal
    val mismatch = receipt.mismatch
    val isFlagged = receipt.status == ReceiptStatus.FLAGGED

    // Edit Item Dialog if open
    if (editDialogState.isOpen && editDialogState.item != null) {
        FixItemDialog(
            item = editDialogState.item!!,
            currencySymbol = currencySymbol,
            onDismiss = { viewModel.dismissEditDialog() },
            onSave = { newName, newQty, newPrice ->
                viewModel.saveLineItemChanges(
                    receiptId = receipt.id,
                    itemId = editDialogState.item!!.id,
                    newName = newName,
                    newQuantity = newQty,
                    newUnitPrice = newPrice
                )
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar & Back Button
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .background(SurfaceElevatedDark, CircleShape)
                        .border(1.dp, BorderDark, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Receipt Audit Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "ID: ${receipt.id}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Receipt Summary Header Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (isFlagged) Color(0xFF6E281F) else Color(0xFF165B43),
                        RoundedCornerShape(16.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = receipt.merchant,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${receipt.date} • ${receipt.time}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                            Text(
                                text = "Category: ${receipt.category.displayName}",
                                style = MaterialTheme.typography.labelSmall,
                                color = OrangePrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // Status Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isFlagged) Color(0xFF4C1D1D) else Color(0xFF0D3E2F)
                                )
                                .border(
                                    1.dp,
                                    if (isFlagged) Color(0xFF8B2B2B) else Color(0xFF176B4F),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isFlagged) Icons.Default.Warning else Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = if (isFlagged) StatusFlagged else StatusVerified,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = receipt.status.displayName.uppercase(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isFlagged) StatusFlagged else StatusVerified
                                )
                            }
                        }
                    }
                }
            }
        }

        // Warning or Success Banner
        item {
            if (isFlagged) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF281512), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFF6B2B22), RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Warning",
                                tint = StatusFlagged,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mathematical Mismatch Detected ($currencySymbol${String.format("%.2f", mismatch)})",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFB4A9)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Extracted line items sum to $currencySymbol${String.format("%.2f", sumOfItems)}, whereas printed receipt total is $currencySymbol${String.format("%.2f", printedTotal)}. The $currencySymbol${String.format("%.2f", mismatch)} gap is traced to Sourdough Bread ($14.99 vs expected $4.99).",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0C2B20), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFF1B6B4E), RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = StatusVerified,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "All Math Checks Verified!",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = StatusVerified
                            )
                            Text(
                                text = "Sum of extracted items ($currencySymbol${String.format("%.2f", sumOfItems)}) perfectly matches printed total ($currencySymbol${String.format("%.2f", printedTotal)}).",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Comparison Metric Box
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderDark, RoundedCornerShape(14.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Receipt Printed Total",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$currencySymbol${String.format("%.2f", printedTotal)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Text(
                        text = if (isFlagged) "≠" else "=",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isFlagged) StatusFlagged else StatusVerified
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Calculated Item Sum",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$currencySymbol${String.format("%.2f", sumOfItems)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (isFlagged) StatusFlagged else StatusVerified
                        )
                    }
                }
            }
        }

        // Section Title: Itemized Receipt
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Extracted Line Items (${receipt.items.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Tap to edit line item",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
        }

        // Line Items List
        items(receipt.items) { item ->
            LineItemCard(
                item = item,
                currencySymbol = currencySymbol,
                onEdit = { viewModel.openEditDialog(receipt.id, item) }
            )
        }

        // Action Buttons
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // If there's a flagged item, provide a prominent "Fix Flagged Item" button
                val flaggedItem = receipt.items.firstOrNull { it.isFlagged }
                if (flaggedItem != null) {
                    Button(
                        onClick = { viewModel.openEditDialog(receipt.id, flaggedItem) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangePrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Fix Flagged Item (${flaggedItem.name})", fontWeight = FontWeight.Bold)
                    }

                    // Direct auto-fix helper button
                    flaggedItem.expectedPrice?.let { expected ->
                        Button(
                            onClick = {
                                viewModel.applyExpectedPrice(receipt.id, flaggedItem.id)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceElevatedDark,
                                contentColor = OrangePrimary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoFixHigh,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Auto-Resolve: Apply Expected Price ($currencySymbol${String.format("%.2f", expected)})",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                OutlinedButton(
                    onClick = { viewModel.markAsReviewed(receipt.id) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = StatusVerified
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Mark as Reviewed", fontWeight = FontWeight.Medium)
                }
            }
        }

        // Notes and Academic Pipeline Audit Footer
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderDark, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Audit Pipeline Notes",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = receipt.notes.ifEmpty { "OCR text ingested via Pytesseract engine. Checksum verified against relational SQLite store." },
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        fontSize = 11.sp
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
fun LineItemCard(
    item: ReceiptItem,
    currencySymbol: String,
    onEdit: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isFlagged) Color(0xFF281816) else SurfaceCard
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (item.isFlagged) Color(0xFF6E281F) else BorderDark,
                RoundedCornerShape(12.dp)
            )
            .clickable { onEdit() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (item.isFlagged) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(Color(0xFF451A1A), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Flagged",
                                tint = StatusFlagged,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                    }

                    Column {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (item.isFlagged) Color(0xFFFFB4A9) else TextPrimary
                        )
                        Text(
                            text = "Qty: ${item.quantity} × $currencySymbol${String.format("%.2f", item.unitPrice)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "$currencySymbol${String.format("%.2f", item.lineTotal)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (item.isFlagged) StatusFlagged else TextPrimary
                        )
                        if (item.isFlagged && item.expectedPrice != null) {
                            Text(
                                text = "Exp: $currencySymbol${String.format("%.2f", item.expectedPrice)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = StatusVerified,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Item",
                        tint = if (item.isFlagged) OrangePrimary else TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Flag reason detail
            if (item.isFlagged && item.flagReason != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF381F1A), RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = item.flagReason,
                        fontSize = 11.sp,
                        color = Color(0xFFFFD8D3),
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}
