package com.example.expenser.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.expenser.domain.model.Category
import com.example.expenser.presentation.components.CreateCategoryDialog
import com.example.expenser.util.TransactionType
import com.example.expenser.presentation.dashboard.DashboardState
import com.example.expenser.ui.theme.fonts


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropDown(
    selectedValue: String,
    selectedValueColor: Color,
    onValueChangedEvent: (String) -> Unit,
    modifier: Modifier = Modifier,
    onCategoryCreate: (Category) -> Unit,
    transactionType: TransactionType,
    dashboardState: DashboardState,
    getAllCategories: (String, TransactionType) -> Unit,
    showSnackbar: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var createCategoryDialogOpen by remember { mutableStateOf(false) }
    var categoryNameVal by remember { mutableStateOf("") }
    var categoryIconVal by remember { mutableStateOf("") }
    val categoryList = if(transactionType == TransactionType.Expense) dashboardState.expenseCategoryList else dashboardState.incomeCategoryList
    var categoryListDialogOpen by remember {
        mutableStateOf(false)
    }

    if(createCategoryDialogOpen){
        CreateCategoryDialog(
            onDismissRequest = { createCategoryDialogOpen = false },
            transactionType = transactionType,
            categoryNameVal = categoryNameVal,
            categoryIconVal = categoryIconVal,
            onCategoryIconChange = {
                categoryIconVal = it
            },
            onCategoryNameChange = {
                categoryNameVal = it
            },
            categoryList = categoryList,
            showSnackbar = showSnackbar,
            onCategoryCreate = {
                onCategoryCreate(it)
                createCategoryDialogOpen = false
            },
            userId = dashboardState.userData!!.userId
        )
    }

    if(categoryListDialogOpen){
        Dialog(
            onDismissRequest = { categoryListDialogOpen = false }
        ) {
            Column(
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
            ) {

                Text(
                    text = "Categories",
                    fontFamily = fonts,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        createCategoryDialogOpen = true
                        categoryListDialogOpen = false
                    },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create Category",
                        modifier = Modifier
                            .size(20.dp),
                        tint = MaterialTheme.colorScheme.background
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Create a category",
                        fontFamily = fonts,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.background
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if(categoryList.isEmpty()){
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "No category found! Create one...",
                            fontFamily = fonts,
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }



                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(categoryList){
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(5.dp))
                                .background(MaterialTheme.colorScheme.background)
                                .padding(5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${it.categoryIcon} ${it.name}",
                                fontFamily = fonts,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        categoryListDialogOpen = false
                                        expanded = false
                                        onValueChangedEvent("${it.categoryIcon} ${it.name}")
                                    }
                            )
                        }
                    }
                }
            }
        }
    }

    Button(
        onClick = {
            expanded = true
            getAllCategories(dashboardState.userData!!.userId, transactionType)
            categoryListDialogOpen = true
        },
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        modifier = Modifier
            .fillMaxWidth(.9f)
    ) {
        Text(
            text = selectedValue,
            fontFamily = fonts,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = selectedValueColor
        )
        Spacer(modifier = Modifier.width(5.dp))
        Icon(
            imageVector = Icons.Rounded.ArrowDropDown,
            contentDescription = "Select Date",
            tint = MaterialTheme.colorScheme.onSecondary
        )
    }
}