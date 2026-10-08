package com.example.pos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalGroceryStore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pos.data.CartItem
import com.example.pos.data.Product
import com.example.pos.ui.components.PrimaryGreenButton
import com.example.pos.ui.components.SecondaryDarkButton
import com.example.pos.ui.components.SurfaceCard
import com.example.pos.ui.theme.*
import com.example.pos.viewmodel.PosViewModel

@Composable
fun NewSaleScreen(
    viewModel: PosViewModel,
    onProceedToPayment: () -> Unit
) {
    val products by viewModel.products.collectAsState()
    val cart by viewModel.cart.collectAsState()
    var search by remember { mutableStateOf("") }

    val filteredProducts = products.filter {
        search.isBlank() || it.name.contains(search, ignoreCase = true) || it.sku.contains(search, ignoreCase = true)
    }

    val subtotal = cart.sumOf { it.totalPrice }
    val total = subtotal

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "New Sale",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )

                if (cart.isNotEmpty()) {
                    Text(
                        text = "Clear Cart",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ErrorRed,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.clickable { viewModel.clearCart() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search bar
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                placeholder = { Text("Search for products...", color = TextSecondary, fontSize = 14.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = EmeraldBright)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceDarkVariant,
                    unfocusedContainerColor = SurfaceDark,
                    focusedBorderColor = EmeraldBright,
                    unfocusedBorderColor = SurfaceBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Product Selection List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(filteredProducts) { product ->
                    val cartItem = cart.find { it.product.id == product.id }
                    val quantityInCart = cartItem?.quantity ?: 0

                    PosProductItemRow(
                        product = product,
                        quantityInCart = quantityInCart,
                        onAdd = { viewModel.addToCart(product) },
                        onRemove = { viewModel.removeFromCart(product.id) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(180.dp))
                }
            }
        }

        // Bottom Fixed Summary Bar
        Surface(
            color = SurfaceDark,
            tonalElevation = 12.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .border(1.dp, SurfaceBorder, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Subtotal",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                    Text(
                        text = "KSh ${String.format("%,.0f", subtotal)}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "KSh ${String.format("%,.0f", total)}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = EmeraldBright
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                PrimaryGreenButton(
                    text = if (cart.isEmpty()) "Select Products" else "Complete Sale (${cart.sumOf { it.quantity }})",
                    onClick = onProceedToPayment,
                    enabled = cart.isNotEmpty()
                )
            }
        }
    }
}

@Composable
fun PosProductItemRow(
    product: Product,
    quantityInCart: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceDark,
        borderColor = if (quantityInCart > 0) EmeraldBright.copy(alpha = 0.5f) else SurfaceBorder,
        cornerRadius = 18.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceDarkVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalGroceryStore,
                        contentDescription = null,
                        tint = EmeraldBright,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "KSh ${String.format("%,.0f", product.sellingPrice)} • Stock: ${product.stockQuantity}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    )
                }
            }

            // Quantity Stepper
            if (quantityInCart > 0) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(SurfaceDarkVariant)
                            .clickable { onRemove() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("-", style = MaterialTheme.typography.titleMedium.copy(color = EmeraldBright))
                    }

                    Text(
                        text = "$quantityInCart",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(EmeraldContainer)
                            .clickable { onAdd() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+", style = MaterialTheme.typography.titleMedium.copy(color = EmeraldBright))
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(EmeraldContainer)
                        .clickable { onAdd() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", style = MaterialTheme.typography.titleLarge.copy(color = EmeraldBright))
                }
            }
        }
    }
}

@Composable
fun PaymentScreen(
    viewModel: PosViewModel,
    onBack: () -> Unit
) {
    val cart by viewModel.cart.collectAsState()
    val totalAmount = cart.sumOf { it.totalPrice }

    var selectedPaymentMethod by remember { mutableStateOf("M-Pesa") }
    var customerInput by remember { mutableStateOf("") }
    var isCreditSale by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Payment",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Total Amount Display
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total Amount",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "KSh ${String.format("%,.0f", if (totalAmount > 0) totalAmount else 1010.0)}",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 34.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = EmeraldBright
                        )
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Payment Methods
                Text(
                    text = "Payment Method",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                val methods = listOf("Cash", "M-Pesa", "Card", "Other")

                SurfaceCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SurfaceDark,
                    borderColor = SurfaceBorder,
                    cornerRadius = 20.dp
                ) {
                    methods.forEach { method ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPaymentMethod = method }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedPaymentMethod == method,
                                onClick = { selectedPaymentMethod = method },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = EmeraldBright,
                                    unselectedColor = TextMuted
                                )
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = method,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Customer (Optional) Input
                OutlinedTextField(
                    value = customerInput,
                    onValueChange = { customerInput = it },
                    label = { Text("Customer (Optional)", color = TextSecondary) },
                    placeholder = { Text("Name or phone number", color = TextMuted) },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = EmeraldBright) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceDarkVariant,
                        unfocusedContainerColor = SurfaceDark,
                        focusedBorderColor = EmeraldBright,
                        unfocusedBorderColor = SurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    singleLine = true
                )
            }

            // Complete Sale Button
            PrimaryGreenButton(
                text = "Complete Sale",
                onClick = {
                    viewModel.completeSale(
                        paymentMethod = selectedPaymentMethod,
                        customerName = customerInput,
                        isCredit = isCreditSale
                    )
                },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
fun SaleSuccessDialog(
    viewModel: PosViewModel,
    onDismiss: () -> Unit
) {
    val completedSale by viewModel.completedSale.collectAsState()

    if (completedSale != null) {
        val sale = completedSale!!

        AlertDialog(
            onDismissRequest = onDismiss,
            containerColor = SurfaceDark,
            title = null,
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(EmeraldContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = EmeraldBright,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Sale Completed!",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Transaction ID: ${sale.id}",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "KSh ${String.format("%,.0f", sale.totalAmount)}",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = EmeraldBright
                        )
                    )

                    Text(
                        text = "Paid via ${sale.paymentMethod}",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SecondaryDarkButton(
                            text = "Print",
                            onClick = { },
                            icon = Icons.Default.Print,
                            modifier = Modifier.weight(1f)
                        )
                        SecondaryDarkButton(
                            text = "Share",
                            onClick = { },
                            icon = Icons.Default.Share,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            },
            confirmButton = {
                PrimaryGreenButton(
                    text = "New Sale",
                    onClick = onDismiss
                )
            }
        )
    }
}
