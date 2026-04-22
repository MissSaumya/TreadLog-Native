package com.treadlog.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = OrangeNeon,
    secondary = BlueGlow,
    background = NavyDeep,
    surface = NavyCard,
    onPrimary = TextWhite,
    onSecondary = NavyDeep,
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
