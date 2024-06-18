package com.example.expenser.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expenser.ui.theme.fonts
import com.example.expenser.util.shimmerEffect

@Composable
fun DateRangeView(
    selectedStartDate: String,
    selectedEndDate: String,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(vertical = 5.dp, horizontal = 10.dp)
    ){
        Text(
            text = selectedStartDate,
            fontFamily = fonts,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = "-",
            fontFamily = fonts,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = selectedEndDate,
            fontFamily = fonts,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(10.dp))
    }
}