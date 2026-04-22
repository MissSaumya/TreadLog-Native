package com.treadlog.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = OrangePrimary,
    secondary = BlueGlow,
    background = NavyBlue,
    surface = NavyLight,
    onPrimary = TextWhite,
    onSecondary = NavyBlue,
    onBackground = TextWhite,
    onSurface = TextWhite
)

@Composable
fun TreadLogTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
