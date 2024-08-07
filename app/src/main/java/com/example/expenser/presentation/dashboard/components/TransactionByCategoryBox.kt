package com.example.expenser.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expenser.domain.model.Category
import com.example.expenser.ui.theme.Emerald500
import com.example.expenser.ui.theme.Red500
import com.example.expenser.ui.theme.fonts
import com.example.expenser.util.debug

@Composable
fun TransactionByCategoryBox(
    modifier: Modifier = Modifier,
    categoryList: List<Category>,
    heading: String,
    totalAmount: Double,
    getAmount: (Category) -> Double,
    moneyIcon: String? = null
){

    var count = 0


    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(10.dp)
    ) {
        Row {
            Text(
                text = heading,
                fontFamily = fonts,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = if(heading == "Incomes") Emerald500 else Red500
            )

            Text(
                text = " by Category",
                fontFamily = fonts,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        
        if(categoryList.isEmpty()){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "No data for the selected period",
                    fontFamily = fonts,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Light,
                )
            }
        }

        LazyColumn(
            modifier = Modifier.padding(horizontal = 10.dp),
        ) {
            items(categoryList){
                val p = ((getAmount(it))/totalAmount)
                if(!p.isNaN() && p  != 0.0) {
                    count += 1
                    TransactionByCategoryItem(
                        categoryTitle = it.name,
                        categoryIcon = it.categoryIcon,
                        percentage = String.format("%.2f", p * 100),
                        amount = String.format("%.2f", getAmount(it)),
                        progress = p.toFloat(),
                        moneyIcon = moneyIcon,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

        }
    }
}
