package com.example.ui.state

import androidx.compose.ui.graphics.Color
import com.example.data.catalog.AdCatalog
import com.example.data.catalog.CatalogAppsAndTools
import com.example.data.catalog.EditorialCatalog
import com.example.data.local.AdCampaignEntity
import com.example.data.local.AdOverrideEntity
import com.example.data.local.ArticleOverrideEntity
import com.example.data.local.CustomAiToolEntity
import com.example.data.local.CustomAppEntity
import com.example.data.local.DiscoveryOverrideEntity
import com.example.data.local.SubmissionEntity
import com.example.data.model.AiTool
import com.example.data.model.AppEntry
import com.example.data.model.Article
import com.example.data.model.ArticleBlock
import com.example.data.model.BannerAdUnit
import com.example.data.model.NativeAdUnit

data class AdminArticleEntry(
    val article: Article,
    val isHidden: Boolean,
    val hasOverride: Boolean
)

data class AdminAppEntry(
    val app: AppEntry,
    val isHidden: Boolean,
    val isCustom: Boolean,
    val hasOverride: Boolean
)

data class AdminAiToolEntry(
    val tool: AiTool,
    val isHidden: Boolean,
    val isCustom: Boolean,
    val hasOverride: Boolean
)

data class AdminAdItem(
    val id: String,
    val placementType: String,
    val sponsorName: String,
    val headlineEn: String,
    val badgeText: String,
    val accentColor: Color,
    val isActive: Boolean,
    val isCustom: Boolean,
    val customEntity: AdCampaignEntity? = null
)

object AdminCatalogMerger {

    fun submissionToArticle(sub: SubmissionEntity): Article {
        val enParagraphs = sub.contentEn.split("\n\n").filter { it.isNotBlank() }.map { ArticleBlock.Paragraph(it.trim()) }
        val amParagraphs = sub.contentAm.split("\n\n").filter { it.isNotBlank() }.map { ArticleBlock.Paragraph(it.trim()) }
        val parsedTags = sub.tagsJoined.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        return Article(
            id = sub.id,
            titleEn = sub.titleEn,
            titleAm = sub.titleAm.ifBlank { sub.titleEn },
            summaryEn = sub.summaryEn.ifBlank { sub.contentEn.take(140) },
            summaryAm = sub.summaryAm.ifBlank { sub.contentAm.ifBlank { sub.contentEn }.take(140) },
            blocksEn = enParagraphs.ifEmpty { listOf(ArticleBlock.Paragraph(sub.contentEn)) },
            blocksAm = amParagraphs.ifEmpty { enParagraphs },
            coverKey = sub.coverKey,
            categoryId = sub.category,
            tags = parsedTags,
            authorName = sub.authorName,
            authorRole = "Community Contributor",
            publishedDate = "Published",
            readingMinutes = sub.readingMinutes.coerceAtLeast(1),
            isUserSubmission = true,
            submissionStatus = sub.status
        )
    }

    fun mergeAdminArticles(
        submissions: List<SubmissionEntity>,
        overrides: List<ArticleOverrideEntity>
    ): List<AdminArticleEntry> {
        val overrideMap = overrides.associateBy { it.articleId }
        val publishedUserArticles = submissions
            .filter { it.status == "published" }
            .map { submissionToArticle(it) }
        val combined = publishedUserArticles + EditorialCatalog.articles

        return combined.map { base ->
            val ov = overrideMap[base.id]
            val merged = if (ov == null) {
                base
            } else {
                base.copy(
                    titleEn = ov.customTitleEn?.takeIf { it.isNotBlank() } ?: base.titleEn,
                    titleAm = ov.customTitleAm?.takeIf { it.isNotBlank() } ?: base.titleAm,
                    categoryId = ov.customCategoryId?.takeIf { it.isNotBlank() } ?: base.categoryId,
                    isBreaking = ov.isBreaking ?: base.isBreaking,
                    isFeatured = ov.isFeatured ?: base.isFeatured,
                    isTrending = ov.isTrending ?: base.isTrending
                )
            }
            AdminArticleEntry(
                article = merged,
                isHidden = ov?.isHidden == true,
                hasOverride = ov != null
            )
        }
    }

    fun mergeAdminApps(
        customApps: List<CustomAppEntity>,
        overrides: List<DiscoveryOverrideEntity>
    ): List<AdminAppEntry> {
        val overrideMap = overrides
            .filter { it.itemType == "app" }
            .associateBy { it.itemId }

        val customDomain = customApps.map { entity ->
            entity.toDomainApp() to true
        }
        val builtInDomain = CatalogAppsAndTools.applications.map { it to false }

        return (customDomain + builtInDomain).map { (base, isCustom) ->
            val ov = overrideMap[base.id]
            val merged = if (ov == null) {
                base
            } else {
                base.copy(
                    isFeatured = ov.isFeatured ?: base.isFeatured,
                    isTrending = ov.isTrending ?: base.isTrending
                )
            }
            AdminAppEntry(
                app = merged,
                isHidden = ov?.isHidden == true,
                isCustom = isCustom,
                hasOverride = ov != null
            )
        }
    }

    fun mergeAdminAiTools(
        customTools: List<CustomAiToolEntity>,
        overrides: List<DiscoveryOverrideEntity>
    ): List<AdminAiToolEntry> {
        val overrideMap = overrides
            .filter { it.itemType == "ai_tool" }
            .associateBy { it.itemId }

        val customDomain = customTools.map { entity ->
            entity.toDomainAiTool() to true
        }
        val builtInDomain = CatalogAppsAndTools.aiTools.map { it to false }

        return (customDomain + builtInDomain).map { (base, isCustom) ->
            val ov = overrideMap[base.id]
            val merged = if (ov == null) {
                base
            } else {
                base.copy(isFeatured = ov.isFeatured ?: base.isFeatured)
            }
            AdminAiToolEntry(
                tool = merged,
                isHidden = ov?.isHidden == true,
                isCustom = isCustom,
                hasOverride = ov != null
            )
        }
    }

    fun mergeActiveNativeAds(
        customCampaigns: List<AdCampaignEntity>,
        adOverrides: List<AdOverrideEntity>
    ): List<NativeAdUnit> {
        val pausedIds = adOverrides.filter { it.isPaused }.map { it.adId }.toSet()
        val customNative = customCampaigns
            .filter { it.placementType == "native" && it.isActive }
            .map { it.toNativeAdUnit() }
        val builtInActive = AdCatalog.nativeAds.filter { it.id !in pausedIds }
        return customNative + builtInActive
    }

    fun mergeActiveBannerAds(
        customCampaigns: List<AdCampaignEntity>,
        adOverrides: List<AdOverrideEntity>
    ): List<BannerAdUnit> {
        val pausedIds = adOverrides.filter { it.isPaused }.map { it.adId }.toSet()
        val customBanner = customCampaigns
            .filter { it.placementType == "banner" && it.isActive }
            .map { it.toBannerAdUnit() }
        val builtInActive = AdCatalog.bannerAds.filter { it.id !in pausedIds }
        return customBanner + builtInActive
    }

    fun buildAdminAdItems(
        customCampaigns: List<AdCampaignEntity>,
        adOverrides: List<AdOverrideEntity>
    ): List<AdminAdItem> {
        val pausedIds = adOverrides.filter { it.isPaused }.map { it.adId }.toSet()
        val customItems = customCampaigns.map { c ->
            AdminAdItem(
                id = c.id,
                placementType = c.placementType,
                sponsorName = c.sponsorName,
                headlineEn = c.headlineEn,
                badgeText = c.badgeText.ifBlank { if (c.placementType == "native") "CUSTOM NATIVE" else "CUSTOM BANNER" },
                accentColor = Color(c.accentColorHex),
                isActive = c.isActive,
                isCustom = true,
                customEntity = c
            )
        }
        val builtInNative = AdCatalog.nativeAds.map { ad ->
            AdminAdItem(
                id = ad.id,
                placementType = "native",
                sponsorName = ad.sponsorName,
                headlineEn = ad.headlineEn,
                badgeText = ad.badgeText,
                accentColor = ad.gradientEnd,
                isActive = ad.id !in pausedIds,
                isCustom = false
            )
        }
        val builtInBanner = AdCatalog.bannerAds.map { banner ->
            AdminAdItem(
                id = banner.id,
                placementType = "banner",
                sponsorName = banner.sponsorName,
                headlineEn = banner.offerEn,
                badgeText = "STICKY BANNER",
                accentColor = banner.accentColor,
                isActive = banner.id !in pausedIds,
                isCustom = false
            )
        }
        return customItems + builtInNative + builtInBanner
    }

    private fun CustomAppEntity.toDomainApp(): AppEntry {
        val accent = Color(accentColorHex)
        return AppEntry(
            id = id,
            name = name,
            developer = developer,
            categoryId = categoryId,
            categoryLabelEn = categoryLabelEn,
            categoryLabelAm = categoryLabelEn,
            taglineEn = taglineEn,
            taglineAm = taglineAm.ifBlank { taglineEn },
            descriptionEn = descriptionEn.ifBlank { taglineEn },
            descriptionAm = taglineAm.ifBlank { descriptionEn.ifBlank { taglineEn } },
            featuresEn = listOf(taglineEn),
            featuresAm = listOf(taglineAm.ifBlank { taglineEn }),
            prosEn = listOf("Editorial curated addition"),
            prosAm = listOf("በአዘጋጆች የተመረጠ መተግበሪያ"),
            consEn = emptyList(),
            consAm = emptyList(),
            reviewEn = descriptionEn.ifBlank { taglineEn },
            reviewAm = taglineAm.ifBlank { taglineEn },
            playStoreUrl = playStoreUrl,
            officialUrl = officialUrl.ifBlank { playStoreUrl },
            coverKey = categoryId,
            accentColor = accent,
            isFeatured = isFeatured,
            isTrending = isTrending
        )
    }

    private fun CustomAiToolEntity.toDomainAiTool(): AiTool {
        val accent = Color(accentColorHex)
        return AiTool(
            id = id,
            name = name,
            provider = provider,
            categoryId = categoryId,
            categoryLabelEn = categoryLabelEn,
            categoryLabelAm = categoryLabelEn,
            pricingTier = pricingTier,
            isFreeTier = isFreeTier,
            taglineEn = taglineEn,
            taglineAm = taglineAm.ifBlank { taglineEn },
            descriptionEn = descriptionEn.ifBlank { taglineEn },
            descriptionAm = taglineAm.ifBlank { descriptionEn.ifBlank { taglineEn } },
            useCasesEn = listOf(taglineEn),
            useCasesAm = listOf(taglineAm.ifBlank { taglineEn }),
            tutorialStepsEn = listOf("Open $name via the official link.", "Start with a clear task prompt."),
            tutorialStepsAm = listOf("${name}ን በይፋዊው ሊንክ ይክፈቱ።", "ግልጽ ጥያቄ በማስገባት ይጀምሩ።"),
            officialUrl = officialUrl,
            coverKey = "ai",
            accentColor = accent,
            isFeatured = isFeatured
        )
    }

    private fun AdCampaignEntity.toNativeAdUnit(): NativeAdUnit {
        val accent = Color(accentColorHex)
        return NativeAdUnit(
            id = id,
            sponsorName = sponsorName,
            headlineEn = headlineEn,
            headlineAm = headlineAm.ifBlank { headlineEn },
            bodyEn = bodyEn,
            bodyAm = bodyEn,
            badgeText = badgeText.ifBlank { "SPONSORED PARTNER" },
            ctaEn = ctaEn.ifBlank { "Learn More" },
            ctaAm = ctaEn.ifBlank { "ይመልከቱ" },
            destinationUrl = destinationUrl,
            gradientStart = accent.copy(alpha = 0.85f),
            gradientEnd = accent,
            iconInitials = sponsorName.take(2).uppercase().ifBlank { "AD" },
            categoryTag = "Sponsored · Partner"
        )
    }

    private fun AdCampaignEntity.toBannerAdUnit(): BannerAdUnit {
        val accent = Color(accentColorHex)
        return BannerAdUnit(
            id = id,
            sponsorName = sponsorName,
            offerEn = headlineEn,
            offerAm = headlineAm.ifBlank { headlineEn },
            ctaEn = ctaEn.ifBlank { "OPEN" },
            ctaAm = ctaEn.ifBlank { "ክፈት" },
            destinationUrl = destinationUrl,
            accentColor = accent
        )
    }
}
