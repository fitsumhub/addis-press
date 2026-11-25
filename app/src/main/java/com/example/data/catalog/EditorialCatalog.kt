package com.example.data.catalog

import androidx.compose.ui.graphics.Color
import com.example.data.model.AppNotification
import com.example.data.model.Article
import com.example.data.model.ArticleBlock
import com.example.data.model.EditorialCategory

object EditorialCatalog {

    val categories: List<EditorialCategory> = listOf(
        EditorialCategory("technology", "Technology", "ቴክኖሎጂ", "Ethiopia & global tech updates", "የኢትዮጵያና የዓለም ቴክኖሎጂ ዜናዎች", Color(0xFF3B82F6)),
        EditorialCategory("ai", "Artificial Intelligence", "አርቲፊሻል ኢንተለጀንስ", "AI models, prompts & tutorials", "የAI ሞዴሎች እና ተግባራዊ መመሪያዎች", Color(0xFF6366F1)),
        EditorialCategory("business", "Business", "ቢዝነስ", "Digital commerce & strategy", "ዲጂታል ንግድ እና ስትራቴጂ", Color(0xFFF59E0B)),
        EditorialCategory("startups", "Startups", "ስታርትአፖች", "Addis founders & venture news", "የአዲስ አበባ ጅምር ኩባንያዎች", Color(0xFF10B981)),
        EditorialCategory("apps", "Mobile Applications", "የሞባይል መተግበሪያዎች", "Essential Android apps", "አስፈላጊ የአንድሮይድ መተግበሪያዎች", Color(0xFF0EA5E9)),
        EditorialCategory("app_reviews", "App Reviews", "የመተግበሪያ ግምገማዎች", "Hands-on software testing", "ዝርዝር የመተግበሪያ ግምገማዎች", Color(0xFF8B5CF6)),
        EditorialCategory("web_dev", "Web Development", "የድር ልማት", "Modern frontend & cloud web", "ዘመናዊ የድር ጣቢያ ልማት", Color(0xFF14B8A6)),
        EditorialCategory("programming", "Programming", "ፕሮግራሚንግ", "Kotlin, Dart & Python guides", "የኮትሊን፣ ዳርት እና ፓይተን መመሪያዎች", Color(0xFFEC4899)),
        EditorialCategory("cybersecurity", "Cybersecurity", "ሳይበር ደህንነት", "Privacy, SIM & account safety", "የስልክ እና የመለያ ደህንነት", Color(0xFFEF4444)),
        EditorialCategory("marketing", "Digital Marketing", "ዲጂታል ማርኬቲንግ", "Growth & creator monetization", "የዲጂታል ገበያ እና የፈጣሪዎች ዕድገት", Color(0xFFF97316)),
        EditorialCategory("fintech", "Finance & Fintech", "ፋይናንስ እና ፊንቴክ", "Telebirr, Chapa & digital banking", "ቴሌብር፣ ቻፓ እና ዲጂታል ባንክ", Color(0xFF22C55E)),
        EditorialCategory("innovation", "Innovation", "ፈጠራ", "Hardware, clean tech & R&D", "አዳዲስ የቴክኖሎጂ ፈጠራዎች", Color(0xFF06B6D4)),
        EditorialCategory("tutorials", "Tutorials", "ተግባራዊ መመሪያዎች", "Step-by-step how-to guides", "ደረጃ በደረጃ የአጠቃቀም መመሪያዎች", Color(0xFFEAB308)),
        EditorialCategory("opportunities", "Digital Opportunities", "ዲጂታል ዕድሎች", "Scholarships, remote jobs & grants", "የነጻ ትምህርት እና የርቀት ሥራ ዕድሎች", Color(0xFFA855F7))
    )

    fun categoryById(id: String): EditorialCategory =
        categories.firstOrNull { it.id == id } ?: categories.first()

    val articles: List<Article> = listOf(
        Article(
            id = "fayda_national_id",
            titleEn = "Fayda National ID Ethiopia: What It Is, How to Register, and How to Keep It Safe",
            titleAm = "የፋይዳ ብሔራዊ መታወቂያ፡ ምንድነው? እንዴት ይመዘገባል? እንዴትስ እንጠብቀው?",
            summaryEn = "A practical guide to Ethiopia's 12-digit biometric digital ID, bank & SIM linking, and security practices every citizen should know.",
            summaryAm = "ስለ ኢትዮጵያ ባለ 12 አሃዝ ዲጂታል መታወቂያ፣ ከባንክና ሲም ካርድ ጋር ስለማገናኘት እና ስለ መረጃ ደህንነት የተዘጋጀ ሙሉ መመሪያ።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Fayda is Ethiopia's foundational digital identity program managed by the National ID Program (NIDP). Designed to unify fragmented paper IDs, your 12-digit Fayda Identification Number (FIN) streamlines banking, telecom SIM verification, university enrollment, and government e-services."),
                ArticleBlock.Heading("Before You Visit a Registration Center", "Checklist"),
                ArticleBlock.BulletList(listOf(
                    "Bring an existing kebele ID, passport, driver's license, or an eligible witness if registering for the first time.",
                    "Keep an active Ethiopian phone number ready to receive your confirmation SMS and download the Fayda digital card.",
                    "Registration is free at designated Ethio Post, Mesob, bank, and woreda centers."
                )),
                ArticleBlock.Heading("Step-by-Step Registration Process", "Steps"),
                ArticleBlock.NumberedSteps(listOf(
                    "Locate an authorized Fayda enrollment station near your sub-city or bank branch.",
                    "Provide demographic details accurately — verify spelling in both Amharic and English on the operator screen.",
                    "Complete biometric capture (iris scan, fingerprints, and high-resolution portrait).",
                    "Receive your tracking slip and wait for the SMS containing your digital download link."
                )),
                ArticleBlock.Callout("Security Reminder", "Never share your Fayda OTP code or QR screenshot on public social media groups. Treat your FIN with the same caution as your bank PIN.")
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("ፋይዳ በብሔራዊ መታወቂያ ፕሮግራም የሚተዳደር የኢትዮጵያ ዲጂታል መታወቂያ ስርዓት ነው። ባለ 12 አሃዝ የፋይዳ መለያ ቁጥር (FIN) በባንኮች፣ በቴሌኮም አገልግሎቶች፣ በዩኒቨርሲቲ ምዝገባ እና በመንግስት ዲጂታል አገልግሎቶች ፈጣን ማረጋገጫ ይሰጣል።"),
                ArticleBlock.Heading("ከመመዝገብዎ በፊት ማዘጋጀት ያለብዎት", "ቅድመ-ዝግጅት"),
                ArticleBlock.BulletList(listOf(
                    "የቀበሌ መታወቂያ፣ ፓስፖርት ወይም የመንጃ ፈቃድ ይዘው ይቅረቡ።",
                    "የማረጋገጫ መልዕክት የሚደርስበት ንቁ የስልክ ቁጥር ይኑርዎት።",
                    "ምዝገባው በተመረጡ የፖስታ ቤት፣ ባንክ እና ወረዳ ማዕከላት በነጻ ይሰጣል።"
                )),
                ArticleBlock.Heading("የምዝገባ ደረጃዎች", "ደረጃዎች"),
                ArticleBlock.NumberedSteps(listOf(
                    "በአቅራቢያዎ የሚገኝ የፋይዳ ምዝገባ ማዕከል ይሂዱ።",
                    "ስምዎን እና የግል መረጃዎን በአማርኛና በእንግሊዝኛ ትክክለኛ መሆኑን በስክሪኑ ላይ ያረጋግጡ።",
                    "የጣት አሻራ፣ የአይን እና የፊት ፎቶ መረጃ ይስጡ።",
                    "የምዝገባ ደረሰኝ ተቀብለው በኤስኤምኤስ የሚላክልዎትን ዲጂታል ካርድ ያውርዱ።"
                )),
                ArticleBlock.Callout("የደህንነት ማሳሰቢያ", "የፋይዳ OTP ኮድዎን ወይም የQR ኮድ ፎቶዎን በማህበራዊ ሚዲያ በፍጹም አያጋሩ።")
            ),
            coverKey = "fayda",
            categoryId = "technology",
            tags = listOf("Fayda", "Digital ID", "Ethiopia", "eGov"),
            authorName = "Dawit Mekonnen",
            authorRole = "Senior Tech Editor",
            publishedDate = "Nov 22, 2026",
            readingMinutes = 4,
            isBreaking = true,
            isFeatured = true,
            isTrending = true,
            articleUrl = "https://addispress.et/articles/fayda-national-id-guide"
        ),
        Article(
            id = "chapa_telebirr_fintech",
            titleEn = "How Ethiopian Developers Integrate Telebirr & Chapa Payments in Mobile Apps",
            titleAm = "የኢትዮጵያ ገንቢዎች ቴሌብርን እና ቻፓን በሞባይል መተግበሪያዎች ውስጥ እንዴት ይጠቀማሉ?",
            summaryEn = "Comparing merchant onboarding, webhook verification, and checkout UX across Ethiopia's leading mobile payment gateways.",
            summaryAm = "በኢትዮጵያ ዋና ዋና የክፍያ መንገዶች ላይ የነጋዴ ምዝገባ፣ የዌብሁክ ማረጋገጫ እና የክፍያ ፍሰት አሰራር ንጽጽር።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Digital payments in Ethiopia have grown exponentially, allowing local SaaS founders, e-commerce stores, and creators to accept Ethiopian Birr directly inside Android apps and web checkouts."),
                ArticleBlock.Heading("Key Integration Patterns", "Architecture"),
                ArticleBlock.BulletList(listOf(
                    "Always verify payment webhooks on your backend server — never trust client-side callback status alone.",
                    "Design resilient retry flows for intermittent 3G/4G mobile connections.",
                    "Support clear ETB currency formatting and instant receipt downloads."
                )),
                ArticleBlock.Callout("Developer Tip", "Store merchant API keys strictly on your server environment, never inside Android APK source code.")
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("በኢትዮጵያ የዲጂታል ክፍያ ስርዓት በፍጥነት እያደገ በመምጣቱ የአገር ውስጥ የሶፍትዌር ገንቢዎች በቀላሉ ክፍያዎችን መቀበል ችለዋል።"),
                ArticleBlock.Heading("ዋና የቴክኒክ መመሪያዎች", "አሰራር"),
                ArticleBlock.BulletList(listOf(
                    "የክፍያ ማረጋገጫ (Webhook) ሁልጊዜ በሰርቨር በኩል መረጋገጥ አለበት።",
                    "ለተቆራረጠ የኢንተርኔት ግንኙነት የሚመች ድጋሚ መሞከሪያ ስርዓት ይገንቡ።",
                    "የብር መጠንን በግልጽ ማሳየትና ፈጣን ደረሰኝ መስጠት የተጠቃሚን እምነት ያሳድጋል።"
                )),
                ArticleBlock.Callout("ለገንቢዎች ምክር", "የAPI ሚስጥራዊ ቁልፎችን በሞባይል መተግበሪያ ኮድ ውስጥ በፍጹም አያስቀምጡ።")
            ),
            coverKey = "fintech",
            categoryId = "fintech",
            tags = listOf("Telebirr", "Chapa", "Fintech", "Android"),
            authorName = "Selamawit Tadesse",
            authorRole = "Fintech Analyst",
            publishedDate = "Nov 20, 2026",
            readingMinutes = 5,
            isFeatured = true,
            isTrending = true,
            articleUrl = "https://addispress.et/articles/chapa-telebirr-fintech"
        ),
        Article(
            id = "ai_amharic_productivity",
            titleEn = "Using Modern AI Assistants Effectively for Research, Coding & Amharic Writing",
            titleAm = "ዘመናዊ የAI ረዳቶችን ለጥናት፣ ለኮዲንግ እና ለአማርኛ ጽሑፍ እንዴት በብቃት እንጠቀም?",
            summaryEn = "Practical prompting techniques for university students, software engineers, and content teams working across English and Amharic.",
            summaryAm = "ለዩኒቨርሲቲ ተማሪዎች፣ ለፕሮግራመሮች እና ለይዘት ፈጣሪዎች የAI መሳሪያዎችን በእንግሊዝኛና በአማርኛ በጥራት የመጠቀሚያ ዘዴዎች።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Large language models have become daily productivity companions for Ethiopian students and engineers. Yet getting accurate, culturally grounded results requires structured prompting rather than one-line queries."),
                ArticleBlock.Heading("Prompting Framework for Better Output", "Guide"),
                ArticleBlock.NumberedSteps(listOf(
                    "Specify the role and audience (e.g., 'Explain REST APIs to a 2nd-year computer science student').",
                    "Provide local context or constraints such as low-bandwidth mobile environments.",
                    "Ask for bilingual summaries or terminology glossaries when studying technical subjects.",
                    "Always verify citations, math calculations, and code snippets independently."
                )),
                ArticleBlock.Callout("Academic Integrity", "Use AI to brainstorm outlines, debug errors, and quiz yourself — never submit unverified generated essays.")
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("ዘመናዊ የAI ሞዴሎች ለኢትዮጵያውያን ተማሪዎችና መሐንዲሶች ዕለታዊ የሥራ አጋዥ ሆነዋል። ሆኖም ትክክለኛ ውጤት ለማግኘት ግልጽ የሆነ መመሪያ (Prompt) መስጠት አስፈላጊ ነው።"),
                ArticleBlock.Heading("ጥራት ያለው ውጤት ለማግኘት 4 ደረጃዎች", "መመሪያ"),
                ArticleBlock.NumberedSteps(listOf(
                    "የAI ረዳቱን ሚና እና ተደራሲውን በግልጽ ይጥቀሱ።",
                    "አካባቢያዊ ሁኔታዎችን (ለምሳሌ ዝቅተኛ ኢንተርኔት) ያካትቱ።",
                    "ቴክኒካዊ ቃላትን በእንግሊዝኛና በአማርኛ እንዲያብራራ ይጠይቁ።",
                    "የተሰጠውን መረጃ እና ኮድ ሁልጊዜ በራስዎ ያረጋግጡ።"
                )),
                ArticleBlock.Callout("ማሳሰቢያ", "AIን ለመማሪያና ለስህተት ማረሚያ ይጠቀሙ እንጂ ያልተረጋገጠ ጽሑፍን ቀጥታ አያስገቡ።")
            ),
            coverKey = "ai",
            categoryId = "ai",
            tags = listOf("AI", "Prompting", "Education", "Amharic"),
            authorName = "Yonas Alemu",
            authorRole = "AI & ML Contributor",
            publishedDate = "Nov 19, 2026",
            readingMinutes = 4,
            isFeatured = true,
            isTrending = true,
            articleUrl = "https://addispress.et/articles/ai-amharic-productivity"
        ),
        Article(
            id = "accubattery_review",
            titleEn = "AccuBattery Review: Track and Protect Your Phone's Battery Health",
            titleAm = "የAccuBattery ግምገማ፡ የስልክዎን ባትሪ ጤንነት ይቆጣጠሩ እና ይጠብቁ",
            summaryEn = "A hands-on review of how AccuBattery measures real charging wattage, background drain, and long-term battery wear on Android.",
            summaryAm = "AccuBattery በአንድሮይድ ስልኮች ላይ የኃይል መሙያ ፍጥነትን እና የባትሪ ዕድሜን እንዴት እንደሚለካ የሚያሳይ ዝርዝር ግምገማ።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Replacing a smartphone battery in Addis Ababa can be expensive and risky if third-party parts are low quality. AccuBattery helps Android users preserve lithium-ion battery longevity using science-backed charge alerts."),
                ArticleBlock.Heading("Why It Stands Out", "Review"),
                ArticleBlock.BulletList(listOf(
                    "Real-time charge current (mA) and watt measurement to test original vs fake chargers.",
                    "80% charge alarm to reduce high-voltage battery stress.",
                    "Per-app foreground and screen-off drain breakdown."
                )),
                ArticleBlock.Callout("Verdict", "Essential for anyone keeping an Android phone for 3+ years. No root access required.")
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("የስልክ ባትሪ መቀየር ከፍተኛ ወጪ ስለሚጠይቅ ባትሪን አስቀድሞ መጠበቅ ብልህነት ነው። AccuBattery የባትሪዎን ትክክለኛ አቅም እና የቻርጀር ጥራት ለመለካት ይረዳል።"),
                ArticleBlock.Heading("ዋና ጥቅሞቹ", "ግምገማ"),
                ArticleBlock.BulletList(listOf(
                    "የቻርጀርን ትክክለኛ ፍጥነት (mA/Watt) በቀጥታ ያሳያል።",
                    "ባትሪው 80% ሲደርስ የማስጠንቀቂያ ድምጽ ያሰማል።",
                    "የትኛው መተግበሪያ ብዙ ባትሪ እንደሚጨርስ ይለያል።"
                )),
                ArticleBlock.Callout("ማጠቃለያ", "ስልክዎን ለረጅም ዓመታት በጥራት ለመጠቀም የሚረዳ ምርጥ ነጻ መተግበሪያ ነው።")
            ),
            coverKey = "apps",
            categoryId = "app_reviews",
            tags = listOf("Android", "Battery", "Utilities", "App Review"),
            authorName = "Blen Girma",
            authorRole = "Mobile Apps Editor",
            publishedDate = "Nov 18, 2026",
            readingMinutes = 5,
            isAppReview = true,
            linkedAppId = "app_accubattery",
            articleUrl = "https://addispress.et/articles/accubattery-review"
        ),
        Article(
            id = "vivaldi_browser_review",
            titleEn = "Chrome vs Vivaldi on Android: Why Vivaldi Is Worth Trying for Power Readers",
            titleAm = "ክሮም እና ቪቫልዲ በአንድሮይድ፡ ለምን ቪቫልዲን መሞከር አለብዎት?",
            summaryEn = "Built-in tracker blocking, bottom address bar ergonomics, and desktop-style tabs make Vivaldi a compelling Chrome alternative.",
            summaryAm = "አብሮ የተሰራ የማስታወቂያ መከላከያ፣ የታችኛው የአድራሻ አሞሌ እና ምቹ የታብ አደረጃጀት ቪቫልዲን ተመራጭ ያደርጉታል።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Most Android devices ship with Chrome, yet power readers and developers often crave deeper customization and lower mobile data overhead."),
                ArticleBlock.Heading("Standout Features", "Comparison"),
                ArticleBlock.BulletList(listOf(
                    "Bottom address bar and tab bar for effortless one-handed reach.",
                    "Built-in ad and tracker blocking that saves mobile data bundles.",
                    "Integrated encrypted notes and full-page screenshot tool."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("አብዛኛው ሰው ክሮምን ቢጠቀምም ቪቫልዲ የሞባይል ዳታን በመቆጠብ እና በአንድ እጅ ለመጠቀም ምቹ በመሆኑ ተወዳጅነትን እያገኘ ነው።"),
                ArticleBlock.Heading("ልዩ ባህሪያት", "ንጽጽር"),
                ArticleBlock.BulletList(listOf(
                    "በታችኛው ክፍል የሚገኝ የአድራሻ መጻፊያ እና የታብ መቀየሪያ።",
                    "የሞባይል ዳታን የሚቆጥብ የማስታወቂያ መከላከያ።",
                    "ገጾችን በሙሉ ፎቶ የማንሳት እና ማስታወሻ የመያዝ አገልግሎት።"
                ))
            ),
            coverKey = "apps",
            categoryId = "app_reviews",
            tags = listOf("Browser", "Vivaldi", "Data Saver", "Android"),
            authorName = "Blen Girma",
            authorRole = "Mobile Apps Editor",
            publishedDate = "Nov 16, 2026",
            readingMinutes = 4,
            isAppReview = true,
            linkedAppId = "app_vivaldi",
            articleUrl = "https://addispress.et/articles/vivaldi-android-review"
        ),
        Article(
            id = "gemini_workspace_review",
            titleEn = "Gemini on Android Review: Study Planning, Summarization & Daily Workflows",
            titleAm = "የGoogle Gemini መተግበሪያ ግምገማ፡ ለጥናት፣ ለማጠቃለያ እና ለዕለታዊ ሥራ",
            summaryEn = "How students and founders use Gemini on Android for structured study schedules, document breakdowns, and coding support.",
            summaryAm = "ተማሪዎችና ሥራ ፈጣሪዎች Geminiን በአንድሮይድ ላይ ለጥናት ዕቅድ፣ ለጽሑፍ ማጠቃለያ እና ለኮዲንግ እንዴት እንደሚጠቀሙት።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Whether preparing for university exams or drafting product specs, having an AI assistant integrated cleanly into Android saves hours each week."),
                ArticleBlock.Heading("Practical Workflows", "AI Guide"),
                ArticleBlock.BulletList(listOf(
                    "Turn dense lecture PDFs into structured revision flashcards.",
                    "Draft clean English business proposals from rough bullet notes.",
                    "Compare Kotlin or Python errors with clear explanations."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("ለፈተና ዝግጅትም ሆነ ለቢዝነስ ዕቅድ ዝግጅት ዘመናዊ የAI መሳሪያዎች በአንድሮይድ ስልክ ላይ ከፍተኛ ጊዜ ይቆጥባሉ።"),
                ArticleBlock.Heading("ተግባራዊ አጠቃቀም", "መመሪያ"),
                ArticleBlock.BulletList(listOf(
                    "ረጃጅም የትምህርት ጽሑፎችን ወደ አጫጭር የጥናት ነጥቦች መቀየር።",
                    "የቢዝነስ ፕሮፖዛሎችን እና ኢሜይሎችን በጥራት ማዘጋጀት።",
                    "የፕሮግራሚንግ ስህተቶችን በቀላሉ መረዳትና ማስተካከል።"
                ))
            ),
            coverKey = "ai",
            categoryId = "ai",
            tags = listOf("Gemini", "AI Tools", "Study", "Android"),
            authorName = "Yonas Alemu",
            authorRole = "AI & ML Contributor",
            publishedDate = "Nov 15, 2026",
            readingMinutes = 4,
            isAppReview = true,
            linkedAppId = "app_gemini",
            articleUrl = "https://addispress.et/articles/gemini-android-review"
        ),
        Article(
            id = "ethiopia_sim_cybersecurity",
            titleEn = "Protect Your Phone From SIM Swap Scams, Phishing & Account Hijacking",
            titleAm = "ስልክዎን ከSIM ማጭበርበር፣ ከPhishing እና ከመለያ ጠለፋ እንዴት ይጠብቁ?",
            summaryEn = "Essential 5-minute security hardening steps for Ethiopian mobile banking, Telegram, and Google accounts.",
            summaryAm = "ለሞባይል ባንኪንግ፣ ለቴሌግራም እና ለGoogle መለያዎች በ5 ደቂቃ ውስጥ የሚደረጉ ወሳኝ የደህንነት እርምጃዎች።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("With millions of Ethiopians relying on Telegram and mobile banking daily, phishing links and SIM-related account takeovers have surged."),
                ArticleBlock.Heading("5 Essential Safeguards", "Security"),
                ArticleBlock.NumberedSteps(listOf(
                    "Enable Two-Step Verification (Cloud Password) inside Telegram Settings -> Privacy and Security.",
                    "Set a SIM card PIN lock so a stolen phone's SIM cannot receive banking OTPs in another device.",
                    "Never enter Google or bank passwords from links sent via unknown SMS or group chats.",
                    "Use an authenticator app instead of SMS 2FA wherever supported.",
                    "Review active sessions monthly and sign out unrecognized devices."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("በሚሊዮን የሚቆጠሩ ኢትዮጵያውያን ቴሌግራምን እና ሞባይል ባንኪንግን በሚጠቀሙበት በዚህ ወቅት የመለያ ጠለፋ እና የማጭበርበር መልዕክቶች እየጨመሩ ነው።"),
                ArticleBlock.Heading("5 ወሳኝ የደህንነት እርምጃዎች", "ደህንነት"),
                ArticleBlock.NumberedSteps(listOf(
                    "በቴሌግራም ውስጥ Two-Step Verification (ተጨማሪ የይለፍ ቃል) ያብሩ።",
                    "ስልክ ቢሰረቅ OTP እንዳይነበብ የሲም ካርድ PIN ቁልፍ ያዘጋጁ።",
                    "ከማይታወቁ መልዕክቶች የሚላኩ አጠራጣሪ ሊንኮችን አይክፈቱ።",
                    "በተቻለ መጠን ከSMS ይልቅ Authenticator መተግበሪያዎችን ይጠቀሙ።",
                    "መለያዎ የተከፈተባቸውን መሳሪያዎች በየወሩ ይፈትሹ።"
                ))
            ),
            coverKey = "fayda",
            categoryId = "cybersecurity",
            tags = listOf("Security", "Telegram", "2FA", "Privacy"),
            authorName = "HannaTesfaye",
            authorRole = "Security Researcher",
            publishedDate = "Nov 14, 2026",
            readingMinutes = 5,
            isTrending = true,
            articleUrl = "https://addispress.et/articles/phone-security-ethiopia"
        ),
        Article(
            id = "addis_startup_ecosystem",
            titleEn = "Inside Addis Ababa's Emerging Tech Startups: Logistics, Agritech & EdTech",
            titleAm = "የአዲስ አበባ አዳዲስ የቴክኖሎጂ ስታርትአፖች፡ ሎጂስቲክስ፣ ግብርና እና ትምህርት",
            summaryEn = "How young Ethiopian founders are solving last-mile delivery, digital skills training, and farm market access.",
            summaryAm = "ወጣት ኢትዮጵያውያን የቴክኖሎጂ ሥራ ፈጣሪዎች የትራንስፖርት፣ የዲጂታል ትምህርት እና የግብርና ገበያ ችግሮችን እንዴት እየፈቱ ነው?",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Across Bole, Kazanchis, and university incubators in Addis Ababa, a new wave of engineering teams is building localized software tailored to Ethiopian infrastructure realities."),
                ArticleBlock.Heading("Three High-Momentum Sectors", "Startups"),
                ArticleBlock.BulletList(listOf(
                    "EdTech platforms offering Amharic-first coding, design, and freelancing tracks.",
                    "B2B retail and logistics apps connecting wholesalers directly to neighborhood shops.",
                    "Agritech SMS and smartphone tools delivering weather and market price alerts."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("በአዲስ አበባ እና በዩኒቨርሲቲ ማዕከላት የሚገኙ ወጣት መሐንዲሶች ለአገሪቱ ሁኔታ የሚስማሙ ዘመናዊ የሶፍትዌር መፍትሄዎችን እየገነቡ ነው።"),
                ArticleBlock.Heading("ፈጣን ዕድገት ያላቸው 3 ዘርፎች", "ስታርትአፕ"),
                ArticleBlock.BulletList(listOf(
                    "በአማርኛ የዲጂታል ክህሎትና የፍሪላንሲንግ ስልጠና የሚሰጡ መድረኮች።",
                    "ነጋዴዎችንና አከፋፋዮችን የሚያገናኙ የB2B ሎጂስቲክስ መተግበሪያዎች።",
                    "ለገበሬዎች የአየር ሁኔታ እና የገበያ ዋጋ መረጃ የሚያደርሱ መሳሪያዎች።"
                ))
            ),
            coverKey = "fintech",
            categoryId = "startups",
            tags = listOf("Startups", "Addis Ababa", "EdTech", "Business"),
            authorName = "Selamawit Tadesse",
            authorRole = "Fintech & Startup Analyst",
            publishedDate = "Nov 12, 2026",
            readingMinutes = 6,
            articleUrl = "https://addispress.et/articles/addis-startup-ecosystem"
        ),
        Article(
            id = "remote_tech_opportunities",
            titleEn = "Global Remote Tech Fellowships & Free Cloud Credits for Ethiopian Students",
            titleAm = "ለኢትዮጵያውያን ተማሪዎች ክፍት የሆኑ ዓለም አቀፍ የቴክኖሎጂ ዕድሎች እና ነጻ የክላውድ ስልጠናዎች",
            summaryEn = "Verified programs offering free developer certifications, open-source stipends, and remote internships accessible from Ethiopia.",
            summaryAm = "ከኢትዮጵያ ሆነው መሳተፍ የሚችሉባቸው ነጻ የዲጂታል ክህሎት ሰርተፊኬቶች፣ የክላውድ ክሬዲቶች እና የርቀት ልምምድ ፕሮግራሞች።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Building an international engineering portfolio no longer requires leaving Addis Ababa or Hawassa. Several verified global initiatives accept Ethiopian university students and self-taught builders."),
                ArticleBlock.Heading("Where to Start Applying", "Opportunities"),
                ArticleBlock.BulletList(listOf(
                    "GitHub Student Developer Pack: Free developer tools, domains, and cloud hosting credits with a university email or ID.",
                    "Google Summer of Code & Outreachy: Paid remote open-source mentorships open worldwide.",
                    "ALX & Coursera Financial Aid: Structured software engineering and data analytics tracks."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("ዓለም አቀፍ የሶፍትዌር ልምድ ለመገንባት ከኢትዮጵያ መውጣት አይጠበቅብዎትም። በርካታ የተረጋገጡ ዓለም አቀፍ ፕሮግራሞች ለኢትዮጵያውያን ተማሪዎች ክፍት ናቸው።"),
                ArticleBlock.Heading("ዋና ዋና ዕድሎች", "ዕድሎች"),
                ArticleBlock.BulletList(listOf(
                    "GitHub Student Developer Pack፡ በተማሪ መታወቂያ ነጻ የክላውድና የገንቢ መሳሪያዎች።",
                    "Google Summer of Code እና Outreachy፡ ክፍያ ያላቸው የክፍት ምንጭ ስልጠናዎች።",
                    "ALX እና Coursera Financial Aid፡ ነጻ የሶፍትዌር እና የዳታ ትምህርት ዕድሎች።"
                ))
            ),
            coverKey = "ai",
            categoryId = "opportunities",
            tags = listOf("Scholarships", "Remote Work", "Students", "GitHub"),
            authorName = "Dawit Mekonnen",
            authorRole = "Senior Tech Editor",
            publishedDate = "Nov 10, 2026",
            readingMinutes = 4,
            articleUrl = "https://addispress.et/articles/remote-tech-opportunities"
        ),
        Article(
            id = "kotlin_compose_guide",
            titleEn = "Why Jetpack Compose & Kotlin Multiplatform Are Winning East African Mobile Teams",
            titleAm = "Jetpack Compose እና Kotlin ለምን በምስራቅ አፍሪካ የሞባይል ገንቢዎች ዘንድ ተመራጭ ሆኑ?",
            summaryEn = "Faster UI iteration, smaller APK footprints on budget Android devices, and clean state management with Kotlin Coroutines.",
            summaryAm = "ፈጣን የUI ግንባታ፣ ለተመጣጣኝ ስልኮች የሚሆን አነስተኛ የመተግበሪያ መጠን እና ዘመናዊ የኮትሊን አሰራር።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("In markets where Android accounts for over 90% of smartphones, writing native Kotlin with Jetpack Compose delivers smooth 60fps performance even on entry-level devices."),
                ArticleBlock.Heading("Engineering Advantages", "Programming"),
                ArticleBlock.BulletList(listOf(
                    "Declarative UI reduces boilerplate XML layouts and layout inflation overhead.",
                    "Kotlin Flow and StateFlow provide predictable offline-first state synchronization.",
                    "Material 3 adaptive layouts scale seamlessly from compact phones to tablets."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("ከ90% በላይ ተጠቃሚዎች አንድሮይድን በሚጠቀሙበት ገበያ በKotlin እና Jetpack Compose መስራት ፈጣን እና ለስላሳ አፈጻጸምን ይሰጣል።"),
                ArticleBlock.Heading("የቴክኒክ ጥቅሞች", "ፕሮግራሚንግ"),
                ArticleBlock.BulletList(listOf(
                    "ያለ XML ውስብስብነት በቀጥታ በኮትሊን ዘመናዊ UI መገንባት።",
                    "በKotlin Flow አማካኝነት ዳታን ከኢንተርኔት ውጪም ማስተዳደር።",
                    "Material 3 ለሁሉም የስክሪን መጠኖች በራሱ የሚስተካከል ዲዛይን ይሰጣል።"
                ))
            ),
            coverKey = "apps",
            categoryId = "programming",
            tags = listOf("Kotlin", "Jetpack Compose", "Android", "Mobile Dev"),
            authorName = "Yonas Alemu",
            authorRole = "Engineering Lead",
            publishedDate = "Nov 08, 2026",
            readingMinutes = 5,
            articleUrl = "https://addispress.et/articles/kotlin-compose-east-africa"
        ),
        Article(
            id = "web_performance_ethiopia",
            titleEn = "Building Low-Bandwidth Web Apps Optimized for Ethiopian 3G & 4G Networks",
            titleAm = "ለኢትዮጵያ የ3G እና 4G ኢንተርኔት ፍጥነት የተመቻቹ የድር ጣቢያዎችን እንዴት እንገንባ?",
            summaryEn = "Image compression, edge caching, and lightweight bundle strategies that make websites load under 2 seconds in Addis.",
            summaryAm = "የምስል መጠን መቀነስ፣ ካሺንግ እና አነስተኛ ፋይሎችን በመጠቀም ድር ጣቢያዎች በፍጥነት እንዲከፈቱ ማድረጊያ ዘዴዎች።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Heavy JavaScript bundles frustrate users on mobile data plans. Optimizing web performance is both a user-experience and business conversion superpower in Ethiopia."),
                ArticleBlock.Heading("Optimization Checklist", "Web Dev"),
                ArticleBlock.BulletList(listOf(
                    "Serve WebP/AVIF responsive images with explicit dimensions.",
                    "Eliminate unused third-party tracking scripts and render-blocking fonts.",
                    "Implement service worker caching for core editorial pages."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("ክብደት ያላቸው ድር ጣቢያዎች የሞባይል ዳታን ይጨርሳሉ። ቀላል እና ፈጣን ድር ጣቢያ መገንባት የተጠቃሚን እርካታ በእጅጉ ይጨምራል።"),
                ArticleBlock.Heading("ዋና የማሻሻያ ነጥቦች", "የድር ልማት"),
                ArticleBlock.BulletList(listOf(
                    "ምስሎችን በWebP ቅርጽ እና በአነስተኛ መጠን ማቅረብ።",
                    "አላስፈላጊ የጃቫስክሪፕት ፋይሎችን መቀነስ።",
                    "ለፈጣን መከፈት የካሽ (Cache) ስርዓት መጠቀም።"
                ))
            ),
            coverKey = "ai",
            categoryId = "web_dev",
            tags = listOf("Web", "Performance", "Data Saver", "Frontend"),
            authorName = "Dawit Mekonnen",
            authorRole = "Senior Tech Editor",
            publishedDate = "Nov 06, 2026",
            readingMinutes = 4,
            articleUrl = "https://addispress.et/articles/low-bandwidth-web-apps"
        ),
        Article(
            id = "digital_marketing_creators",
            titleEn = "How Ethiopian Tech Creators & Small Businesses Grow With Ethical Digital Marketing",
            titleAm = "የኢትዮጵያ የቴክ ፈጣሪዎችና አነስተኛ ንግዶች በዲጂታል ማርኬቲንግ እንዴት ማደግ ይችላሉ?",
            summaryEn = "Building authentic Telegram channels, search-friendly Amharic blogs, and sustainable newsletter audiences.",
            summaryAm = "ታማኝ የቴሌግራም ቻናሎችን፣ በGoogle የሚገኙ የአማርኛ ጽሑፎችን እና ዘላቂ የደንበኛ ግንኙነት መገንባት።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Consistent educational content outperforms clickbait every single time. Local creators who solve genuine problems earn long-term audience trust and brand partnerships."),
                ArticleBlock.Heading("Sustainable Growth Channels", "Marketing"),
                ArticleBlock.BulletList(listOf(
                    "Pair concise Telegram summaries with deep-dive articles on the web.",
                    "Write clear bilingual headlines that answer real search questions.",
                    "Track real engagement and reader retention rather than vanity follower counts."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("ተከታታይ እና ጠቃሚ ትምህርታዊ ይዘት መፍጠር ከአሳሳች ርዕሶች ይልቅ ዘላቂ ተዓማኒነትን እና ዕድገትን ያመጣል።"),
                ArticleBlock.Heading("የዕድገት ስልቶች", "ማርኬቲንግ"),
                ArticleBlock.BulletList(listOf(
                    "አጫጭር የቴሌግራም ማብራሪያዎችን ከዝርዝር ጽሑፎች ጋር ማገናኘት።",
                    "ተጠቃሚዎች የሚፈልጉትን ጥያቄ የሚመልሱ ግልጽ ርዕሶችን መጻፍ።",
                    "ከሐሰተኛ ተከታዮች ይልቅ እውነተኛ የተጠቃሚ ተሳትፎ ላይ ማተኮር።"
                ))
            ),
            coverKey = "fintech",
            categoryId = "marketing",
            tags = listOf("Marketing", "Creators", "Telegram", "SEO"),
            authorName = "Selamawit Tadesse",
            authorRole = "Growth Strategist",
            publishedDate = "Nov 04, 2026",
            readingMinutes = 4,
            articleUrl = "https://addispress.et/articles/ethical-digital-marketing"
        ),
        Article(
            id = "budgetbakers_wallet_review",
            titleEn = "Wallet by BudgetBakers Review: Smart ETB Expense & Budget Tracking",
            titleAm = "የWallet by BudgetBakers ግምገማ፡ ወጪን እና በጀትን በብልሃት መቆጣጠር",
            summaryEn = "Organize cash, mobile banking transfers, and monthly savings goals cleanly in one Android personal finance app.",
            summaryAm = "የዕለት ወጪዎችን፣ የባንክ ዝውውሮችን እና ወርሃዊ የቁጠባ ዕቅዶችን በአንድ መተግበሪያ ውስጥ በቀላሉ ያደራጁ።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Managing cash, Telebirr transfers, and monthly rent in Ethiopia gets much easier when every expense category is logged in seconds."),
                ArticleBlock.Heading("Key Money Features", "Review"),
                ArticleBlock.BulletList(listOf(
                    "Custom ETB wallet categories for transport, groceries, internet bundles, and savings.",
                    "Clear weekly and monthly spending charts to catch hidden leaks.",
                    "Recurring bill reminders so utility and Wi-Fi renewals are never missed."
                )),
                ArticleBlock.Callout("Editorial Verdict", "A clean personal finance companion for university students and young professionals.")
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("የዕለት ተዕለት የገንዘብ ወጪን በቴሌብርና በጥሬ ገንዘብ መከታተል ወርሃዊ ቁጠባን ለማሳደግ ይረዳል።"),
                ArticleBlock.Heading("ዋና ዋና ባህሪያት", "ግምገማ"),
                ArticleBlock.BulletList(listOf(
                    "ለትራንስፖርት፣ ለምግብ እና ለኢንተርኔት ወጪዎች የተለያዩ ምድቦች።",
                    "ወርሃዊ ወጪዎን በግልጽ ግራፍ የሚያሳይ ዳሽቦርድ።",
                    "የወርሃዊ ክፍያዎች ማስታወሻ።"
                )),
                ArticleBlock.Callout("ማጠቃለያ", "በጀታቸውን በሥርዓት መምራት ለሚፈልጉ ተማሪዎችና ሠራተኞች ምርጥ ምርጫ ነው።")
            ),
            coverKey = "fintech",
            categoryId = "app_reviews",
            tags = listOf("Finance", "Budget", "App Review", "ETB"),
            authorName = "Selamawit Tadesse",
            authorRole = "Fintech Analyst",
            publishedDate = "Nov 02, 2026",
            readingMinutes = 4,
            isAppReview = true,
            articleUrl = "https://addispress.et/articles/wallet-budgetbakers-review"
        ),
        Article(
            id = "google_translate_amharic_guide",
            titleEn = "Google Translate Amharic Guide: Offline Packs, Camera Scan & Study Tips",
            titleAm = "የGoogle Translate የአማርኛ መመሪያ፡ ያለ ኢንተርኔት እና በካሜራ መተርጎም",
            summaryEn = "How to download offline Amharic-English language packs, scan printed textbook pages, and translate voice conversations.",
            summaryAm = "የአማርኛ እና እንግሊዝኛ ቋንቋ ፋይሎችን በማውረድ ያለ ኢንተርኔት እና በካሜራ ጽሑፎችን የመተርጎሚያ ዘዴዎች።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Students, travelers, and remote workers often need fast bilingual translation even when mobile data is weak or switched off."),
                ArticleBlock.Heading("Essential Offline & Camera Features", "Guide"),
                ArticleBlock.NumberedSteps(listOf(
                    "Open Google Translate -> Profile -> Downloaded languages and save Amharic + English (~45MB).",
                    "Use Instant Camera mode to translate printed manuals, medicine labels, or textbook diagrams.",
                    "Save frequently used technical phrases into your personal Phrasebook for quick review."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("ተማሪዎችና ተጓዦች የሞባይል ዳታ በማይኖርበት ጊዜ እንኳን በቀላሉ መተርጎም እንዲችሉ Google Translateን ማዘጋጀት ይችላሉ።"),
                ArticleBlock.Heading("ዋና የአጠቃቀም ደረጃዎች", "መመሪያ"),
                ArticleBlock.NumberedSteps(listOf(
                    "በመተግበሪያው ውስጥ Downloaded Languages በመክፈት አማርኛ እና እንግሊዝኛን ያውርዱ።",
                    "የታተሙ መጽሐፍትን እና ሰነዶችን በካሜራ በቀጥታ ይተርጉሙ።",
                    "አስፈላጊ ቃላትን በPhrasebook ውስጥ ያስቀምጡ።"
                ))
            ),
            coverKey = "apps",
            categoryId = "tutorials",
            tags = listOf("Translate", "Amharic", "Offline", "Study"),
            authorName = "Blen Girma",
            authorRole = "Mobile Apps Editor",
            publishedDate = "Oct 30, 2026",
            readingMinutes = 5,
            isTrending = true,
            articleUrl = "https://addispress.et/articles/google-translate-amharic-guide"
        ),
        Article(
            id = "dv_lottery_guide_ethiopia",
            titleEn = "DV Lottery Guide: Official Rules, Photo Specs & Avoiding Broker Scams",
            titleAm = "የDV ሎተሪ መመሪያ፡ ኦፊሴላዊ ህጎች፣ የፎቶ መስፈርቶች እና ከአጭበርባሪዎች መጠንቀቅ",
            summaryEn = "Everything Ethiopian applicants must verify before submitting an entry — photo dimensions, confirmation codes, and zero-fee official filing.",
            summaryAm = "አመልካቾች ማወቅ ያለባቸው ትክክለኛ የፎቶ መጠን፣ የማረጋገጫ ቁጥር አጠባበቅ እና በነጻ የመመዝገቢያ መንገድ።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Every registration season, thousands of applicants lose control of their confirmation numbers to third-party brokers. Applying directly from your phone takes under 15 minutes."),
                ArticleBlock.Heading("Photo & Submission Checklist", "Guide"),
                ArticleBlock.BulletList(listOf(
                    "Official entry on dvprogram.state.gov is always 100% free.",
                    "Use a recent 600x600 px square portrait taken against a plain white background with no filters.",
                    "Save your Confirmation Number in two secure places — never let someone else hold it hostage."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("በየዓመቱ ብዙ አመልካቾች የማረጋገጫ ቁጥራቸውን ለደላሎች በመስጠት ችግር ውስጥ ይገባሉ። በስልክዎ በራስዎ መመዝገብ ቀላልና ነጻ ነው።"),
                ArticleBlock.Heading("ዋና የምዝገባ ነጥቦች", "መመሪያ"),
                ArticleBlock.BulletList(listOf(
                    "በኦፊሴላዊው ድረ-ገጽ መመዝገብ ሁልጊዜ 100% ነጻ ነው።",
                    "600x600 ፒክስል የሆነ ነጭ ጀርባ ያለው ትክክለኛ ፎቶ ይጠቀሙ።",
                    "የConfirmation Number ቁጥርዎን በጥንቃቄ በግልዎ ያስቀምጡ።"
                ))
            ),
            coverKey = "fayda",
            categoryId = "opportunities",
            tags = listOf("DV Lottery", "Opportunities", "Photo Guide", "Safety"),
            authorName = "Dawit Mekonnen",
            authorRole = "Senior Tech Editor",
            publishedDate = "Oct 28, 2026",
            readingMinutes = 5,
            articleUrl = "https://addispress.et/articles/dv-lottery-guide-ethiopia"
        ),
        Article(
            id = "removebg_ai_review",
            titleEn = "Remove.bg & AI Photo Cutout Tools: Fast E-Commerce Product Edits",
            titleAm = "Remove.bg እና የAI ፎቶ ማጽጃ መሳሪያዎች፡ ለኦንላይን ንግድ ፈጣን ምስሎች",
            summaryEn = "Remove messy backgrounds from product photos, ID portraits, and social thumbnails in one tap on Android.",
            summaryAm = "የምርት ፎቶዎችን እና የማህበራዊ ሚዲያ ምስሎችን ጀርባ በአንድ ንክኪ በAI የማጽጃ መሳሪያዎች ግምገማ።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Ethiopian online sellers on Telegram and Instagram can dramatically boost buyer trust with clean, studio-style product backgrounds."),
                ArticleBlock.Heading("Why Creators Love Instant Cutouts", "Review"),
                ArticleBlock.BulletList(listOf(
                    "Clean hair and product edge detection without desktop Photoshop.",
                    "Swap in crisp white or gradient studio backgrounds in seconds.",
                    "Compress final PNG/WebP output to save customer mobile data."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("በቴሌግራም እና ኢንስታግራም የሚሸጡ ነጋዴዎች የምርት ፎቶዎቻቸውን ጀርባ በማጽዳት የተሻለ ሽያጭ ማግኘት ይችላሉ።"),
                ArticleBlock.Heading("ዋና ጥቅሞች", "ግምገማ"),
                ArticleBlock.BulletList(listOf(
                    "ያለ ኮምፒውተር በስልክ ብቻ የፎቶ ጀርባን በጥራት ማጥፋት።",
                    "ነጭ ወይም ዘመናዊ ቀለም ያለው ጀርባ በሰከንዶች መጨመር።",
                    "ፋይሉን በአነስተኛ መጠን በማውረድ ዳታ መቆጠብ።"
                ))
            ),
            coverKey = "apps",
            categoryId = "app_reviews",
            tags = listOf("Photo AI", "E-Commerce", "Remove.bg", "App Review"),
            authorName = "Blen Girma",
            authorRole = "Mobile Apps Editor",
            publishedDate = "Oct 25, 2026",
            readingMinutes = 4,
            isAppReview = true,
            articleUrl = "https://addispress.et/articles/removebg-ai-review"
        ),
        Article(
            id = "perplexity_real_time_ai",
            titleEn = "Perplexity & Real-Time AI Search: Cited Research for Students & Journalists",
            titleAm = "Perplexity AI፡ ከተረጋገጡ ምንጮች ጋር የሚመልስ ዘመናዊ የAI መፈለጊያ",
            summaryEn = "Why citation-first AI answer engines help Ethiopian university students and researchers verify facts faster than traditional link lists.",
            summaryAm = "ከምንጭ ማጣቀሻ ጋር ትክክለኛ መረጃ የሚሰጡ የAI መፈለጊያዎች ለተማሪዎችና ለተመራማሪዎች የሚሰጡት ጥቅም።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Unlike chatbots that guess from memory, answer engines like Perplexity search the live web and attach numbered citations to every paragraph."),
                ArticleBlock.Heading("Best Research Workflows", "AI"),
                ArticleBlock.BulletList(listOf(
                    "Use Academic Focus mode when preparing literature reviews or seminar papers.",
                    "Click numbered source badges to verify primary publications before quoting.",
                    "Ask follow-up questions in the same thread to compare regional statistics."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("Perplexity AI ከኢንተርኔት ላይ ትክክለኛ ምንጮችን በመፈለግ ከቁጥር ማጣቀሻ ጋር ግልጽ መልስ ይሰጣል።"),
                ArticleBlock.Heading("ለጥናት የሚረዱ ዘዴዎች", "AI"),
                ArticleBlock.BulletList(listOf(
                    "ለዩኒቨርሲቲ ጥናቶች Academic Focusን መጠቀም።",
                    "የተሰጡትን ምንጮች በመጫን ትክክለኛነታቸውን ማረጋገጥ።",
                    "ተጨማሪ ጥያቄዎችን በመጠየቅ መረጃን ማነጻጸር።"
                ))
            ),
            coverKey = "ai",
            categoryId = "ai",
            tags = listOf("Perplexity", "AI Search", "Research", "Students"),
            authorName = "Yonas Alemu",
            authorRole = "AI & ML Contributor",
            publishedDate = "Oct 22, 2026",
            readingMinutes = 4,
            isTrending = true,
            articleUrl = "https://addispress.et/articles/perplexity-realtime-ai"
        ),
        Article(
            id = "ethiopia_superapp_5g_shift",
            titleEn = "Ethiopia's SuperApp Shift: 5G Rollout, Mini-Apps & Digital Commerce",
            titleAm = "የኢትዮጵያ የSuperApp ለውጥ፡ የ5G መስፋፋት፣ Mini-Apps እና ዲጂታል ንግድ",
            summaryEn = "How faster mobile broadband and in-app mini-programs are reshaping utility payments, transport, and local e-commerce.",
            summaryAm = "ፈጣን የሞባይል ኢንተርኔት እና Mini-Apps የትራንስፖርት፣ የመብራትና የንግድ ክፍያዎችን እንዴት እየቀየሩ ነው?",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Across Addis Ababa, Adama, Hawassa, and Bahir Dar, expanding 4G/5G coverage is fueling lightweight mini-apps embedded inside payment super-apps."),
                ArticleBlock.Heading("What Changes for Local Builders", "Innovation"),
                ArticleBlock.BulletList(listOf(
                    "Zero-install web mini-apps lower customer acquisition cost for SMEs.",
                    "Instant QR settlement reduces cash handling friction for retail shops.",
                    "Lower latency enables richer live delivery tracking and cloud POS tools."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("በዋና ዋና ከተሞች እየተስፋፋ ያለው የ4G እና 5G ኔትወርክ በSuperApp ውስጥ የሚሰሩ ቀላል Mini-Appsን እያበራከተ ነው።"),
                ArticleBlock.Heading("ለአገር ውስጥ ንግዶች ያለው ፋይዳ", "ፈጠራ"),
                ArticleBlock.BulletList(listOf(
                    "ተጨማሪ መተግበሪያ መጫን ሳያስፈልግ አገልግሎት መስጠት።",
                    "በQR ኮድ ፈጣንና አስተማማኝ ክፍያ መፈጸም።",
                    "የትዕዛዝ እና የዴሊቨሪ ክትትልን በቀጥታ ማሳየት።"
                ))
            ),
            coverKey = "fintech",
            categoryId = "innovation",
            tags = listOf("5G", "SuperApp", "Digital Ethiopia", "Innovation"),
            authorName = "Dawit Mekonnen",
            authorRole = "Senior Tech Editor",
            publishedDate = "Oct 19, 2026",
            readingMinutes = 5,
            articleUrl = "https://addispress.et/articles/ethiopia-superapp-5g"
        ),
        Article(
            id = "upwork_freelancing_ethiopia",
            titleEn = "Remote Freelancing From Ethiopia: Portfolio, Proposals & Payment Tips",
            titleAm = "ከኢትዮጵያ ሆነው የርቀት ፍሪላንሲንግ መሥራት፡ ፖርትፎሊዮ እና የፕሮፖዛል ምክሮች",
            summaryEn = "Practical advice for Ethiopian software developers, UI designers, and video editors landing international remote clients.",
            summaryAm = "ለኢትዮጵያውያን የሶፍትዌር ገንቢዎች፣ ዲዛይነሮች እና የቪዲዮ አርታኢዎች ዓለም አቀፍ ደንበኞችን ለማግኘት የሚረዱ ቁልፍ ምክሮች።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("International clients hire freelancers who demonstrate reliable communication, clear case studies, and niche problem solving — not generic copy-pasted bids."),
                ArticleBlock.Heading("Winning First Contracts", "Business"),
                ArticleBlock.NumberedSteps(listOf(
                    "Publish 3 focused case-study projects on GitHub, Behance, or a fast personal portfolio.",
                    "Open every proposal by addressing the client's exact technical problem in 2 sentences.",
                    "Agree on milestone deliverables and clear timezone overlap upfront."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("ዓለም አቀፍ ደንበኞች ጥሩ የሥራ ናሙና (Portfolio) እና ግልጽ የመግባቢያ ክህሎት ላላቸው ባለሙያዎች ቅድሚያ ይሰጣሉ።"),
                ArticleBlock.Heading("የመጀመሪያ ሥራ ለማግኘት", "ቢዝነስ"),
                ArticleBlock.NumberedSteps(listOf(
                    "3 ጥራት ያላቸው የሥራ ናሙናዎችን በGitHub ወይም Behance ላይ ያዘጋጁ።",
                    "ፕሮፖዛል ሲጽፉ የደንበኛውን ችግር እንዴት እንደሚፈቱ በአጭሩ ያብራሩ።",
                    "የመረካከቢያ ጊዜን እና የክፍያ ደረጃዎችን አስቀድመው ይስማሙ።"
                ))
            ),
            coverKey = "fintech",
            categoryId = "business",
            tags = listOf("Freelancing", "Remote Work", "Career", "Portfolio"),
            authorName = "Selamawit Tadesse",
            authorRole = "Business & Fintech Editor",
            publishedDate = "Oct 17, 2026",
            readingMinutes = 6,
            articleUrl = "https://addispress.et/articles/upwork-freelancing-ethiopia"
        ),
        Article(
            id = "ethiopia_tv_streaming_review",
            titleEn = "Ethiopian Live TV & Radio Apps Review: Bandwidth, Channels & Quality",
            titleAm = "የኢትዮጵያ የቀጥታ ቲቪ እና ሬዲዮ መተግበሪያዎች ግምገማ",
            summaryEn = "Testing mobile streaming apps for news, football highlights, and FM radio with low-data adaptive quality controls.",
            summaryAm = "ዜና፣ ስፖርት እና የኤፍኤም ሬዲዮን በዝቅተኛ ኢንተርኔት በጥራት የሚያቀርቡ መተግበሪያዎች ግምገማ።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Whether commuting in Addis or studying abroad, reliable mobile streams with adjustable 360p/480p quality save huge amounts of mobile data."),
                ArticleBlock.Heading("What to Check Before Installing", "App Review"),
                ArticleBlock.BulletList(listOf(
                    "Look for official broadcaster apps or verified Play Store publishers.",
                    "Prefer players that let you switch between audio-only radio and adaptive video.",
                    "Avoid APKs requesting unnecessary Contacts or SMS permissions."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("በጉዞ ላይ ወይም በውጭ አገር ሆነው የአገር ውስጥ ዜናና ሬዲዮን በዝቅተኛ ዳታ ለመከታተል ትክክለኛ መተግበሪያ መምረጥ አስፈላጊ ነው።"),
                ArticleBlock.Heading("ከመጫንዎ በፊት የሚፈትሿቸው", "ግምገማ"),
                ArticleBlock.BulletList(listOf(
                    "ከታመኑ የPlay Store አሳታሚዎች ብቻ ያውርዱ።",
                    "የቪዲዮ ጥራትን (360p/480p) መቀየር የሚያስችሉ መሆናቸውን ያረጋግጡ።",
                    "አላስፈላጊ የSMS ወይም የስልክ ማውጫ ፈቃድ የሚጠይቁትን አይጫኑ።"
                ))
            ),
            coverKey = "apps",
            categoryId = "apps",
            tags = listOf("Streaming", "Ethiopia TV", "Radio", "Android"),
            authorName = "Blen Girma",
            authorRole = "Mobile Apps Editor",
            publishedDate = "Oct 14, 2026",
            readingMinutes = 4,
            articleUrl = "https://addispress.et/articles/ethiopia-tv-streaming-review"
        ),
        Article(
            id = "ai_study_flashcards_anki",
            titleEn = "Turn Lecture PDFs Into Smart Flashcards With AI & AnkiDroid",
            titleAm = "የትምህርት PDF ፋይሎችን በAI እና AnkiDroid ወደ Smart Flashcards መቀየር",
            summaryEn = "A proven spaced-repetition study system for Ethiopian medical, engineering, and law students preparing for national exit exams.",
            summaryAm = "ለዩኒቨርሲቲ መውጫ ፈተናዎች የሚዘጋጁ ተማሪዎች በAI አማካኝነት የመከለሻ ካርዶችን የሚሰሩበት ውጤታማ ዘዴ።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Reading a 200-page PDF once is rarely enough for retention. Combining AI question generation with AnkiDroid's spaced-repetition algorithm cuts study time dramatically."),
                ArticleBlock.Heading("3-Step Study Pipeline", "Tutorial"),
                ArticleBlock.NumberedSteps(listOf(
                    "Paste one lecture chapter into your AI study assistant and ask for 15 concise Q&A flashcards.",
                    "Review and correct any clinical or technical definitions manually.",
                    "Import into AnkiDroid and review 20 minutes daily on your commute."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("ረጃጅም የትምህርት ጽሑፎችን ደጋግሞ ከማንበብ ይልቅ በጥያቄና መልስ መልክ ማዘጋጀት ትምህርትን በቀላሉ ለማስታወስ ይረዳል።"),
                ArticleBlock.Heading("3 የጥናት ደረጃዎች", "መመሪያ"),
                ArticleBlock.NumberedSteps(listOf(
                    "አንዱን ምዕራፍ ወደ AI በማስገባት 15 የጥያቄና መልስ ካርዶችን እንዲያዘጋጅ ይጠይቁ።",
                    "መልሶቹን ከመጽሐፉ ጋር አመሳክረው ያረጋግጡ።",
                    "በAnkiDroid ውስጥ በማስገባት በየቀኑ ለ20 ደቂቃ ይከልሱ።"
                ))
            ),
            coverKey = "ai",
            categoryId = "tutorials",
            tags = listOf("AnkiDroid", "Exit Exam", "AI Study", "Students"),
            authorName = "Yonas Alemu",
            authorRole = "AI & ML Contributor",
            publishedDate = "Oct 12, 2026",
            readingMinutes = 5,
            articleUrl = "https://addispress.et/articles/ai-study-flashcards-anki"
        ),
        Article(
            id = "lensiq_ocr_scanner_review",
            titleEn = "Document Scanner & OCR Apps Review: Digitize Notes Without Watermarks",
            titleAm = "የሰነድ ስካነር እና OCR መተግበሪያዎች ግምገማ፡ ያለ Watermark ሰነድ ማንሳት",
            summaryEn = "Compare clean Android document scanners that turn camera shots of IDs, receipts, and handwritten notes into crisp searchable PDFs.",
            summaryAm = "መታወቂያዎችን፣ ደረሰኞችን እና የእጅ ማስታወሻዎችን በንጹህ PDF የሚቀይሩ ምርጥ የአንድሮይድ ስካነሮች።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Submitting crisp PDFs for university applications or business tenders shouldn't require paying for a watermark removal subscription."),
                ArticleBlock.Heading("Top Scanner Qualities", "App Review"),
                ArticleBlock.BulletList(listOf(
                    "Automatic edge cropping and shadow removal for indoor lighting.",
                    "On-device OCR text extraction so you can copy text straight from photos.",
                    "Zero forced watermarks and compact PDF export under 1 MB."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("ለትምህርት ወይም ለሥራ ማመልከቻ ጥራት ያለው PDF ለመላክ ውድ መተግበሪያዎችን መግዛት አያስፈልግም።"),
                ArticleBlock.Heading("ምርጥ ስካነር መመዘኛዎች", "ግምገማ"),
                ArticleBlock.BulletList(listOf(
                    "የሰነዱን ጠርዝ በራሱ የሚቆርጥ እና ጥላን የሚያጠፋ።",
                    "ከፎቶ ላይ ጽሑፍን ቀጥታ ኮፒ ለማድረግ የሚያስችል OCR።",
                    "Watermark የማይጨምር እና አነስተኛ መጠን ያለው PDF የሚሰጥ።"
                ))
            ),
            coverKey = "apps",
            categoryId = "app_reviews",
            tags = listOf("OCR", "PDF Scanner", "Productivity", "App Review"),
            authorName = "Blen Girma",
            authorRole = "Mobile Apps Editor",
            publishedDate = "Oct 09, 2026",
            readingMinutes = 4,
            isAppReview = true,
            articleUrl = "https://addispress.et/articles/document-scanner-ocr-review"
        ),
        Article(
            id = "proton_vpn_privacy_ethiopia",
            titleEn = "Mobile VPN & Public Wi-Fi Safety: What to Look For and Free Limits",
            titleAm = "የሞባይል VPN እና የህዝብ Wi-Fi ደህንነት፡ ምን መምረጥ አለብዎት?",
            summaryEn = "Protect logins on café and campus Wi-Fi networks while avoiding shady 'unlimited free VPN' apps that harvest user traffic.",
            summaryAm = "በካፌ እና በግቢ Wi-Fi ሲጠቀሙ መረጃዎን ከጠለፋ የሚጠብቁበት እና አደገኛ ነጻ VPNዎችን የሚለዩበት መመሪያ።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Many unknown 'free VPN' apps monetize by logging browsing traffic or injecting aggressive adware. Reputable audited providers offer safer free tiers."),
                ArticleBlock.Heading("Safety Checklist", "Cybersecurity"),
                ArticleBlock.BulletList(listOf(
                    "Choose audited no-logs VPN providers with transparent funding models.",
                    "Verify TLS/HTTPS lock icons on banking and email logins even when on VPN.",
                    "Disable unused VPN profiles to preserve battery when on trusted mobile data."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("ብዙ ያልተረጋገጡ 'ነጻ VPN' መተግበሪያዎች የተጠቃሚን መረጃ ይሰበስባሉ። ታማኝ እና የተመረመሩ መተግበሪያዎችን መምረጥ ወሳኝ ነው።"),
                ArticleBlock.Heading("የደህንነት ነጥቦች", "ደህንነት"),
                ArticleBlock.BulletList(listOf(
                    "የተጠቃሚ መረጃ የማይመዘግቡ (No-Logs) የታወቁ VPNዎችን ይምረጡ።",
                    "በባንክ እና በኢሜይል ገጾች ላይ HTTPS መኖሩን ሁልጊዜ ያረጋግጡ።",
                    "አላስፈላጊ በሆነ ጊዜ VPNን በማጥፋት ባትሪ ይቆጥቡ።"
                ))
            ),
            coverKey = "fayda",
            categoryId = "cybersecurity",
            tags = listOf("VPN", "Privacy", "Wi-Fi", "Cybersecurity"),
            authorName = "Hanna Tesfaye",
            authorRole = "Security Researcher",
            publishedDate = "Oct 06, 2026",
            readingMinutes = 5,
            articleUrl = "https://addispress.et/articles/mobile-vpn-wifi-safety"
        ),
        Article(
            id = "ethiopian_ecommerce_telegram_bots",
            titleEn = "How Addis Boutiques Automate Orders With Telegram Bots & Mini Apps",
            titleAm = "የአዲስ አበባ ንግዶች በቴሌግራም ቦቶች እና Mini Apps ትዕዛዝን እንዴት ያቀላጥፋሉ?",
            summaryEn = "Replacing manual DM replies with catalog menus, delivery zone selection, and automated order receipts inside Telegram.",
            summaryAm = "በቴሌግራም ውስጥ የዕቃ ዝርዝር፣ የአድራሻ መቀበያ እና ፈጣን ደረሰኝ በቦቶች እንዴት እንደሚሰራ።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Small retail shops across Ethiopia already reach buyers on Telegram. Adding structured order bots eliminates missed messages during busy hours."),
                ArticleBlock.Heading("Automation Benefits", "Startups"),
                ArticleBlock.BulletList(listOf(
                    "Customers pick item size, color, and Addis sub-city in 3 taps.",
                    "Store owners receive formatted order summaries ready for courier dispatch.",
                    "Automatic out-of-stock updates prevent awkward refund conversations."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("በቴሌግራም የሚሸጡ ሱቆች በቦቶች በመታገዝ ደንበኞች በቀላሉ የምርት መጠን፣ ቀለም እና አድራሻ እንዲመርጡ ማድረግ ይችላሉ።"),
                ArticleBlock.Heading("የቴክኖሎጂው ጥቅም", "ስታርትአፕ"),
                ArticleBlock.BulletList(listOf(
                    "ደንበኞች በ3 ንክኪ ብቻ ትዕዛዝ ይሰጣሉ።",
                    "ባለሱቆች የተደራጀ የትዕዛዝ መረጃ ለዴሊቨሪ ያገኛሉ።",
                    "ያለቁ ዕቃዎችን በራሱ ስለሚያሳውቅ ጊዜ ይቆጥባል።"
                ))
            ),
            coverKey = "fintech",
            categoryId = "startups",
            tags = listOf("Telegram Bots", "E-Commerce", "Addis Ababa", "Automation"),
            authorName = "Selamawit Tadesse",
            authorRole = "Fintech & Startup Analyst",
            publishedDate = "Oct 03, 2026",
            readingMinutes = 4,
            articleUrl = "https://addispress.et/articles/telegram-bots-ecommerce"
        ),
        Article(
            id = "clean_android_storage_guide",
            titleEn = "Free Up 10GB on Budget Android Phones Safely Without Fake 'Booster' Apps",
            titleAm = "ያለ ሐሰተኛ 'Booster' መተግበሪያዎች በስልክዎ ላይ 10GB ቦታ እንዴት ነጻ ያድርጉ?",
            summaryEn = "Clear hidden Telegram media caches, WhatsApp duplicates, and system trash using Android's built-in storage tools.",
            summaryAm = "የቴሌግራም ካሽ፣ ተደጋጋሚ ቪዲዮዎችን እና አላስፈላጊ ፋይሎችን በስልክዎ ቅንብር ብቻ በማጽዳት ቦታ ማስለቀቅ።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Third-party 'RAM Booster' apps often drain your battery and show intrusive pop-up ads. Android already includes safe storage cleanup tools."),
                ArticleBlock.Heading("Safe Cleanup Routine", "Technology"),
                ArticleBlock.NumberedSteps(listOf(
                    "Open Telegram -> Settings -> Data and Storage -> Storage Usage -> Clear Cache (your cloud chats stay safe).",
                    "Use Files by Google to review duplicate downloads and large forwarded videos.",
                    "Move old camera backups to a computer or cloud drive before deleting local copies."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("ብዙ የ'RAM Booster' መተግበሪያዎች ባትሪን ይጨርሳሉ እንጂ ቦታ አያጸዱም። አንድሮይድ በራሱ አስተማማኝ ማጽጃ አለው።"),
                ArticleBlock.Heading("ትክክለኛ የማጽዳት ደረጃዎች", "ቴክኖሎጂ"),
                ArticleBlock.NumberedSteps(listOf(
                    "በቴሌግራም Settings -> Data and Storage -> Clear Cache የሚለውን ይጫኑ (ቻቶች አይጠፉም)።",
                    "Files by Googleን በመጠቀም ተደጋጋሚ ቪዲዮዎችን ያጥፉ።",
                    "የቆዩ ፎቶዎችን ወደ ኮምፒውተር ወይም ክላውድ ያዘዋውሩ።"
                ))
            ),
            coverKey = "apps",
            categoryId = "technology",
            tags = listOf("Android", "Storage", "Telegram Cache", "Tips"),
            authorName = "Blen Girma",
            authorRole = "Mobile Apps Editor",
            publishedDate = "Sep 29, 2026",
            readingMinutes = 4,
            articleUrl = "https://addispress.et/articles/free-up-android-storage"
        ),
        Article(
            id = "dart_flutter_career_guide",
            titleEn = "Flutter vs Jetpack Compose in 2026: Which Stack Should Ethiopian Devs Learn?",
            titleAm = "Flutter እና Jetpack Compose በ2026፡ የኢትዮጵያ ገንቢዎች የትኛውን ይማሩ?",
            summaryEn = "Comparing job demand in Addis startups, remote freelancing gigs, app performance, and learning curve for new mobile developers.",
            summaryAm = "በአዲስ አበባ ስታርትአፖች እና በዓለም አቀፍ ፍሪላንሲንግ ገበያ ላይ ለሞባይል ገንቢዎች የትኛው ቴክኖሎጂ ይበልጥ ተፈላጊ ነው?",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Both Flutter and Kotlin Jetpack Compose power top Ethiopian apps. Choosing depends on whether you target fast cross-platform MVPs or deep native Android engineering."),
                ArticleBlock.Heading("Side-by-Side Comparison", "Programming"),
                ArticleBlock.BulletList(listOf(
                    "Flutter shines for startups shipping Android + iOS + Web from a single Dart codebase.",
                    "Jetpack Compose dominates native Android banking, fintech, and hardware-heavy roles.",
                    "Mastering State Management, REST APIs, and clean architecture transfers between both."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("Flutter እና Jetpack Compose ሁለቱም በኢትዮጵያ የሶፍትዌር ኩባንያዎች በስፋት ይሰራባቸዋል። ምርጫዎ በግብዎ ላይ ይወሰናል።"),
                ArticleBlock.Heading("ንጽጽር", "ፕሮግራሚንግ"),
                ArticleBlock.BulletList(listOf(
                    "Flutter በአንድ ኮድ ለAndroid እና iOS ለመስራት ተመራጭ ነው።",
                    "Jetpack Compose ለባንክ እና ለጥልቅ አንድሮይድ መተግበሪያዎች ተፈላጊ ነው።",
                    "የClean Architecture እና API እውቀት በሁለቱም ላይ ይሰራል።"
                ))
            ),
            coverKey = "apps",
            categoryId = "programming",
            tags = listOf("Flutter", "Kotlin", "Compose", "Career"),
            authorName = "Yonas Alemu",
            authorRole = "Engineering Lead",
            publishedDate = "Sep 25, 2026",
            readingMinutes = 6,
            articleUrl = "https://addispress.et/articles/flutter-vs-jetpack-compose"
        ),
        Article(
            id = "canva_social_creators_review",
            titleEn = "Canva Mobile Review: Fast Amharic & English Social Graphics on Android",
            titleAm = "የCanva ሞባይል ግምገማ፡ ፈጣን የአማርኛና እንግሊዝኛ ዲዛይኖች በስልክ",
            summaryEn = "Create YouTube thumbnails, Telegram promos, and startup pitch slides right from your Android phone with reusable templates.",
            summaryAm = "ለYouTube፣ ለቴሌግራም እና ለቢዝነስ ማስታወቂያዎች የሚሆኑ ምስሎችን በቀላሉ በስልክዎ መሥሪያ መተግበሪያ ግምገማ።",
            blocksEn = listOf(
                ArticleBlock.Paragraph("You don't need a heavy laptop to design crisp marketing posters. Canva's Android editor handles custom brand colors, clean typography, and social aspect ratios effortlessly."),
                ArticleBlock.Heading("Creator Highlights", "Marketing"),
                ArticleBlock.BulletList(listOf(
                    "Built-in presets for Instagram Reels, Telegram banners, and A4 flyers.",
                    "Upload custom Ethiopic fonts cleanly on creator teams.",
                    "Export crisp PNG or compressed JPG ready for fast mobile sharing."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("ጥራት ያላቸውን የማስታወቂያ ፖስተሮች ለመስራት ከፍተኛ ኮምፒውተር አያስፈልግም፤ Canva በስልክዎ ላይ በቀላሉ ይሰራል።"),
                ArticleBlock.Heading("ዋና ጥቅሞች", "ማርኬቲንግ"),
                ArticleBlock.BulletList(listOf(
                    "ለInstagram፣ Telegram እና YouTube ዝግጁ የሆኑ መጠኖች።",
                    "የድርጅትዎን ቀለም እና ሎጎ በቀላሉ ማስገባት።",
                    "በPNG ወይም በቀላል JPG ፋይል ማውረድ።"
                ))
            ),
            coverKey = "fintech",
            categoryId = "marketing",
            tags = listOf("Canva", "Design", "Creators", "Social Media"),
            authorName = "Selamawit Tadesse",
            authorRole = "Growth Strategist",
            publishedDate = "Sep 22, 2026",
            readingMinutes = 4,
            articleUrl = "https://addispress.et/articles/canva-mobile-review"
        ),
        Article(
            id = "solar_offgrid_tech_ethiopia",
            titleEn = "Smart Solar Pay-As-You-Go Tech Expanding Clean Power Across Rural Ethiopia",
            titleAm = "በገጠር ኢትዮጵያ ንጹህ ኃይልን የሚያስፋፋው Smart Solar Pay-As-You-Go ቴክኖሎጂ",
            summaryEn = "How IoT-enabled solar home kits paired with mobile money micro-payments bring reliable lighting and phone charging off-grid.",
            summaryAm = "በሞባይል ክፍያ እና በIoT የተገናኙ የፀሐይ ኃይል ሲስተሞች በገጠር አካባቢዎች ብርሃንና የስልክ ቻርጅ እንዴት እያዳረሱ ነው?",
            blocksEn = listOf(
                ArticleBlock.Paragraph("Clean-tech startups are pairing affordable solar panels with GSM-enabled controllers that unlock daily power via tiny mobile money installments."),
                ArticleBlock.Heading("Why It Matters", "Innovation"),
                ArticleBlock.BulletList(listOf(
                    "Replaces expensive kerosene lamps with clean LED lighting and USB phone charging.",
                    "Enables students to study safely after sunset and small shops to stay open longer.",
                    "Mobile micro-payments eliminate heavy upfront hardware barriers."
                ))
            ),
            blocksAm = listOf(
                ArticleBlock.Paragraph("የፀሐይ ኃይል ሲስተሞችን ከሞባይል ክፍያ ጋር በማገናኘት አነስተኛ ገቢ ያላቸው ቤተሰቦች ንጹህ የኤሌክትሪክ ኃይል ማግኘት ችለዋል።"),
                ArticleBlock.Heading("ዋና ጠቀሜታ", "ፈጠራ"),
                ArticleBlock.BulletList(listOf(
                    "ከኩራዝ ይልቅ ንጹህ ብርሃን እና የስልክ ቻርጅ አገልግሎት ይሰጣል።",
                    "ተማሪዎች ማታ በደህና እንዲያጠኑ ያግዛል።",
                    "በትንሽ ወርሃዊ ክፍያ በባለቤትነት መያዝ ያስችላል።"
                ))
            ),
            coverKey = "fayda",
            categoryId = "innovation",
            tags = listOf("CleanTech", "Solar", "IoT", "Rural Ethiopia"),
            authorName = "Dawit Mekonnen",
            authorRole = "Senior Tech Editor",
            publishedDate = "Sep 18, 2026",
            readingMinutes = 5,
            articleUrl = "https://addispress.et/articles/solar-payg-ethiopia"
        )
    )

    val notifications: List<AppNotification> = listOf(
        AppNotification(
            id = "notif_1",
            titleEn = "Breaking: Fayda National ID Registration Guide",
            titleAm = "ሰበር፡ የፋይዳ ብሔራዊ መታወቂያ ሙሉ መመሪያ",
            bodyEn = "Updated 2026 enrollment steps, bank linking, and OTP safety tips.",
            bodyAm = "የተሻሻሉ የ2026 የምዝገባ ደረጃዎች እና የደህንነት ምክሮች።",
            categoryId = "technology",
            timeAgoEn = "15m ago",
            timeAgoAm = "ከ15 ደቂቃ በፊት",
            targetArticleId = "fayda_national_id"
        ),
        AppNotification(
            id = "notif_2",
            titleEn = "New AI Guide: Bilingual Prompting Workflows",
            titleAm = "አዲስ የAI መመሪያ፡ በአማርኛ እና እንግሊዝኛ መጠቀሚያ",
            bodyEn = "4 practical techniques for Ethiopian students and developers.",
            bodyAm = "ለተማሪዎች እና ለገንቢዎች የተዘጋጁ 4 ተግባራዊ ዘዴዎች።",
            categoryId = "ai",
            timeAgoEn = "2h ago",
            timeAgoAm = "ከ2 ሰዓት በፊት",
            targetArticleId = "ai_amharic_productivity"
        ),
        AppNotification(
            id = "notif_3",
            titleEn = "App Review: AccuBattery Battery Health",
            titleAm = "የመተግበሪያ ግምገማ፡ AccuBattery",
            bodyEn = "Test charger wattage and extend Android battery lifespan.",
            bodyAm = "የቻርጀርዎን ጥራት ይፈትሹ እና የባትሪ ዕድሜን ያራዝሙ።",
            categoryId = "app_reviews",
            timeAgoEn = "Yesterday",
            timeAgoAm = "ትላንት",
            targetArticleId = "accubattery_review"
        )
    )
}
