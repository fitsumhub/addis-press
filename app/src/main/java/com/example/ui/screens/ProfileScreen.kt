package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.core.localization.AppLanguage
import com.example.core.localization.AppStrings
import com.example.core.localization.pick
import com.example.ui.state.MainDashboardState
import com.example.ui.theme.EmeraldAccent
import com.example.ui.theme.ThemeMode
import com.example.ui.theme.WarmAmber

@Composable
fun ProfileScreen(
    state: MainDashboardState,
    onUpdateDisplayName: (String) -> Unit,
    onThemeModeChange: (ThemeMode) -> Unit,
    onLanguageChange: (AppLanguage) -> Unit,
    onUpdateNotificationSettings: (Boolean, Boolean, Boolean) -> Unit,
    onUpdateMonetizationSettings: (Boolean, Boolean, Boolean, String, String) -> Unit,
    onOpenStudio: () -> Unit,
    onOpenAdmin: () -> Unit,
    onClearAllLocalData: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = state.language
    val context = LocalContext.current

    var showEditNameDialog by remember { mutableStateOf(false) }
    var showAdMobDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showClearConfirm by remember { mutableStateOf(false) }

    val prefs = state.preferences
    val displayName = prefs.displayName.ifBlank { "Reader" }
    val notifEnabled = prefs.notificationsEnabled
    val breakingEnabled = prefs.breakingAlertsEnabled
    val aiDigestEnabled = prefs.aiDigestEnabled

    val ctrPercent = if (prefs.trackedAdImpressions > 0) {
        (prefs.trackedAdClicks.toFloat() / prefs.trackedAdImpressions.toFloat()) * 100f
    } else 0f

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 104.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = AppStrings.profileTitle(lang),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Reader Profile Card (editable locally, no sign-in required)
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = displayName.take(1).uppercase(),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = displayName,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = lang.pick(
                                "Local reader profile · No sign-in needed",
                                "የግል አንባቢ መገለጫ · መግባት አያስፈልግም"
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "${state.bookmarks.size} ${AppStrings.bookmarksTab(lang)} · ${state.followedCategoryIds.size} ${AppStrings.subscribedTopics(lang)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = { showEditNameDialog = true },
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("edit_profile_name_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = lang.pick("Edit name", "ስም አስተካክል"),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }

        // Admin Control Center Entry Card
        item {
            Surface(
                onClick = onOpenAdmin,
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_admin_dashboard")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.AdminPanelSettings,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = lang.pick("Admin Control Center", "የአስተዳደር ዳሽቦርድ"),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = lang.pick(
                                "${state.articles.size} live articles · ${state.activeNativeAds.size} active ads · ${state.reports.size} reports",
                                "${state.articles.size} ንቁ ጽሑፎች · ${state.activeNativeAds.size} ማስታወቂያዎች · ${state.reports.size} ሪፖርቶች"
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Monetization & Ads Manager Card
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, WarmAmber.copy(alpha = 0.45f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Campaign,
                            contentDescription = null,
                            tint = WarmAmber,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = lang.pick("Monetization & Ads", "ማስታወቂያ እና ገቢ"),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MetricMiniTile(
                            label = lang.pick("Impressions", "እይታዎች"),
                            value = prefs.trackedAdImpressions.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        MetricMiniTile(
                            label = lang.pick("Clicks", "ጠቅታዎች"),
                            value = prefs.trackedAdClicks.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        MetricMiniTile(
                            label = "CTR",
                            value = String.format(java.util.Locale.US, "%.1f%%", ctrPercent),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    NotificationToggleRow(
                        label = lang.pick("Enable In-App Ads", "ማስታወቂያዎችን አሳይ"),
                        checked = prefs.adsEnabled,
                        onCheckedChange = {
                            onUpdateMonetizationSettings(
                                it,
                                prefs.highYieldAds,
                                prefs.bannerAdsEnabled,
                                prefs.admobBannerUnitId,
                                prefs.admobNativeUnitId
                            )
                        }
                    )
                    NotificationToggleRow(
                        label = lang.pick("High-Yield Ad Frequency", "ከፍተኛ የማስታወቂያ ብዛት"),
                        checked = prefs.highYieldAds,
                        onCheckedChange = {
                            onUpdateMonetizationSettings(
                                prefs.adsEnabled,
                                it,
                                prefs.bannerAdsEnabled,
                                prefs.admobBannerUnitId,
                                prefs.admobNativeUnitId
                            )
                        }
                    )
                    NotificationToggleRow(
                        label = lang.pick("Sticky Bottom Banner Ads", "የታችኛው ባነር ማስታወቂያ"),
                        checked = prefs.bannerAdsEnabled,
                        onCheckedChange = {
                            onUpdateMonetizationSettings(
                                prefs.adsEnabled,
                                prefs.highYieldAds,
                                it,
                                prefs.admobBannerUnitId,
                                prefs.admobNativeUnitId
                            )
                        }
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { showAdMobDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("configure_admob_button")
                    ) {
                        Text(
                            text = if (prefs.admobBannerUnitId.isBlank() && prefs.admobNativeUnitId.isBlank()) {
                                lang.pick("Configure AdMob Unit IDs", "የAdMob መለያዎችን ያስገቡ")
                            } else {
                                lang.pick("AdMob Units Configured", "የAdMob መለያዎች ተዘጋጅተዋል")
                            },
                            color = if (prefs.admobBannerUnitId.isNotBlank()) EmeraldAccent else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Appearance (Light / Dark / Auto)
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AppStrings.appearance(lang),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(Modifier.height(12.dp))
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        val modes = listOf(
                            ThemeMode.LIGHT to AppStrings.lightMode(lang),
                            ThemeMode.DARK to AppStrings.darkMode(lang),
                            ThemeMode.AUTO to AppStrings.autoMode(lang)
                        )
                        modes.forEachIndexed { idx, (mode, label) ->
                            SegmentedButton(
                                selected = prefs.themeMode == mode,
                                onClick = { onThemeModeChange(mode) },
                                shape = SegmentedButtonDefaults.itemShape(index = idx, count = modes.size),
                                modifier = Modifier.testTag("theme_mode_${mode.storageKey}")
                            ) {
                                Text(label)
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = lang.pick(
                            "Auto follows your phone's system setting.",
                            "ራስ-ሰር የስልክዎን ስርዓት ቅንብር ይከተላል።"
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Language Selector (English / Amharic)
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Language,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = AppStrings.languageLabel(lang),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        AppLanguage.entries.forEachIndexed { idx, appLang ->
                            SegmentedButton(
                                selected = lang == appLang,
                                onClick = { onLanguageChange(appLang) },
                                shape = SegmentedButtonDefaults.itemShape(index = idx, count = 2),
                                modifier = Modifier.testTag("profile_lang_${appLang.code}")
                            ) {
                                Text(appLang.nativeName)
                            }
                        }
                    }
                }
            }
        }

        // Notification Preferences
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.NotificationsActive,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = AppStrings.notifications(lang),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    NotificationToggleRow(
                        label = lang.pick("Push Notifications", "ማሳወቂያዎች"),
                        checked = notifEnabled,
                        onCheckedChange = { onUpdateNotificationSettings(it, breakingEnabled, aiDigestEnabled) }
                    )
                    NotificationToggleRow(
                        label = lang.pick("Breaking Tech News", "ሰበር የቴክኖሎጂ ዜናዎች"),
                        checked = breakingEnabled,
                        onCheckedChange = { onUpdateNotificationSettings(notifEnabled, it, aiDigestEnabled) }
                    )
                    NotificationToggleRow(
                        label = lang.pick("Weekly AI Tools Digest", "ሳምንታዊ የAI መሳሪያዎች ማጠቃለያ"),
                        checked = aiDigestEnabled,
                        onCheckedChange = { onUpdateNotificationSettings(notifEnabled, breakingEnabled, it) }
                    )
                }
            }
        }

        // Editorial Studio & App Actions
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    ProfileActionRow(
                        icon = Icons.Rounded.EditNote,
                        label = "${AppStrings.editorialStudio(lang)} (${state.submissions.size})",
                        onClick = onOpenStudio,
                        modifier = Modifier.testTag("profile_editorial_studio")
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    ProfileActionRow(
                        icon = Icons.Rounded.Share,
                        label = lang.pick("Share AddisPress", "AddisPressን ያጋሩ"),
                        onClick = {
                            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Discover Ethiopian tech news, AI guides, and app reviews on AddisPress!"
                                )
                            }
                            context.startActivity(Intent.createChooser(sendIntent, null))
                        }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    ProfileActionRow(
                        icon = Icons.Rounded.Security,
                        label = lang.pick("Privacy Policy", "የግላዊነት ፖሊሲ"),
                        onClick = { showPrivacyDialog = true }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    ProfileActionRow(
                        icon = Icons.Rounded.Info,
                        label = lang.pick("About AddisPress", "ስለ AddisPress"),
                        onClick = { showAboutDialog = true }
                    )
                }
            }
        }

        // Clear Local Data
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                ProfileActionRow(
                    icon = Icons.Rounded.DeleteForever,
                    label = AppStrings.clearSavedData(lang),
                    isDestructive = true,
                    onClick = { showClearConfirm = true },
                    modifier = Modifier.testTag("clear_saved_data_button")
                )
            }
        }
    }

    if (showEditNameDialog) {
        var nameInput by remember(displayName) { mutableStateOf(displayName) }
        AlertDialog(
            onDismissRequest = { showEditNameDialog = false },
            title = { Text(lang.pick("Reader Name", "የአንባቢ ስም")) },
            text = {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text(lang.pick("Display name", "የሚታይ ስም")) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (nameInput.isNotBlank()) {
                            onUpdateDisplayName(nameInput)
                            showEditNameDialog = false
                        }
                    }
                ) {
                    Text(lang.pick("Save", "አስቀምጥ"))
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditNameDialog = false }) {
                    Text(lang.pick("Cancel", "ሰርዝ"))
                }
            }
        )
    }

    if (showAdMobDialog) {
        var bannerUnit by remember(prefs.admobBannerUnitId) { mutableStateOf(prefs.admobBannerUnitId) }
        var nativeUnit by remember(prefs.admobNativeUnitId) { mutableStateOf(prefs.admobNativeUnitId) }
        AlertDialog(
            onDismissRequest = { showAdMobDialog = false },
            title = { Text(lang.pick("AdMob Ad Unit Setup", "የAdMob ማስታወቂያ መለያዎች")) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = lang.pick(
                            "Enter your production AdMob Banner & Native unit IDs to keep your monetization configuration ready for Play Store release.",
                            "ለGoogle Play ህትመት የAdMob ባነር እና ኔቲቭ መለያዎችን እዚህ ያስቀምጡ።"
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
                        onUpdateMonetizationSettings(
                            prefs.adsEnabled,
                            prefs.highYieldAds,
                            prefs.bannerAdsEnabled,
                            bannerUnit,
                            nativeUnit
                        )
                        showAdMobDialog = false
                    }
                ) {
                    Text(lang.pick("Save", "አስቀምጥ"))
                }
            },
            dismissButton = {
                TextButton(onClick = { showAdMobDialog = false }) {
                    Text(lang.pick("Cancel", "ሰርዝ"))
                }
            }
        )
    }

    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text(lang.pick("Privacy Policy", "የግላዊነት ፖሊሲ")) },
            text = {
                Text(
                    lang.pick(
                        "AddisPress keeps your preferred language, followed topics, bookmarks, reading history, and editorial drafts strictly on your device. No sign-in or personal account is required.",
                        "AddisPress የእርስዎን የቋንቋ ምርጫ፣ የተከተሏቸውን ርዕሶች፣ ዕልባቶች እና የንባብ ታሪክ በስልክዎ ላይ ብቻ ያስቀምጣል። መግባት ወይም የግል መለያ አያስፈልግም።"
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text(lang.pick("Close", "ዝጋ"))
                }
            }
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("AddisPress v1.0") },
            text = {
                Text(
                    lang.pick(
                        "An original Ethiopian digital media, technology journalism, AI tools, and Android app discovery platform supporting English and Amharic.",
                        "በእንግሊዝኛ እና በአማርኛ የሚቀርብ የኢትዮጵያ የቴክኖሎጂ ዜና፣ የAI መመሪያ እና የመተግበሪያ መገኛ መድረክ።"
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text(lang.pick("OK", "እሺ"))
                }
            }
        )
    }

    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            title = { Text(AppStrings.clearSavedData(lang)) },
            text = {
                Text(
                    lang.pick(
                        "This removes all saved bookmarks, reading history, liked stories, drafts, and local preferences from this device.",
                        "ይህ የተቀመጡ ዕልባቶችን፣ የንባብ ታሪክን፣ የወደዷቸውን ጽሑፎች እና ቅንብሮችን ከስልክዎ ላይ ያጸዳል።"
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showClearConfirm = false
                        onClearAllLocalData()
                    }
                ) {
                    Text(lang.pick("Clear", "አጽዳ"), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirm = false }) {
                    Text(lang.pick("Cancel", "ሰርዝ"))
                }
            }
        )
    }
}

@Composable
private fun MetricMiniTile(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun NotificationToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun ProfileActionRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDestructive: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
