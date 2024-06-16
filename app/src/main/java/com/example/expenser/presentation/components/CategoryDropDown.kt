package com.example.expenser.presentation.components

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.expenser.domain.model.Category
import com.example.expenser.util.TransactionType
import com.example.expenser.presentation.dashboard.DashboardState
import com.example.expenser.util.CreateCategoryError
import com.example.expenser.util.validateCategoryName
import com.example.expenser.ui.theme.fonts


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropDown(
    selectedValue: String,
    onValueChangedEvent: (String) -> Unit,
    modifier: Modifier = Modifier,
    onCategoryCreate: (Category) -> Unit,
    transactionType: TransactionType,
    dashboardState: DashboardState,
    getAllCategories: (String, TransactionType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var categoryTextBoxOpen by remember { mutableStateOf(false) }
    var categoryNameVal by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<CreateCategoryError?>(null) }

    if(categoryTextBoxOpen){
        Dialog(
            onDismissRequest = { categoryTextBoxOpen = false }
        ) {
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    DialogTitle(
                        modifier = Modifier.fillMaxWidth(),
                        transactionType = transactionType,
                        valueType = " category"
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    FormTextField(
                        text = categoryNameVal,
                        onValueChange = {
                            validationError = null
                            categoryNameVal = it
                        },
                        label = "Category Name",
                        validationError = validationError != null,
                        errorMessage = when(validationError){
                            is CreateCategoryError.ValidationError -> "Limit: ${categoryNameVal.length}/10"
                            is CreateCategoryError.DuplicateError -> "category already exist"
                            CreateCategoryError.ContainNumberError -> "category can't contain number"
                            null -> ""
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(15.dp))

                    Button(
                        onClick ={
                            validationError = validateCategoryName(categoryNameVal, dashboardState.categoryList)
                            if(validationError == null){
                                onCategoryCreate(
                                    Category(
                                        createdAt = System.currentTimeMillis(),
                                        name = categoryNameVal,
                                        userId = dashboardState.userData?.userId ?: "",
                                        type = transactionType.type
                                    )
                                )
                                categoryTextBoxOpen = false
                                categoryNameVal = ""
                            }
                        },
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Create",
                            fontFamily = fonts,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedButton(
            onClick = {
                expanded = true
                getAllCategories(dashboardState.userData!!.userId, transactionType)
            },
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            ),
            modifier = modifier
                .fillMaxWidth(.9f)
                .menuAnchor()
        ) {
            Text(
                text = selectedValue,
                fontFamily = fonts,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Spacer(modifier = Modifier.width(5.dp))
            Icon(
                imageVector = Icons.Rounded.ArrowDropDown,
                contentDescription = "Select Date",
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.AddCircleOutline,
                        contentDescription = "Create Category"
                    )
                },
                text = { 
                       Text(text = "Create")
                },
                onClick = { categoryTextBoxOpen = true }
            )
            Log.e("TAG", dashboardState.categoryList.size.toString() )
            dashboardState.categoryList.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option.name) },
                    onClick = {
                        expanded = false
                        onValueChangedEvent(option.name)
                    }
                )
            }
        }
    }
}