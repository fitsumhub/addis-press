package com.example.ui.screens

import android.content.Intent
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.FormatSize
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.localization.AppStrings
import com.example.core.localization.pick
import com.example.data.catalog.EditorialCatalog
import com.example.data.model.Article
import com.example.data.model.ArticleBlock
import com.example.ui.components.CompactArticleRow
import com.example.ui.components.NativeFeedAdCard
import com.example.ui.components.SectionHeaderRow
import com.example.ui.components.StickyBannerAdBar
import com.example.ui.components.coverDrawableForKey
import com.example.ui.state.MainDashboardState
import com.example.ui.theme.CoralRed
import com.example.ui.theme.WarmAmber
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ArticleDetailScreen(
    articleId: String,
    state: MainDashboardState,
    onBack: () -> Unit,
    onToggleBookmark: (Article) -> Unit,
    onToggleLike: (String) -> Unit,
    onFontScaleChange: (Float) -> Unit,
    onRecordProgress: (Article, Int) -> Unit,
    onSubmitReport: (String, String, String, String) -> Unit,
    onOpenArticle: (String) -> Unit,
    onOpenApp: (String) -> Unit,
    onAdImpression: () -> Unit,
    onAdClicked: () -> Unit
) {
    val lang = state.language
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    val adsEnabled = state.preferences.adsEnabled
    val highYieldAds = state.preferences.highYieldAds
    val bannerAdsEnabled = state.preferences.bannerAdsEnabled
    val activeBannerAds = state.activeBannerAds
    val activeNativeAds = state.activeNativeAds

    val article = remember(articleId, state.articles) {
        state.articles.firstOrNull { it.id == articleId } ?: state.articles.first()
    }
    val category = EditorialCatalog.categoryById(article.categoryId)
    val isBookmarked = remember(state.bookmarks, article.id) {
        state.bookmarks.any { it.itemType == "article" && it.itemId == article.id }
    }
    val isLiked = remember(state.likes, article.id) {
        state.likes.any { it.articleId == article.id }
    }
    val relatedArticles = remember(article, state.articles) {
        state.articles.filter { it.id != article.id }
            .sortedByDescending { if (it.categoryId == article.categoryId) 1 else 0 }
            .take(5)
    }

    val title = lang.pick(article.titleEn, article.titleAm)
    val summary = lang.pick(article.summaryEn, article.summaryAm)
    val blocks = if (lang.code == "am" && article.blocksAm.isNotEmpty()) article.blocksAm else article.blocksEn
    val fontScale = state.preferences.readerFontScale

    val listState = rememberLazyListState()
    val scrollProgress by remember {
        derivedStateOf {
            val total = listState.layoutInfo.totalItemsCount.coerceAtLeast(1)
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            ((lastVisible + 1).toFloat() / total.toFloat()).coerceIn(0.08f, 1f)
        }
    }

    var lastSavedMilestone by remember(article.id) { mutableStateOf(0) }
    LaunchedEffect(scrollProgress, article.id) {
        val percent = (scrollProgress * 100).toInt().coerceIn(10, 100)
        if (percent - lastSavedMilestone >= 25 || (percent == 100 && lastSavedMilestone < 100)) {
            lastSavedMilestone = percent
            onRecordProgress(article, percent)
        }
    }

    var showFontSheet by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var isSpeaking by remember { mutableStateOf(false) }
    var ttsReady by remember { mutableStateOf(false) }

    val tts = remember(context) {
        var engine: TextToSpeech? = null
        engine = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                engine?.language = Locale.US
                ttsReady = true
            }
        }
        engine
    }

    DisposableEffect(tts) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    fun shareArticle() {
        val url = article.articleUrl.ifBlank { "https://addispress.et/articles/${article.id}" }
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, "$title\n\n$summary\n\n$url")
        }
        context.startActivity(Intent.createChooser(intent, null))
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("article_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = lang.pick("Back", "ተመለስ"))
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { showFontSheet = true },
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("article_font_button")
                    ) {
                        Icon(Icons.Rounded.FormatSize, contentDescription = lang.pick("Text size", "የጽሑፍ መጠን"))
                    }
                    IconButton(
                        onClick = {
                            if (isSpeaking) {
                                tts.stop()
                                isSpeaking = false
                            } else if (ttsReady) {
                                val speechText = buildString {
                                    append(title).append(". ")
                                    append(summary).append(". ")
                                    blocks.forEach { block ->
                                        when (block) {
                                            is ArticleBlock.Paragraph -> append(block.text).append(" ")
                                            is ArticleBlock.Heading -> append(block.text).append(". ")
                                            is ArticleBlock.BulletList -> block.items.forEach { append(it).append(". ") }
                                            is ArticleBlock.NumberedSteps -> block.steps.forEach { append(it).append(". ") }
                                            is ArticleBlock.Callout -> append(block.title).append(". ").append(block.body).append(" ")
                                        }
                                    }
                                }
                                tts.speak(speechText.take(3500), TextToSpeech.QUEUE_FLUSH, null, article.id)
                                isSpeaking = true
                            }
                        },
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("article_tts_button")
                    ) {
                        Icon(
                            imageVector = if (isSpeaking) Icons.Rounded.Stop else Icons.Rounded.GraphicEq,
                            contentDescription = if (isSpeaking) AppStrings.stopListening(lang) else AppStrings.listen(lang),
                            tint = if (isSpeaking) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                        )
                    }
                    IconButton(
                        onClick = { onToggleBookmark(article) },
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("article_bookmark_button")
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = AppStrings.save(lang),
                            tint = if (isBookmarked) WarmAmber else MaterialTheme.colorScheme.onBackground
                        )
                    }
                    IconButton(
                        onClick = { shareArticle() },
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("article_share_button")
                    ) {
                        Icon(Icons.Rounded.Share, contentDescription = AppStrings.share(lang))
                    }
                }
                LinearProgressIndicator(
                    progress = { scrollProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.outlineVariant
                )
            }
        },
        bottomBar = {
            if (adsEnabled && bannerAdsEnabled && activeBannerAds.isNotEmpty()) {
                val slot = kotlin.math.abs(article.id.hashCode()) % activeBannerAds.size
                StickyBannerAdBar(
                    ad = activeBannerAds[slot],
                    language = lang,
                    onImpression = onAdImpression,
                    onAdClicked = onAdClicked,
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .let { mod -> if (adsEnabled && bannerAdsEnabled) mod else mod.navigationBarsPadding() },
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 720.dp),
                contentPadding = PaddingValues(bottom = 48.dp)
            ) {
                item {
                    Image(
                        painter = painterResource(id = coverDrawableForKey(article.coverKey)),
                        contentDescription = title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                    )
                }

                item {
                    Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp)) {
                        SectionHeaderRow(
                            title = lang.pick(category.titleEn, category.titleAm).uppercase(),
                            trailingActionLabel = AppStrings.minRead(lang, article.readingMinutes)
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontSize = (24 * fontScale).sp,
                                lineHeight = (31 * fontScale).sp
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = summary,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = (16 * fontScale).sp,
                                lineHeight = (24 * fontScale).sp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape,
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = article.authorName.take(1).uppercase(),
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Spacer(Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = article.authorName,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = "${article.authorRole} · ${article.publishedDate}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { onToggleBookmark(article) },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                            ) {
                                Icon(
                                    imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                    contentDescription = null,
                                    tint = if (isBookmarked) WarmAmber else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(if (isBookmarked) AppStrings.saved(lang) else AppStrings.save(lang))
                            }
                            OutlinedButton(
                                onClick = { shareArticle() },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                            ) {
                                Icon(Icons.Rounded.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(6.dp))
                                Text(AppStrings.share(lang))
                            }
                        }
                    }
                }

                // Rich Content Blocks with Mid-Article Native Ad
                blocks.forEachIndexed { index, block ->
                    item(key = "block_${article.id}_$index") {
                        Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)) {
                            when (block) {
                                is ArticleBlock.Paragraph -> {
                                    Text(
                                        text = block.text,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontSize = (16.5f * fontScale).sp,
                                            lineHeight = (26 * fontScale).sp
                                        ),
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                                is ArticleBlock.Heading -> {
                                    Spacer(Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (block.badge.isNotBlank()) {
                                            Surface(
                                                color = MaterialTheme.colorScheme.primaryContainer,
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = block.badge.uppercase(),
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                                )
                                            }
                                            Spacer(Modifier.width(8.dp))
                                        }
                                        Text(
                                            text = block.text,
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontSize = (19 * fontScale).sp,
                                                lineHeight = (25 * fontScale).sp
                                            ),
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                }
                                is ArticleBlock.BulletList -> {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        block.items.forEach { bullet ->
                                            Row(verticalAlignment = Alignment.Top) {
                                                Text(
                                                    text = "•",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = MaterialTheme.colorScheme.secondary,
                                                    modifier = Modifier.padding(end = 10.dp)
                                                )
                                                Text(
                                                    text = bullet,
                                                    style = MaterialTheme.typography.bodyLarge.copy(
                                                        fontSize = (15.5f * fontScale).sp,
                                                        lineHeight = (23 * fontScale).sp
                                                    ),
                                                    color = MaterialTheme.colorScheme.onBackground
                                                )
                                            }
                                        }
                                    }
                                }
                                is ArticleBlock.NumberedSteps -> {
                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        block.steps.forEachIndexed { idx, step ->
                                            Row(verticalAlignment = Alignment.Top) {
                                                Text(
                                                    text = "${idx + 1}.",
                                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = MaterialTheme.colorScheme.secondary,
                                                    modifier = Modifier.padding(end = 10.dp, top = 2.dp)
                                                )
                                                Text(
                                                    text = step,
                                                    style = MaterialTheme.typography.bodyLarge.copy(
                                                        fontSize = (15.5f * fontScale).sp,
                                                        lineHeight = (23 * fontScale).sp
                                                    ),
                                                    color = MaterialTheme.colorScheme.onBackground
                                                )
                                            }
                                        }
                                    }
                                }
                                is ArticleBlock.Callout -> {
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Text(
                                                text = block.title,
                                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            Spacer(Modifier.height(4.dp))
                                            Text(
                                                text = block.body,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontSize = (14.5f * fontScale).sp,
                                                    lineHeight = (22 * fontScale).sp
                                                ),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    val midBlockIndex = (blocks.size / 2).coerceAtLeast(1)
                    if (adsEnabled && activeNativeAds.isNotEmpty() && index == midBlockIndex) {
                        val slot = kotlin.math.abs(article.id.hashCode()) % activeNativeAds.size
                        item(key = "mid_article_ad_${article.id}") {
                            NativeFeedAdCard(
                                ad = activeNativeAds[slot],
                                language = lang,
                                onImpression = onAdImpression,
                                onAdClicked = onAdClicked,
                                modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
                            )
                        }
                    }
                }

                // Linked App CTA if article is an app review
                if (article.linkedAppId != null) {
                    item {
                        Box(modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)) {
                            Button(
                                onClick = { onOpenApp(article.linkedAppId) },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth().height(48.dp)
                            ) {
                                Icon(Icons.Rounded.OpenInNew, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(lang.pick("Open Full App Profile & Links", "ሙሉ የመተግበሪያ መረጃ ክፈት"))
                            }
                        }
                    }
                }

                // Tags + Like + Copy Link + Report
                item {
                    Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)) {
                        if (article.tags.isNotEmpty()) {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                article.tags.forEach { tag ->
                                    AssistChip(onClick = {}, label = { Text("#$tag") })
                                }
                            }
                            Spacer(Modifier.height(14.dp))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { onToggleLike(article.id) },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).testTag("article_like_button")
                            ) {
                                Icon(
                                    imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = null,
                                    tint = if (isLiked) CoralRed else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(lang.pick(if (isLiked) "Liked" else "Like", if (isLiked) "ተወዷል" else "ውደድ"))
                            }
                            OutlinedButton(
                                onClick = {
                                    val url = article.articleUrl.ifBlank { "https://addispress.et/articles/${article.id}" }
                                    clipboard.setText(AnnotatedString(url))
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Rounded.ContentCopy, contentDescription = null, modifier = Modifier.size(17.dp))
                                Spacer(Modifier.width(6.dp))
                                Text(AppStrings.copyLink(lang))
                            }
                            IconButton(
                                onClick = { showReportDialog = true },
                                modifier = Modifier.minimumInteractiveComponentSize()
                            ) {
                                Icon(
                                    Icons.Rounded.Flag,
                                    contentDescription = AppStrings.report(lang),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        // Save to read later banner matching reference editorial pattern
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onToggleBookmark(article) }
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = if (isBookmarked) AppStrings.saved(lang) else AppStrings.saveToReadLater(lang),
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = AppStrings.savedOfflineHint(lang),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                if (adsEnabled && highYieldAds && activeNativeAds.isNotEmpty()) {
                    val slot = (kotlin.math.abs(article.id.hashCode()) + 1) % activeNativeAds.size
                    item(key = "end_article_ad_${article.id}") {
                        NativeFeedAdCard(
                            ad = activeNativeAds[slot],
                            language = lang,
                            onImpression = onAdImpression,
                            onAdClicked = onAdClicked,
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                        )
                    }
                }

                // Read Next Related Articles
                item {
                    Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)) {
                        SectionHeaderRow(title = AppStrings.readNext(lang))
                        Spacer(Modifier.height(8.dp))
                        relatedArticles.forEach { rel ->
                            CompactArticleRow(
                                article = rel,
                                language = lang,
                                onOpen = { onOpenArticle(rel.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showFontSheet) {
        ModalBottomSheet(onDismissRequest = { showFontSheet = false }) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = lang.pick("Reader Typography Size", "የንባብ ጽሑፍ መጠን"),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "${(fontScale * 100).toInt()}%",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Slider(
                    value = fontScale,
                    onValueChange = onFontScaleChange,
                    valueRange = 0.85f..1.40f
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    if (showReportDialog) {
        var reason by remember { mutableStateOf("Outdated information") }
        var details by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = { Text(AppStrings.report(lang)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = reason,
                        onValueChange = { reason = it },
                        label = { Text(lang.pick("Reason", "ምክንያት")) },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = details,
                        onValueChange = { details = it },
                        label = { Text(lang.pick("Additional details (optional)", "ተጨማሪ ማብራሪያ")) },
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onSubmitReport(article.id, "article", reason, details)
                        showReportDialog = false
                    }
                ) {
                    Text(lang.pick("Submit", "ላክ"))
                }
            },
            dismissButton = {
                TextButton(onClick = { showReportDialog = false }) {
                    Text(lang.pick("Cancel", "ሰርዝ"))
                }
            }
        )
    }
}
