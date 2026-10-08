package com.example.pos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.pos.ui.components.CustomBottomNavBar
import com.example.pos.ui.theme.BgDark
import com.example.pos.viewmodel.PosViewModel

@Composable
fun MainContainerScreen(
    viewModel: PosViewModel
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    var activeModal by remember { mutableStateOf<String?>(null) } // "payment", "add_product", "stock_in", "credit_sales", "expenses", "employees"

    val completedSale by viewModel.completedSale.collectAsState()

    // Show sale receipt modal when a sale completes
    if (completedSale != null) {
        SaleSuccessDialog(
            viewModel = viewModel,
            onDismiss = {
                viewModel.dismissReceiptModal()
                activeModal = null
                viewModel.selectTab(0) // Return to home
            }
        )
    }

    Scaffold(
        bottomBar = {
            if (activeModal == null) {
                CustomBottomNavBar(
                    selectedTab = selectedTab,
                    onTabSelected = { viewModel.selectTab(it) }
                )
            }
        },
        containerColor = BgDark
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BgDark)
        ) {
            if (activeModal != null) {
                when (activeModal) {
                    "payment" -> PaymentScreen(
                        viewModel = viewModel,
                        onBack = { activeModal = null }
                    )
                    "add_product" -> AddProductScreen(
                        viewModel = viewModel,
                        onClose = { activeModal = null }
                    )
                    "stock_in" -> StockInScreen(
                        viewModel = viewModel,
                        onClose = { activeModal = null }
                    )
                    "credit_sales" -> CreditSalesScreen(
                        viewModel = viewModel,
                        onClose = { activeModal = null }
                    )
                    "expenses" -> ExpensesScreen(
                        viewModel = viewModel,
                        onClose = { activeModal = null }
                    )
                    "employees" -> EmployeesScreen(
                        viewModel = viewModel,
                        onClose = { activeModal = null }
                    )
                }
            } else {
                when (selectedTab) {
                    0 -> DashboardScreen(
                        viewModel = viewModel,
                        onNavigateToTab = { viewModel.selectTab(it) },
                        onOpenStockIn = { activeModal = "stock_in" },
                        onOpenCreditSales = { activeModal = "credit_sales" },
                        onOpenExpenses = { activeModal = "expenses" }
                    )
                    1 -> NewSaleScreen(
                        viewModel = viewModel,
                        onProceedToPayment = { activeModal = "payment" }
                    )
                    2 -> ProductsScreen(
                        viewModel = viewModel,
                        onOpenAddProduct = { activeModal = "add_product" }
                    )
                    3 -> ReportsScreen(
                        viewModel = viewModel
                    )
                    4 -> SettingsScreen(
                        viewModel = viewModel,
                        onOpenEmployees = { activeModal = "employees" },
                        onOpenCreditReminders = { activeModal = "credit_sales" },
                        onOpenExpenses = { activeModal = "expenses" }
                    )
                }
            }
        }
    }
}
