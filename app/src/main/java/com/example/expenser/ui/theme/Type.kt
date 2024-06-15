package com.example.expenser.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.expenser.R


val fonts = FontFamily(
    Font(R.font.lato_black),
    Font(R.font.lato_bold, weight = FontWeight.Bold),
    Font(R.font.lato_light, weight = FontWeight.Light),
    Font(R.font.lato_thin, weight = FontWeight.Thin),
    Font(R.font.lato_regular, weight = FontWeight.Normal),
    Font(R.font.lato_black_italic, style = FontStyle.Italic),
    Font(R.font.lato_bold_italic, style = FontStyle.Italic, weight = FontWeight.Bold),
    Font(R.font.lato_light_italic, style = FontStyle.Italic, weight = FontWeight.Light),
    Font(R.font.lato_thin_italic, style = FontStyle.Italic, weight = FontWeight.Thin),
    Font(R.font.lato_italic, style = FontStyle.Italic, weight = FontWeight.Normal),
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Thin,
        fontSize = 16.sp
    ),
    // Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleSmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Thin,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = fonts,
        fontStyle = FontStyle.Italic,
        fontSize = 16.sp
    )

)