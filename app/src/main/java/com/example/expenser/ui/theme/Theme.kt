package com.example.expenser.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFbfc2ff),
    secondary = Color(0xFFc5c4dd),
    tertiary = Color(0xFFe8b9d4),
    error = Color(0xFFffb4ab),
    onPrimary = Color(0xFF272b60),
    onSecondary = Color(0xFF2e2f42),
    onTertiary = Color(0xFF46263b),
    onError = Color(0xFF690005),
    primaryContainer = Color(0xFF3e4278),
    onPrimaryContainer = Color(0xFFe0e0ff),
    secondaryContainer = Color(0xFF444559),
    onSecondaryContainer = Color(0xFFe1e0f9),
    tertiaryContainer = Color(0xFF5f3c52),
    onTertiaryContainer = Color(0xFFffd8ed),
    surface = Color(0xFF131318),
    onSurface = Color(0xFFe4e1e9)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF565992),
    secondary = Color(0xFF5c5d72),
    tertiary = Color(0xFF78536a),
    error = Color(0xFFba1a1a),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onError = Color.White,
    primaryContainer = Color(0xFFe0e0ff),
    onPrimaryContainer = Color(0xFF11144b),
    secondaryContainer = Color(0xFFe1e0f9),
    onSecondaryContainer = Color(0xFF191a2c),
    tertiaryContainer = Color(0xFFffd8ed),
    onTertiaryContainer = Color(0xFF2e1125),
    surface = Color(0xFFfbf8ff),
    onSurface = Color(0xFF1b1b21)
)

@Composable
fun ExpenserTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}