package com.example.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.core.localization.AppLanguage
import com.example.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "addispress_prefs")

data class LocalAppPreferences(
    val displayName: String = "Reader",
    val themeMode: ThemeMode = ThemeMode.DARK,
    val language: AppLanguage = AppLanguage.EN,
    val readerFontScale: Float = 1.0f,
    val recentSearches: List<String> = emptyList(),
    val customReadingLists: List<String> = listOf("Read Later", "Tech Research", "Startup Ideas"),
    val followedCategories: Set<String> = setOf("technology", "ai", "fintech"),
    val notificationsEnabled: Boolean = true,
    val breakingAlertsEnabled: Boolean = true,
    val aiDigestEnabled: Boolean = true,
    val adsEnabled: Boolean = true,
    val highYieldAds: Boolean = true,
    val bannerAdsEnabled: Boolean = true,
    val admobBannerUnitId: String = "",
    val admobNativeUnitId: String = "",
    val trackedAdImpressions: Int = 0,
    val trackedAdClicks: Int = 0,
    // Admin Home Layout & Announcement Controls
    val showBreakingBanner: Boolean = true,
    val showFeaturedCarousel: Boolean = true,
    val showAppReviewsRail: Boolean = true,
    val showTrendingSection: Boolean = true,
    val homeFeedPageSize: Int = 20,
    val adminTickerMessageEn: String = "",
    val adminTickerMessageAm: String = ""
)

class PreferencesRepository(private val context: Context) {
    private val appContext = context.applicationContext

    private object Keys {
        val DISPLAY_NAME = stringPreferencesKey("display_name")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val LANGUAGE = stringPreferencesKey("language")
        val READER_FONT_SCALE = floatPreferencesKey("reader_font_scale")
        val RECENT_SEARCHES = stringPreferencesKey("recent_searches")
        val READING_LISTS = stringPreferencesKey("reading_lists")
        val FOLLOWED_CATEGORIES = stringPreferencesKey("followed_categories")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val BREAKING_ALERTS_ENABLED = booleanPreferencesKey("breaking_alerts_enabled")
        val AI_DIGEST_ENABLED = booleanPreferencesKey("ai_digest_enabled")
        val ADS_ENABLED = booleanPreferencesKey("ads_enabled")
        val HIGH_YIELD_ADS = booleanPreferencesKey("high_yield_ads")
        val BANNER_ADS_ENABLED = booleanPreferencesKey("banner_ads_enabled")
        val ADMOB_BANNER_UNIT_ID = stringPreferencesKey("admob_banner_unit_id")
        val ADMOB_NATIVE_UNIT_ID = stringPreferencesKey("admob_native_unit_id")
        val TRACKED_AD_IMPRESSIONS = intPreferencesKey("tracked_ad_impressions")
        val TRACKED_AD_CLICKS = intPreferencesKey("tracked_ad_clicks")
        val SHOW_BREAKING_BANNER = booleanPreferencesKey("show_breaking_banner")
        val SHOW_FEATURED_CAROUSEL = booleanPreferencesKey("show_featured_carousel")
        val SHOW_APP_REVIEWS_RAIL = booleanPreferencesKey("show_app_reviews_rail")
        val SHOW_TRENDING_SECTION = booleanPreferencesKey("show_trending_section")
        val HOME_FEED_PAGE_SIZE = intPreferencesKey("home_feed_page_size")
        val ADMIN_TICKER_EN = stringPreferencesKey("admin_ticker_en")
        val ADMIN_TICKER_AM = stringPreferencesKey("admin_ticker_am")
    }

    val preferences: Flow<LocalAppPreferences> = appContext.dataStore.data.map { prefs ->
        val searchesRaw = prefs[Keys.RECENT_SEARCHES].orEmpty()
        val searches = if (searchesRaw.isBlank()) emptyList() else searchesRaw.split("||").filter { it.isNotBlank() }

        val listsRaw = prefs[Keys.READING_LISTS].orEmpty()
        val lists = if (listsRaw.isBlank()) {
            listOf("Read Later", "Tech Research", "Startup Ideas")
        } else {
            listsRaw.split("||").filter { it.isNotBlank() }.distinct()
        }

        val followedRaw = prefs[Keys.FOLLOWED_CATEGORIES]
        val followed = if (followedRaw == null) {
            setOf("technology", "ai", "fintech")
        } else if (followedRaw.isBlank()) {
            emptySet()
        } else {
            followedRaw.split("||").filter { it.isNotBlank() }.toSet()
        }

        LocalAppPreferences(
            displayName = prefs[Keys.DISPLAY_NAME]?.takeIf { it.isNotBlank() } ?: "Reader",
            themeMode = ThemeMode.fromKey(prefs[Keys.THEME_MODE]),
            language = AppLanguage.fromCode(prefs[Keys.LANGUAGE]),
            readerFontScale = (prefs[Keys.READER_FONT_SCALE] ?: 1.0f).coerceIn(0.85f, 1.45f),
            recentSearches = searches,
            customReadingLists = lists,
            followedCategories = followed,
            notificationsEnabled = prefs[Keys.NOTIFICATIONS_ENABLED] ?: true,
            breakingAlertsEnabled = prefs[Keys.BREAKING_ALERTS_ENABLED] ?: true,
            aiDigestEnabled = prefs[Keys.AI_DIGEST_ENABLED] ?: true,
            adsEnabled = prefs[Keys.ADS_ENABLED] ?: true,
            highYieldAds = prefs[Keys.HIGH_YIELD_ADS] ?: true,
            bannerAdsEnabled = prefs[Keys.BANNER_ADS_ENABLED] ?: true,
            admobBannerUnitId = prefs[Keys.ADMOB_BANNER_UNIT_ID].orEmpty(),
            admobNativeUnitId = prefs[Keys.ADMOB_NATIVE_UNIT_ID].orEmpty(),
            trackedAdImpressions = prefs[Keys.TRACKED_AD_IMPRESSIONS] ?: 0,
            trackedAdClicks = prefs[Keys.TRACKED_AD_CLICKS] ?: 0,
            showBreakingBanner = prefs[Keys.SHOW_BREAKING_BANNER] ?: true,
            showFeaturedCarousel = prefs[Keys.SHOW_FEATURED_CAROUSEL] ?: true,
            showAppReviewsRail = prefs[Keys.SHOW_APP_REVIEWS_RAIL] ?: true,
            showTrendingSection = prefs[Keys.SHOW_TRENDING_SECTION] ?: true,
            homeFeedPageSize = (prefs[Keys.HOME_FEED_PAGE_SIZE] ?: 20).coerceIn(8, 50),
            adminTickerMessageEn = prefs[Keys.ADMIN_TICKER_EN].orEmpty(),
            adminTickerMessageAm = prefs[Keys.ADMIN_TICKER_AM].orEmpty()
        )
    }

    suspend fun setDisplayName(name: String) {
        val clean = name.trim().take(50)
        if (clean.isNotEmpty()) {
            appContext.dataStore.edit { it[Keys.DISPLAY_NAME] = clean }
        }
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        appContext.dataStore.edit { it[Keys.THEME_MODE] = mode.storageKey }
    }

    suspend fun setLanguage(language: AppLanguage) {
        appContext.dataStore.edit { it[Keys.LANGUAGE] = language.code }
    }

    suspend fun setReaderFontScale(scale: Float) {
        appContext.dataStore.edit { it[Keys.READER_FONT_SCALE] = scale.coerceIn(0.85f, 1.45f) }
    }

    suspend fun toggleFollowedCategory(categoryId: String) {
        appContext.dataStore.edit { prefs ->
            val current = prefs[Keys.FOLLOWED_CATEGORIES]
                ?.split("||")
                ?.filter { it.isNotBlank() }
                ?.toMutableSet()
                ?: mutableSetOf("technology", "ai", "fintech")
            if (categoryId in current) current.remove(categoryId) else current.add(categoryId)
            prefs[Keys.FOLLOWED_CATEGORIES] = current.joinToString("||")
        }
    }

    suspend fun updateNotificationSettings(
        notificationsEnabled: Boolean,
        breakingAlertsEnabled: Boolean,
        aiDigestEnabled: Boolean
    ) {
        appContext.dataStore.edit { prefs ->
            prefs[Keys.NOTIFICATIONS_ENABLED] = notificationsEnabled
            prefs[Keys.BREAKING_ALERTS_ENABLED] = breakingAlertsEnabled
            prefs[Keys.AI_DIGEST_ENABLED] = aiDigestEnabled
        }
    }

    suspend fun updateMonetizationSettings(
        adsEnabled: Boolean,
        highYieldAds: Boolean,
        bannerAdsEnabled: Boolean,
        admobBannerUnitId: String,
        admobNativeUnitId: String
    ) {
        appContext.dataStore.edit { prefs ->
            prefs[Keys.ADS_ENABLED] = adsEnabled
            prefs[Keys.HIGH_YIELD_ADS] = highYieldAds
            prefs[Keys.BANNER_ADS_ENABLED] = bannerAdsEnabled
            prefs[Keys.ADMOB_BANNER_UNIT_ID] = admobBannerUnitId.trim()
            prefs[Keys.ADMOB_NATIVE_UNIT_ID] = admobNativeUnitId.trim()
        }
    }

    suspend fun updateAdminHomeLayout(
        showBreakingBanner: Boolean,
        showFeaturedCarousel: Boolean,
        showAppReviewsRail: Boolean,
        showTrendingSection: Boolean,
        homeFeedPageSize: Int
    ) {
        appContext.dataStore.edit { prefs ->
            prefs[Keys.SHOW_BREAKING_BANNER] = showBreakingBanner
            prefs[Keys.SHOW_FEATURED_CAROUSEL] = showFeaturedCarousel
            prefs[Keys.SHOW_APP_REVIEWS_RAIL] = showAppReviewsRail
            prefs[Keys.SHOW_TRENDING_SECTION] = showTrendingSection
            prefs[Keys.HOME_FEED_PAGE_SIZE] = homeFeedPageSize.coerceIn(8, 50)
        }
    }

    suspend fun updateAdminTickerMessage(messageEn: String, messageAm: String) {
        appContext.dataStore.edit { prefs ->
            prefs[Keys.ADMIN_TICKER_EN] = messageEn.trim()
            prefs[Keys.ADMIN_TICKER_AM] = messageAm.trim()
        }
    }

    suspend fun recordAdImpression() {
        appContext.dataStore.edit { prefs ->
            prefs[Keys.TRACKED_AD_IMPRESSIONS] = (prefs[Keys.TRACKED_AD_IMPRESSIONS] ?: 0) + 1
        }
    }

    suspend fun recordAdClick() {
        appContext.dataStore.edit { prefs ->
            prefs[Keys.TRACKED_AD_CLICKS] = (prefs[Keys.TRACKED_AD_CLICKS] ?: 0) + 1
        }
    }

    suspend fun addRecentSearch(query: String) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return
        appContext.dataStore.edit { prefs ->
            val current = prefs[Keys.RECENT_SEARCHES]
                ?.split("||")
                ?.filter { it.isNotBlank() && !it.equals(trimmed, ignoreCase = true) }
                .orEmpty()
            val updated = (listOf(trimmed) + current).take(8)
            prefs[Keys.RECENT_SEARCHES] = updated.joinToString("||")
        }
    }

    suspend fun clearRecentSearches() {
        appContext.dataStore.edit { it.remove(Keys.RECENT_SEARCHES) }
    }

    suspend fun addReadingList(name: String) {
        val trimmed = name.trim().take(40)
        if (trimmed.isEmpty()) return
        appContext.dataStore.edit { prefs ->
            val current = prefs[Keys.READING_LISTS]
                ?.split("||")
                ?.filter { it.isNotBlank() }
                .orEmpty()
                .ifEmpty { listOf("Read Later", "Tech Research", "Startup Ideas") }
            val updated = (current + trimmed).distinct()
            prefs[Keys.READING_LISTS] = updated.joinToString("||")
        }
    }

    suspend fun resetAll() {
        appContext.dataStore.edit { it.clear() }
    }
}
