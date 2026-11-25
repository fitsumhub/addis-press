package com.example.core.localization

import java.util.Calendar

enum class AppLanguage(val code: String, val nativeName: String) {
    EN("en", "English"),
    AM("am", "አማርኛ");

    companion object {
        fun fromCode(raw: String?): AppLanguage =
            entries.firstOrNull { it.code == raw } ?: EN
    }
}

fun AppLanguage.pick(en: String, am: String): String =
    if (this == AppLanguage.AM && am.isNotBlank()) am else en

object AppStrings {
    fun navHome(lang: AppLanguage) = lang.pick("Home", "መነሻ")
    fun navExplore(lang: AppLanguage) = lang.pick("Explore", "ያስሱ")
    fun navDiscover(lang: AppLanguage) = lang.pick("Discover", "መተግበሪያዎች")
    fun navSaved(lang: AppLanguage) = lang.pick("Saved", "የተቀመጡ")
    fun navProfile(lang: AppLanguage) = lang.pick("Profile", "መገለጫ")

    fun greeting(lang: AppLanguage, firstName: String): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val prefix = when {
            hour < 12 -> lang.pick("Good morning", "እንደምን አደሩ")
            hour < 17 -> lang.pick("Good afternoon", "እንደምን ዋሉ")
            else -> lang.pick("Good evening", "እንደምን አመሹ")
        }
        return if (firstName.isNotBlank()) "$prefix, $firstName" else prefix
    }

    fun searchHint(lang: AppLanguage) = lang.pick(
        "Search articles, AI tools, apps...",
        "ጽሑፎችን፣ AI መሳሪያዎችን፣ መተግበሪያዎችን ይፈልጉ..."
    )

    fun breakingLabel(lang: AppLanguage) = lang.pick("BREAKING", "ሰበር ዜና")
    fun featuredLabel(lang: AppLanguage) = lang.pick("Featured Stories", "ተመራጭ ጽሑፎች")
    fun appReviewsLabel(lang: AppLanguage) = lang.pick("App reviews", "የመተግበሪያ ግምገማዎች")
    fun swipeHint(lang: AppLanguage) = lang.pick("Swipe ->", "ያንሸራትቱ ->")
    fun readReview(lang: AppLanguage) = lang.pick("Read review ->", "ግምገማውን ያንብቡ ->")
    fun trendingLabel(lang: AppLanguage) = lang.pick("Trending now", "አሁን ተወዳጅ")
    fun latestLabel(lang: AppLanguage) = lang.pick("Latest", "አዳዲስ")
    fun forYouChip(lang: AppLanguage) = lang.pick("For you", "ለእርስዎ")
    fun loadMore(lang: AppLanguage) = lang.pick("Load more stories", "ተጨማሪ ጽሑፎችን ጫን")
    fun writeStory(lang: AppLanguage) = lang.pick("Write story", "ጽሑፍ ጻፍ")

    fun minRead(lang: AppLanguage, minutes: Int) =
        if (lang == AppLanguage.AM) "$minutes ደቂቃ ንባብ" else "$minutes min read"

    fun minShort(lang: AppLanguage, minutes: Int) =
        if (lang == AppLanguage.AM) "$minutes ደቂቃ" else "$minutes min"

    fun exploreTitle(lang: AppLanguage) = lang.pick("Explore Topics", "ርዕሶችን ያስሱ")
    fun subscribedTopics(lang: AppLanguage) = lang.pick("Followed", "የሚከተሏቸው")
    fun allTopics(lang: AppLanguage) = lang.pick("All topics", "ሁሉም ርዕሶች")
    fun follow(lang: AppLanguage) = lang.pick("Follow", "ተከተል")
    fun following(lang: AppLanguage) = lang.pick("Following", "እየተከተሉ ነው")

    fun discoverTitle(lang: AppLanguage) = lang.pick("Discover", "ያግኙ")
    fun appsTab(lang: AppLanguage) = lang.pick("Apps Directory", "መተግበሪያዎች")
    fun aiToolsTab(lang: AppLanguage) = lang.pick("AI Tools Hub", "የAI መሳሪያዎች")
    fun featuredApps(lang: AppLanguage) = lang.pick("Featured Android Apps", "ተመራጭ የአንድሮይድ መተግበሪያዎች")
    fun allApps(lang: AppLanguage) = lang.pick("Curated Apps", "የተመረጡ መተግበሪያዎች")
    fun freeAiBadge(lang: AppLanguage) = lang.pick("Free tier", "ነጻ አገልግሎት")

    fun savedTitle(lang: AppLanguage) = lang.pick("Saved", "የተቀመጡ")
    fun savedEmptyTitle(lang: AppLanguage) = lang.pick("Nothing saved yet", "እስካሁን ምንም አልተቀመጠም")
    fun savedEmptySubtitle(lang: AppLanguage) = lang.pick(
        "Tap the bookmark on any story, app, or AI tool to save it here for offline reading.",
        "ማንኛውንም ጽሑፍ፣ መተግበሪያ ወይም AI መሳሪያ ከኢንተርኔት ውጪ ለማንበብ እዚህ ያስቀምጡ።"
    )
    fun readableOffline(lang: AppLanguage, count: Int) =
        if (lang == AppLanguage.AM) "$count የተቀመጡ · ከኢንተርኔት ውጪ የሚነበብ" else "$count saved · readable offline"

    fun readingHistoryTab(lang: AppLanguage) = lang.pick("History", "የንባብ ታሪክ")
    fun bookmarksTab(lang: AppLanguage) = lang.pick("Bookmarks", "ዕልባቶች")
    fun readingLists(lang: AppLanguage) = lang.pick("Reading Lists", "የንባብ ዝርዝሮች")

    fun saveToReadLater(lang: AppLanguage) = lang.pick("Save to read later", "በኋላ ለማንበብ ያስቀምጡ")
    fun savedOfflineHint(lang: AppLanguage) = lang.pick(
        "Saved locally for offline reading",
        "ከኢንተርኔት ውጪ ለማንበብ በስልክዎ ላይ የተቀመጠ"
    )
    fun readNext(lang: AppLanguage) = lang.pick("Read next", "ቀጥሎ ያንብቡ")
    fun save(lang: AppLanguage) = lang.pick("Save", "አስቀምጥ")
    fun saved(lang: AppLanguage) = lang.pick("Saved", "ተቀምጧል")
    fun share(lang: AppLanguage) = lang.pick("Share", "አጋራ")
    fun copyLink(lang: AppLanguage) = lang.pick("Copy link", "አገናኝ ቅዳ")
    fun listen(lang: AppLanguage) = lang.pick("Listen", "አዳምጥ")
    fun stopListening(lang: AppLanguage) = lang.pick("Stop", "አቁም")
    fun report(lang: AppLanguage) = lang.pick("Report", "ሪፖርት አድርግ")

    fun profileTitle(lang: AppLanguage) = lang.pick("Profile & Settings", "መገለጫ እና ቅንብሮች")
    fun appearance(lang: AppLanguage) = lang.pick("Appearance", "ገጽታ")
    fun languageLabel(lang: AppLanguage) = lang.pick("Language / ቋንቋ", "ቋንቋ / Language")
    fun lightMode(lang: AppLanguage) = lang.pick("Light", "ብርሃን")
    fun darkMode(lang: AppLanguage) = lang.pick("Dark", "ጨለማ")
    fun autoMode(lang: AppLanguage) = lang.pick("Auto", "ራስ-ሰር")
    fun notifications(lang: AppLanguage) = lang.pick("Notification Preferences", "የማሳወቂያ ምርጫዎች")
    fun editorialStudio(lang: AppLanguage) = lang.pick("Editorial Studio", "የጽሑፍ ስቱዲዮ")
    fun clearSavedData(lang: AppLanguage) = lang.pick("Clear saved data", "የተቀመጠ መረጃ አጽዳ")
}
