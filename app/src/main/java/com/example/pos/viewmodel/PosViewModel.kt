package com.example.pos.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pos.data.CartItem
import com.example.pos.data.CustomerCredit
import com.example.pos.data.Employee
import com.example.pos.data.Expense
import com.example.pos.data.Product
import com.example.pos.data.Sale
import com.example.pos.data.Shop
import com.example.pos.data.StockInRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PosViewModel : ViewModel() {

    // Shop & Profile
    private val _shop = MutableStateFlow(Shop())
    val shop: StateFlow<Shop> = _shop.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // Navigation state
    private val _currentScreen = MutableStateFlow("main") // "splash", "login", "register_shop", "main"
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    private val _selectedTab = MutableStateFlow(0) // 0: Home, 1: Sales, 2: Inventory, 3: Reports, 4: More
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    // Products & Inventory
    private val _products = MutableStateFlow(
        listOf(
            Product("p1", "Unga", "Groceries", "UG001", "pcs", 110.0, 150.0, 20, 10, "Pemba Millers", "groceries"),
            Product("p2", "Sukari", "Groceries", "SK001", "kg", 110.0, 140.0, 35, 15, "Mumias Sugar", "sugar"),
            Product("p3", "Cooking Oil", "Groceries", "CO001", "L", 340.0, 420.0, 12, 10, "Bidco Africa", "oil"),
            Product("p4", "Rice", "Groceries", "RI001", "kg", 260.0, 350.0, 8, 10, "Mwea Farmers", "rice"),
            Product("p5", "Detergent", "Household", "DE001", "pkt", 200.0, 280.0, 18, 5, "Unilever", "household"),
            Product("p6", "Fresh Milk", "Beverages", "MK001", "pkt", 50.0, 70.0, 4, 10, "Brookside", "beverage"),
            Product("p7", "Soda 500ml", "Beverages", "SD001", "btl", 45.0, 65.0, 40, 12, "Coca Cola", "beverage"),
            Product("p8", "Potato Chips", "Snacks", "SN001", "pkt", 80.0, 120.0, 25, 8, "Tropical", "snack")
        )
    )
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // POS Cart
    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    // Payment & Completed Sale State
    private val _completedSale = MutableStateFlow<Sale?>(null)
    val completedSale: StateFlow<Sale?> = _completedSale.asStateFlow()

    // Sales History
    private val _salesHistory = MutableStateFlow(
        listOf(
            Sale("SL-1092", listOf(
                CartItem(Product("p1", "Unga", "Groceries", "UG001", "pcs", 110.0, 150.0, 20, 10), 3),
                CartItem(Product("p3", "Cooking Oil", "Groceries", "CO001", "L", 340.0, 420.0, 12, 10), 1),
                CartItem(Product("p2", "Sukari", "Groceries", "SK001", "kg", 110.0, 140.0, 35, 15), 1)
            ), 1010.0, "M-Pesa", "John Doe", "Today, 10:42 AM", System.currentTimeMillis() - 3600000),
            Sale("SL-1091", listOf(
                CartItem(Product("p7", "Soda 500ml", "Beverages", "SD001", "btl", 45.0, 65.0, 40, 12), 4)
            ), 260.0, "Cash", "Walk-in Customer", "Today, 09:15 AM", System.currentTimeMillis() - 7200000),
            Sale("SL-1090", listOf(
                CartItem(Product("p4", "Rice", "Groceries", "RI001", "kg", 260.0, 350.0, 8, 10), 5)
            ), 1750.0, "Card", "Sarah M.", "Yesterday, 04:30 PM", System.currentTimeMillis() - 86400000)
        )
    )
    val salesHistory: StateFlow<List<Sale>> = _salesHistory.asStateFlow()

    // Expenses
    private val _expenses = MutableStateFlow(
        listOf(
            Expense("e1", "Rent", "Utility", 3000.0, "Sep 28, 2026", "rent"),
            Expense("e2", "Electricity", "Utility", 1200.0, "Sep 26, 2026", "electricity"),
            Expense("e3", "Transport", "Logistics", 800.0, "Sep 24, 2026", "transport"),
            Expense("e4", "Salaries", "Payroll", 5000.0, "Sep 22, 2026", "salaries"),
            Expense("e5", "Internet", "Utility", 1000.0, "Sep 20, 2026", "wifi"),
            Expense("e6", "Repairs", "Maintenance", 600.0, "Sep 18, 2026", "repairs")
        )
    )
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    // Customer Credit Balances
    private val _customerCredits = MutableStateFlow(
        listOf(
            CustomerCredit("c1", "Samuel Njuguna", "+254 722 111 222", 8460.0, "2 Items | Unga, Cooking Oil", 500.0, 750.0, "Sep 30, 2026", "Pending"),
            CustomerCredit("c2", "Mary Wanjiku", "+254 733 444 555", 3200.0, "4 Items | Sukari, Rice", 1000.0, 2200.0, "Oct 05, 2026", "Pending"),
            CustomerCredit("c3", "David Ochieng", "+254 711 888 999", 5510.0, "1 Item | Cooking Oil x5", 1500.0, 4010.0, "Oct 12, 2026", "Pending")
        )
    )
    val customerCredits: StateFlow<List<CustomerCredit>> = _customerCredits.asStateFlow()

    // Employees
    private val _employees = MutableStateFlow(
        listOf(
            Employee("emp1", "Faith W.", "Owner", "+254 712 345 678", "faith@store.com", isOwner = true),
            Employee("emp2", "Kevin Otieno", "Sales Clerk", "+254 720 998 877", "kevin@store.com"),
            Employee("emp3", "Amina Hassan", "Manager", "+254 735 112 233", "amina@store.com")
        )
    )
    val employees: StateFlow<List<Employee>> = _employees.asStateFlow()

    // Stock In History
    private val _stockInRecords = MutableStateFlow(
        listOf(
            StockInRecord("stk1", "p1", "Unga", 50, 110.0, "Pemba Millers", "Sep 28, 2026 09:41 AM", "Faith"),
            StockInRecord("stk2", "p3", "Cooking Oil", 20, 340.0, "Bidco Africa", "Sep 25, 2026 02:15 PM", "Amina")
        )
    )
    val stockInRecords: StateFlow<List<StockInRecord>> = _stockInRecords.asStateFlow()

    // Navigation & Auth actions
    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }

    fun login(email: String, pass: String) {
        _isLoggedIn.value = true
        _currentScreen.value = "main"
    }

    fun registerShop(shopName: String, ownerName: String, email: String, phone: String, address: String, category: String) {
        _shop.value = Shop(
            name = shopName.ifBlank { "Faith's Store" },
            ownerName = ownerName.ifBlank { "Faith" },
            email = email.ifBlank { "faith@store.com" },
            phone = phone.ifBlank { "+254 712 345 678" },
            address = address.ifBlank { "Nairobi, Kenya" },
            category = category.ifBlank { "Retail & Grocery" }
        )
        _isLoggedIn.value = true
        _currentScreen.value = "main"
    }

    fun logout() {
        _isLoggedIn.value = false
        _currentScreen.value = "login"
    }

    // Category & Search
    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Cart operations
    fun addToCart(product: Product) {
        _cart.update { currentCart ->
            val existingIndex = currentCart.indexOfFirst { it.product.id == product.id }
            if (existingIndex >= 0) {
                val existing = currentCart[existingIndex]
                if (existing.quantity < product.stockQuantity) {
                    currentCart.toMutableList().apply {
                        this[existingIndex] = existing.copy(quantity = existing.quantity + 1)
                    }
                } else currentCart
            } else {
                currentCart + CartItem(product, 1)
            }
        }
    }

    fun removeFromCart(productId: String) {
        _cart.update { currentCart ->
            val existingIndex = currentCart.indexOfFirst { it.product.id == productId }
            if (existingIndex >= 0) {
                val existing = currentCart[existingIndex]
                if (existing.quantity > 1) {
                    currentCart.toMutableList().apply {
                        this[existingIndex] = existing.copy(quantity = existing.quantity - 1)
                    }
                } else {
                    currentCart.filterNot { it.product.id == productId }
                }
            } else currentCart
        }
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    // Process Sale & Automatic Stock Deduction
    fun completeSale(paymentMethod: String, customerName: String = "", isCredit: Boolean = false, paidAmount: Double = 0.0, dueDate: String = ""): Sale {
        val currentCartItems = _cart.value
        val total = currentCartItems.sumOf { it.totalPrice }

        // Deduct inventory
        _products.update { currentList ->
            currentList.map { product ->
                val cartItem = currentCartItems.find { it.product.id == product.id }
                if (cartItem != null) {
                    val newQty = (product.stockQuantity - cartItem.quantity).coerceAtLeast(0)
                    product.copy(stockQuantity = newQty)
                } else {
                    product
                }
            }
        }

        val newSale = Sale(
            id = "SL-${(1093..9999).random()}",
            items = currentCartItems,
            totalAmount = total,
            paymentMethod = paymentMethod,
            customerName = customerName.ifBlank { if (isCredit) "Credit Customer" else "Walk-in Customer" },
            dateFormatted = "Just now",
            timestamp = System.currentTimeMillis(),
            isCreditSale = isCredit,
            paidAmount = if (isCredit) paidAmount else total,
            balance = if (isCredit) (total - paidAmount).coerceAtLeast(0.0) else 0.0,
            dueDate = dueDate
        )

        _salesHistory.update { listOf(newSale) + it }

        if (isCredit) {
            val creditRecord = CustomerCredit(
                id = "c_${System.currentTimeMillis()}",
                customerName = customerName.ifBlank { "Credit Customer" },
                customerPhone = "+254 700 000 000",
                totalOutstanding = total,
                productsSummary = "${currentCartItems.size} Items | ${currentCartItems.joinToString { it.product.name }}",
                amountPaid = paidAmount,
                balance = total - paidAmount,
                dueDate = dueDate.ifBlank { "In 14 Days" }
            )
            _customerCredits.update { listOf(creditRecord) + it }
        }

        _completedSale.value = newSale
        _cart.value = emptyList()
        return newSale
    }

    fun dismissReceiptModal() {
        _completedSale.value = null
    }

    // Add Product
    fun addProduct(
        name: String, category: String, sku: String, unit: String,
        buyingPrice: Double, sellingPrice: Double, quantity: Int, minStock: Int, supplier: String
    ) {
        val newProduct = Product(
            id = "p_${System.currentTimeMillis()}",
            name = name,
            category = category,
            sku = sku,
            unit = unit,
            buyingPrice = buyingPrice,
            sellingPrice = sellingPrice,
            stockQuantity = quantity,
            minStock = minStock,
            supplier = supplier
        )
        _products.update { listOf(newProduct) + it }
    }

    // Stock In / Restocking
    fun recordStockIn(productId: String, quantityAdded: Int, buyingPrice: Double, supplier: String, employeeName: String) {
        _products.update { currentList ->
            currentList.map { p ->
                if (p.id == productId) {
                    p.copy(
                        stockQuantity = p.stockQuantity + quantityAdded,
                        buyingPrice = if (buyingPrice > 0) buyingPrice else p.buyingPrice
                    )
                } else p
            }
        }

        val prodName = _products.value.find { it.id == productId }?.name ?: "Product"
        val record = StockInRecord(
            id = "stk_${System.currentTimeMillis()}",
            productId = productId,
            productName = prodName,
            quantityAdded = quantityAdded,
            buyingPrice = buyingPrice,
            supplier = supplier,
            dateFormatted = "Today, 10:00 AM",
            employeeName = employeeName
        )
        _stockInRecords.update { listOf(record) + it }
    }

    // Expenses
    fun addExpense(title: String, category: String, amount: Double) {
        val newExpense = Expense(
            id = "exp_${System.currentTimeMillis()}",
            title = title,
            category = category,
            amount = amount,
            dateFormatted = "Today",
            iconType = category.lowercase()
        )
        _expenses.update { listOf(newExpense) + it }
    }

    // Add Employee
    fun addEmployee(name: String, role: String, phone: String, email: String) {
        val emp = Employee(
            id = "emp_${System.currentTimeMillis()}",
            name = name,
            role = role,
            phone = phone,
            email = email
        )
        _employees.update { it + emp }
    }
}
