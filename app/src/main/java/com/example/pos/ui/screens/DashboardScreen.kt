package com.example.pos.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pos.ui.components.MetricStatCard
import com.example.pos.ui.components.RetailSyncHeader
import com.example.pos.ui.components.SurfaceCard
import com.example.pos.ui.theme.*
import com.example.pos.viewmodel.PosViewModel

@Composable
fun DashboardScreen(
    viewModel: PosViewModel,
    onNavigateToTab: (Int) -> Unit,
    onOpenStockIn: () -> Unit,
    onOpenCreditSales: () -> Unit,
    onOpenExpenses: () -> Unit
) {
    val shop by viewModel.shop.collectAsState()
    val products by viewModel.products.collectAsState()
    val salesHistory by viewModel.salesHistory.collectAsState()
    val expenses by viewModel.expenses.collectAsState()

    val totalSalesToday = 24580.0
    val grossProfit = 18320.0
    val totalExpenses = expenses.sumOf { it.amount }
    val netProfit = grossProfit - totalExpenses
    val totalTransactions = salesHistory.size + 9
    val lowStockCount = products.count { it.isLowStock }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        // Header Bar
        RetailSyncHeader(
            shopName = shop.name,
            ownerName = shop.ownerName,
            onNotificationClick = { },
            onProfileClick = { onNavigateToTab(4) }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Today's Sales Hero Card with Green Graph Line
            SurfaceCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = SurfaceDark,
                borderColor = SurfaceBorder,
                cornerRadius = 24.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Today's Sales",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextSecondary,
                                fontSize = 13.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "KSh ${String.format("%,.0f", totalSalesToday)}",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary,
                                fontSize = 28.sp
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(EmeraldContainer)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "+ 12% from yesterday",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = EmeraldBright,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Green Trend Line Canvas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height

                        val path = Path().apply {
                            moveTo(0f, height * 0.8f)
                            cubicTo(
                                width * 0.2f, height * 0.7f,
                                width * 0.35f, height * 0.9f,
                                width * 0.5f, height * 0.4f
                            )
                            cubicTo(
                                width * 0.65f, height * 0.1f,
                                width * 0.8f, height * 0.5f,
                                width, height * 0.1f
                            )
                        }

                        val fillPath = Path().apply {
                            addPath(path)
                            lineTo(width, height)
                            lineTo(0f, height)
                            close()
                        }

                        // Gradient fill under curve
                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(EmeraldBright.copy(alpha = 0.35f), Color.Transparent)
                            )
                        )

                        // Smooth line stroke
                        drawPath(
                            path = path,
                            color = EmeraldBright,
                            style = Stroke(width = 3.dp.toPx())
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Metrics 2x2 Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricStatCard(
                    title = "Gross Profit",
                    value = "KSh ${String.format("%,.0f", grossProfit)}",
                    icon = Icons.Default.TrendingUp,
                    iconBgColor = EmeraldContainer,
                    iconTint = EmeraldBright,
                    modifier = Modifier.weight(1f)
                )

                MetricStatCard(
                    title = "Expenses",
                    value = "KSh ${String.format("%,.0f", totalExpenses)}",
                    icon = Icons.Outlined.ReceiptLong,
                    iconBgColor = SurfaceDarkVariant,
                    iconTint = EmeraldPrimary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricStatCard(
                    title = "Net Profit",
                    value = "KSh ${String.format("%,.0f", netProfit)}",
                    icon = Icons.Default.AccountBalanceWallet,
                    iconBgColor = EmeraldContainer,
                    iconTint = EmeraldBright,
                    modifier = Modifier.weight(1f)
                )

                MetricStatCard(
                    title = "Transactions",
                    value = "$totalTransactions",
                    icon = Icons.Outlined.Receipt,
                    iconBgColor = SurfaceDarkVariant,
                    iconTint = EmeraldPrimary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Actions Section
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionItem(
                    label = "New Sale",
                    icon = Icons.Default.PointOfSale,
                    onClick = { onNavigateToTab(1) },
                    modifier = Modifier.weight(1f)
                )
                QuickActionItem(
                    label = "Stock In",
                    icon = Icons.Default.AddBox,
                    onClick = onOpenStockIn,
                    modifier = Modifier.weight(1f)
                )
                QuickActionItem(
                    label = "Products",
                    icon = Icons.Default.Inventory2,
                    onClick = { onNavigateToTab(2) },
                    modifier = Modifier.weight(1f)
                )
                QuickActionItem(
                    label = "More",
                    icon = Icons.Default.MoreHoriz,
                    onClick = onOpenCreditSales,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Low Stock Alert Banner (if any)
            if (lowStockCount > 0) {
                SurfaceCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = WarningContainer,
                    borderColor = WarningYellow.copy(alpha = 0.4f),
                    cornerRadius = 16.dp,
                    onClick = { onNavigateToTab(2) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(WarningYellow.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WarningAmber,
                                    contentDescription = "Low Stock",
                                    tint = WarningYellow,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "$lowStockCount Products Low in Stock",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "Restock items like Rice, Fresh Milk",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "View",
                            tint = WarningYellow,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Recent Transactions Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )

                Text(
                    text = "See All",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = EmeraldBright,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable { onNavigateToTab(3) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            salesHistory.take(3).forEach { sale ->
                SurfaceCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    backgroundColor = SurfaceDark,
                    borderColor = SurfaceBorder,
                    cornerRadius = 16.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(EmeraldContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = null,
                                    tint = EmeraldBright,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = sale.id,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "${sale.items.size} items • ${sale.paymentMethod}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "KSh ${String.format("%,.0f", sale.totalAmount)}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldBright
                                )
                            )
                            Text(
                                text = sale.dateFormatted,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun QuickActionItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(18.dp))
            .background(EmeraldContainer)
            .border(1.dp, EmeraldBright.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(SurfaceDarkVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = EmeraldBright,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp
                )
            )
        }
    }
}
