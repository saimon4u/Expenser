package com.example.expenser.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expenser.domain.model.Category
import com.example.expenser.presentation.components.CreateCategoryDialog
import com.example.expenser.ui.theme.fonts
import com.example.expenser.util.TransactionType

@Composable
fun CategoryBoxHeader(
    bgColor: Color,
    iconTint: Color,
    icon: ImageVector,
    title: String,
    transactionType: TransactionType,
    categoryList: List<Category>,
    showSnackbar: (String) -> Unit,
    onCategoryCreate: (Category, String) -> Unit,
    userId: String
){

    var createCategoryDialogOpen by remember { mutableStateOf(false) }
    var categoryNameVal by remember { mutableStateOf("") }
    var categoryIconVal by remember {
        mutableStateOf("")
    }

    if(createCategoryDialogOpen){
        CreateCategoryDialog(
            onDismissRequest = { createCategoryDialogOpen = false },
            transactionType = transactionType,
            categoryNameVal = categoryNameVal,
            onCategoryNameChange = {
                categoryNameVal = it
            },
            categoryList = categoryList,
            showSnackbar = showSnackbar,
            onCategoryCreate = {
                onCategoryCreate(it, userId)
                createCategoryDialogOpen = false
            },
            userId = userId,
            categoryIconVal = categoryIconVal,
            onCategoryIconChange = {
                categoryIconVal = it
            }
        )
    }

    Column {
        Row {
            Box(
                modifier = Modifier
                    .background(bgColor)
            ){
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier
                        .size(30.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontFamily = fonts,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Sorted by name",
                    fontFamily = fonts,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                createCategoryDialogOpen = true
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Create Category",
                fontFamily = fonts,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}