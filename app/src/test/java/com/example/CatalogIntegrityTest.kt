package com.example

import com.example.core.localization.AppLanguage
import com.example.core.localization.pick
import com.example.data.catalog.AdCatalog
import com.example.data.catalog.CatalogAppsAndTools
import com.example.data.catalog.EditorialCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogIntegrityTest {
    @Test
    fun catalogHasAllFourteenCategories() {
        assertEquals(14, EditorialCatalog.categories.size)
        EditorialCatalog.categories.forEach { cat ->
            assertTrue(cat.titleEn.isNotBlank())
            assertTrue(cat.titleAm.isNotBlank())
        }
    }

    @Test
    fun allArticlesHaveValidCategoriesAndBilingualContent() {
        val validCategoryIds = EditorialCatalog.categories.map { it.id }.toSet()
        assertTrue(EditorialCatalog.articles.size >= 24)
        EditorialCatalog.articles.forEach { article ->
            assertTrue("Unknown category for ${article.id}", article.categoryId in validCategoryIds)
            assertTrue(article.titleEn.isNotBlank())
            assertTrue(article.titleAm.isNotBlank())
            assertTrue(article.blocksEn.isNotEmpty())
            assertTrue(article.blocksAm.isNotEmpty())
            assertTrue(article.readingMinutes >= 1)
        }
    }

    @Test
    fun appsAndAiToolsHaveValidLinks() {
        assertTrue(CatalogAppsAndTools.applications.isNotEmpty())
        CatalogAppsAndTools.applications.forEach { app ->
            assertTrue(app.playStoreUrl.startsWith("https://"))
            assertTrue(app.officialUrl.startsWith("https://"))
        }
        assertTrue(CatalogAppsAndTools.aiTools.isNotEmpty())
        CatalogAppsAndTools.aiTools.forEach { tool ->
            assertTrue(tool.officialUrl.startsWith("https://"))
        }
    }

    @Test
    fun adCatalogUnitsHaveValidLinksAndCopy() {
        assertTrue(AdCatalog.nativeAds.size >= 4)
        AdCatalog.nativeAds.forEach { ad ->
            assertTrue(ad.sponsorName.isNotBlank())
            assertTrue(ad.headlineEn.isNotBlank())
            assertTrue(ad.headlineAm.isNotBlank())
            assertTrue(ad.destinationUrl.startsWith("https://"))
        }
        assertTrue(AdCatalog.bannerAds.size >= 2)
        AdCatalog.bannerAds.forEach { banner ->
            assertTrue(banner.sponsorName.isNotBlank())
            assertTrue(banner.destinationUrl.startsWith("https://"))
        }
    }

    @Test
    fun languagePickFallsBackGracefully() {
        assertEquals("English", AppLanguage.EN.pick("English", "አማርኛ"))
        assertEquals("አማርኛ", AppLanguage.AM.pick("English", "አማርኛ"))
        assertEquals("English", AppLanguage.AM.pick("English", ""))
    }
}
