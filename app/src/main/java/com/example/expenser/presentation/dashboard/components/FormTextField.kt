package com.example.expenser.presentation.dashboard.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.expenser.ui.theme.fonts

@Composable
fun FormTextField(
    text: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text
    ),
    validationError: Boolean = false,
    errorMessage: String = ""
){
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                fontFamily = fonts,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                fontFamily = fonts,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        textStyle = TextStyle(
            fontFamily = fonts,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        supportingText = {
            if(validationError){
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontFamily = fonts,
                    fontWeight = FontWeight.Light,
                    fontSize = 10.sp
                )
            }
        }
    )
}