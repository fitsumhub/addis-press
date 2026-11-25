package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.core.localization.AppLanguage
import com.example.core.localization.pick
import com.example.data.model.BannerAdUnit
import com.example.data.model.NativeAdUnit
import com.example.ui.theme.WarmAmber

@Composable
fun NativeFeedAdCard(
    ad: NativeAdUnit,
    language: AppLanguage,
    onImpression: () -> Unit,
    onAdClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showAdInfo by remember { mutableStateOf(false) }

    LaunchedEffect(ad.id) {
        onImpression()
    }

    fun openSponsorLink() {
        onAdClicked()
        runCatching {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ad.destinationUrl)))
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("native_ad_${ad.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Clear Ad disclosure header (Play Policy compliant)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = WarmAmber,
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text(
                            text = language.pick("Ad", "ማስታወቂያ"),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = language.pick("Advertisement", "የተከፈለበት ማስታወቂያ"),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(
                    onClick = { showAdInfo = true },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = language.pick("Ad info", "የማስታወቂያ መረጃ"),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // Sponsor Identity Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Brush.linearGradient(listOf(ad.gradientStart, ad.gradientEnd))),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ad.iconInitials,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = Color.White
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = ad.sponsorName,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = language.pick(ad.headlineEn, ad.headlineAm),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Rich Gradient Promo Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(132.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.linearGradient(listOf(ad.gradientStart, ad.gradientEnd)))
                    .clickable { openSponsorLink() }
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.CenterStart),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        color = Color.White.copy(alpha = 0.20f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = ad.badgeText,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                    }
                    Text(
                        text = ad.sponsorName,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = language.pick(ad.bodyEn, ad.bodyAm),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.92f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Full-width CTA button
            Button(
                onClick = { openSponsorLink() },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WarmAmber,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("native_ad_cta_${ad.id}")
            ) {
                Text(
                    text = language.pick(ad.ctaEn, ad.ctaAm),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Rounded.OpenInNew,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }

    if (showAdInfo) {
        AdInfoDialog(
            sponsorName = ad.sponsorName,
            language = language,
            onDismiss = { showAdInfo = false }
        )
    }
}

@Composable
fun StickyBannerAdBar(
    ad: BannerAdUnit,
    language: AppLanguage,
    onImpression: () -> Unit,
    onAdClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showAdInfo by remember { mutableStateOf(false) }

    LaunchedEffect(ad.id) {
        onImpression()
    }

    fun openSponsorLink() {
        onAdClicked()
        runCatching {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ad.destinationUrl)))
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("sticky_banner_ad_${ad.id}"),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { openSponsorLink() }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { showAdInfo = true },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = language.pick("Ad info", "የማስታወቂያ መረጃ"),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(15.dp)
                )
            }
            Spacer(Modifier.width(4.dp))
            Surface(
                color = WarmAmber,
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "Ad",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ad.sponsorName,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = language.pick(ad.offerEn, ad.offerAm),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = { openSponsorLink() },
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ad.accentColor,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .height(38.dp)
                    .minimumInteractiveComponentSize()
            ) {
                Text(
                    text = language.pick(ad.ctaEn, ad.ctaAm),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }

    if (showAdInfo) {
        AdInfoDialog(
            sponsorName = ad.sponsorName,
            language = language,
            onDismiss = { showAdInfo = false }
        )
    }
}

@Composable
private fun AdInfoDialog(
    sponsorName: String,
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(language.pick("Sponsored Placement", "የማስታወቂያ መረጃ")) },
        text = {
            Text(
                language.pick(
                    "Ads from partners like $sponsorName keep AddisPress free to read. Publishers can configure AdMob Ad Unit IDs and ad frequency anytime in Profile -> Monetization & Ads.",
                    "እንደ $sponsorName ያሉ ማስታወቂያዎች AddisPressን በነጻ ለማንበብ ያግዛሉ። በመገለጫ -> የማስታወቂያ ቅንብሮች ውስጥ የAdMob መለያዎችን ማስተካከል ይችላሉ።"
                )
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(language.pick("OK", "እሺ"))
            }
        }
    )
}
