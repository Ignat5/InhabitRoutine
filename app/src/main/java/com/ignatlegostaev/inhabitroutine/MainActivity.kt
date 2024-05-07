package com.ignatlegostaev.inhabitroutine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.ignatlegostaev.inhabitroutine.navigation.root.RootGraph
import com.ignatlegostaev.inhabitroutine.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var keepSplash: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        if (savedInstanceState == null) {
            splashScreen.setKeepOnScreenCondition { keepSplash }
        }
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                RootGraph(
                    onReady = { keepSplash = false }
                )
            }
        }
    }
}