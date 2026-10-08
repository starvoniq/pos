package com.example.pos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.pos.ui.screens.LoginScreen
import com.example.pos.ui.screens.MainContainerScreen
import com.example.pos.ui.screens.RegisterShopScreen
import com.example.pos.ui.screens.SplashScreen
import com.example.pos.ui.theme.BgDark
import com.example.pos.ui.theme.POSTheme
import com.example.pos.viewmodel.PosViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            POSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BgDark
                ) {
                    val posViewModel = remember { PosViewModel() }
                    val currentScreen by posViewModel.currentScreen.collectAsState()

                    when (currentScreen) {
                        "splash" -> SplashScreen(
                            onGetStarted = { posViewModel.navigateTo("login") }
                        )
                        "login" -> LoginScreen(
                            onLoginSuccess = { email, pass ->
                                posViewModel.login(email, pass)
                            },
                            onNavigateToRegister = {
                                posViewModel.navigateTo("register_shop")
                            }
                        )
                        "register_shop" -> RegisterShopScreen(
                            onRegisterSuccess = { name, owner, email, phone, addr, cat ->
                                posViewModel.registerShop(name, owner, email, phone, addr, cat)
                            },
                            onBackToLogin = {
                                posViewModel.navigateTo("login")
                            }
                        )
                        else -> MainContainerScreen(
                            viewModel = posViewModel
                        )
                    }
                }
            }
        }
    }
}
