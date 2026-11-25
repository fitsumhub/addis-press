package com.example.core.search

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import com.example.core.localization.AppLanguage
import com.example.data.catalog.EditorialCatalog
import com.example.data.model.AiTool
import com.example.data.model.AppEntry
import com.example.data.model.Article
import com.example.data.model.ArticleBlock
import java.util.Locale

object SearchEngine {

    fun tokenize(query: String): List<String> =
        query.trim()
            .lowercase(Locale.ROOT)
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }
            .distinct()

    fun searchArticles(articles: List<Article>, query: String): List<Article> {
        val tokens = tokenize(query)
        if (tokens.isEmpty()) return articles
        val fullPhrase = query.trim().lowercase(Locale.ROOT)

        return articles
            .mapNotNull { article ->
                val score = scoreArticle(article, tokens, fullPhrase)
                if (score > 0) article to score else null
            }
            .sortedByDescending { it.second }
            .map { it.first }
    }

    fun scoreArticle(article: Article, tokens: List<String>, fullPhrase: String): Int {
        if (tokens.isEmpty()) return 1
        val cat = EditorialCatalog.categoryById(article.categoryId)
        val titleEn = article.titleEn.lowercase(Locale.ROOT)
        val titleAm = article.titleAm.lowercase(Locale.ROOT)
        val summaryEn = article.summaryEn.lowercase(Locale.ROOT)
        val summaryAm = article.summaryAm.lowercase(Locale.ROOT)
        val catText = "${article.categoryId} ${cat.titleEn} ${cat.titleAm}".lowercase(Locale.ROOT)
        val tagsText = article.tags.joinToString(" ").lowercase(Locale.ROOT)
        val authorText = article.authorName.lowercase(Locale.ROOT)
        val bodyEn = extractBlocksText(article.blocksEn).lowercase(Locale.ROOT)
        val bodyAm = extractBlocksText(article.blocksAm).lowercase(Locale.ROOT)

        var score = 0
        for (token in tokens) {
            var tokenMatched = false
            if (titleEn.contains(token) || titleAm.contains(token)) {
                score += 12
                tokenMatched = true
            }
            if (catText.contains(token) || tagsText.contains(token)) {
                score += 8
                tokenMatched = true
            }
            if (summaryEn.contains(token) || summaryAm.contains(token)) {
                score += 5
                tokenMatched = true
            }
            if (authorText.contains(token)) {
                score += 4
                tokenMatched = true
            }
            if (bodyEn.contains(token) || bodyAm.contains(token)) {
                score += 2
                tokenMatched = true
            }
            if (!tokenMatched) return 0
        }

        if (fullPhrase.length >= 2 && (titleEn.contains(fullPhrase) || titleAm.contains(fullPhrase))) {
            score += 20
        }
        if (article.isFeatured) score += 2
        if (article.isTrending) score += 1
        return score
    }

    fun searchApps(apps: List<AppEntry>, query: String): List<AppEntry> {
        val tokens = tokenize(query)
        if (tokens.isEmpty()) return apps
        return apps.filter { app ->
            val corpus = listOf(
                app.name,
                app.developer,
                app.categoryId,
                app.categoryLabelEn,
                app.categoryLabelAm,
                app.taglineEn,
                app.taglineAm,
                app.descriptionEn,
                app.descriptionAm
            ).joinToString(" ").lowercase(Locale.ROOT)
            tokens.all { corpus.contains(it) }
        }
    }

    fun searchAiTools(tools: List<AiTool>, query: String): List<AiTool> {
        val tokens = tokenize(query)
        if (tokens.isEmpty()) return tools
        return tools.filter { tool ->
            val corpus = listOf(
                tool.name,
                tool.provider,
                tool.categoryId,
                tool.categoryLabelEn,
                tool.categoryLabelAm,
                tool.pricingTier,
                tool.taglineEn,
                tool.taglineAm,
                tool.descriptionEn,
                tool.descriptionAm
            ).joinToString(" ").lowercase(Locale.ROOT)
            tokens.all { corpus.contains(it) }
        }
    }

    fun extractMatchedSnippet(
        article: Article,
        query: String,
        preferredLanguage: AppLanguage
    ): String {
        val tokens = tokenize(query)
        val primarySummary = if (preferredLanguage == AppLanguage.AM && article.summaryAm.isNotBlank()) {
            article.summaryAm
        } else {
            article.summaryEn
        }
        if (tokens.isEmpty()) return primarySummary

        val candidates = if (preferredLanguage == AppLanguage.AM) {
            listOf(article.summaryAm, article.summaryEn) +
                article.blocksAm.flatMap(::blockSentences) +
                article.blocksEn.flatMap(::blockSentences)
        } else {
            listOf(article.summaryEn, article.summaryAm) +
                article.blocksEn.flatMap(::blockSentences) +
                article.blocksAm.flatMap(::blockSentences)
        }.filter { it.isNotBlank() }

        val matched = candidates.firstOrNull { text ->
            val lower = text.lowercase(Locale.ROOT)
            tokens.any { lower.contains(it) }
        } ?: primarySummary

        return if (matched.length > 150) matched.take(147).trimEnd() + "..." else matched
    }

    fun highlightMatches(
        text: String,
        query: String,
        highlightColor: Color
    ): AnnotatedString {
        val tokens = tokenize(query)
        if (tokens.isEmpty() || text.isEmpty()) return AnnotatedString(text)
        val lower = text.lowercase(Locale.ROOT)
        return buildAnnotatedString {
            append(text)
            for (token in tokens) {
                var startIndex = 0
                while (startIndex < lower.length) {
                    val found = lower.indexOf(token, startIndex)
                    if (found < 0) break
                    val end = found + token.length
                    addStyle(
                        style = SpanStyle(
                            color = highlightColor,
                            fontWeight = FontWeight.ExtraBold,
                            background = highlightColor.copy(alpha = 0.14f)
                        ),
                        start = found,
                        end = end
                    )
                    startIndex = end
                }
            }
        }
    }

    private fun extractBlocksText(blocks: List<ArticleBlock>): String =
        blocks.joinToString(" ") { block ->
            when (block) {
                is ArticleBlock.Paragraph -> block.text
                is ArticleBlock.Heading -> "${block.badge} ${block.text}"
                is ArticleBlock.BulletList -> block.items.joinToString(" ")
                is ArticleBlock.NumberedSteps -> block.steps.joinToString(" ")
                is ArticleBlock.Callout -> "${block.title} ${block.body}"
            }
        }

    private fun blockSentences(block: ArticleBlock): List<String> =
        when (block) {
            is ArticleBlock.Paragraph -> listOf(block.text)
            is ArticleBlock.Heading -> listOf(block.text)
            is ArticleBlock.BulletList -> block.items
            is ArticleBlock.NumberedSteps -> block.steps
            is ArticleBlock.Callout -> listOf("${block.title}: ${block.body}")
        }
}
