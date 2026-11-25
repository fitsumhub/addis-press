package com.example.data.model

import androidx.compose.ui.graphics.Color

data class NativeAdUnit(
    val id: String,
    val sponsorName: String,
    val headlineEn: String,
    val headlineAm: String,
    val bodyEn: String,
    val bodyAm: String,
    val badgeText: String,
    val ctaEn: String,
    val ctaAm: String,
    val destinationUrl: String,
    val gradientStart: Color,
    val gradientEnd: Color,
    val iconInitials: String,
    val categoryTag: String
)

data class BannerAdUnit(
    val id: String,
    val sponsorName: String,
    val offerEn: String,
    val offerAm: String,
    val ctaEn: String,
    val ctaAm: String,
    val destinationUrl: String,
    val accentColor: Color
)
