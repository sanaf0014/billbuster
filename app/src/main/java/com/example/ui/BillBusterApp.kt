package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.Receipt
import com.example.ui.screens.AuditDetailScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.InsightsScreen
import com.example.ui.screens.OverviewScreen
import com.example.ui.screens.ScanScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderDark
import com.example.ui.theme.OrangePrimary
import com.example.ui.theme.StatusFlagged
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.BillBusterViewModel

enum class ScreenTab(val title: String, val icon: ImageVector) {
    OVERVIEW("Overview", Icons.Default.Dashboard),
    SCAN("Scan", Icons.Default.DocumentScanner),
    HISTORY("History", Icons.Default.History),
    INSIGHTS("Insights", Icons.Default.Insights),
    SETTINGS("Settings", Icons.Default.Settings)
}

@Composable
fun BillBusterApp(
    viewModel: BillBusterViewModel = viewModel()
) {
    var currentTab by remember { mutableStateOf(ScreenTab.OVERVIEW) }
    var viewingAuditReceipt by remember { mutableStateOf<Receipt?>(null) }
    val flaggedCount by viewModel.flaggedCount.collectAsState()
    val notification by viewModel.statusNotification.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(notification) {
        notification?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissNotification()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundDark,
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = SurfaceCard,
                    contentColor = TextPrimary,
                    actionColor = OrangePrimary
                )
            }
        },
        bottomBar = {
            // Show bottom navigation bar when not deep in audit details
            if (viewingAuditReceipt == null) {
                NavigationBar(
                    containerColor = SurfaceDark,
                    contentColor = TextSecondary,
                    tonalElevation = 8.dp
                ) {
                    ScreenTab.entries.forEach { tab ->
                        val isSelected = currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                viewingAuditReceipt = null
                                currentTab = tab
                            },
                            icon = {
                                if (tab == ScreenTab.HISTORY && flaggedCount > 0) {
                                    BadgedBox(
                                        badge = {
                                            Badge(
                                                containerColor = StatusFlagged,
                                                contentColor = Color.White
                                            ) {
                                                Text(
                                                    text = "$flaggedCount",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = tab.icon,
                                            contentDescription = tab.title,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = tab.icon,
                                        contentDescription = tab.title,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = OrangePrimary,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted,
                                indicatorColor = OrangePrimary
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BackgroundDark)
        ) {
            if (viewingAuditReceipt != null) {
                AuditDetailScreen(
                    receipt = viewingAuditReceipt!!,
                    viewModel = viewModel,
                    onBack = { viewingAuditReceipt = null }
                )
            } else {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "TabTransition"
                ) { targetTab ->
                    when (targetTab) {
                        ScreenTab.OVERVIEW -> OverviewScreen(
                            viewModel = viewModel,
                            onNavigateToScan = { currentTab = ScreenTab.SCAN },
                            onNavigateToAudit = { receipt ->
                                viewingAuditReceipt = receipt
                            },
                            onNavigateToHistory = { currentTab = ScreenTab.HISTORY }
                        )

                        ScreenTab.SCAN -> ScanScreen(
                            viewModel = viewModel,
                            onAuditReady = { receipt ->
                                viewingAuditReceipt = receipt
                            }
                        )

                        ScreenTab.HISTORY -> HistoryScreen(
                            viewModel = viewModel,
                            onSelectReceipt = { receipt ->
                                viewingAuditReceipt = receipt
                            }
                        )

                        ScreenTab.INSIGHTS -> InsightsScreen(
                            viewModel = viewModel
                        )

                        ScreenTab.SETTINGS -> SettingsScreen(
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}
