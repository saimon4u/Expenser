package com.example.expenser.presentation.splash.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.expenser.ui.theme.fonts

@Composable
fun Greeting(
    name: String
){
    Row {
        Text(
            text = "Welcome, ",
            fontFamily = fonts,
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "$name! 👋",
            fontFamily = fonts,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}