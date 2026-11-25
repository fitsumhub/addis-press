package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.core.localization.AppStrings
import com.example.core.localization.pick
import com.example.data.catalog.EditorialCatalog
import com.example.ui.components.CompactArticleRow
import com.example.ui.components.EmptyEditorialState
import com.example.ui.components.NativeFeedAdCard
import com.example.ui.components.SectionHeaderRow
import com.example.ui.components.StickyBannerAdBar
import com.example.ui.state.MainDashboardState

@Composable
fun ExploreScreen(
    state: MainDashboardState,
    onToggleFollowCategory: (String) -> Unit,
    onOpenArticle: (String) -> Unit,
    onAdImpression: () -> Unit,
    onAdClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = state.language
    val adsEnabled = state.preferences.adsEnabled
    val bannerAdsEnabled = state.preferences.bannerAdsEnabled
    val adInterval = if (state.preferences.highYieldAds) 4 else 6
    val activeBannerAds = state.activeBannerAds
    val activeNativeAds = state.activeNativeAds

    var selectedCategoryId by remember { mutableStateOf<String?>(null) }
    var showOnlyFollowed by remember { mutableStateOf(false) }

    val displayedCategories = remember(showOnlyFollowed, state.followedCategoryIds) {
        if (showOnlyFollowed) {
            EditorialCatalog.categories.filter { it.id in state.followedCategoryIds }
        } else {
            EditorialCatalog.categories
        }
    }

    val feedArticles = remember(state.articles, selectedCategoryId, showOnlyFollowed, state.followedCategoryIds) {
        when {
            selectedCategoryId != null -> state.articles.filter { it.categoryId == selectedCategoryId }
            showOnlyFollowed -> state.articles.filter { it.categoryId in state.followedCategoryIds }
            else -> state.articles
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (adsEnabled && bannerAdsEnabled && activeBannerAds.isNotEmpty()) {
                StickyBannerAdBar(
                    ad = activeBannerAds[1 % activeBannerAds.size],
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
                    text = AppStrings.exploreTitle(lang),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = lang.pick(
                        "Browse 14 Ethiopian & global technology beats and customize your feed.",
                        "14 የቴክኖሎጂ እና የዲጂታል ዕድል ርዕሶችን በመምረጥ ዜናዎችን ያደራጁ።"
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = !showOnlyFollowed && selectedCategoryId == null,
                        onClick = {
                            showOnlyFollowed = false
                            selectedCategoryId = null
                        },
                        label = { Text(AppStrings.allTopics(lang)) },
                        modifier = Modifier.testTag("explore_all_chip")
                    )
                    FilterChip(
                        selected = showOnlyFollowed,
                        onClick = {
                            showOnlyFollowed = !showOnlyFollowed
                            selectedCategoryId = null
                        },
                        label = { Text("${AppStrings.subscribedTopics(lang)} (${state.followedCategoryIds.size})") },
                        modifier = Modifier.testTag("explore_followed_chip")
                    )
                }
            }

            // 14 Category cards horizontal shelf
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(displayedCategories, key = { it.id }) { category ->
                        val isSelected = selectedCategoryId == category.id
                        val isFollowed = category.id in state.followedCategoryIds
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier
                                .width(210.dp)
                                .testTag("category_card_${category.id}")
                                .clickable {
                                    selectedCategoryId = if (isSelected) null else category.id
                                }
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = category.accentColor.copy(alpha = 0.16f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = lang.pick(category.titleEn, category.titleAm),
                                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                            color = category.accentColor,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = lang.pick(category.subtitleEn, category.subtitleAm),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    minLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(Modifier.height(10.dp))
                                OutlinedButton(
                                    onClick = { onToggleFollowCategory(category.id) },
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier
                                        .height(36.dp)
                                        .testTag("follow_category_${category.id}")
                                ) {
                                    Icon(
                                        imageVector = if (isFollowed) Icons.Rounded.Check else Icons.Rounded.Add,
                                        contentDescription = null,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        text = if (isFollowed) AppStrings.following(lang) else AppStrings.follow(lang),
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(6.dp))
                val activeLabel = selectedCategoryId?.let {
                    val cat = EditorialCatalog.categoryById(it)
                    lang.pick(cat.titleEn, cat.titleAm)
                } ?: if (showOnlyFollowed) AppStrings.subscribedTopics(lang) else AppStrings.latestLabel(lang)
                SectionHeaderRow(title = activeLabel)
            }

            if (feedArticles.isEmpty()) {
                item {
                    EmptyEditorialState(
                        title = lang.pick("No stories in this topic yet", "በዚህ ርዕስ ገና ጽሑፍ የለም"),
                        subtitle = lang.pick(
                            "Follow more categories or clear your filter to browse all articles.",
                            "ተጨማሪ ርዕሶችን ይከተሉ ወይም ሁሉንም ጽሑፎች ይመልከቱ።"
                        )
                    )
                }
            } else {
                feedArticles.forEachIndexed { index, article ->
                    item(key = "explore_article_${article.id}") {
                        CompactArticleRow(
                            article = article,
                            language = lang,
                            onOpen = { onOpenArticle(article.id) }
                        )
                    }
                    if (adsEnabled && activeNativeAds.isNotEmpty() && (index + 1) % adInterval == 0) {
                        val adSlot = (index + 1) / adInterval
                        item(key = "explore_ad_$adSlot") {
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
