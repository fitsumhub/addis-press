package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.TravelExplore
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.core.localization.AppStrings
import com.example.core.localization.pick
import com.example.core.search.SearchEngine
import com.example.data.catalog.EditorialCatalog
import com.example.data.model.Article
import com.example.ui.components.AddisPressMasthead
import com.example.ui.components.AppReviewHighlightCard
import com.example.ui.components.CompactArticleRow
import com.example.ui.components.EmptyEditorialState
import com.example.ui.components.FeaturedArticleCard
import com.example.ui.components.GridArticleCard
import com.example.ui.components.NativeFeedAdCard
import com.example.ui.components.SearchArticleResultCard
import com.example.ui.components.SectionHeaderRow
import com.example.ui.components.StickyBannerAdBar
import com.example.ui.state.MainDashboardState
import com.example.ui.theme.WarmAmber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    state: MainDashboardState,
    selectedChip: String,
    searchQuery: String,
    visibleCount: Int,
    onSelectChip: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearchSubmitted: (String) -> Unit,
    onLoadMore: () -> Unit,
    onOpenSearch: (String) -> Unit,
    onOpenArticle: (String) -> Unit,
    onToggleBookmark: (Article) -> Unit,
    onOpenStudio: () -> Unit,
    onOpenAdmin: () -> Unit,
    onAdImpression: () -> Unit,
    onAdClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = state.language
    val focusManager = LocalFocusManager.current
    val firstName = state.preferences.displayName.substringBefore(" ")
    val adsEnabled = state.preferences.adsEnabled
    val highYieldAds = state.preferences.highYieldAds
    val bannerAdsEnabled = state.preferences.bannerAdsEnabled
    val adIntervalRows = if (highYieldAds) 3 else 5
    val activeBannerAds = state.activeBannerAds
    val activeNativeAds = state.activeNativeAds
    val prefs = state.preferences
    val isSearching = searchQuery.trim().isNotEmpty()

    val bookmarkedArticleIds = remember(state.bookmarks) {
        state.bookmarks.filter { it.itemType == "article" }.map { it.itemId }.toSet()
    }
    var showNotificationsSheet by remember { mutableStateOf(false) }

    val homeChips = remember(lang) {
        listOf(
            "for_you" to AppStrings.forYouChip(lang),
            "ai" to "AI",
            "apps" to lang.pick("Apps", "መተግበሪያዎች"),
            "fintech" to lang.pick("Fintech", "ፊንቴክ"),
            "business" to lang.pick("Business", "ቢዝነስ"),
            "technology" to lang.pick("Tech", "ቴክ"),
            "cybersecurity" to lang.pick("Security", "ደህንነት"),
            "opportunities" to lang.pick("Opportunities", "ዕድሎች")
        )
    }

    val filteredArticles = remember(state.articles, selectedChip, state.followedCategoryIds, searchQuery) {
        val chipFiltered = when (selectedChip) {
            "for_you" -> {
                val followed = state.followedCategoryIds
                state.articles.sortedByDescending { article ->
                    (if (article.categoryId in followed) 2 else 0) +
                        (if (article.isFeatured) 2 else 0) +
                        (if (article.isTrending) 1 else 0)
                }
            }
            "apps" -> state.articles.filter { it.categoryId == "apps" || it.categoryId == "app_reviews" }
            else -> state.articles.filter { it.categoryId == selectedChip }
        }
        SearchEngine.searchArticles(chipFiltered, searchQuery)
    }

    val breakingArticle = remember(state.articles) {
        state.articles.firstOrNull { it.isBreaking }
    }
    val featuredList = remember(state.articles) {
        state.articles.filter { it.isFeatured }.ifEmpty { state.articles.take(3) }
    }
    val appReviews = remember(state.articles) {
        state.articles.filter { it.isAppReview }
    }
    val trendingList = remember(state.articles) {
        state.articles.filter { it.isTrending }.take(4)
    }
    val gridArticles = remember(filteredArticles, visibleCount) {
        filteredArticles.take(visibleCount)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (adsEnabled && bannerAdsEnabled && activeBannerAds.isNotEmpty()) {
                StickyBannerAdBar(
                    ad = activeBannerAds[0],
                    language = lang,
                    onImpression = onAdImpression,
                    onAdClicked = onAdClicked
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onOpenStudio,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("write_story_fab")
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = AppStrings.writeStory(lang)
                )
            }
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            val columns = if (maxWidth >= 720.dp) 3 else 2
            val gridRows = remember(gridArticles, columns) {
                gridArticles.chunked(columns)
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding() + 88.dp)
            ) {
                // Masthead + Greeting + Notifications + Search trigger
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            AddisPressMasthead()
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = onOpenAdmin,
                                    modifier = Modifier
                                        .minimumInteractiveComponentSize()
                                        .testTag("home_admin_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.AdminPanelSettings,
                                        contentDescription = lang.pick("Admin Dashboard", "የአስተዳደር ዳሽቦርድ"),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                IconButton(
                                    onClick = { showNotificationsSheet = true },
                                    modifier = Modifier
                                        .minimumInteractiveComponentSize()
                                        .testTag("notifications_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.NotificationsNone,
                                        contentDescription = AppStrings.notifications(lang),
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = AppStrings.greeting(lang, firstName),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            placeholder = {
                                Text(
                                    text = lang.pick(
                                        "Search news & tech in English or አማርኛ...",
                                        "ዜና እና ቴክኖሎጂ በአማርኛ ወይም በEnglish ይፈልጉ..."
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = lang.pick("Search", "ፈልግ"),
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            },
                            trailingIcon = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(
                                            onClick = { onSearchQueryChange("") },
                                            modifier = Modifier
                                                .minimumInteractiveComponentSize()
                                                .testTag("home_search_clear")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.Close,
                                                contentDescription = lang.pick("Clear search", "ፍለጋ አጽዳ")
                                            )
                                        }
                                    }
                                    IconButton(
                                        onClick = { onOpenSearch(searchQuery) },
                                        modifier = Modifier
                                            .minimumInteractiveComponentSize()
                                            .testTag("home_search_bar")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.TravelExplore,
                                            contentDescription = lang.pick("Advanced search", "ዝርዝር ፍለጋ"),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    onSearchSubmitted(searchQuery)
                                    focusManager.clearFocus()
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("home_search_input")
                        )
                    }
                }

                // Category Chips Row
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(homeChips, key = { it.first }) { (chipId, label) ->
                            val selected = selectedChip == chipId
                            FilterChip(
                                selected = selected,
                                onClick = { onSelectChip(chipId) },
                                label = {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.onBackground,
                                    selectedLabelColor = MaterialTheme.colorScheme.background
                                ),
                                modifier = Modifier.testTag("home_chip_$chipId")
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }

                if (isSearching) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = lang.pick(
                                    "${filteredArticles.size} articles matching \"${searchQuery.trim()}\"",
                                    "\"${searchQuery.trim()}\" ለሚለው ${filteredArticles.size} ጽሑፎች ተገኝተዋል"
                                ),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(
                                onClick = { onOpenSearch(searchQuery) },
                                modifier = Modifier.testTag("home_search_apps_ai_link")
                            ) {
                                Text(lang.pick("Apps & AI", "መተግበሪያ እና AI"))
                            }
                        }
                    }

                    if (filteredArticles.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                EmptyEditorialState(
                                    title = lang.pick(
                                        "No articles found for \"${searchQuery.trim()}\"",
                                        "ለ\"${searchQuery.trim()}\" ምንም ጽሑፍ አልተገኘም"
                                    ),
                                    subtitle = lang.pick(
                                        "Try searching English or Amharic keywords below or switch category to \"For you\".",
                                        "ከታች ያሉትን የእንግሊዝኛ ወይም የአማርኛ ቃላት ይሞክሩ ወይም ምድቡን ወደ \"ለእርስዎ\" ይቀይሩ።"
                                    )
                                )
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                ) {
                                    listOf("Fayda", "ቴሌብር", "AI", "Kotlin", "ፊንቴክ", "Startups").forEach { suggestion ->
                                        AssistChip(
                                            onClick = {
                                                onSearchQueryChange(suggestion)
                                                onSearchSubmitted(suggestion)
                                            },
                                            label = { Text(suggestion) }
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        filteredArticles.forEachIndexed { idx, article ->
                            item(key = "home_search_result_${article.id}") {
                                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                                    SearchArticleResultCard(
                                        article = article,
                                        query = searchQuery,
                                        language = lang,
                                        isBookmarked = article.id in bookmarkedArticleIds,
                                        onOpen = {
                                            onSearchSubmitted(searchQuery)
                                            onOpenArticle(article.id)
                                        },
                                        onToggleBookmark = { onToggleBookmark(article) }
                                    )
                                }
                            }
                            if (adsEnabled && activeNativeAds.isNotEmpty() && (idx + 1) % adIntervalRows == 0) {
                                val adSlot = (idx + 1) / adIntervalRows - 1
                                item(key = "home_search_ad_$adSlot") {
                                    NativeFeedAdCard(
                                        ad = activeNativeAds[adSlot % activeNativeAds.size],
                                        language = lang,
                                        onImpression = onAdImpression,
                                        onAdClicked = onAdClicked,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                                    )
                                }
                            }
                        }
                    }
                } else {
                // Breaking News Banner (respects Admin toggle and custom ticker override)
                val customTicker = lang.pick(prefs.adminTickerMessageEn, prefs.adminTickerMessageAm).trim()
                val showTicker = prefs.showBreakingBanner &&
                    selectedChip == "for_you" &&
                    (breakingArticle != null || customTicker.isNotEmpty())
                if (showTicker) {
                    val tickerText = customTicker.ifEmpty {
                        breakingArticle?.let { lang.pick(it.titleEn, it.titleAm) }.orEmpty()
                    }
                    val targetId = breakingArticle?.id ?: state.articles.firstOrNull()?.id
                    item {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                            border = BorderStroke(1.dp, WarmAmber.copy(alpha = 0.45f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .testTag("breaking_news_banner")
                                .clickable(enabled = targetId != null) {
                                    targetId?.let(onOpenArticle)
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = WarmAmber,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Bolt,
                                            contentDescription = null,
                                            tint = Color.Black,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Text(
                                            text = AppStrings.breakingLabel(lang),
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = Color.Black
                                        )
                                    }
                                }
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    text = tickerText,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }

                // Featured Editorial Carousel
                if (prefs.showFeaturedCarousel && selectedChip == "for_you" && featuredList.isNotEmpty()) {
                    item {
                        val pagerState = rememberPagerState(pageCount = { featuredList.size })
                        Column {
                            HorizontalPager(
                                state = pagerState,
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                pageSpacing = 12.dp
                            ) { page ->
                                val article = featuredList[page]
                                FeaturedArticleCard(
                                    article = article,
                                    language = lang,
                                    isBookmarked = article.id in bookmarkedArticleIds,
                                    onOpen = { onOpenArticle(article.id) },
                                    onToggleBookmark = { onToggleBookmark(article) }
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                repeat(featuredList.size) { index ->
                                    val active = pagerState.currentPage == index
                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 3.dp)
                                            .height(5.dp)
                                            .width(if (active) 18.dp else 5.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (active) MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.outline
                                            )
                                    )
                                }
                            }
                            Spacer(Modifier.height(20.dp))
                        }
                    }
                }

                // App Reviews Horizontal Rail
                if (prefs.showAppReviewsRail && selectedChip == "for_you" && appReviews.isNotEmpty()) {
                    item {
                        SectionHeaderRow(
                            title = AppStrings.appReviewsLabel(lang),
                            trailingActionLabel = AppStrings.swipeHint(lang),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(10.dp))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(appReviews, key = { it.id }) { review ->
                                AppReviewHighlightCard(
                                    article = review,
                                    language = lang,
                                    onOpen = { onOpenArticle(review.id) }
                                )
                            }
                        }
                        Spacer(Modifier.height(22.dp))
                    }
                }

                // Trending Section
                if (prefs.showTrendingSection && selectedChip == "for_you" && trendingList.isNotEmpty()) {
                    item {
                        SectionHeaderRow(
                            title = AppStrings.trendingLabel(lang),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            trendingList.forEach { article ->
                                CompactArticleRow(
                                    article = article,
                                    language = lang,
                                    onOpen = { onOpenArticle(article.id) }
                                )
                            }
                        }
                        Spacer(Modifier.height(18.dp))
                    }
                }

                // Latest Long Grid Section with Interspersed Native Ads
                item {
                    SectionHeaderRow(
                        title = AppStrings.latestLabel(lang),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                }

                gridRows.forEachIndexed { rowIndex, rowItems ->
                    item(key = "grid_row_$rowIndex") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowItems.forEach { article ->
                                GridArticleCard(
                                    article = article,
                                    language = lang,
                                    isBookmarked = article.id in bookmarkedArticleIds,
                                    onOpen = { onOpenArticle(article.id) },
                                    onToggleBookmark = { onToggleBookmark(article) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowItems.size < columns) {
                                repeat(columns - rowItems.size) {
                                    Spacer(Modifier.weight(1f))
                                }
                            }
                        }
                    }

                    // Insert a prominent full-width Native Ad card every adIntervalRows rows
                    if (adsEnabled && activeNativeAds.isNotEmpty() && (rowIndex + 1) % adIntervalRows == 0) {
                        val adSlotIndex = (rowIndex + 1) / adIntervalRows - 1
                        item(key = "home_native_ad_$adSlotIndex") {
                            NativeFeedAdCard(
                                ad = activeNativeAds[adSlotIndex % activeNativeAds.size],
                                language = lang,
                                onImpression = onAdImpression,
                                onAdClicked = onAdClicked,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                            )
                        }
                    }
                }

                if (filteredArticles.size > visibleCount) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            OutlinedButton(
                                onClick = onLoadMore,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("load_more_button")
                            ) {
                                Text(AppStrings.loadMore(lang))
                            }
                        }
                    }
                }
                } // end else (!isSearching)
            }
        }
    }

    if (showNotificationsSheet) {
        ModalBottomSheet(onDismissRequest = { showNotificationsSheet = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = lang.pick("Editorial Alerts", "የአዘጋጁ ማሳወቂያዎች"),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(Modifier.height(12.dp))
                EditorialCatalog.notifications.forEach { notif ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .clickable {
                                showNotificationsSheet = false
                                onOpenArticle(notif.targetArticleId)
                            }
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = lang.pick(notif.titleEn, notif.titleAm),
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = lang.pick(notif.timeAgoEn, notif.timeAgoAm),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = lang.pick(notif.bodyEn, notif.bodyAm),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
