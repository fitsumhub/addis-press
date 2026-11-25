package com.example

import com.example.core.localization.AppLanguage
import com.example.core.search.SearchEngine
import com.example.data.catalog.CatalogAppsAndTools
import com.example.data.catalog.EditorialCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchEngineTest {

    @Test
    fun englishMultiKeywordSearchFiltersAndRanksTitleMatchesFirst() {
        val results = SearchEngine.searchArticles(EditorialCatalog.articles, "Fayda ID")
        assertTrue(results.isNotEmpty())
        assertTrue(results.first().titleEn.contains("Fayda", ignoreCase = true))
    }

    @Test
    fun amharicKeywordSearchFindsMatchingArticlesAcrossBilingualFields() {
        val results = SearchEngine.searchArticles(EditorialCatalog.articles, "ፋይዳ")
        assertTrue(results.isNotEmpty())
        assertTrue(
            results.any {
                it.titleAm.contains("ፋይዳ") || it.summaryAm.contains("ፋይዳ")
            }
        )
    }

    @Test
    fun emptyQueryReturnsAllArticlesUnchanged() {
        val results = SearchEngine.searchArticles(EditorialCatalog.articles, "   ")
        assertEquals(EditorialCatalog.articles.size, results.size)
    }

    @Test
    fun unmatchedTokensReturnEmptyList() {
        val results = SearchEngine.searchArticles(EditorialCatalog.articles, "xyznonexistenttoken123")
        assertTrue(results.isEmpty())
    }

    @Test
    fun searchAppsAndAiToolsSupportsEnglishAndAmharic() {
        val appMatchesEn = SearchEngine.searchApps(CatalogAppsAndTools.applications, "battery")
        assertTrue(appMatchesEn.any { it.id == "app_accubattery" })

        val appMatchesAm = SearchEngine.searchApps(CatalogAppsAndTools.applications, "ባትሪ")
        assertTrue(appMatchesAm.any { it.id == "app_accubattery" })

        val toolMatches = SearchEngine.searchAiTools(CatalogAppsAndTools.aiTools, "PDF")
        assertTrue(toolMatches.any { it.id == "ai_notebooklm" })
    }

    @Test
    fun extractMatchedSnippetPrefersMatchingBilingualContent() {
        val article = EditorialCatalog.articles.first { it.titleAm.contains("ፋይዳ") || it.summaryAm.contains("ፋይዳ") }
        val snippet = SearchEngine.extractMatchedSnippet(article, "ፋይዳ", AppLanguage.AM)
        assertTrue(snippet.isNotBlank())
    }
}
