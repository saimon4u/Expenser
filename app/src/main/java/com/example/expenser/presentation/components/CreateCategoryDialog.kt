package com.example.expenser.presentation.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import com.example.expenser.presentation.dashboard.components.DialogTitle
import com.example.expenser.presentation.dashboard.components.FormTextField
import com.example.expenser.ui.theme.fonts
import com.example.expenser.util.CreateCategoryErrors
import com.example.expenser.util.TransactionType
import com.example.expenser.util.validateCategoryName

@Composable
fun CreateCategoryDialog(
    onDismissRequest: () -> Unit,
    transactionType: TransactionType,
    categoryNameVal: String,
    onCategoryNameChange: (String) -> Unit,
    categoryList: List<Category>,
    showSnackbar: (String) -> Unit,
    onCategoryCreate: (Category) -> Unit,
    userId: String,
){
    var validationError by remember { mutableStateOf<CreateCategoryErrors?>(null) }

    Dialog(
        onDismissRequest = onDismissRequest
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
                        onCategoryNameChange(it)
                        validationError = null
                    },
                    label = "Category Name",
                )

                Spacer(modifier = Modifier.height(15.dp))

                Button(
                    onClick ={
                        validationError = validateCategoryName(categoryNameVal, categoryList)

                        when(validationError){
                            is CreateCategoryErrors.ContainNumberError -> showSnackbar("Can't contain number or special char!")
                            is CreateCategoryErrors.DuplicateError -> showSnackbar("Category already exists!")
                            is CreateCategoryErrors.InternetError -> showSnackbar("Check your network connection!")
                            is CreateCategoryErrors.BlackNameError -> showSnackbar("Category can't be blank!")
                            is CreateCategoryErrors.LongNameError -> showSnackbar("Char limit is: 10!")
                            null -> {
                                onCategoryCreate(
                                    Category(
                                        createdAt = System.currentTimeMillis(),
                                        name = categoryNameVal,
                                        userId = userId,
                                        type = transactionType.type
                                    )
                                )
                                showSnackbar("Category created!")
                                onCategoryNameChange("")
                            }
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