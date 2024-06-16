package com.example.expenser.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.expenser.util.TransactionType
import com.example.expenser.ui.theme.Emerald500
import com.example.expenser.ui.theme.Red500
import com.example.expenser.ui.theme.fonts

@Composable
fun DialogTitle(
    modifier: Modifier = Modifier,
    transactionType: TransactionType,
    valueType: String
){
    Row (
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){
        Text(
            text = "Create an ",
            fontFamily = fonts,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = transactionType.type,
            fontFamily = fonts,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if(transactionType == TransactionType.Income) Emerald500 else Red500
        )
        Text(
            text = valueType,
            fontFamily = fonts,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}