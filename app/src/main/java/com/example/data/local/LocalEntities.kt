package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val id: String,
    val itemId: String,
    val itemType: String,
    val readingListName: String,
    val titleEn: String,
    val titleAm: String,
    val category: String,
    val coverKey: String,
    val updatedAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "reading_history")
data class ReadingHistoryEntity(
    @PrimaryKey val articleId: String,
    val titleEn: String,
    val titleAm: String,
    val category: String,
    val progressPercent: Int,
    val updatedAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "article_likes")
data class ArticleLikeEntity(
    @PrimaryKey val articleId: String,
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "submissions")
data class SubmissionEntity(
    @PrimaryKey val id: String,
    val titleEn: String,
    val titleAm: String,
    val summaryEn: String,
    val summaryAm: String,
    val contentEn: String,
    val contentAm: String,
    val category: String,
    val tagsJoined: String,
    val coverKey: String,
    val authorName: String,
    val readingMinutes: Int,
    val status: String,
    val updatedAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey val id: String,
    val targetId: String,
    val targetType: String,
    val reason: String,
    val details: String,
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "article_overrides")
data class ArticleOverrideEntity(
    @PrimaryKey val articleId: String,
    val isHidden: Boolean = false,
    val isBreaking: Boolean? = null,
    val isFeatured: Boolean? = null,
    val isTrending: Boolean? = null,
    val customTitleEn: String? = null,
    val customTitleAm: String? = null,
    val customCategoryId: String? = null,
    val updatedAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "discovery_overrides")
data class DiscoveryOverrideEntity(
    @PrimaryKey val keyId: String, // "app_<id>" or "tool_<id>"
    val itemId: String,
    val itemType: String, // "app" or "ai_tool"
    val isHidden: Boolean = false,
    val isFeatured: Boolean? = null,
    val isTrending: Boolean? = null,
    val updatedAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "custom_apps")
data class CustomAppEntity(
    @PrimaryKey val id: String,
    val name: String,
    val developer: String,
    val categoryId: String,
    val categoryLabelEn: String,
    val taglineEn: String,
    val taglineAm: String,
    val descriptionEn: String,
    val playStoreUrl: String,
    val officialUrl: String,
    val accentColorHex: Long,
    val isFeatured: Boolean = true,
    val isTrending: Boolean = true,
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "custom_ai_tools")
data class CustomAiToolEntity(
    @PrimaryKey val id: String,
    val name: String,
    val provider: String,
    val categoryId: String,
    val categoryLabelEn: String,
    val pricingTier: String,
    val isFreeTier: Boolean,
    val taglineEn: String,
    val taglineAm: String,
    val descriptionEn: String,
    val officialUrl: String,
    val accentColorHex: Long,
    val isFeatured: Boolean = true,
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "ad_campaigns")
data class AdCampaignEntity(
    @PrimaryKey val id: String,
    val placementType: String, // "native" or "banner"
    val sponsorName: String,
    val headlineEn: String,
    val headlineAm: String,
    val bodyEn: String,
    val ctaEn: String,
    val badgeText: String,
    val destinationUrl: String,
    val accentColorHex: Long,
    val isActive: Boolean = true,
    val updatedAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "ad_overrides")
data class AdOverrideEntity(
    @PrimaryKey val adId: String,
    val isPaused: Boolean = false,
    val updatedAtMillis: Long = System.currentTimeMillis()
)
