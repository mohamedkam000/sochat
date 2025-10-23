package com.so.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.so.chat.navigation.SoChatNavigation
import com.so.chat.ui.theme.SoChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkTheme = isSystemInDarkTheme()

            SoChatTheme(useDarkTheme = isDarkTheme) {
                val view = LocalView.current
                SideEffect {
                    val window = window
                    val insetsController = WindowInsetsControllerCompat(window, view)
                
                    val backgroundColor = MaterialTheme.colorScheme.background.toArgb()
                    window.statusBarColor = backgroundColor
                    window.navigationBarColor = backgroundColor
                    insetsController.isAppearanceLightStatusBars = !isDarkTheme
                    insetsController.isAppearanceLightNavigationBars = !isDarkTheme
                }

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(WindowInsets.safeDrawing.asPaddingValues()),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SoChatNavigation()
                }
            }
        }
    }
}