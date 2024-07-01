package com.example.expenser.presentation.settings.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expenser.ui.theme.fonts

@Composable
fun Header(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
    ){
        Column {
            Text(
                text = "Settings",
                fontFamily = fonts,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Manage your account settings and categories",
                fontFamily = fonts,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}