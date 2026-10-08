package com.example.pos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pos.ui.components.FilterChipItem
import com.example.pos.ui.components.SurfaceCard
import com.example.pos.ui.theme.*
import com.example.pos.viewmodel.PosViewModel

@Composable
fun ReportsScreen(
    viewModel: PosViewModel
) {
    val products by viewModel.products.collectAsState()
    val expenses by viewModel.expenses.collectAsState()
    var selectedPeriod by remember { mutableStateOf("Daily") }

    val revenue = 24580.0
    val cogs = 6260.0
    val grossProfit = revenue - cogs
    val totalExpenses = expenses.sumOf { it.amount }
    val netProfit = grossProfit - totalExpenses

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Text(
            text = "Reports & Analytics",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Period Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Daily", "Weekly", "Monthly").forEach { period ->
                FilterChipItem(
                    label = period,
                    isSelected = selectedPeriod == period,
                    onClick = { selectedPeriod = period }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Financial Health Card
        SurfaceCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = SurfaceDark,
            borderColor = SurfaceBorder,
            cornerRadius = 22.dp
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(EmeraldContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = null,
                            tint = EmeraldBright,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "$selectedPeriod Overview",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                ReportMetricRow("Revenue", "KSh ${String.format("%,.0f", revenue)}", EmeraldBright)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SurfaceBorder)

                ReportMetricRow("Cost of Goods (COGS)", "- KSh ${String.format("%,.0f", cogs)}", TextSecondary)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SurfaceBorder)

                ReportMetricRow("Gross Profit", "KSh ${String.format("%,.0f", grossProfit)}", TextPrimary)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SurfaceBorder)

                ReportMetricRow("Total Expenses", "- KSh ${String.format("%,.0f", totalExpenses)}", ErrorRed)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SurfaceBorder)

                ReportMetricRow("Net Profit", "KSh ${String.format("%,.0f", netProfit)}", EmeraldBright, isHighlight = true)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Top Selling Products
        Text(
            text = "Top Selling Products",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        products.take(4).forEachIndexed { index, product ->
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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(EmeraldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "#${index + 1}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = EmeraldBright,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "${product.stockQuantity + 45} units sold",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            )
                        }
                    }

                    Text(
                        text = "KSh ${String.format("%,.0f", product.sellingPrice * 45)}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = EmeraldBright,
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun ReportMetricRow(
    label: String,
    amount: String,
    amountColor: Color,
    isHighlight: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = if (isHighlight) MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
            else MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
        )
        Text(
            text = amount,
            style = if (isHighlight) MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, color = amountColor)
            else MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = amountColor)
        )
    }
}
