package com.example.liuruyi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple = Color(0xFF7C5CFC)
val Pink = Color(0xFFFF7AB8)
val BubbleMine = Color(0xFF95EC69)
val BubbleTheirs = Color(0xFFFFFFFF)
val WeChatBg = Color(0xFFEDEDED)

private val LightColors = lightColorScheme(
    primary = Purple,
    secondary = Pink,
    surface = Color(0xFFFDFCFF),
    background = Color(0xFFFDFCFF),
)

@Composable
fun LiuruyiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content,
    )
}
