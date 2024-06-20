package com.example.expenser.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expenser.domain.model.Category
import com.example.expenser.ui.theme.Emerald500
import com.example.expenser.ui.theme.fonts

@Composable
fun TransactionByCategoryBox(
    modifier: Modifier = Modifier,
    categoryList: List<Category>,
    heading: String,
    totalAmount: Double,
    getAmount: (Category) -> Double,
    typeHeading: String,
){
    Box(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = heading,
                    fontFamily = fonts,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            items(categoryList){
                val p = ((getAmount(it))/totalAmount)
                TransactionByCategoryItem(
                    categoryTitle = typeHeading,
                    percentage = (p*100).toString(),
                    amount = getAmount(it).toString(),
                    progress = p.toFloat()
                )
            }

        }
    }
}

@Composable
fun TransactionByCategoryItem(
    categoryTitle: String,
    percentage: String,
    amount: String,
    progress: Float,
){
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$categoryTitle ($percentage%)",
                fontFamily = fonts,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
            Text(
                text = amount,
                fontFamily = fonts,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        LinearProgressIndicator(
            progress = progress,
            color = Emerald500,
            trackColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )
    }
}