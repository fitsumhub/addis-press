package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val PlusJakartaSansFamily = FontFamily(
    Font(R.font.plus_jakarta_sans, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans, FontWeight.Bold)
)

val SpaceGroteskFamily = FontFamily(
    Font(R.font.space_grotesk, FontWeight.Medium),
    Font(R.font.space_grotesk, FontWeight.Bold)
)

val EthiopicSansFamily = FontFamily(
    Font(R.font.noto_sans_ethiopic, FontWeight.Normal),
    Font(R.font.noto_sans_ethiopic, FontWeight.Bold)
)

fun appBodyFont(isAmharic: Boolean): FontFamily =
    if (isAmharic) EthiopicSansFamily else PlusJakartaSansFamily

fun appDisplayFont(isAmharic: Boolean): FontFamily =
    if (isAmharic) EthiopicSansFamily else SpaceGroteskFamily

fun createAppTypography(isAmharic: Boolean): Typography {
    val display = appDisplayFont(isAmharic)
    val body = appBodyFont(isAmharic)
    return Typography(
        displaySmall = TextStyle(
            fontFamily = display,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            lineHeight = 36.sp,
            letterSpacing = (-0.5).sp
        ),
        headlineMedium = TextStyle(
            fontFamily = display,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 31.sp,
            letterSpacing = (-0.25).sp
        ),
        headlineSmall = TextStyle(
            fontFamily = display,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 27.sp
        ),
        titleLarge = TextStyle(
            fontFamily = body,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            lineHeight = 25.sp
        ),
        titleMedium = TextStyle(
            fontFamily = body,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 22.sp
        ),
        titleSmall = TextStyle(
            fontFamily = body,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = body,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 25.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = body,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 21.sp
        ),
        bodySmall = TextStyle(
            fontFamily = body,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 17.sp
        ),
        labelLarge = TextStyle(
            fontFamily = display,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            letterSpacing = 0.2.sp
        ),
        labelMedium = TextStyle(
            fontFamily = display,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            lineHeight = 14.sp,
            letterSpacing = 0.6.sp
        ),
        labelSmall = TextStyle(
            fontFamily = display,
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp,
            lineHeight = 13.sp,
            letterSpacing = 0.5.sp
        )
    )
}
