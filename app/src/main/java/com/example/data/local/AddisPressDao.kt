package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AddisPressDao {
    // Bookmarks
    @Query("SELECT * FROM bookmarks ORDER BY updatedAtMillis DESC")
    fun observeBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBookmark(bookmark: BookmarkEntity)

    @Query("UPDATE bookmarks SET readingListName = :listName, updatedAtMillis = :updatedAt WHERE id = :id")
    suspend fun updateBookmarkList(id: String, listName: String, updatedAt: Long)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmarkById(id: String)

    @Query("DELETE FROM bookmarks")
    suspend fun clearBookmarks()

    // Reading History
    @Query("SELECT * FROM reading_history ORDER BY updatedAtMillis DESC")
    fun observeReadingHistory(): Flow<List<ReadingHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertReadingHistory(history: ReadingHistoryEntity)

    @Query("DELETE FROM reading_history")
    suspend fun clearReadingHistory()

    // Likes
    @Query("SELECT * FROM article_likes ORDER BY createdAtMillis DESC")
    fun observeLikes(): Flow<List<ArticleLikeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLike(like: ArticleLikeEntity)

    @Query("DELETE FROM article_likes WHERE articleId = :articleId")
    suspend fun deleteLike(articleId: String)

    @Query("DELETE FROM article_likes")
    suspend fun clearLikes()

    // Editorial Submissions
    @Query("SELECT * FROM submissions ORDER BY updatedAtMillis DESC")
    fun observeSubmissions(): Flow<List<SubmissionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSubmission(submission: SubmissionEntity)

    @Query("UPDATE submissions SET status = :status, updatedAtMillis = :updatedAt WHERE id = :id")
    suspend fun updateSubmissionStatus(id: String, status: String, updatedAt: Long)

    @Query("DELETE FROM submissions WHERE id = :id")
    suspend fun deleteSubmission(id: String)

    @Query("DELETE FROM submissions")
    suspend fun clearSubmissions()

    // Reports
    @Query("SELECT * FROM reports ORDER BY createdAtMillis DESC")
    fun observeReports(): Flow<List<ReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity)

    @Query("DELETE FROM reports WHERE id = :id")
    suspend fun deleteReport(id: String)

    @Query("DELETE FROM reports")
    suspend fun clearReports()

    // Admin: Article Overrides
    @Query("SELECT * FROM article_overrides ORDER BY updatedAtMillis DESC")
    fun observeArticleOverrides(): Flow<List<ArticleOverrideEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertArticleOverride(override: ArticleOverrideEntity)

    @Query("DELETE FROM article_overrides")
    suspend fun clearArticleOverrides()

    // Admin: Discovery Overrides
    @Query("SELECT * FROM discovery_overrides ORDER BY updatedAtMillis DESC")
    fun observeDiscoveryOverrides(): Flow<List<DiscoveryOverrideEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDiscoveryOverride(override: DiscoveryOverrideEntity)

    @Query("DELETE FROM discovery_overrides")
    suspend fun clearDiscoveryOverrides()

    // Admin: Custom Apps
    @Query("SELECT * FROM custom_apps ORDER BY createdAtMillis DESC")
    fun observeCustomApps(): Flow<List<CustomAppEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCustomApp(app: CustomAppEntity)

    @Query("DELETE FROM custom_apps WHERE id = :id")
    suspend fun deleteCustomApp(id: String)

    @Query("DELETE FROM custom_apps")
    suspend fun clearCustomApps()

    // Admin: Custom AI Tools
    @Query("SELECT * FROM custom_ai_tools ORDER BY createdAtMillis DESC")
    fun observeCustomAiTools(): Flow<List<CustomAiToolEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCustomAiTool(tool: CustomAiToolEntity)

    @Query("DELETE FROM custom_ai_tools WHERE id = :id")
    suspend fun deleteCustomAiTool(id: String)

    @Query("DELETE FROM custom_ai_tools")
    suspend fun clearCustomAiTools()

    // Admin: Ad Campaigns & Overrides
    @Query("SELECT * FROM ad_campaigns ORDER BY updatedAtMillis DESC")
    fun observeAdCampaigns(): Flow<List<AdCampaignEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAdCampaign(campaign: AdCampaignEntity)

    @Query("DELETE FROM ad_campaigns WHERE id = :id")
    suspend fun deleteAdCampaign(id: String)

    @Query("DELETE FROM ad_campaigns")
    suspend fun clearAdCampaigns()

    @Query("SELECT * FROM ad_overrides")
    fun observeAdOverrides(): Flow<List<AdOverrideEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAdOverride(override: AdOverrideEntity)

    @Query("DELETE FROM ad_overrides")
    suspend fun clearAdOverrides()
}
