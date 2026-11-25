package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.core.localization.AppStrings
import com.example.core.localization.pick
import com.example.data.model.AiTool
import com.example.data.model.AppEntry
import com.example.ui.components.NativeFeedAdCard
import com.example.ui.components.SectionHeaderRow
import com.example.ui.components.StickyBannerAdBar
import com.example.ui.components.coverDrawableForKey
import com.example.ui.state.MainDashboardState
import com.example.ui.theme.EmeraldAccent
import com.example.ui.theme.WarmAmber

@Composable
fun DiscoverScreen(
    state: MainDashboardState,
    onOpenApp: (String) -> Unit,
    onOpenAiTool: (String) -> Unit,
    onToggleAppBookmark: (AppEntry) -> Unit,
    onToggleAiToolBookmark: (AiTool) -> Unit,
    onAdImpression: () -> Unit,
    onAdClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = state.language
    val adsEnabled = state.preferences.adsEnabled
    val bannerAdsEnabled = state.preferences.bannerAdsEnabled
    val adInterval = if (state.preferences.highYieldAds) 3 else 4
    val activeBannerAds = state.activeBannerAds
    val activeNativeAds = state.activeNativeAds

    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Apps, 1 = AI Tools
    var selectedFilter by remember { mutableStateOf("all") }

    val savedAppIds = remember(state.bookmarks) {
        state.bookmarks.filter { it.itemType == "application" }.map { it.itemId }.toSet()
    }
    val savedToolIds = remember(state.bookmarks) {
        state.bookmarks.filter { it.itemType == "ai_tool" }.map { it.itemId }.toSet()
    }

    val appCategories = remember(lang) {
        listOf(
            "all" to lang.pick("All", "ሁሉም"),
            "apps" to lang.pick("Productivity", "ምርታማነት"),
            "ai" to "AI",
            "fintech" to lang.pick("Fintech", "ፊንቴክ"),
            "programming" to lang.pick("Developer", "ገንቢዎች")
        )
    }
    val aiCategories = remember(lang) {
        listOf(
            "all" to lang.pick("All", "ሁሉም"),
            "productivity" to lang.pick("Productivity", "ምርታማነት"),
            "coding" to lang.pick("Coding", "ኮዲንግ"),
            "image_gen" to lang.pick("Image Gen", "ምስል"),
            "video_gen" to lang.pick("Video Gen", "ቪዲዮ"),
            "business" to lang.pick("Business", "ቢዝነስ")
        )
    }

    val filteredApps = remember(state.applications, selectedFilter) {
        if (selectedFilter == "all") state.applications
        else state.applications.filter { it.categoryId == selectedFilter }
    }
    val filteredTools = remember(state.aiTools, selectedFilter) {
        if (selectedFilter == "all") state.aiTools
        else state.aiTools.filter { it.categoryId == selectedFilter }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (adsEnabled && bannerAdsEnabled && activeBannerAds.isNotEmpty()) {
                StickyBannerAdBar(
                    ad = activeBannerAds[2 % activeBannerAds.size],
                    language = lang,
                    onImpression = onAdImpression,
                    onAdClicked = onAdClicked
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = innerPadding.calculateBottomPadding() + 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = AppStrings.discoverTitle(lang),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = lang.pick(
                        "Vetted Android applications and AI tools with editorial reviews.",
                        "የተመረጡ የአንድሮይድ መተግበሪያዎች እና የAI መሳሪያዎች ከዝርዝር ግምገማ ጋር።"
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(14.dp))

                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = selectedTab == 0,
                        onClick = {
                            selectedTab = 0
                            selectedFilter = "all"
                        },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                        icon = { Icon(Icons.Rounded.Apps, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        modifier = Modifier.testTag("discover_tab_apps")
                    ) {
                        Text(AppStrings.appsTab(lang))
                    }
                    SegmentedButton(
                        selected = selectedTab == 1,
                        onClick = {
                            selectedTab = 1
                            selectedFilter = "all"
                        },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                        icon = { Icon(Icons.Rounded.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        modifier = Modifier.testTag("discover_tab_ai")
                    ) {
                        Text(AppStrings.aiToolsTab(lang))
                    }
                }
            }

            item {
                val chips = if (selectedTab == 0) appCategories else aiCategories
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(chips, key = { it.first }) { (id, label) ->
                        FilterChip(
                            selected = selectedFilter == id,
                            onClick = { selectedFilter = id },
                            label = { Text(label) }
                        )
                    }
                }
            }

            if (selectedTab == 0) {
                item {
                    SectionHeaderRow(title = AppStrings.allApps(lang))
                }
                filteredApps.forEachIndexed { index, app ->
                    item(key = "discover_app_${app.id}") {
                        val isSaved = app.id in savedAppIds
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("app_card_${app.id}")
                                .clickable { onOpenApp(app.id) }
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = app.accentColor.copy(alpha = 0.16f),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.size(56.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = app.name.take(2).uppercase(),
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                            color = app.accentColor
                                        )
                                    }
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = app.name,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        if (app.isTrending) {
                                            Spacer(Modifier.width(6.dp))
                                            Surface(
                                                color = WarmAmber.copy(alpha = 0.18f),
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = lang.pick("TRENDING", "ተወዳጅ"),
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = WarmAmber,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = "${app.developer} · ${lang.pick(app.categoryLabelEn, app.categoryLabelAm)}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = lang.pick(app.taglineEn, app.taglineAm),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                IconButton(
                                    onClick = { onToggleAppBookmark(app) },
                                    modifier = Modifier.minimumInteractiveComponentSize()
                                ) {
                                    Icon(
                                        imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                        contentDescription = AppStrings.save(lang),
                                        tint = if (isSaved) WarmAmber else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    if (adsEnabled && activeNativeAds.isNotEmpty() && (index + 1) % adInterval == 0) {
                        val adSlot = ((index + 1) / adInterval) + 2
                        item(key = "discover_app_ad_$adSlot") {
                            NativeFeedAdCard(
                                ad = activeNativeAds[adSlot % activeNativeAds.size],
                                language = lang,
                                onImpression = onAdImpression,
                                onAdClicked = onAdClicked
                            )
                        }
                    }
                }
            } else {
                item {
                    SectionHeaderRow(title = AppStrings.aiToolsTab(lang))
                }
                filteredTools.forEachIndexed { index, tool ->
                    item(key = "discover_tool_${tool.id}") {
                        val isSaved = tool.id in savedToolIds
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("ai_tool_card_${tool.id}")
                                .clickable { onOpenAiTool(tool.id) }
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = coverDrawableForKey(tool.coverKey)),
                                    contentDescription = tool.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(62.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = tool.name,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        if (tool.isFreeTier) {
                                            Surface(
                                                color = EmeraldAccent.copy(alpha = 0.16f),
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = AppStrings.freeAiBadge(lang),
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = EmeraldAccent,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = "${tool.provider} · ${lang.pick(tool.categoryLabelEn, tool.categoryLabelAm)}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = lang.pick(tool.taglineEn, tool.taglineAm),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                IconButton(
                                    onClick = { onToggleAiToolBookmark(tool) },
                                    modifier = Modifier.minimumInteractiveComponentSize()
                                ) {
                                    Icon(
                                        imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                        contentDescription = AppStrings.save(lang),
                                        tint = if (isSaved) WarmAmber else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    if (adsEnabled && activeNativeAds.isNotEmpty() && (index + 1) % adInterval == 0) {
                        val adSlot = ((index + 1) / adInterval) + 1
                        item(key = "discover_tool_ad_$adSlot") {
                            NativeFeedAdCard(
                                ad = activeNativeAds[adSlot % activeNativeAds.size],
                                language = lang,
                                onImpression = onAdImpression,
                                onAdClicked = onAdClicked
                            )
                        }
                    }
                }
            }
        }
    }
}
