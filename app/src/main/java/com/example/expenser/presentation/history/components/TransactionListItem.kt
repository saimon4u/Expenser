package com.example.expenser.presentation.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expenser.domain.model.Category
import com.example.expenser.domain.model.Transaction
import com.example.expenser.ui.theme.Emerald500
import com.example.expenser.ui.theme.Red500
import com.example.expenser.ui.theme.fonts
import com.example.expenser.util.TransactionType
import com.example.expenser.util.convertMillisToDate

@Composable
fun TransactionListItem(
    item: Transaction,
    category: Category?,
    moneyIcon: String?,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (category != null) {
            CategoryItem(
                icon = category.categoryIcon,
                name = category.name
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "${item.amount.toString()} ${moneyIcon ?: ""}",
            fontFamily = fonts,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            modifier = Modifier
                .width(150.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = item.date.convertMillisToDate(),
            fontFamily = fonts,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .width(150.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = if(item.description.isNullOrEmpty()) "No description" else item.description,
            fontFamily = fonts,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .width(150.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(if (item.type == TransactionType.Income.type) Emerald500 else Red500)
                .padding(5.dp),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = item.type.uppercase(),
                fontFamily = fonts,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .width(30.dp),
            contentAlignment = Alignment.Center
        ){
            IconButton(
                onClick = onDelete,
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    icon: String,
    name: String
) {
    Row(
        modifier = Modifier
            .width(100.dp),
    ) {
        Text(
            text = icon ,
            fontFamily = fonts,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = name,
            fontFamily = fonts,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}