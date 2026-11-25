package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.core.localization.AppLanguage
import com.example.core.localization.pick
import com.example.data.catalog.EditorialCatalog
import com.example.ui.state.AdminArticleEntry

private val adminAccentSwatches = listOf(
    0xFF10B981L,
    0xFF2563EBL,
    0xFFF59E0BL,
    0xFF8B5CF6L,
    0xFFEF4444L,
    0xFF0EA5E9L
)

@Composable
fun EditArticleOverrideDialog(
    entry: AdminArticleEntry,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (articleId: String, titleEn: String, titleAm: String, categoryId: String) -> Unit
) {
    var titleEn by remember(entry.article.id) { mutableStateOf(entry.article.titleEn) }
    var titleAm by remember(entry.article.id) { mutableStateOf(entry.article.titleAm) }
    var categoryId by remember(entry.article.id) { mutableStateOf(entry.article.categoryId) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(language.pick("Edit Article Metadata", "የጽሑፍ መረጃ አስተካክል"))
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = titleEn,
                    onValueChange = { titleEn = it },
                    label = { Text(language.pick("English Headline", "የእንግሊዝኛ ርዕስ")) },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = titleAm,
                    onValueChange = { titleAm = it },
                    label = { Text(language.pick("Amharic Headline", "የአማርኛ ርዕስ")) },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = language.pick("Category Beat", "ምድብ"),
                    style = MaterialTheme.typography.labelLarge
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(EditorialCatalog.categories, key = { it.id }) { cat ->
                        FilterChip(
                            selected = categoryId == cat.id,
                            onClick = { categoryId = cat.id },
                            label = { Text(language.pick(cat.titleEn, cat.titleAm)) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (titleEn.isNotBlank()) {
                        onSave(entry.article.id, titleEn, titleAm, categoryId)
                        onDismiss()
                    }
                },
                modifier = Modifier.testTag("admin_save_article_override")
            ) {
                Text(language.pick("Save", "አስቀምጥ"))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(language.pick("Cancel", "ሰርዝ"))
            }
        }
    )
}

@Composable
fun AddCustomAppDialog(
    language: AppLanguage,
    onDismiss: () -> Unit,
    onCreate: (
        name: String,
        developer: String,
        categoryId: String,
        categoryLabelEn: String,
        taglineEn: String,
        taglineAm: String,
        descriptionEn: String,
        playStoreUrl: String,
        officialUrl: String,
        accentColorHex: Long
    ) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var developer by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("apps") }
    var taglineEn by remember { mutableStateOf("") }
    var taglineAm by remember { mutableStateOf("") }
    var descriptionEn by remember { mutableStateOf("") }
    var playStoreUrl by remember { mutableStateOf("https://play.google.com/store/apps") }
    var accentHex by remember { mutableLongStateOf(0xFF10B981L) }

    val appCats = remember {
        listOf(
            "apps" to "Productivity",
            "ai" to "AI Assistant",
            "fintech" to "Fintech & Payments",
            "programming" to "Developer Tools"
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(language.pick("Add Android App", "አዲስ መተግበሪያ ጨምር")) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(language.pick("App Name *", "የመተግበሪያ ስም *")) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("admin_app_name_input")
                )
                OutlinedTextField(
                    value = developer,
                    onValueChange = { developer = it },
                    label = { Text(language.pick("Developer / Studio", "ገንቢ / ኩባንያ")) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(appCats, key = { it.first }) { (id, label) ->
                        FilterChip(
                            selected = categoryId == id,
                            onClick = { categoryId = id },
                            label = { Text(label) }
                        )
                    }
                }
                OutlinedTextField(
                    value = taglineEn,
                    onValueChange = { taglineEn = it },
                    label = { Text(language.pick("Tagline (English) *", "አጭር መግለጫ (እንግሊዝኛ) *")) },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = taglineAm,
                    onValueChange = { taglineAm = it },
                    label = { Text(language.pick("Tagline (Amharic)", "አጭር መግለጫ (አማርኛ)")) },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = descriptionEn,
                    onValueChange = { descriptionEn = it },
                    label = { Text(language.pick("Editorial Review / Notes", "ዝርዝር ግምገማ")) },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = playStoreUrl,
                    onValueChange = { playStoreUrl = it },
                    label = { Text("Play Store URL") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                AccentColorPickerRow(selectedHex = accentHex, onSelect = { accentHex = it })
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && taglineEn.isNotBlank()) {
                        val label = appCats.firstOrNull { it.first == categoryId }?.second ?: "Apps"
                        onCreate(
                            name,
                            developer,
                            categoryId,
                            label,
                            taglineEn,
                            taglineAm,
                            descriptionEn,
                            playStoreUrl,
                            playStoreUrl,
                            accentHex
                        )
                        onDismiss()
                    }
                },
                modifier = Modifier.testTag("admin_confirm_add_app")
            ) {
                Text(language.pick("Publish App", "መተግበሪያውን ጨምር"))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(language.pick("Cancel", "ሰርዝ"))
            }
        }
    )
}

@Composable
fun AddCustomAiToolDialog(
    language: AppLanguage,
    onDismiss: () -> Unit,
    onCreate: (
        name: String,
        provider: String,
        categoryId: String,
        categoryLabelEn: String,
        pricingTier: String,
        isFreeTier: Boolean,
        taglineEn: String,
        taglineAm: String,
        descriptionEn: String,
        officialUrl: String,
        accentColorHex: Long
    ) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var provider by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("productivity") }
    var pricingTier by remember { mutableStateOf("Free & Pro") }
    var isFreeTier by remember { mutableStateOf(true) }
    var taglineEn by remember { mutableStateOf("") }
    var taglineAm by remember { mutableStateOf("") }
    var officialUrl by remember { mutableStateOf("https://") }
    var accentHex by remember { mutableLongStateOf(0xFF2563EBL) }

    val aiCats = remember {
        listOf(
            "productivity" to "Productivity",
            "coding" to "Coding",
            "image_gen" to "Image Gen",
            "video_gen" to "Video Gen",
            "business" to "Business"
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(language.pick("Add AI Tool", "አዲስ የAI መሳሪያ ጨምር")) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(language.pick("AI Tool Name *", "የመሳሪያው ስም *")) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("admin_tool_name_input")
                )
                OutlinedTextField(
                    value = provider,
                    onValueChange = { provider = it },
                    label = { Text(language.pick("Provider / Company", "አቅራቢ ድርጅት")) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(aiCats, key = { it.first }) { (id, label) ->
                        FilterChip(
                            selected = categoryId == id,
                            onClick = { categoryId = id },
                            label = { Text(label) }
                        )
                    }
                }
                OutlinedTextField(
                    value = pricingTier,
                    onValueChange = { pricingTier = it },
                    label = { Text(language.pick("Pricing Tier", "የክፍያ ደረጃ")) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(language.pick("Has Free Tier Badge", "ነጻ አማራጭ አለው"), style = MaterialTheme.typography.bodyMedium)
                    Switch(checked = isFreeTier, onCheckedChange = { isFreeTier = it })
                }
                OutlinedTextField(
                    value = taglineEn,
                    onValueChange = { taglineEn = it },
                    label = { Text(language.pick("Tagline (English) *", "አጭር መግለጫ (እንግሊዝኛ) *")) },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = taglineAm,
                    onValueChange = { taglineAm = it },
                    label = { Text(language.pick("Tagline (Amharic)", "አጭር መግለጫ (አማርኛ)")) },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = officialUrl,
                    onValueChange = { officialUrl = it },
                    label = { Text("Official URL") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                AccentColorPickerRow(selectedHex = accentHex, onSelect = { accentHex = it })
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && taglineEn.isNotBlank()) {
                        val label = aiCats.firstOrNull { it.first == categoryId }?.second ?: "AI Tool"
                        onCreate(
                            name,
                            provider,
                            categoryId,
                            label,
                            pricingTier,
                            isFreeTier,
                            taglineEn,
                            taglineAm,
                            taglineEn,
                            officialUrl,
                            accentHex
                        )
                        onDismiss()
                    }
                },
                modifier = Modifier.testTag("admin_confirm_add_tool")
            ) {
                Text(language.pick("Publish AI Tool", "መሳሪያውን ጨምር"))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(language.pick("Cancel", "ሰርዝ"))
            }
        }
    )
}

@Composable
fun CreateAdCampaignDialog(
    language: AppLanguage,
    onDismiss: () -> Unit,
    onCreate: (
        placementType: String,
        sponsorName: String,
        headlineEn: String,
        headlineAm: String,
        bodyEn: String,
        ctaEn: String,
        badgeText: String,
        destinationUrl: String,
        accentColorHex: Long
    ) -> Unit
) {
    var placementType by remember { mutableStateOf("native") }
    var sponsorName by remember { mutableStateOf("") }
    var headlineEn by remember { mutableStateOf("") }
    var headlineAm by remember { mutableStateOf("") }
    var bodyEn by remember { mutableStateOf("") }
    var ctaEn by remember { mutableStateOf("Learn More") }
    var badgeText by remember { mutableStateOf("SPONSORED PARTNER") }
    var destinationUrl by remember { mutableStateOf("https://") }
    var accentHex by remember { mutableLongStateOf(0xFFF59E0BL) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(language.pick("Launch Ad Campaign", "አዲስ የማስታወቂያ ዘመቻ ፍጠር")) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = placementType == "native",
                        onClick = { placementType = "native" },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                    ) {
                        Text(language.pick("Native Feed Card", "የፊድ ካርድ"))
                    }
                    SegmentedButton(
                        selected = placementType == "banner",
                        onClick = { placementType = "banner" },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                    ) {
                        Text(language.pick("Sticky Banner", "ባነር"))
                    }
                }
                OutlinedTextField(
                    value = sponsorName,
                    onValueChange = { sponsorName = it },
                    label = { Text(language.pick("Sponsor Name *", "የአስተዋዋቂ ስም *")) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("admin_ad_sponsor_input")
                )
                OutlinedTextField(
                    value = headlineEn,
                    onValueChange = { headlineEn = it },
                    label = { Text(language.pick("Offer / Headline (English) *", "ዋና ጽሑፍ (እንግሊዝኛ) *")) },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = headlineAm,
                    onValueChange = { headlineAm = it },
                    label = { Text(language.pick("Offer / Headline (Amharic)", "ዋና ጽሑፍ (አማርኛ)")) },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                if (placementType == "native") {
                    OutlinedTextField(
                        value = bodyEn,
                        onValueChange = { bodyEn = it },
                        label = { Text(language.pick("Body Description", "ዝርዝር መግለጫ")) },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = badgeText,
                        onValueChange = { badgeText = it },
                        label = { Text(language.pick("Partner Badge", "መለያ ባጅ")) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                OutlinedTextField(
                    value = ctaEn,
                    onValueChange = { ctaEn = it },
                    label = { Text(language.pick("Button CTA Label", "የቁልፍ ጽሑፍ")) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = destinationUrl,
                    onValueChange = { destinationUrl = it },
                    label = { Text("Destination URL") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                AccentColorPickerRow(selectedHex = accentHex, onSelect = { accentHex = it })
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (sponsorName.isNotBlank() && headlineEn.isNotBlank()) {
                        onCreate(
                            placementType,
                            sponsorName,
                            headlineEn,
                            headlineAm,
                            bodyEn,
                            ctaEn,
                            badgeText,
                            destinationUrl,
                            accentHex
                        )
                        onDismiss()
                    }
                },
                modifier = Modifier.testTag("admin_confirm_add_ad")
            ) {
                Text(language.pick("Launch Live", "አሁን አስጀምር"))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(language.pick("Cancel", "ሰርዝ"))
            }
        }
    )
}

@Composable
fun EditAdMobUnitsDialog(
    currentBannerId: String,
    currentNativeId: String,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (bannerUnitId: String, nativeUnitId: String) -> Unit
) {
    var bannerUnit by remember(currentBannerId) { mutableStateOf(currentBannerId) }
    var nativeUnit by remember(currentNativeId) { mutableStateOf(currentNativeId) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(language.pick("AdMob Ad Unit Setup", "የAdMob ማስታወቂያ መለያዎች")) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = language.pick(
                        "Enter production AdMob Banner & Native unit IDs for Play Store monetization.",
                        "ለGoogle Play ህትመት የAdMob ባነር እና ኔቲቭ መለያዎችን ያስቀምጡ።"
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedTextField(
                    value = bannerUnit,
                    onValueChange = { bannerUnit = it },
                    label = { Text("Banner Ad Unit ID") },
                    placeholder = { Text("ca-app-pub-xxxxxxxxxxxxxxxx/xxxxxxxxxx") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = nativeUnit,
                    onValueChange = { nativeUnit = it },
                    label = { Text("Native Ad Unit ID") },
                    placeholder = { Text("ca-app-pub-xxxxxxxxxxxxxxxx/xxxxxxxxxx") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(bannerUnit, nativeUnit)
                    onDismiss()
                }
            ) {
                Text(language.pick("Save", "አስቀምጥ"))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(language.pick("Cancel", "ሰርዝ"))
            }
        }
    )
}

@Composable
private fun AccentColorPickerRow(
    selectedHex: Long,
    onSelect: (Long) -> Unit
) {
    Column {
        Text("Brand Accent Color", style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            adminAccentSwatches.forEach { hex ->
                val isSelected = selectedHex == hex
                Surface(
                    shape = CircleShape,
                    color = Color(hex),
                    border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface) else null,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .clickable { onSelect(hex) }
                ) {
                    if (isSelected) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
