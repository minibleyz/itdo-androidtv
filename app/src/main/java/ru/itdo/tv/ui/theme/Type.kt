package ru.itdo.tv.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val UnboundedLike = FontFamily.SansSerif
val OnestLike = FontFamily.SansSerif

val ItdoTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = UnboundedLike,
        fontWeight = FontWeight.Black,
        fontSize = 44.sp,
        letterSpacing = 0.2.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = UnboundedLike,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = UnboundedLike,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = OnestLike,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = OnestLike,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = OnestLike,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
    ),
)
