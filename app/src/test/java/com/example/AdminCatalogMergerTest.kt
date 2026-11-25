package com.example

import com.example.data.catalog.AdCatalog
import com.example.data.catalog.EditorialCatalog
import com.example.data.local.AdCampaignEntity
import com.example.data.local.AdOverrideEntity
import com.example.data.local.ArticleOverrideEntity
import com.example.data.local.CustomAiToolEntity
import com.example.data.local.CustomAppEntity
import com.example.data.local.DiscoveryOverrideEntity
import com.example.data.local.SubmissionEntity
import com.example.ui.state.AdminCatalogMerger
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AdminCatalogMergerTest {

    @Test
    fun articleOverridesHideAndUpdateFlagsAndHeadlines() {
        val targetId = EditorialCatalog.articles.first().id
        val overrides = listOf(
            ArticleOverrideEntity(
                articleId = targetId,
                isHidden = true,
                isBreaking = true,
                isFeatured = false,
                customTitleEn = "Admin Edited Headline"
            )
        )
        val merged = AdminCatalogMerger.mergeAdminArticles(emptyList(), overrides)
        val updated = merged.first { it.article.id == targetId }
        assertTrue(updated.isHidden)
        assertTrue(updated.article.isBreaking)
        assertFalse(updated.article.isFeatured)
        assertEquals("Admin Edited Headline", updated.article.titleEn)
        assertEquals(EditorialCatalog.articles.size - 1, merged.count { !it.isHidden })
    }

    @Test
    fun publishedUserSubmissionsAppearFirstInAdminArticles() {
        val sub = SubmissionEntity(
            id = "sub_1",
            titleEn = "Addis Tech Summit",
            titleAm = "የአዲስ ቴክ ጉባኤ",
            summaryEn = "Summary",
            summaryAm = "ማጠቃለያ",
            contentEn = "Full editorial body text.",
            contentAm = "ሙሉ ጽሑፍ።",
            category = "technology",
            tagsJoined = "Addis,Tech",
            coverKey = "ai",
            authorName = "Abebe",
            readingMinutes = 3,
            status = "published"
        )
        val draft = sub.copy(id = "sub_2", status = "draft")
        val merged = AdminCatalogMerger.mergeAdminArticles(listOf(sub, draft), emptyList())
        assertEquals("sub_1", merged.first().article.id)
        assertFalse(merged.any { it.article.id == "sub_2" })
    }

    @Test
    fun customAppsAndAiToolsMergeWithDiscoveryOverrides() {
        val customApp = CustomAppEntity(
            id = "custom_app_1",
            name = "EthioPay",
            developer = "Addis Labs",
            categoryId = "fintech",
            categoryLabelEn = "Fintech",
            taglineEn = "Instant QR wallet",
            taglineAm = "ፈጣን ክፍያ",
            descriptionEn = "Editorial review",
            playStoreUrl = "https://play.google.com/store",
            officialUrl = "https://addispress.et",
            accentColorHex = 0xFF10B981L
        )
        val customTool = CustomAiToolEntity(
            id = "custom_tool_1",
            name = "AmharicTutor AI",
            provider = "Addis AI",
            categoryId = "productivity",
            categoryLabelEn = "Productivity",
            pricingTier = "Free",
            isFreeTier = true,
            taglineEn = "Bilingual tutor",
            taglineAm = "አጋዥ",
            descriptionEn = "Study tool",
            officialUrl = "https://addispress.et",
            accentColorHex = 0xFF2563EBL
        )
        val overrides = listOf(
            DiscoveryOverrideEntity(
                keyId = "app_custom_app_1",
                itemId = "custom_app_1",
                itemType = "app",
                isHidden = true
            )
        )

        val apps = AdminCatalogMerger.mergeAdminApps(listOf(customApp), overrides)
        assertTrue(apps.first { it.app.id == "custom_app_1" }.isHidden)
        assertTrue(apps.first { it.app.id == "custom_app_1" }.isCustom)

        val tools = AdminCatalogMerger.mergeAdminAiTools(listOf(customTool), emptyList())
        assertEquals("custom_tool_1", tools.first().tool.id)
        assertFalse(tools.first().isHidden)
    }

    @Test
    fun adCampaignsAndOverridesControlActiveNativeAndBannerAds() {
        val pausedBuiltInId = AdCatalog.nativeAds.first().id
        val customNative = AdCampaignEntity(
            id = "ad_custom_native",
            placementType = "native",
            sponsorName = "EthioCloud",
            headlineEn = "Fast SSD Hosting",
            headlineAm = "ፈጣን ሰርቨር",
            bodyEn = "Deploy in Addis",
            ctaEn = "Launch",
            badgeText = "CLOUD",
            destinationUrl = "https://addispress.et",
            accentColorHex = 0xFF2563EBL,
            isActive = true
        )
        val customBanner = AdCampaignEntity(
            id = "ad_custom_banner",
            placementType = "banner",
            sponsorName = "Zemen Dev",
            headlineEn = "Zero fee API",
            headlineAm = "ነጻ API",
            bodyEn = "",
            ctaEn = "TRY",
            badgeText = "BANNER",
            destinationUrl = "https://addispress.et",
            accentColorHex = 0xFFF59E0BL,
            isActive = true
        )

        val activeNative = AdminCatalogMerger.mergeActiveNativeAds(
            customCampaigns = listOf(customNative, customBanner),
            adOverrides = listOf(AdOverrideEntity(adId = pausedBuiltInId, isPaused = true))
        )
        assertEquals("ad_custom_native", activeNative.first().id)
        assertFalse(activeNative.any { it.id == pausedBuiltInId })

        val activeBanner = AdminCatalogMerger.mergeActiveBannerAds(
            customCampaigns = listOf(customNative, customBanner),
            adOverrides = emptyList()
        )
        assertEquals("ad_custom_banner", activeBanner.first().id)
    }
}
