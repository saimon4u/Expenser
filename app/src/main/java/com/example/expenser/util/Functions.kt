package com.example.expenser.util
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.expenser.domain.model.Category
import com.example.expenser.domain.model.Transaction
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.coloredShadow(
    color: Color,
    alpha: Float = 0.2f,
    borderRadius: Dp = 0.dp,
    shadowRadius: Dp = 20.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = composed {
    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparent = color.copy(alpha = 0f).toArgb()
    this.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            frameworkPaint.color = transparent
            frameworkPaint.setShadowLayer(
                shadowRadius.toPx(),
                offsetX.toPx(),
                offsetY.toPx(),
                shadowColor
            )
            it.drawRoundRect(
                0f,
                0f,
                this.size.width,
                this.size.height,
                borderRadius.toPx(),
                borderRadius.toPx(),
                paint
            )
        }
    }
}


fun Long.convertMillisToDate(): String {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = this@convertMillisToDate
        val zoneOffset = get(Calendar.ZONE_OFFSET)
        val dstOffset = get(Calendar.DST_OFFSET)
        add(Calendar.MILLISECOND, -(zoneOffset + dstOffset))
    }
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    return sdf.format(calendar.time)
}


fun debug(message: String){
    Log.d("debug", message)
}


fun validateCategoryName(name: String, list: List<Category>): CreateCategoryErrors?{
    if(name.isBlank()) return CreateCategoryErrors.ValidationError
    if(name.contains(regex = Regex("[0-9]"))) return CreateCategoryErrors.ContainNumberError
    if(name.length > 10) return CreateCategoryErrors.ValidationError
    if(list.map { it.name }.contains(name)) return CreateCategoryErrors.DuplicateError(name)
    return null
}

fun validateTransaction(amount: Double, category: String, date: String): CreateTransactionErrors?{
    if(amount == 0.0) return CreateTransactionErrors.AmountError
    if(category == "Category") return CreateTransactionErrors.CategorySelectError
    if(date == "Select Date") return CreateTransactionErrors.DateError
    return null
}