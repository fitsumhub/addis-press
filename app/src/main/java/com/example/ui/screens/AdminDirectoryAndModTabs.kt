package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.core.localization.AppLanguage
import com.example.core.localization.pick
import com.example.data.local.ReportEntity
import com.example.ui.components.EmptyEditorialState
import com.example.ui.components.SectionHeaderRow
import com.example.ui.state.AdminAdItem
import com.example.ui.state.AdminAiToolEntry
import com.example.ui.state.AdminAppEntry
import com.example.ui.state.MainDashboardState
import com.example.ui.theme.EmeraldAccent
import com.example.ui.theme.WarmAmber
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdminDirectoryTab(
    state: MainDashboardState,
    language: AppLanguage,
    onOpenAddApp: () -> Unit,
    onOpenAddTool: () -> Unit,
    onToggleAppHidden: (AdminAppEntry) -> Unit,
    onToggleAppFeatured: (AdminAppEntry) -> Unit,
    onToggleAppTrending: (AdminAppEntry) -> Unit,
    onDeleteCustomApp: (String) -> Unit,
    onToggleToolHidden: (AdminAiToolEntry) -> Unit,
    onToggleToolFeatured: (AdminAiToolEntry) -> Unit,
    onDeleteCustomTool: (String) -> Unit
) {
    var directoryType by remember { mutableIntStateOf(0) } // 0 = Apps, 1 = AI Tools

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = directoryType == 0,
                    onClick = { directoryType = 0 },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                    modifier = Modifier.testTag("admin_dir_apps")
                ) {
                    Text(language.pick("Android Apps (${state.adminApps.size})", "መተግበሪያዎች (${state.adminApps.size})"))
                }
                SegmentedButton(
                    selected = directoryType == 1,
                    onClick = { directoryType = 1 },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                    modifier = Modifier.testTag("admin_dir_tools")
                ) {
                    Text(language.pick("AI Tools (${state.adminAiTools.size})", "የAI መሳሪያዎች (${state.adminAiTools.size})"))
                }
            }
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = { if (directoryType == 0) onOpenAddApp() else onOpenAddTool() },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("admin_add_directory_item")
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    if (directoryType == 0) {
                        language.pick("Add New Android App", "አዲስ የአንድሮይድ መተግበሪያ ጨምር")
                    } else {
                        language.pick("Add New AI Tool", "አዲስ የAI መሳሪያ ጨምር")
                    }
                )
            }
        }

        if (directoryType == 0) {
            items(state.adminApps, key = { it.app.id }) { entry ->
                val app = entry.app
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(
                        1.dp,
                        if (entry.isHidden) MaterialTheme.colorScheme.error.copy(alpha = 0.45f)
                        else MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = app.accentColor.copy(alpha = 0.16f),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = app.name.take(2).uppercase(),
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = app.accentColor
                                    )
                                }
                            }
                            Spacer(Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = app.name,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    if (entry.isCustom) {
                                        Spacer(Modifier.width(6.dp))
                                        AdminStatusPill("CUSTOM", MaterialTheme.colorScheme.primary)
                                    }
                                }
                                Text(
                                    text = "${app.developer} · ${app.categoryLabelEn}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (entry.isCustom) {
                                IconButton(
                                    onClick = { onDeleteCustomApp(app.id) },
                                    modifier = Modifier.minimumInteractiveComponentSize()
                                ) {
                                    Icon(
                                        Icons.Rounded.DeleteOutline,
                                        contentDescription = language.pick("Delete", "ሰርዝ"),
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FilterChip(
                                selected = !entry.isHidden,
                                onClick = { onToggleAppHidden(entry) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (entry.isHidden) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                label = { Text(if (entry.isHidden) language.pick("Hidden", "ተደብቋል") else language.pick("Visible", "ይታያል")) }
                            )
                            FilterChip(
                                selected = app.isFeatured,
                                onClick = { onToggleAppFeatured(entry) },
                                label = { Text(language.pick("Featured", "ተመራጭ")) }
                            )
                            FilterChip(
                                selected = app.isTrending,
                                onClick = { onToggleAppTrending(entry) },
                                label = { Text(language.pick("Trending", "ተወዳጅ")) }
                            )
                        }
                    }
                }
            }
        } else {
            items(state.adminAiTools, key = { it.tool.id }) { entry ->
                val tool = entry.tool
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(
                        1.dp,
                        if (entry.isHidden) MaterialTheme.colorScheme.error.copy(alpha = 0.45f)
                        else MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = tool.accentColor.copy(alpha = 0.16f),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = tool.name.take(2).uppercase(),
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = tool.accentColor
                                    )
                                }
                            }
                            Spacer(Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = tool.name,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    if (entry.isCustom) {
                                        Spacer(Modifier.width(6.dp))
                                        AdminStatusPill("CUSTOM", MaterialTheme.colorScheme.primary)
                                    }
                                }
                                Text(
                                    text = "${tool.provider} · ${tool.pricingTier}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (entry.isCustom) {
                                IconButton(
                                    onClick = { onDeleteCustomTool(tool.id) },
                                    modifier = Modifier.minimumInteractiveComponentSize()
                                ) {
                                    Icon(
                                        Icons.Rounded.DeleteOutline,
                                        contentDescription = language.pick("Delete", "ሰርዝ"),
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FilterChip(
                                selected = !entry.isHidden,
                                onClick = { onToggleToolHidden(entry) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (entry.isHidden) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                label = { Text(if (entry.isHidden) language.pick("Hidden", "ተደብቋል") else language.pick("Visible", "ይታያል")) }
                            )
                            FilterChip(
                                selected = tool.isFeatured,
                                onClick = { onToggleToolFeatured(entry) },
                                label = { Text(language.pick("Featured", "ተመራጭ")) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminAdsRevenueTab(
    state: MainDashboardState,
    language: AppLanguage,
    onUpdateMonetization: (Boolean, Boolean, Boolean, String, String) -> Unit,
    onOpenAdMobDialog: () -> Unit,
    onOpenCreateCampaign: () -> Unit,
    onToggleAdActive: (AdminAdItem) -> Unit,
    onDeleteCampaign: (String) -> Unit
) {
    val prefs = state.preferences
    val ctr = if (prefs.trackedAdImpressions > 0) {
        (prefs.trackedAdClicks.toFloat() / prefs.trackedAdImpressions.toFloat()) * 100f
    } else 0f

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, WarmAmber.copy(alpha = 0.45f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Campaign, contentDescription = null, tint = WarmAmber)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = language.pick("Ad Revenue & Delivery Controls", "የማስታወቂያ ገቢ እና ስርጭት መቆጣጠሪያ"),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AdminMetricBox(
                            label = language.pick("Native Live", "ንቁ ኔቲቭ"),
                            value = state.activeNativeAds.size.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        AdminMetricBox(
                            label = language.pick("Impressions", "እይታዎች"),
                            value = prefs.trackedAdImpressions.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        AdminMetricBox(
                            label = "CTR",
                            value = String.format(Locale.US, "%.1f%%", ctr),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    AdminSwitchRow(
                        label = language.pick("Global In-App Ads", "ሁሉንም ማስታወቂያዎች አብራ"),
                        checked = prefs.adsEnabled,
                        onCheckedChange = {
                            onUpdateMonetization(it, prefs.highYieldAds, prefs.bannerAdsEnabled, prefs.admobBannerUnitId, prefs.admobNativeUnitId)
                        }
                    )
                    AdminSwitchRow(
                        label = language.pick("High-Yield Frequency (Every 3 rows)", "ከፍተኛ ማስታወቂያ (በየ 3 ረድፍ)"),
                        checked = prefs.highYieldAds,
                        onCheckedChange = {
                            onUpdateMonetization(prefs.adsEnabled, it, prefs.bannerAdsEnabled, prefs.admobBannerUnitId, prefs.admobNativeUnitId)
                        }
                    )
                    AdminSwitchRow(
                        label = language.pick("Sticky Bottom Banner Bars", "የታችኛው ባነር ማስታወቂያ"),
                        checked = prefs.bannerAdsEnabled,
                        onCheckedChange = {
                            onUpdateMonetization(prefs.adsEnabled, prefs.highYieldAds, it, prefs.admobBannerUnitId, prefs.admobNativeUnitId)
                        }
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = onOpenAdMobDialog,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).height(46.dp)
                        ) {
                            Text(
                                text = if (prefs.admobBannerUnitId.isBlank()) "AdMob IDs" else "AdMob Ready",
                                color = if (prefs.admobBannerUnitId.isNotBlank()) EmeraldAccent else MaterialTheme.colorScheme.primary
                            )
                        }
                        Button(
                            onClick = onOpenCreateCampaign,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1.3f)
                                .height(46.dp)
                                .testTag("admin_new_ad_campaign")
                        ) {
                            Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(language.pick("New Campaign", "አዲስ ዘመቻ"))
                        }
                    }
                }
            }
        }

        item {
            SectionHeaderRow(
                title = language.pick("Active & Paused Ad Inventory", "የማስታወቂያ ክምችት"),
                trailingActionLabel = "${state.adminAdUnits.count { it.isActive }}/${state.adminAdUnits.size}"
            )
        }

        items(state.adminAdUnits, key = { it.id }) { item ->
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(
                    1.dp,
                    if (item.isActive) item.accentColor.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outlineVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            AdminStatusPill(
                                text = item.placementType.uppercase(),
                                color = if (item.placementType == "native") MaterialTheme.colorScheme.primary else WarmAmber
                            )
                            if (item.isCustom) {
                                AdminStatusPill("CUSTOM", EmeraldAccent)
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = item.sponsorName,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = item.headlineEn,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (item.isCustom) {
                        IconButton(
                            onClick = { onDeleteCampaign(item.id) },
                            modifier = Modifier.minimumInteractiveComponentSize()
                        ) {
                            Icon(
                                Icons.Rounded.DeleteOutline,
                                contentDescription = language.pick("Delete", "ሰርዝ"),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    Switch(
                        checked = item.isActive,
                        onCheckedChange = { onToggleAdActive(item) },
                        modifier = Modifier.testTag("admin_ad_switch_${item.id}")
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdminModerationTab(
    state: MainDashboardState,
    language: AppLanguage,
    onHideReportedArticle: (ReportEntity) -> Unit,
    onDismissReport: (String) -> Unit,
    onUpdateSubmissionStatus: (String, String) -> Unit,
    onDeleteSubmission: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SectionHeaderRow(
                title = language.pick("User Reports Queue", "የተጠቃሚ ሪፖርቶች"),
                trailingActionLabel = "${state.reports.size}"
            )
        }

        if (state.reports.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = EmeraldAccent)
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = language.pick(
                                "All clear — no pending reader reports.",
                                "ምንም ያልተፈታ የተጠቃሚ ሪፖርት የለም።"
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(state.reports, key = { it.id }) { report ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.45f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Flag, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "${report.targetType.uppercase()} · ${report.targetId}",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = report.reason,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                        if (report.details.isNotBlank()) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = report.details,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { onHideReportedArticle(report) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(language.pick("Hide Content", "ይዘቱን ደብቅ"))
                            }
                            OutlinedButton(
                                onClick = { onDismissReport(report.id) },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(language.pick("Dismiss", "አጥፋ"))
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(6.dp))
            SectionHeaderRow(
                title = language.pick("Community Stories Workflow", "የተጠቃሚ ጽሑፎች ቁጥጥር"),
                trailingActionLabel = "${state.submissions.size}"
            )
        }

        if (state.submissions.isEmpty()) {
            item {
                EmptyEditorialState(
                    title = language.pick("No community submissions yet", "ገና የተላኩ ጽሑፎች የሉም"),
                    subtitle = language.pick(
                        "Stories authored in Editorial Studio appear here for status approval and publishing.",
                        "በEditorial Studio የተጻፉ ጽሑፎች ለህትመት ፈቃድ እዚህ ይታያሉ።"
                    )
                )
            }
        } else {
            items(state.submissions, key = { it.id }) { sub ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${sub.category.uppercase()} · ${sub.authorName}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = language.pick(sub.titleEn, sub.titleAm),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            IconButton(
                                onClick = { onDeleteSubmission(sub.id) },
                                modifier = Modifier.minimumInteractiveComponentSize()
                            ) {
                                Icon(
                                    Icons.Rounded.DeleteOutline,
                                    contentDescription = language.pick("Delete", "ሰርዝ"),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("draft", "review", "published", "archived").forEach { status ->
                                FilterChip(
                                    selected = sub.status == status,
                                    onClick = { onUpdateSubmissionStatus(sub.id, status) },
                                    label = { Text(status.replaceFirstChar { it.uppercase() }) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminStatusPill(text: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.16f),
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = color,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun AdminMetricBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
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
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun AdminSwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f).padding(end = 12.dp)
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
