package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.core.localization.AppLanguage
import com.example.core.localization.pick
import com.example.data.catalog.EditorialCatalog
import com.example.data.local.ReportEntity
import com.example.ui.components.SectionHeaderRow
import com.example.ui.state.AdminAdItem
import com.example.ui.state.AdminAiToolEntry
import com.example.ui.state.AdminAppEntry
import com.example.ui.state.AdminArticleEntry
import com.example.ui.state.MainDashboardState
import com.example.ui.theme.EmeraldAccent
import com.example.ui.theme.WarmAmber
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    state: MainDashboardState,
    onBack: () -> Unit,
    onOpenStudio: () -> Unit,
    onUpdateHomeLayout: (Boolean, Boolean, Boolean, Boolean, Int) -> Unit,
    onUpdateTickerMessage: (String, String) -> Unit,
    onToggleArticleHidden: (AdminArticleEntry) -> Unit,
    onToggleArticleBreaking: (AdminArticleEntry) -> Unit,
    onToggleArticleFeatured: (AdminArticleEntry) -> Unit,
    onToggleArticleTrending: (AdminArticleEntry) -> Unit,
    onEditArticleOverride: (String, String, String, String) -> Unit,
    onToggleAppHidden: (AdminAppEntry) -> Unit,
    onToggleAppFeatured: (AdminAppEntry) -> Unit,
    onToggleAppTrending: (AdminAppEntry) -> Unit,
    onAddCustomApp: (String, String, String, String, String, String, String, String, String, Long) -> Unit,
    onDeleteCustomApp: (String) -> Unit,
    onToggleToolHidden: (AdminAiToolEntry) -> Unit,
    onToggleToolFeatured: (AdminAiToolEntry) -> Unit,
    onAddCustomAiTool: (String, String, String, String, String, Boolean, String, String, String, String, Long) -> Unit,
    onDeleteCustomTool: (String) -> Unit,
    onUpdateMonetization: (Boolean, Boolean, Boolean, String, String) -> Unit,
    onToggleAdActive: (AdminAdItem) -> Unit,
    onCreateAdCampaign: (String, String, String, String, String, String, String, String, Long) -> Unit,
    onDeleteAdCampaign: (String) -> Unit,
    onHideReportedArticle: (ReportEntity) -> Unit,
    onDismissReport: (String) -> Unit,
    onUpdateSubmissionStatus: (String, String) -> Unit,
    onDeleteSubmission: (String) -> Unit,
    onResetAllOverrides: () -> Unit
) {
    val lang = state.language
    var selectedTab by remember { mutableIntStateOf(0) }
    var showQuickMenu by remember { mutableStateOf(false) }
    var editingArticle by remember { mutableStateOf<AdminArticleEntry?>(null) }
    var showAddAppDialog by remember { mutableStateOf(false) }
    var showAddToolDialog by remember { mutableStateOf(false) }
    var showCreateAdDialog by remember { mutableStateOf(false) }
    var showAdMobDialog by remember { mutableStateOf(false) }
    var showResetConfirm by remember { mutableStateOf(false) }

    val moderationCount = state.reports.size + state.submissions.count { it.status == "review" }

    val tabTitles = listOf(
        lang.pick("Overview", "አጠቃላይ"),
        lang.pick("Articles (${state.adminArticles.size})", "ጽሑፎች (${state.adminArticles.size})"),
        lang.pick("Apps & AI", "መተግበሪያ እና AI"),
        lang.pick("Ads & Revenue", "ማስታወቂያ"),
        if (moderationCount > 0) {
            lang.pick("Moderation ($moderationCount)", "ቁጥጥር ($moderationCount)")
        } else {
            lang.pick("Moderation", "ቁጥጥር")
        }
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("admin_back_button")
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = lang.pick("Back", "ተመለስ")
                        )
                    }
                    Icon(
                        imageVector = Icons.Rounded.AdminPanelSettings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = lang.pick("Admin Control Center", "የአስተዳደር ዳሽቦርድ"),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    AdminStatusPill(text = "LIVE", color = EmeraldAccent)
                    Spacer(Modifier.width(4.dp))
                    IconButton(
                        onClick = { showQuickMenu = true },
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("admin_quick_add_button")
                    ) {
                        Icon(
                            Icons.Rounded.Add,
                            contentDescription = lang.pick("Quick Create", "አዲስ ፍጠር")
                        )
                    }
                    DropdownMenu(
                        expanded = showQuickMenu,
                        onDismissRequest = { showQuickMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(lang.pick("Write New Article", "አዲስ ጽሑፍ ጻፍ")) },
                            onClick = {
                                showQuickMenu = false
                                onOpenStudio()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(lang.pick("Add Android App", "መተግበሪያ ጨምር")) },
                            onClick = {
                                showQuickMenu = false
                                showAddAppDialog = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(lang.pick("Add AI Tool", "የAI መሳሪያ ጨምር")) },
                            onClick = {
                                showQuickMenu = false
                                showAddToolDialog = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(lang.pick("Launch Ad Campaign", "ማስታወቂያ ፍጠር")) },
                            onClick = {
                                showQuickMenu = false
                                showCreateAdDialog = true
                            }
                        )
                    }
                }

                SecondaryScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    edgePadding = 12.dp,
                    containerColor = MaterialTheme.colorScheme.background
                ) {
                    tabTitles.forEachIndexed { idx, label ->
                        val tag = when (idx) {
                            0 -> "admin_tab_overview"
                            1 -> "admin_tab_articles"
                            2 -> "admin_tab_directory"
                            3 -> "admin_tab_ads"
                            else -> "admin_tab_moderation"
                        }
                        Tab(
                            selected = selectedTab == idx,
                            onClick = { selectedTab = idx },
                            text = {
                                Text(
                                    text = label,
                                    fontWeight = if (selectedTab == idx) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            modifier = Modifier.testTag(tag)
                        )
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> AdminOverviewTab(
                    state = state,
                    language = lang,
                    onUpdateHomeLayout = onUpdateHomeLayout,
                    onUpdateTickerMessage = onUpdateTickerMessage,
                    onOpenArticlesTab = { selectedTab = 1 },
                    onOpenDirectoryTab = { selectedTab = 2 },
                    onOpenAdsTab = { selectedTab = 3 },
                    onOpenModerationTab = { selectedTab = 4 },
                    onRequestReset = { showResetConfirm = true }
                )
                1 -> AdminArticlesTab(
                    state = state,
                    language = lang,
                    onOpenStudio = onOpenStudio,
                    onToggleHidden = onToggleArticleHidden,
                    onToggleBreaking = onToggleArticleBreaking,
                    onToggleFeatured = onToggleArticleFeatured,
                    onToggleTrending = onToggleArticleTrending,
                    onEditArticle = { editingArticle = it }
                )
                2 -> AdminDirectoryTab(
                    state = state,
                    language = lang,
                    onOpenAddApp = { showAddAppDialog = true },
                    onOpenAddTool = { showAddToolDialog = true },
                    onToggleAppHidden = onToggleAppHidden,
                    onToggleAppFeatured = onToggleAppFeatured,
                    onToggleAppTrending = onToggleAppTrending,
                    onDeleteCustomApp = onDeleteCustomApp,
                    onToggleToolHidden = onToggleToolHidden,
                    onToggleToolFeatured = onToggleToolFeatured,
                    onDeleteCustomTool = onDeleteCustomTool
                )
                3 -> AdminAdsRevenueTab(
                    state = state,
                    language = lang,
                    onUpdateMonetization = onUpdateMonetization,
                    onOpenAdMobDialog = { showAdMobDialog = true },
                    onOpenCreateCampaign = { showCreateAdDialog = true },
                    onToggleAdActive = onToggleAdActive,
                    onDeleteCampaign = onDeleteAdCampaign
                )
                4 -> AdminModerationTab(
                    state = state,
                    language = lang,
                    onHideReportedArticle = onHideReportedArticle,
                    onDismissReport = onDismissReport,
                    onUpdateSubmissionStatus = onUpdateSubmissionStatus,
                    onDeleteSubmission = onDeleteSubmission
                )
            }
        }
    }

    editingArticle?.let { target ->
        EditArticleOverrideDialog(
            entry = target,
            language = lang,
            onDismiss = { editingArticle = null },
            onSave = onEditArticleOverride
        )
    }

    if (showAddAppDialog) {
        AddCustomAppDialog(
            language = lang,
            onDismiss = { showAddAppDialog = false },
            onCreate = onAddCustomApp
        )
    }

    if (showAddToolDialog) {
        AddCustomAiToolDialog(
            language = lang,
            onDismiss = { showAddToolDialog = false },
            onCreate = onAddCustomAiTool
        )
    }

    if (showCreateAdDialog) {
        CreateAdCampaignDialog(
            language = lang,
            onDismiss = { showCreateAdDialog = false },
            onCreate = onCreateAdCampaign
        )
    }

    if (showAdMobDialog) {
        EditAdMobUnitsDialog(
            currentBannerId = state.preferences.admobBannerUnitId,
            currentNativeId = state.preferences.admobNativeUnitId,
            language = lang,
            onDismiss = { showAdMobDialog = false },
            onSave = { bannerId, nativeId ->
                val p = state.preferences
                onUpdateMonetization(p.adsEnabled, p.highYieldAds, p.bannerAdsEnabled, bannerId, nativeId)
            }
        )
    }

    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = { Text(lang.pick("Reset CMS Overrides?", "የአስተዳደር ለውጦችን ወደ ነበሩበት ይመልሱ?")) },
            text = {
                Text(
                    lang.pick(
                        "This restores default visibility, featured flags, custom apps/tools, custom ads, and home layout settings.",
                        "ይህ የተደበቁ ጽሑፎችን፣ የተጨመሩ መተግበሪያዎችን፣ ማስታወቂያዎችን እና የመነሻ ገጽ ቅንብሮችን ወደ መጀመሪያው ይመልሳል።"
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetConfirm = false
                        onResetAllOverrides()
                    }
                ) {
                    Text(lang.pick("Reset All", "ሁሉንም መልስ"), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) {
                    Text(lang.pick("Cancel", "ሰርዝ"))
                }
            }
        )
    }
}

@Composable
private fun AdminOverviewTab(
    state: MainDashboardState,
    language: AppLanguage,
    onUpdateHomeLayout: (Boolean, Boolean, Boolean, Boolean, Int) -> Unit,
    onUpdateTickerMessage: (String, String) -> Unit,
    onOpenArticlesTab: () -> Unit,
    onOpenDirectoryTab: () -> Unit,
    onOpenAdsTab: () -> Unit,
    onOpenModerationTab: () -> Unit,
    onRequestReset: () -> Unit
) {
    val prefs = state.preferences
    var tickerEn by remember(prefs.adminTickerMessageEn) { mutableStateOf(prefs.adminTickerMessageEn) }
    var tickerAm by remember(prefs.adminTickerMessageAm) { mutableStateOf(prefs.adminTickerMessageAm) }

    val liveArticles = state.articles.size
    val hiddenArticles = state.adminArticles.count { it.isHidden }
    val liveDirItems = state.applications.size + state.aiTools.size
    val ctr = if (prefs.trackedAdImpressions > 0) {
        (prefs.trackedAdClicks.toFloat() / prefs.trackedAdImpressions.toFloat()) * 100f
    } else 0f
    val modQueue = state.reports.size + state.submissions.count { it.status == "review" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SectionHeaderRow(title = language.pick("Platform Live Metrics", "የመድረኩ ቀጥታ አኃዞች"))
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminKpiCard(
                    title = language.pick("Live Articles", "ንቁ ጽሑፎች"),
                    value = "$liveArticles",
                    subtitle = language.pick("$hiddenArticles hidden", "$hiddenArticles ተደብቀዋል"),
                    onClick = onOpenArticlesTab,
                    modifier = Modifier.weight(1f)
                )
                AdminKpiCard(
                    title = language.pick("Apps & AI Tools", "መተግበሪያ እና AI"),
                    value = "$liveDirItems",
                    subtitle = "${state.applications.size} apps · ${state.aiTools.size} AI",
                    onClick = onOpenDirectoryTab,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminKpiCard(
                    title = language.pick("Ad Performance", "የማስታወቂያ አፈጻጸም"),
                    value = "${prefs.trackedAdImpressions} imp",
                    subtitle = "${prefs.trackedAdClicks} clicks · ${String.format(Locale.US, "%.1f%%", ctr)} CTR",
                    onClick = onOpenAdsTab,
                    modifier = Modifier.weight(1f)
                )
                AdminKpiCard(
                    title = language.pick("Moderation Queue", "የቁጥጥር ወረፋ"),
                    value = "$modQueue",
                    subtitle = "${state.reports.size} reports · ${state.submissions.size} stories",
                    onClick = onOpenModerationTab,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Home Section Controls
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = language.pick("Home Feed Layout Controls", "የመነሻ ገጽ ክፍሎች መቆጣጠሪያ"),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(Modifier.height(8.dp))
                    AdminSwitchRow(
                        label = language.pick("Breaking Alert Banner", "ሰበር ዜና ባነር"),
                        checked = prefs.showBreakingBanner,
                        onCheckedChange = {
                            onUpdateHomeLayout(it, prefs.showFeaturedCarousel, prefs.showAppReviewsRail, prefs.showTrendingSection, prefs.homeFeedPageSize)
                        }
                    )
                    AdminSwitchRow(
                        label = language.pick("Featured Editorial Carousel", "የተመረጡ ጽሑፎች ካሩሴል"),
                        checked = prefs.showFeaturedCarousel,
                        onCheckedChange = {
                            onUpdateHomeLayout(prefs.showBreakingBanner, it, prefs.showAppReviewsRail, prefs.showTrendingSection, prefs.homeFeedPageSize)
                        }
                    )
                    AdminSwitchRow(
                        label = language.pick("App Reviews Highlight Rail", "የመተግበሪያ ግምገማዎች መደርደሪያ"),
                        checked = prefs.showAppReviewsRail,
                        onCheckedChange = {
                            onUpdateHomeLayout(prefs.showBreakingBanner, prefs.showFeaturedCarousel, it, prefs.showTrendingSection, prefs.homeFeedPageSize)
                        }
                    )
                    AdminSwitchRow(
                        label = language.pick("Trending Stories Section", "ተወዳጅ ጽሑፎች ክፍል"),
                        checked = prefs.showTrendingSection,
                        onCheckedChange = {
                            onUpdateHomeLayout(prefs.showBreakingBanner, prefs.showFeaturedCarousel, prefs.showAppReviewsRail, it, prefs.homeFeedPageSize)
                        }
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = language.pick("Default Home Feed Page Length", "የመነሻ ገጽ ጽሑፎች ብዛት"),
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(12 to "12", 20 to "20", 28 to "28", 50 to language.pick("All", "ሁሉም")).forEach { (size, label) ->
                            FilterChip(
                                selected = prefs.homeFeedPageSize == size,
                                onClick = {
                                    onUpdateHomeLayout(
                                        prefs.showBreakingBanner,
                                        prefs.showFeaturedCarousel,
                                        prefs.showAppReviewsRail,
                                        prefs.showTrendingSection,
                                        size
                                    )
                                },
                                label = { Text(label) },
                                modifier = Modifier.testTag("admin_page_size_$size")
                            )
                        }
                    }
                }
            }
        }

        // Breaking Ticker Custom Announcement
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, WarmAmber.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Bolt, contentDescription = null, tint = WarmAmber)
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = language.pick("Breaking Alert Ticker Override", "የሰበር ዜና ማስታወቂያ መቀየሪያ"),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = tickerEn,
                        onValueChange = { tickerEn = it },
                        label = { Text(language.pick("Custom Ticker Text (English)", "ብጁ የሰበር ጽሑፍ (እንግሊዝኛ)")) },
                        placeholder = { Text(language.pick("Leave blank to use latest breaking article", "ነባሪውን ለመጠቀም ባዶ ይተዉት")) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("admin_ticker_en_input")
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = tickerAm,
                        onValueChange = { tickerAm = it },
                        label = { Text(language.pick("Custom Ticker Text (Amharic)", "ብጁ የሰበር ጽሑፍ (አማርኛ)")) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { onUpdateTickerMessage(tickerEn, tickerAm) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).testTag("admin_apply_ticker")
                        ) {
                            Text(language.pick("Apply Ticker", "አስቀምጥ"))
                        }
                        OutlinedButton(
                            onClick = {
                                tickerEn = ""
                                tickerAm = ""
                                onUpdateTickerMessage("", "")
                            },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(language.pick("Clear", "አጽዳ"))
                        }
                    }
                }
            }
        }

        item {
            OutlinedButton(
                onClick = onRequestReset,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("admin_reset_overrides_button")
            ) {
                Icon(Icons.Rounded.RestartAlt, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = language.pick("Reset All Admin CMS Overrides", "ሁሉንም የአስተዳደር ለውጦች መልስ"),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AdminArticlesTab(
    state: MainDashboardState,
    language: AppLanguage,
    onOpenStudio: () -> Unit,
    onToggleHidden: (AdminArticleEntry) -> Unit,
    onToggleBreaking: (AdminArticleEntry) -> Unit,
    onToggleFeatured: (AdminArticleEntry) -> Unit,
    onToggleTrending: (AdminArticleEntry) -> Unit,
    onEditArticle: (AdminArticleEntry) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf("all") }

    val filtered = remember(state.adminArticles, query, filter) {
        val q = query.trim().lowercase()
        state.adminArticles.filter { entry ->
            val matchesFilter = when (filter) {
                "live" -> !entry.isHidden
                "hidden" -> entry.isHidden
                "featured" -> entry.article.isFeatured
                "breaking" -> entry.article.isBreaking
                else -> true
            }
            val matchesQuery = q.isEmpty() ||
                entry.article.titleEn.lowercase().contains(q) ||
                entry.article.titleAm.lowercase().contains(q) ||
                entry.article.categoryId.lowercase().contains(q)
            matchesFilter && matchesQuery
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Button(
                onClick = onOpenStudio,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("admin_write_article")
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(language.pick("Write & Publish New Article", "አዲስ ጽሑፍ ጻፍ እና አትም"))
            }
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                placeholder = { Text(language.pick("Search articles by title or beat...", "ጽሑፎችን በርዕስ ይፈልጉ...")) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().testTag("admin_article_search")
            )
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val filters = listOf(
                    "all" to language.pick("All", "ሁሉም"),
                    "live" to language.pick("Live", "ንቁ"),
                    "hidden" to language.pick("Hidden", "የተደበቁ"),
                    "featured" to language.pick("Featured", "ተመራጭ"),
                    "breaking" to language.pick("Breaking", "ሰበር")
                )
                items(filters, key = { it.first }) { (id, label) ->
                    FilterChip(
                        selected = filter == id,
                        onClick = { filter = id },
                        label = { Text(label) }
                    )
                }
            }
        }

        items(filtered, key = { it.article.id }) { entry ->
            val article = entry.article
            val cat = EditorialCatalog.categoryById(article.categoryId)
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            AdminStatusPill(
                                text = language.pick(cat.titleEn, cat.titleAm),
                                color = cat.accentColor
                            )
                            AdminStatusPill(
                                text = if (entry.isHidden) "HIDDEN" else "LIVE",
                                color = if (entry.isHidden) MaterialTheme.colorScheme.error else EmeraldAccent
                            )
                        }
                        IconButton(
                            onClick = { onEditArticle(entry) },
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("admin_edit_article_${article.id}")
                        ) {
                            Icon(
                                Icons.Rounded.Edit,
                                contentDescription = language.pick("Edit metadata", "አስተካክል"),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Text(
                        text = language.pick(article.titleEn, article.titleAm),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "${article.authorName} · ${article.publishedDate} · ${article.readingMinutes} min",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(10.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = !entry.isHidden,
                            onClick = { onToggleHidden(entry) },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (entry.isHidden) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            label = { Text(if (entry.isHidden) language.pick("Hidden", "ተደብቋል") else language.pick("Visible", "ይታያል")) },
                            modifier = Modifier.testTag("admin_toggle_hide_${article.id}")
                        )
                        FilterChip(
                            selected = article.isBreaking,
                            onClick = { onToggleBreaking(entry) },
                            label = { Text(language.pick("Breaking", "ሰበር")) }
                        )
                        FilterChip(
                            selected = article.isFeatured,
                            onClick = { onToggleFeatured(entry) },
                            label = { Text(language.pick("Featured", "ተመራጭ")) }
                        )
                        FilterChip(
                            selected = article.isTrending,
                            onClick = { onToggleTrending(entry) },
                            label = { Text(language.pick("Trending", "ተወዳጅ")) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminKpiCard(
    title: String,
    value: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
