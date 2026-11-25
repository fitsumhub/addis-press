package com.example.data.catalog

import androidx.compose.ui.graphics.Color
import com.example.data.model.BannerAdUnit
import com.example.data.model.NativeAdUnit

object AdCatalog {

    val nativeAds: List<NativeAdUnit> = listOf(
        NativeAdUnit(
            id = "ad_native_chapa",
            sponsorName = "Chapa Business",
            headlineEn = "Accept Telebirr, CBE Birr & Cards in Minutes",
            headlineAm = "ቴሌብር፣ CBE ብር እና ካርዶችን በደቂቃዎች ይቀበሉ",
            bodyEn = "Fast ETB checkout APIs, instant payouts, and merchant dashboard built for Ethiopian apps and stores.",
            bodyAm = "ለኢትዮጵያ መተግበሪያዎችና ንግዶች የተዘጋጀ ፈጣን የክፍያ መቀበያ API።",
            badgeText = "FINTECH PARTNER",
            ctaEn = "Get Started",
            ctaAm = "አሁን ይጀምሩ",
            destinationUrl = "https://chapa.co",
            gradientStart = Color(0xFF065F46),
            gradientEnd = Color(0xFF10B981),
            iconInitials = "CH",
            categoryTag = "Sponsored · Fintech"
        ),
        NativeAdUnit(
            id = "ad_native_gemini",
            sponsorName = "Google Workspace & AI",
            headlineEn = "Write, Code & Summarize Faster with Gemini",
            headlineAm = "በGemini በፍጥነት ይጻፉ፣ ኮድ ያዘጋጁ እና ያጠቃልሉ",
            bodyEn = "Turn lecture notes into study guides, draft clean proposals, and debug Android apps faster.",
            bodyAm = "የትምህርት ማስታወሻዎችን በቀላሉ ያጠቃልሉ እና የፕሮግራሚንግ ሥራዎን ያፋጥኑ።",
            badgeText = "AI PRODUCTIVITY",
            ctaEn = "Try Free",
            ctaAm = "በነጻ ይሞክሩ",
            destinationUrl = "https://gemini.google.com",
            gradientStart = Color(0xFF1E3A8A),
            gradientEnd = Color(0xFF3B82F6),
            iconInitials = "GM",
            categoryTag = "Sponsored · AI Tools"
        ),
        NativeAdUnit(
            id = "ad_native_alx",
            sponsorName = "ALX Ethiopia Tech Hub",
            headlineEn = "Launch Your Software Engineering & Data Career",
            headlineAm = "የሶፍትዌር ምህንድስና እና የዳታ ሳይንስ ሙያዎን ይጀምሩ",
            bodyEn = "Apply for career-focused AI, Cloud, and Software Engineering tracks with Addis hub access.",
            bodyAm = "በአዲስ አበባ የቴክ ማዕከል እና በኦንላይን የAI እና የሶፍትዌር ስልጠናዎችን ይቀላቀሉ።",
            badgeText = "CAREER TRACK",
            ctaEn = "Apply Now",
            ctaAm = "አሁን ይመዝገቡ",
            destinationUrl = "https://www.alxafrica.com",
            gradientStart = Color(0xFF7C2D12),
            gradientEnd = Color(0xFFF59E0B),
            iconInitials = "AX",
            categoryTag = "Sponsored · Education"
        ),
        NativeAdUnit(
            id = "ad_native_accubattery",
            sponsorName = "AccuBattery Pro",
            headlineEn = "Protect Your Android Battery Lifespan",
            headlineAm = "የአንድሮይድ ስልክዎን ባትሪ ዕድሜ ያራዝሙ",
            bodyEn = "Measure real charger wattage, spot battery-draining apps, and set smart 80% charge alarms.",
            bodyAm = "የቻርጀር ፍጥነትን ይለኩ እና 80% ሲሞላ ማስጠንቀቂያ ያግኙ።",
            badgeText = "TOP UTILITY",
            ctaEn = "Install",
            ctaAm = "ጫን",
            destinationUrl = "https://play.google.com/store/apps/details?id=com.digibites.accubattery",
            gradientStart = Color(0xFF0F766E),
            gradientEnd = Color(0xFF14B8A6),
            iconInitials = "AB",
            categoryTag = "Sponsored · Android App"
        ),
        NativeAdUnit(
            id = "ad_native_canva",
            sponsorName = "Canva Design Studio",
            headlineEn = "Design Social Posts & Thumbnails on Mobile",
            headlineAm = "ለማህበራዊ ሚዲያ የሚሆኑ ምስሎችን በስልክዎ ይፍጠሩ",
            bodyEn = "Thousands of free templates for Telegram channels, startup pitch decks, and Amharic banners.",
            bodyAm = "ለቴሌግራም ቻናሎች እና ለቢዝነስ ማስታወቂያዎች ነጻ ዲዛይኖች።",
            badgeText = "CREATOR TOOL",
            ctaEn = "Install",
            ctaAm = "ጫን",
            destinationUrl = "https://play.google.com/store/apps/details?id=com.canva.editor",
            gradientStart = Color(0xFF4C1D95),
            gradientEnd = Color(0xFF8B5CF6),
            iconInitials = "CV",
            categoryTag = "Sponsored · Design"
        )
    )

    val bannerAds: List<BannerAdUnit> = listOf(
        BannerAdUnit(
            id = "ad_banner_chapa",
            sponsorName = "Chapa Checkout",
            offerEn = "Accept ETB online payments in 5 mins",
            offerAm = "በ5 ደቂቃ ውስጥ የኦንላይን ክፍያ ይቀበሉ",
            ctaEn = "SIGN UP",
            ctaAm = "ተመዝገብ",
            destinationUrl = "https://chapa.co",
            accentColor = Color(0xFF16A34A)
        ),
        BannerAdUnit(
            id = "ad_banner_vivaldi",
            sponsorName = "Vivaldi Browser",
            offerEn = "Save mobile data with built-in ad block",
            offerAm = "ዳታ ቆጣቢ ፈጣን አሳሽ በነጻ ይጫኑ",
            ctaEn = "INSTALL",
            ctaAm = "ጫን",
            destinationUrl = "https://play.google.com/store/apps/details?id=com.vivaldi.browser",
            accentColor = Color(0xFF2563EB)
        ),
        BannerAdUnit(
            id = "ad_banner_perplexity",
            sponsorName = "Perplexity AI",
            offerEn = "Ask anything with real-time cited sources",
            offerAm = "ከተረጋገጡ ምንጮች ጋር ፈጣን የAI መልስ ያግኙ",
            ctaEn = "OPEN",
            ctaAm = "ክፈት",
            destinationUrl = "https://www.perplexity.ai",
            accentColor = Color(0xFF0D9488)
        )
    )

    fun nativeAdAt(index: Int): NativeAdUnit = nativeAds[index % nativeAds.size]

    fun bannerAdAt(index: Int): BannerAdUnit = bannerAds[index % bannerAds.size]
}
