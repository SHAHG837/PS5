package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DualSenseColorScheme = darkColorScheme(
    primary = PS5Cyan,
    onPrimary = Color.Black,
    primaryContainer = PS5Blue,
    onPrimaryContainer = Color.White,
    secondary = PS5Blue,
    onSecondary = Color.White,
    tertiary = PS5AccentPink,
    background = PS5DarkNavy,
    onBackground = Color(0xFFF1F5F9),
    surface = PS5Surface,
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = PS5SurfaceVariant,
    onSurfaceVariant = Color(0xFF94A3B8)
)

@Composable
fun MurtazaShahJiTheme(
    darkTheme: Boolean = true, // Default to sleek PS5 dark theme
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DualSenseColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MurtazaShahJiTheme(darkTheme = darkTheme, content = content)
}

