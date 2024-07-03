package com.example.expenser.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expenser.domain.model.Category
import com.example.expenser.ui.theme.Emerald500
import com.example.expenser.ui.theme.Red500
import com.example.expenser.ui.theme.fonts
import com.example.expenser.util.TransactionType

@Composable
fun CategoryBox(
    categoryList: List<Category>,
    modifier: Modifier = Modifier,
    bgColor: Color,
    iconTint: Color,
    icon: ImageVector,
    title: String,
    type: String,
    transactionType: TransactionType,
    showSnackbar: (String) -> Unit,
    onCategoryCreate: (Category, String) -> Unit,
    userId: String,
    onCategoryDelete: (Category) -> Unit
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        CategoryBoxHeader(
            bgColor = bgColor,
            iconTint = iconTint,
            icon = icon,
            title = title,
            transactionType = transactionType,
            categoryList = categoryList,
            showSnackbar = showSnackbar,
            onCategoryCreate = onCategoryCreate,
            userId = userId
        )

        Spacer(modifier = Modifier.height(20.dp))

        if(categoryList.isEmpty()){
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(150.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "No ",
                    fontFamily = fonts,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = type,
                    fontFamily = fonts,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if(type=="income") Emerald500 else Red500
                )
                Text(
                    text = " categories yet!",
                    fontFamily = fonts,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }else{
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .height(300.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(categoryList){
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(5.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = 16.dp, vertical = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${it.categoryIcon} ${it.name}",
                            fontFamily = fonts,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    onCategoryDelete(it)
                                }
                        )
                    }
                }
            }
        }
    }
}