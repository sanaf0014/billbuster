package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderDark
import com.example.ui.theme.CategoryDining
import com.example.ui.theme.CategoryGas
import com.example.ui.theme.CategoryGroceries
import com.example.ui.theme.CategoryOffice
import com.example.ui.theme.OrangePrimary
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class CategoryShare(
    val name: String,
    val percentage: Int,
    val color: Color,
    val amountFormatted: String
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryDonutChart(
    modifier: Modifier = Modifier,
    centerValue: String = "$1,284.52",
    centerLabel: String = "Total Spent",
    shares: List<CategoryShare> = listOf(
        CategoryShare("Groceries", 42, CategoryGroceries, "$539.50"),
        CategoryShare("Gas", 21, CategoryGas, "$269.75"),
        CategoryShare("Dining", 19, CategoryDining, "$244.05"),
        CategoryShare("Office", 18, CategoryOffice, "$231.22")
    )
) {
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceCard, RoundedCornerShape(16.dp))
            .border(1.dp, BorderDark, RoundedCornerShape(16.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Expense Distribution",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "By Category",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Donut Canvas with Center Text
        Box(
            modifier = Modifier.size(190.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(175.dp)) {
                val strokeWidth = 24.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2
                val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
                val arcSize = Size(radius * 2, radius * 2)

                // Background track
                drawArc(
                    color = Color(0xFF222733),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth)
                )

                var currentAngle = -90f
                shares.forEach { share ->
                    val sweep = (share.percentage / 100f) * 360f * animationProgress.value
                    if (sweep > 0f) {
                        drawArc(
                            color = share.color,
                            startAngle = currentAngle,
                            sweepAngle = sweep - 2.5f, // Small gap between slices
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                        currentAngle += (share.percentage / 100f) * 360f
                    }
                }
            }

            // Center details
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = centerLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
                Text(
                    text = centerValue,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Legend Badges
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            shares.forEach { share ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(share.color, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${share.name} (${share.percentage}%)",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

data class WeeklySpend(
    val weekLabel: String,
    val amount: Double,
    val isPeak: Boolean = false
)

@Composable
fun WeeklyBarChart(
    modifier: Modifier = Modifier,
    weeklyData: List<WeeklySpend> = listOf(
        WeeklySpend("Week 1", 212.0),
        WeeklySpend("Week 2", 356.0),
        WeeklySpend("Week 3", 424.0, isPeak = true),
        WeeklySpend("Week 4", 292.0)
    ),
    currencySymbol: String = "$"
) {
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
        )
    }

    val maxAmount = remember(weeklyData) { (weeklyData.maxOfOrNull { it.amount } ?: 500.0) * 1.15 }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceCard, RoundedCornerShape(16.dp))
            .border(1.dp, BorderDark, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Weekly Spending Pace",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Highest in Week 3 ($currencySymbol 424)",
                    style = MaterialTheme.typography.labelSmall,
                    color = OrangePrimary
                )
            }
            Box(
                modifier = Modifier
                    .background(Color(0xFF262016), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "4-Week Cycle",
                    style = MaterialTheme.typography.labelSmall,
                    color = OrangePrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Bars Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            weeklyData.forEach { item ->
                val barRatio = ((item.amount / maxAmount) * animationProgress.value).toFloat()
                val isPeak = item.isPeak

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "$currencySymbol${item.amount.toInt()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isPeak) OrangePrimary else TextSecondary,
                        fontWeight = if (isPeak) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height((110 * barRatio).dp.coerceAtLeast(4.dp))
                            .background(
                                brush = if (isPeak) {
                                    Brush.verticalGradient(
                                        listOf(OrangePrimary, Color(0xFFD9480F))
                                    )
                                } else {
                                    Brush.verticalGradient(
                                        listOf(Color(0xFF3B4455), Color(0xFF242A36))
                                    )
                                },
                                shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                            )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = item.weekLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isPeak) TextPrimary else TextMuted,
                        fontWeight = if (isPeak) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}
