package com.example.expenser.presentation.history.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expenser.ui.theme.fonts
import com.example.expenser.util.SortFilterItem


@Composable
fun FilterSection(
    sortFilterItems: List<SortFilterItem>,
    onTypeClick: () -> Unit,
    onCategoryClick: () -> Unit,
){
    Row {
        Button(
            onClick = onCategoryClick,
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .padding(horizontal = 8.dp),
            colors = ButtonDefaults.buttonColors(
                MaterialTheme.colorScheme.surface
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier
                    .size(15.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Category | ${sortFilterItems.filter { it.isSelected && it.type == "category" }.size.toString()}",
                fontFamily = fonts,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = onTypeClick,
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .padding(horizontal = 8.dp),
            colors = ButtonDefaults.buttonColors(
                MaterialTheme.colorScheme.surface
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier
                    .size(15.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Type | ${sortFilterItems.filter { it.isSelected && it.type == "transaction_type" }.size.toString()}",
                fontFamily = fonts,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}