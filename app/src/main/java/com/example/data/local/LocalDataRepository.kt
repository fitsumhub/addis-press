package com.example.data.local

import kotlinx.coroutines.flow.Flow

class LocalDataRepository(private val dao: AddisPressDao) {
    fun observeBookmarks(): Flow<List<BookmarkEntity>> = dao.observeBookmarks()
    fun observeReadingHistory(): Flow<List<ReadingHistoryEntity>> = dao.observeReadingHistory()
    fun observeLikes(): Flow<List<ArticleLikeEntity>> = dao.observeLikes()
    fun observeSubmissions(): Flow<List<SubmissionEntity>> = dao.observeSubmissions()
    fun observeReports(): Flow<List<ReportEntity>> = dao.observeReports()
    fun observeArticleOverrides(): Flow<List<ArticleOverrideEntity>> = dao.observeArticleOverrides()
    fun observeDiscoveryOverrides(): Flow<List<DiscoveryOverrideEntity>> = dao.observeDiscoveryOverrides()
    fun observeCustomApps(): Flow<List<CustomAppEntity>> = dao.observeCustomApps()
    fun observeCustomAiTools(): Flow<List<CustomAiToolEntity>> = dao.observeCustomAiTools()
    fun observeAdCampaigns(): Flow<List<AdCampaignEntity>> = dao.observeAdCampaigns()
    fun observeAdOverrides(): Flow<List<AdOverrideEntity>> = dao.observeAdOverrides()

    suspend fun toggleBookmark(
        itemId: String,
        itemType: String,
        titleEn: String,
        titleAm: String,
        category: String,
        coverKey: String,
        readingListName: String,
        currentlySaved: Boolean
    ): Boolean {
        val docId = "${itemType}_$itemId"
        return if (currentlySaved) {
            dao.deleteBookmarkById(docId)
            false
        } else {
            dao.upsertBookmark(
                BookmarkEntity(
                    id = docId,
                    itemId = itemId,
                    itemType = itemType,
                    readingListName = readingListName.trim().ifEmpty { "Read Later" },
                    titleEn = titleEn,
                    titleAm = titleAm.ifBlank { titleEn },
                    category = category,
                    coverKey = coverKey,
                    updatedAtMillis = System.currentTimeMillis()
                )
            )
            true
        }
    }

    suspend fun updateBookmarkReadingList(bookmarkId: String, listName: String) {
        dao.updateBookmarkList(
            id = bookmarkId,
            listName = listName.trim().ifEmpty { "Read Later" },
            updatedAt = System.currentTimeMillis()
        )
    }

    suspend fun recordReadingProgress(
        articleId: String,
        titleEn: String,
        titleAm: String,
        category: String,
        progressPercent: Int
    ) {
        dao.upsertReadingHistory(
            ReadingHistoryEntity(
                articleId = articleId,
                titleEn = titleEn,
                titleAm = titleAm.ifBlank { titleEn },
                category = category,
                progressPercent = progressPercent.coerceIn(0, 100),
                updatedAtMillis = System.currentTimeMillis()
            )
        )
    }

    suspend fun clearReadingHistory() = dao.clearReadingHistory()

    suspend fun toggleLike(articleId: String, currentlyLiked: Boolean): Boolean {
        return if (currentlyLiked) {
            dao.deleteLike(articleId)
            false
        } else {
            dao.insertLike(ArticleLikeEntity(articleId = articleId))
            true
        }
    }

    suspend fun saveSubmission(
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
        authorName: String,
        readingMinutes: Int,
        status: String
    ): String {
        val id = submissionId?.takeIf { it.isNotBlank() } ?: "story_${System.currentTimeMillis()}"
        dao.upsertSubmission(
            SubmissionEntity(
                id = id,
                titleEn = titleEn.trim(),
                titleAm = titleAm.trim(),
                summaryEn = summaryEn.trim(),
                summaryAm = summaryAm.trim(),
                contentEn = contentEn.trim(),
                contentAm = contentAm.trim(),
                category = category,
                tagsJoined = tags.map { it.trim() }.filter { it.isNotEmpty() }.distinct().joinToString(","),
                coverKey = coverKey,
                authorName = authorName.trim().ifEmpty { "AddisPress Reader" },
                readingMinutes = readingMinutes.coerceIn(1, 60),
                status = status,
                updatedAtMillis = System.currentTimeMillis()
            )
        )
        return id
    }

    suspend fun updateSubmissionStatus(id: String, status: String) {
        dao.updateSubmissionStatus(id, status, System.currentTimeMillis())
    }

    suspend fun deleteSubmission(id: String) = dao.deleteSubmission(id)

    suspend fun submitReport(targetId: String, targetType: String, reason: String, details: String) {
        dao.insertReport(
            ReportEntity(
                id = "rep_${System.currentTimeMillis()}",
                targetId = targetId,
                targetType = targetType,
                reason = reason.trim(),
                details = details.trim(),
                createdAtMillis = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteReport(reportId: String) = dao.deleteReport(reportId)

    suspend fun saveArticleOverride(override: ArticleOverrideEntity) {
        dao.upsertArticleOverride(override.copy(updatedAtMillis = System.currentTimeMillis()))
    }

    suspend fun saveDiscoveryOverride(override: DiscoveryOverrideEntity) {
        dao.upsertDiscoveryOverride(override.copy(updatedAtMillis = System.currentTimeMillis()))
    }

    suspend fun saveCustomApp(app: CustomAppEntity) = dao.upsertCustomApp(app)

    suspend fun deleteCustomApp(id: String) = dao.deleteCustomApp(id)

    suspend fun saveCustomAiTool(tool: CustomAiToolEntity) = dao.upsertCustomAiTool(tool)

    suspend fun deleteCustomAiTool(id: String) = dao.deleteCustomAiTool(id)

    suspend fun saveAdCampaign(campaign: AdCampaignEntity) {
        dao.upsertAdCampaign(campaign.copy(updatedAtMillis = System.currentTimeMillis()))
    }

    suspend fun deleteAdCampaign(id: String) = dao.deleteAdCampaign(id)

    suspend fun setBuiltInAdPaused(adId: String, isPaused: Boolean) {
        dao.upsertAdOverride(AdOverrideEntity(adId = adId, isPaused = isPaused))
    }

    suspend fun resetAllAdminOverrides() {
        dao.clearArticleOverrides()
        dao.clearDiscoveryOverrides()
        dao.clearCustomApps()
        dao.clearCustomAiTools()
        dao.clearAdCampaigns()
        dao.clearAdOverrides()
    }

    suspend fun clearAllLocalData() {
        dao.clearBookmarks()
        dao.clearReadingHistory()
        dao.clearLikes()
        dao.clearSubmissions()
        dao.clearReports()
        resetAllAdminOverrides()
    }
}
