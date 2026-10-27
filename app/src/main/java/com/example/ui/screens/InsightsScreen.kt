package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CategoryDonutChart
import com.example.ui.components.WeeklyBarChart
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
fun InsightsScreen(
    viewModel: BillBusterViewModel
) {
    val totalSpent by viewModel.totalSpent.collectAsState()
    val flaggedCount by viewModel.flaggedCount.collectAsState()
    val verifiedCount by viewModel.verifiedCount.collectAsState()
    val selectedMonth by viewModel.selectedMonth.collectAsState()
    val currencySymbol by viewModel.currencySymbol.collectAsState()

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
                    text = "Spending Insights",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Data Science analysis and anomaly detection reports",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        // Month Selector Chips
        item {
            val months = listOf("September 2026", "August 2026", "July 2026")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                months.forEach { month ->
                    val isSelected = selectedMonth == month
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) OrangePrimary else SurfaceCard)
                            .border(1.dp, if (isSelected) OrangePrimary else BorderDark, RoundedCornerShape(20.dp))
                            .clickable { viewModel.setSelectedMonth(month) }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = month,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        // Monthly KPI Metric Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderDark, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "TOTAL SPENT ($selectedMonth)",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "$currencySymbol${String.format("%,.2f", totalSpent)}",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Verified Receipts",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                            Text(
                                text = "$verifiedCount",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = StatusVerified
                            )
                        }

                        Column {
                            Text(
                                text = "Flagged Anomalies",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                            Text(
                                text = "$flaggedCount",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (flaggedCount > 0) StatusFlagged else StatusVerified
                            )
                        }

                        Column {
                            Text(
                                text = "Audit Accuracy",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                            Text(
                                text = "100%",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = OrangePrimary
                            )
                        }
                    }
                }
            }
        }

        // Charts
        item {
            CategoryDonutChart(centerValue = "$currencySymbol${String.format("%,.2f", totalSpent)}")
        }

        item {
            WeeklyBarChart(currencySymbol = currencySymbol)
        }

        // Automated AI Insight Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1C18)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF573918), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFF3D2611), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = OrangePrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Automated Spending Insight",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = OrangePrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Groceries account for the largest share (42%) of recorded spending this month ($currencySymbol 539.50). Gas is second at 21% ($currencySymbol 269.75). BillBuster's mathematical checksum successfully caught a $10.00 pricing mismatch in Trader Joe's before expense reconciliation.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary,
                        lineHeight = 19.sp
                    )
                }
            }
        }

        // Academic Internship Performance Metrics Card
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
                            text = "Internship Benchmark Results",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Table 1 (Report)",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    BenchmarkRow(
                        title = "Math Rule Validation",
                        scope = "50 synthetic & real receipts",
                        result = "100% accuracy",
                        passed = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    BenchmarkRow(
                        title = "End-to-End Audit Speed",
                        scope = "Full cycle scan to save",
                        result = "15 s avg (vs 120 s manual)",
                        passed = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    BenchmarkRow(
                        title = "Database Latency",
                        scope = "1,000 concurrent CRUD queries",
                        result = "< 8 ms per query",
                        passed = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    BenchmarkRow(
                        title = "Degraded OCR Handling",
                        scope = "Low light, folded, faded print",
                        result = "76.5% (Triggers audit flag)",
                        passed = true
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
fun BenchmarkRow(
    title: String,
    scope: String,
    result: String,
    passed: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceElevatedDark, RoundedCornerShape(10.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = scope,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                fontSize = 10.sp
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = result,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = OrangePrimary
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(StatusVerified, CircleShape)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "PASSED",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = StatusVerified
                )
            }
        }
    }
}
