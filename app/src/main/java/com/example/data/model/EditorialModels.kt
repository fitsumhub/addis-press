package com.example.data.model

import androidx.compose.ui.graphics.Color

data class EditorialCategory(
    val id: String,
    val titleEn: String,
    val titleAm: String,
    val subtitleEn: String,
    val subtitleAm: String,
    val accentColor: Color
)

sealed interface ArticleBlock {
    data class Paragraph(val text: String) : ArticleBlock
    data class Heading(val text: String, val badge: String = "") : ArticleBlock
    data class BulletList(val items: List<String>) : ArticleBlock
    data class NumberedSteps(val steps: List<String>) : ArticleBlock
    data class Callout(val title: String, val body: String) : ArticleBlock
}

data class Article(
    val id: String,
    val titleEn: String,
    val titleAm: String,
    val summaryEn: String,
    val summaryAm: String,
    val blocksEn: List<ArticleBlock>,
    val blocksAm: List<ArticleBlock>,
    val coverKey: String,
    val categoryId: String,
    val tags: List<String>,
    val authorName: String,
    val authorRole: String,
    val publishedDate: String,
    val readingMinutes: Int,
    val isBreaking: Boolean = false,
    val isFeatured: Boolean = false,
    val isAppReview: Boolean = false,
    val isTrending: Boolean = false,
    val linkedAppId: String? = null,
    val articleUrl: String = "",
    val isUserSubmission: Boolean = false,
    val submissionStatus: String = "published"
)

data class AppEntry(
    val id: String,
    val name: String,
    val developer: String,
    val categoryId: String,
    val categoryLabelEn: String,
    val categoryLabelAm: String,
    val taglineEn: String,
    val taglineAm: String,
    val descriptionEn: String,
    val descriptionAm: String,
    val featuresEn: List<String>,
    val featuresAm: List<String>,
    val prosEn: List<String>,
    val prosAm: List<String>,
    val consEn: List<String>,
    val consAm: List<String>,
    val reviewEn: String,
    val reviewAm: String,
    val playStoreUrl: String,
    val officialUrl: String,
    val coverKey: String,
    val accentColor: Color,
    val isFeatured: Boolean = false,
    val isTrending: Boolean = false
)

data class AiTool(
    val id: String,
    val name: String,
    val provider: String,
    val categoryId: String,
    val categoryLabelEn: String,
    val categoryLabelAm: String,
    val pricingTier: String,
    val isFreeTier: Boolean,
    val taglineEn: String,
    val taglineAm: String,
    val descriptionEn: String,
    val descriptionAm: String,
    val useCasesEn: List<String>,
    val useCasesAm: List<String>,
    val tutorialStepsEn: List<String>,
    val tutorialStepsAm: List<String>,
    val officialUrl: String,
    val coverKey: String,
    val accentColor: Color,
    val isFeatured: Boolean = false
)

data class AppNotification(
    val id: String,
    val titleEn: String,
    val titleAm: String,
    val bodyEn: String,
    val bodyAm: String,
    val categoryId: String,
    val timeAgoEn: String,
    val timeAgoAm: String,
    val targetArticleId: String
)
