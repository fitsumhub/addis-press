package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        BookmarkEntity::class,
        ReadingHistoryEntity::class,
        ArticleLikeEntity::class,
        SubmissionEntity::class,
        ReportEntity::class,
        ArticleOverrideEntity::class,
        DiscoveryOverrideEntity::class,
        CustomAppEntity::class,
        CustomAiToolEntity::class,
        AdCampaignEntity::class,
        AdOverrideEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AddisPressDatabase : RoomDatabase() {
    abstract fun addisPressDao(): AddisPressDao

    companion object {
        @Volatile
        private var INSTANCE: AddisPressDatabase? = null

        fun getInstance(context: Context): AddisPressDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AddisPressDatabase::class.java,
                    "addispress_local.db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
