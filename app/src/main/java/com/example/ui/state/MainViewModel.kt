package com.example.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.localization.AppLanguage
import com.example.data.catalog.AdCatalog
import com.example.data.catalog.CatalogAppsAndTools
import com.example.data.catalog.EditorialCatalog
import com.example.data.local.AdCampaignEntity
import com.example.data.local.AdOverrideEntity
import com.example.data.local.ArticleLikeEntity
import com.example.data.local.ArticleOverrideEntity
import com.example.data.local.BookmarkEntity
import com.example.data.local.CustomAiToolEntity
import com.example.data.local.CustomAppEntity
import com.example.data.local.DiscoveryOverrideEntity
import com.example.data.local.LocalDataRepository
import com.example.data.local.ReadingHistoryEntity
import com.example.data.local.ReportEntity
import com.example.data.local.SubmissionEntity
import com.example.data.model.AiTool
import com.example.data.model.AppEntry
import com.example.data.model.Article
import com.example.data.model.BannerAdUnit
import com.example.data.model.NativeAdUnit
import com.example.data.preferences.LocalAppPreferences
import com.example.data.preferences.PreferencesRepository
import com.example.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MainDashboardState(
    val bookmarks: List<BookmarkEntity> = emptyList(),
    val history: List<ReadingHistoryEntity> = emptyList(),
    val likes: List<ArticleLikeEntity> = emptyList(),
    val submissions: List<SubmissionEntity> = emptyList(),
    val reports: List<ReportEntity> = emptyList(),
    val preferences: LocalAppPreferences = LocalAppPreferences(),
    val articles: List<Article> = EditorialCatalog.articles,
    val applications: List<AppEntry> = CatalogAppsAndTools.applications,
    val aiTools: List<AiTool> = CatalogAppsAndTools.aiTools,
    val activeNativeAds: List<NativeAdUnit> = AdCatalog.nativeAds,
    val activeBannerAds: List<BannerAdUnit> = AdCatalog.bannerAds,
    val adminArticles: List<AdminArticleEntry> = EditorialCatalog.articles.map { AdminArticleEntry(it, false, false) },
    val adminApps: List<AdminAppEntry> = CatalogAppsAndTools.applications.map { AdminAppEntry(it, false, false, false) },
    val adminAiTools: List<AdminAiToolEntry> = CatalogAppsAndTools.aiTools.map { AdminAiToolEntry(it, false, false, false) },
    val adminAdUnits: List<AdminAdItem> = AdminCatalogMerger.buildAdminAdItems(emptyList(), emptyList()),
    val articleOverrides: Map<String, ArticleOverrideEntity> = emptyMap(),
    val discoveryOverrides: Map<String, DiscoveryOverrideEntity> = emptyMap()
) {
    val language: AppLanguage
        get() = preferences.language

    val followedCategoryIds: Set<String>
        get() = preferences.followedCategories
}

private data class UserActivityBundle(
    val bookmarks: List<BookmarkEntity>,
    val history: List<ReadingHistoryEntity>,
    val likes: List<ArticleLikeEntity>,
    val submissions: List<SubmissionEntity>,
    val reports: List<ReportEntity>
)

private data class AdminCatalogBundle(
    val articleOverrides: List<ArticleOverrideEntity>,
    val discoveryOverrides: List<DiscoveryOverrideEntity>,
    val customApps: List<CustomAppEntity>,
    val customAiTools: List<CustomAiToolEntity>
)

private data class AdminAdsBundle(
    val customCampaigns: List<AdCampaignEntity>,
    val adOverrides: List<AdOverrideEntity>
)

class MainViewModel(
    private val localRepository: LocalDataRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _snackbarEvents = MutableSharedFlow<String>(extraBufferCapacity = 6)
    val snackbarEvents = _snackbarEvents.asSharedFlow()

    private val _selectedHomeChip = MutableStateFlow("for_you")
    val selectedHomeChip: StateFlow<String> = _selectedHomeChip.asStateFlow()

    private val _homeSearchQuery = MutableStateFlow("")
    val homeSearchQuery: StateFlow<String> = _homeSearchQuery.asStateFlow()

    private val _homeVisibleCount = MutableStateFlow(20)
    val homeVisibleCount: StateFlow<Int> = _homeVisibleCount.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.preferences
                .map { it.homeFeedPageSize }
                .distinctUntilChanged()
                .collect { pageSize ->
                    _homeVisibleCount.value = pageSize
                }
        }
    }

    private val userActivityFlow = combine(
        localRepository.observeBookmarks(),
        localRepository.observeReadingHistory(),
        localRepository.observeLikes(),
        localRepository.observeSubmissions(),
        localRepository.observeReports()
    ) { bookmarks, history, likes, submissions, reports ->
        UserActivityBundle(bookmarks, history, likes, submissions, reports)
    }

    private val adminCatalogFlow = combine(
        localRepository.observeArticleOverrides(),
        localRepository.observeDiscoveryOverrides(),
        localRepository.observeCustomApps(),
        localRepository.observeCustomAiTools()
    ) { articleOverrides, discoveryOverrides, customApps, customAiTools ->
        AdminCatalogBundle(articleOverrides, discoveryOverrides, customApps, customAiTools)
    }

    private val adminAdsFlow = combine(
        localRepository.observeAdCampaigns(),
        localRepository.observeAdOverrides()
    ) { customCampaigns, adOverrides ->
        AdminAdsBundle(customCampaigns, adOverrides)
    }

    val dashboardState: StateFlow<MainDashboardState> = combine(
        userActivityFlow,
        adminCatalogFlow,
        adminAdsFlow,
        preferencesRepository.preferences
    ) { activity, catalog, ads, prefs ->
        val adminArticles = AdminCatalogMerger.mergeAdminArticles(
            submissions = activity.submissions,
            overrides = catalog.articleOverrides
        )
        val adminApps = AdminCatalogMerger.mergeAdminApps(
            customApps = catalog.customApps,
            overrides = catalog.discoveryOverrides
        )
        val adminAiTools = AdminCatalogMerger.mergeAdminAiTools(
            customTools = catalog.customAiTools,
            overrides = catalog.discoveryOverrides
        )
        val activeNativeAds = AdminCatalogMerger.mergeActiveNativeAds(
            customCampaigns = ads.customCampaigns,
            adOverrides = ads.adOverrides
        )
        val activeBannerAds = AdminCatalogMerger.mergeActiveBannerAds(
            customCampaigns = ads.customCampaigns,
            adOverrides = ads.adOverrides
        )
        val adminAdUnits = AdminCatalogMerger.buildAdminAdItems(
            customCampaigns = ads.customCampaigns,
            adOverrides = ads.adOverrides
        )

        MainDashboardState(
            bookmarks = activity.bookmarks,
            history = activity.history,
            likes = activity.likes,
            submissions = activity.submissions,
            reports = activity.reports,
            preferences = prefs,
            articles = adminArticles.filter { !it.isHidden }.map { it.article },
            applications = adminApps.filter { !it.isHidden }.map { it.app },
            aiTools = adminAiTools.filter { !it.isHidden }.map { it.tool },
            activeNativeAds = activeNativeAds,
            activeBannerAds = activeBannerAds,
            adminArticles = adminArticles,
            adminApps = adminApps,
            adminAiTools = adminAiTools,
            adminAdUnits = adminAdUnits,
            articleOverrides = catalog.articleOverrides.associateBy { it.articleId },
            discoveryOverrides = catalog.discoveryOverrides.associateBy { it.keyId }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = MainDashboardState()
    )

    fun selectHomeChip(chipId: String) {
        _selectedHomeChip.value = chipId
        _homeVisibleCount.value = dashboardState.value.preferences.homeFeedPageSize
    }

    fun setHomeSearchQuery(query: String) {
        _homeSearchQuery.value = query
    }

    fun loadMoreHomeArticles() {
        _homeVisibleCount.value += 10
    }

    fun setDisplayName(name: String) {
        viewModelScope.launch {
            preferencesRepository.setDisplayName(name)
            _snackbarEvents.tryEmit("Profile name updated")
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            preferencesRepository.setThemeMode(mode)
        }
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch {
            preferencesRepository.setLanguage(language)
        }
    }

    fun setReaderFontScale(scale: Float) {
        viewModelScope.launch {
            preferencesRepository.setReaderFontScale(scale)
        }
    }

    fun addRecentSearch(query: String) {
        viewModelScope.launch {
            preferencesRepository.addRecentSearch(query)
        }
    }

    fun clearRecentSearches() {
        viewModelScope.launch {
            preferencesRepository.clearRecentSearches()
        }
    }

    fun addReadingList(name: String) {
        viewModelScope.launch {
            preferencesRepository.addReadingList(name)
            _snackbarEvents.tryEmit("Reading list \"$name\" added")
        }
    }

    fun toggleFollowedCategory(categoryId: String) {
        viewModelScope.launch {
            preferencesRepository.toggleFollowedCategory(categoryId)
        }
    }

    fun updateNotificationSettings(
        notificationsEnabled: Boolean,
        breakingAlertsEnabled: Boolean,
        aiDigestEnabled: Boolean
    ) {
        viewModelScope.launch {
            preferencesRepository.updateNotificationSettings(
                notificationsEnabled = notificationsEnabled,
                breakingAlertsEnabled = breakingAlertsEnabled,
                aiDigestEnabled = aiDigestEnabled
            )
        }
    }

    fun updateMonetizationSettings(
        adsEnabled: Boolean,
        highYieldAds: Boolean,
        bannerAdsEnabled: Boolean,
        admobBannerUnitId: String,
        admobNativeUnitId: String
    ) {
        viewModelScope.launch {
            preferencesRepository.updateMonetizationSettings(
                adsEnabled = adsEnabled,
                highYieldAds = highYieldAds,
                bannerAdsEnabled = bannerAdsEnabled,
                admobBannerUnitId = admobBannerUnitId,
                admobNativeUnitId = admobNativeUnitId
            )
            _snackbarEvents.tryEmit("Monetization settings saved")
        }
    }

    fun updateAdminHomeLayout(
        showBreakingBanner: Boolean,
        showFeaturedCarousel: Boolean,
        showAppReviewsRail: Boolean,
        showTrendingSection: Boolean,
        homeFeedPageSize: Int
    ) {
        viewModelScope.launch {
            preferencesRepository.updateAdminHomeLayout(
                showBreakingBanner = showBreakingBanner,
                showFeaturedCarousel = showFeaturedCarousel,
                showAppReviewsRail = showAppReviewsRail,
                showTrendingSection = showTrendingSection,
                homeFeedPageSize = homeFeedPageSize
            )
            _homeVisibleCount.value = homeFeedPageSize.coerceIn(8, 50)
            _snackbarEvents.tryEmit("Home layout updated")
        }
    }

    fun updateAdminTickerMessage(messageEn: String, messageAm: String) {
        viewModelScope.launch {
            preferencesRepository.updateAdminTickerMessage(messageEn, messageAm)
            _snackbarEvents.tryEmit(
                if (messageEn.isBlank() && messageAm.isBlank()) "Breaking ticker reset to default"
                else "Breaking alert banner updated"
            )
        }
    }

    fun recordAdImpression() {
        viewModelScope.launch {
            preferencesRepository.recordAdImpression()
        }
    }

    fun recordAdClick() {
        viewModelScope.launch {
            preferencesRepository.recordAdClick()
        }
    }

    // --- Admin Articles CMS ---

    fun toggleArticleHidden(entry: AdminArticleEntry) {
        viewModelScope.launch {
            val current = dashboardState.value.articleOverrides[entry.article.id]
                ?: ArticleOverrideEntity(articleId = entry.article.id)
            val newHidden = !entry.isHidden
            localRepository.saveArticleOverride(current.copy(isHidden = newHidden))
            _snackbarEvents.tryEmit(if (newHidden) "Article hidden from feed" else "Article published to feed")
        }
    }

    fun toggleArticleBreaking(entry: AdminArticleEntry) {
        viewModelScope.launch {
            val current = dashboardState.value.articleOverrides[entry.article.id]
                ?: ArticleOverrideEntity(articleId = entry.article.id)
            localRepository.saveArticleOverride(current.copy(isBreaking = !entry.article.isBreaking))
        }
    }

    fun toggleArticleFeatured(entry: AdminArticleEntry) {
        viewModelScope.launch {
            val current = dashboardState.value.articleOverrides[entry.article.id]
                ?: ArticleOverrideEntity(articleId = entry.article.id)
            localRepository.saveArticleOverride(current.copy(isFeatured = !entry.article.isFeatured))
        }
    }

    fun toggleArticleTrending(entry: AdminArticleEntry) {
        viewModelScope.launch {
            val current = dashboardState.value.articleOverrides[entry.article.id]
                ?: ArticleOverrideEntity(articleId = entry.article.id)
            localRepository.saveArticleOverride(current.copy(isTrending = !entry.article.isTrending))
        }
    }

    fun editArticleOverride(
        articleId: String,
        titleEn: String,
        titleAm: String,
        categoryId: String
    ) {
        viewModelScope.launch {
            val current = dashboardState.value.articleOverrides[articleId]
                ?: ArticleOverrideEntity(articleId = articleId)
            localRepository.saveArticleOverride(
                current.copy(
                    customTitleEn = titleEn.trim().takeIf { it.isNotEmpty() },
                    customTitleAm = titleAm.trim().takeIf { it.isNotEmpty() },
                    customCategoryId = categoryId.trim().takeIf { it.isNotEmpty() }
                )
            )
            _snackbarEvents.tryEmit("Article overrides saved")
        }
    }

    // --- Admin Apps & AI Directory CMS ---

    fun toggleAppHidden(entry: AdminAppEntry) {
        viewModelScope.launch {
            val key = "app_${entry.app.id}"
            val current = dashboardState.value.discoveryOverrides[key]
                ?: DiscoveryOverrideEntity(keyId = key, itemId = entry.app.id, itemType = "app")
            localRepository.saveDiscoveryOverride(current.copy(isHidden = !entry.isHidden))
        }
    }

    fun toggleAppFeatured(entry: AdminAppEntry) {
        viewModelScope.launch {
            val key = "app_${entry.app.id}"
            val current = dashboardState.value.discoveryOverrides[key]
                ?: DiscoveryOverrideEntity(keyId = key, itemId = entry.app.id, itemType = "app")
            localRepository.saveDiscoveryOverride(current.copy(isFeatured = !entry.app.isFeatured))
        }
    }

    fun toggleAppTrending(entry: AdminAppEntry) {
        viewModelScope.launch {
            val key = "app_${entry.app.id}"
            val current = dashboardState.value.discoveryOverrides[key]
                ?: DiscoveryOverrideEntity(keyId = key, itemId = entry.app.id, itemType = "app")
            localRepository.saveDiscoveryOverride(current.copy(isTrending = !entry.app.isTrending))
        }
    }

    fun toggleAiToolHidden(entry: AdminAiToolEntry) {
        viewModelScope.launch {
            val key = "tool_${entry.tool.id}"
            val current = dashboardState.value.discoveryOverrides[key]
                ?: DiscoveryOverrideEntity(keyId = key, itemId = entry.tool.id, itemType = "ai_tool")
            localRepository.saveDiscoveryOverride(current.copy(isHidden = !entry.isHidden))
        }
    }

    fun toggleAiToolFeatured(entry: AdminAiToolEntry) {
        viewModelScope.launch {
            val key = "tool_${entry.tool.id}"
            val current = dashboardState.value.discoveryOverrides[key]
                ?: DiscoveryOverrideEntity(keyId = key, itemId = entry.tool.id, itemType = "ai_tool")
            localRepository.saveDiscoveryOverride(current.copy(isFeatured = !entry.tool.isFeatured))
        }
    }

    fun addCustomApp(
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
    ) {
        viewModelScope.launch {
            val cleanName = name.trim()
            if (cleanName.isEmpty()) return@launch
            localRepository.saveCustomApp(
                CustomAppEntity(
                    id = "custom_app_${System.currentTimeMillis()}",
                    name = cleanName,
                    developer = developer.trim().ifEmpty { "Independent Studio" },
                    categoryId = categoryId,
                    categoryLabelEn = categoryLabelEn,
                    taglineEn = taglineEn.trim(),
                    taglineAm = taglineAm.trim(),
                    descriptionEn = descriptionEn.trim(),
                    playStoreUrl = playStoreUrl.trim().ifEmpty { "https://play.google.com/store" },
                    officialUrl = officialUrl.trim().ifEmpty { playStoreUrl.trim().ifEmpty { "https://addispress.et" } },
                    accentColorHex = accentColorHex
                )
            )
            _snackbarEvents.tryEmit("Added \"$cleanName\" to Discover")
        }
    }

    fun deleteCustomApp(id: String) {
        viewModelScope.launch {
            localRepository.deleteCustomApp(id)
            _snackbarEvents.tryEmit("Custom app deleted")
        }
    }

    fun addCustomAiTool(
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
    ) {
        viewModelScope.launch {
            val cleanName = name.trim()
            if (cleanName.isEmpty()) return@launch
            localRepository.saveCustomAiTool(
                CustomAiToolEntity(
                    id = "custom_tool_${System.currentTimeMillis()}",
                    name = cleanName,
                    provider = provider.trim().ifEmpty { "AI Lab" },
                    categoryId = categoryId,
                    categoryLabelEn = categoryLabelEn,
                    pricingTier = pricingTier.trim().ifEmpty { "Free" },
                    isFreeTier = isFreeTier,
                    taglineEn = taglineEn.trim(),
                    taglineAm = taglineAm.trim(),
                    descriptionEn = descriptionEn.trim(),
                    officialUrl = officialUrl.trim().ifEmpty { "https://addispress.et" },
                    accentColorHex = accentColorHex
                )
            )
            _snackbarEvents.tryEmit("Added \"$cleanName\" to AI Tools")
        }
    }

    fun deleteCustomAiTool(id: String) {
        viewModelScope.launch {
            localRepository.deleteCustomAiTool(id)
            _snackbarEvents.tryEmit("Custom AI tool deleted")
        }
    }

    // --- Admin Ads & Campaigns ---

    fun toggleAdActive(item: AdminAdItem) {
        viewModelScope.launch {
            if (item.isCustom && item.customEntity != null) {
                localRepository.saveAdCampaign(item.customEntity.copy(isActive = !item.isActive))
            } else {
                localRepository.setBuiltInAdPaused(item.id, isPaused = item.isActive)
            }
            _snackbarEvents.tryEmit(if (item.isActive) "Ad campaign paused" else "Ad campaign activated")
        }
    }

    fun createAdCampaign(
        placementType: String,
        sponsorName: String,
        headlineEn: String,
        headlineAm: String,
        bodyEn: String,
        ctaEn: String,
        badgeText: String,
        destinationUrl: String,
        accentColorHex: Long
    ) {
        viewModelScope.launch {
            val cleanSponsor = sponsorName.trim()
            if (cleanSponsor.isEmpty()) return@launch
            localRepository.saveAdCampaign(
                AdCampaignEntity(
                    id = "ad_custom_${System.currentTimeMillis()}",
                    placementType = placementType,
                    sponsorName = cleanSponsor,
                    headlineEn = headlineEn.trim(),
                    headlineAm = headlineAm.trim(),
                    bodyEn = bodyEn.trim(),
                    ctaEn = ctaEn.trim().ifEmpty { "Learn More" },
                    badgeText = badgeText.trim().ifEmpty { "SPONSORED" },
                    destinationUrl = destinationUrl.trim().ifEmpty { "https://addispress.et" },
                    accentColorHex = accentColorHex,
                    isActive = true
                )
            )
            _snackbarEvents.tryEmit("Ad campaign \"$cleanSponsor\" live")
        }
    }

    fun deleteAdCampaign(id: String) {
        viewModelScope.launch {
            localRepository.deleteAdCampaign(id)
            _snackbarEvents.tryEmit("Ad campaign deleted")
        }
    }

    // --- Admin Moderation ---

    fun updateSubmissionStatus(submissionId: String, status: String) {
        viewModelScope.launch {
            localRepository.updateSubmissionStatus(submissionId, status)
            _snackbarEvents.tryEmit("Submission status -> $status")
        }
    }

    fun dismissReport(reportId: String) {
        viewModelScope.launch {
            localRepository.deleteReport(reportId)
            _snackbarEvents.tryEmit("Report dismissed")
        }
    }

    fun hideReportedArticle(report: ReportEntity) {
        viewModelScope.launch {
            if (report.targetType == "article") {
                val current = dashboardState.value.articleOverrides[report.targetId]
                    ?: ArticleOverrideEntity(articleId = report.targetId)
                localRepository.saveArticleOverride(current.copy(isHidden = true))
            }
            localRepository.deleteReport(report.id)
            _snackbarEvents.tryEmit("Reported content hidden and resolved")
        }
    }

    fun resetAllAdminOverrides() {
        viewModelScope.launch {
            localRepository.resetAllAdminOverrides()
            preferencesRepository.updateAdminHomeLayout(
                showBreakingBanner = true,
                showFeaturedCarousel = true,
                showAppReviewsRail = true,
                showTrendingSection = true,
                homeFeedPageSize = 20
            )
            preferencesRepository.updateAdminTickerMessage("", "")
            _snackbarEvents.tryEmit("All CMS overrides reset to catalog defaults")
        }
    }

    // --- Reader Bookmarks, Progress, Submissions ---

    fun toggleArticleBookmark(article: Article, readingListName: String = "Read Later") {
        viewModelScope.launch {
            val isSaved = dashboardState.value.bookmarks.any {
                it.itemType == "article" && it.itemId == article.id
            }
            val savedNow = localRepository.toggleBookmark(
                itemId = article.id,
                itemType = "article",
                titleEn = article.titleEn,
                titleAm = article.titleAm,
                category = article.categoryId,
                coverKey = article.coverKey,
                readingListName = readingListName,
                currentlySaved = isSaved
            )
            _snackbarEvents.tryEmit(if (savedNow) "Saved to \"$readingListName\"" else "Removed from Saved")
        }
    }

    fun toggleAppBookmark(app: AppEntry, readingListName: String = "Read Later") {
        viewModelScope.launch {
            val isSaved = dashboardState.value.bookmarks.any {
                it.itemType == "application" && it.itemId == app.id
            }
            val savedNow = localRepository.toggleBookmark(
                itemId = app.id,
                itemType = "application",
                titleEn = app.name,
                titleAm = app.name,
                category = app.categoryId,
                coverKey = app.coverKey,
                readingListName = readingListName,
                currentlySaved = isSaved
            )
            _snackbarEvents.tryEmit(if (savedNow) "Saved ${app.name}" else "Removed ${app.name}")
        }
    }

    fun toggleAiToolBookmark(tool: AiTool, readingListName: String = "Read Later") {
        viewModelScope.launch {
            val isSaved = dashboardState.value.bookmarks.any {
                it.itemType == "ai_tool" && it.itemId == tool.id
            }
            val savedNow = localRepository.toggleBookmark(
                itemId = tool.id,
                itemType = "ai_tool",
                titleEn = tool.name,
                titleAm = tool.name,
                category = tool.categoryId,
                coverKey = tool.coverKey,
                readingListName = readingListName,
                currentlySaved = isSaved
            )
            _snackbarEvents.tryEmit(if (savedNow) "Saved ${tool.name}" else "Removed ${tool.name}")
        }
    }

    fun moveBookmarkToList(bookmarkId: String, listName: String) {
        viewModelScope.launch {
            localRepository.updateBookmarkReadingList(bookmarkId, listName)
            _snackbarEvents.tryEmit("Moved to \"$listName\"")
        }
    }

    fun removeBookmark(bookmark: BookmarkEntity) {
        viewModelScope.launch {
            localRepository.toggleBookmark(
                itemId = bookmark.itemId,
                itemType = bookmark.itemType,
                titleEn = bookmark.titleEn,
                titleAm = bookmark.titleAm,
                category = bookmark.category,
                coverKey = bookmark.coverKey,
                readingListName = bookmark.readingListName,
                currentlySaved = true
            )
            _snackbarEvents.tryEmit("Removed from Saved")
        }
    }

    fun toggleLike(articleId: String) {
        viewModelScope.launch {
            val isLiked = dashboardState.value.likes.any { it.articleId == articleId }
            localRepository.toggleLike(articleId, isLiked)
        }
    }

    fun recordReadingProgress(article: Article, progressPercent: Int) {
        viewModelScope.launch {
            localRepository.recordReadingProgress(
                articleId = article.id,
                titleEn = article.titleEn,
                titleAm = article.titleAm,
                category = article.categoryId,
                progressPercent = progressPercent
            )
        }
    }

    fun clearReadingHistory() {
        viewModelScope.launch {
            localRepository.clearReadingHistory()
            _snackbarEvents.tryEmit("Reading history cleared")
        }
    }

    fun submitReport(targetId: String, targetType: String, reason: String, details: String) {
        viewModelScope.launch {
            localRepository.submitReport(targetId, targetType, reason, details)
            _snackbarEvents.tryEmit("Report recorded for editorial review")
        }
    }

    fun saveEditorialSubmission(
        submissionId: String?,
        titleEn: String,
        titleAm: String,
        summaryEn: String,
        summaryAm: String,
        contentEn: String,
        contentAm: String,
        category: String,
        tags: List<String>,
        coverKey: String,
        readingMinutes: Int,
        status: String,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            val authorName = dashboardState.value.preferences.displayName.ifBlank { "AddisPress Reader" }
            localRepository.saveSubmission(
                submissionId = submissionId,
                titleEn = titleEn,
                titleAm = titleAm,
                summaryEn = summaryEn,
                summaryAm = summaryAm,
                contentEn = contentEn,
                contentAm = contentAm,
                category = category,
                tags = tags,
                coverKey = coverKey,
                authorName = authorName,
                readingMinutes = readingMinutes,
                status = status
            )
            _snackbarEvents.tryEmit("Story saved ($status)")
            onDone()
        }
    }

    fun deleteSubmission(submissionId: String) {
        viewModelScope.launch {
            localRepository.deleteSubmission(submissionId)
            _snackbarEvents.tryEmit("Story deleted")
        }
    }

    fun clearAllLocalData() {
        viewModelScope.launch {
            localRepository.clearAllLocalData()
            preferencesRepository.resetAll()
            _snackbarEvents.tryEmit("Saved data cleared")
        }
    }
}
