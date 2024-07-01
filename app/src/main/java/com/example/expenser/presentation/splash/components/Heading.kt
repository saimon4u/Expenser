package com.example.expenser.presentation.splash.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expenser.ui.theme.fonts

@Composable
fun Heading(
    userName: String
){
    Greeting(userName)

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = "Let's get started by setting up your currency",
        fontFamily = fonts,
        fontSize = 12.sp,
        fontWeight = FontWeight.Light,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = "You can change these setting at any time",
        fontFamily = fonts,
        fontSize = 10.sp,
        fontWeight = FontWeight.Light,
        color = MaterialTheme.colorScheme.onSurface
    )
}