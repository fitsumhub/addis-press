package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.core.localization.AppStrings
import com.example.core.localization.pick
import com.example.core.search.SearchEngine
import com.example.data.model.Article
import com.example.ui.components.EmptyEditorialState
import com.example.ui.components.SearchArticleResultCard
import com.example.ui.components.SectionHeaderRow
import com.example.ui.state.MainDashboardState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    state: MainDashboardState,
    initialQuery: String = "",
    onBack: () -> Unit,
    onSearchSubmitted: (String) -> Unit,
    onClearRecentSearches: () -> Unit,
    onOpenArticle: (String) -> Unit,
    onToggleArticleBookmark: (Article) -> Unit,
    onOpenApp: (String) -> Unit,
    onOpenAiTool: (String) -> Unit
) {
    val lang = state.language
    val focusManager = LocalFocusManager.current
    var query by remember(initialQuery) { mutableStateOf(initialQuery) }
    var scopeFilter by remember { mutableStateOf("all") }
    var sortByQuickRead by remember { mutableStateOf(false) }

    val bookmarkedArticleIds = remember(state.bookmarks) {
        state.bookmarks.filter { it.itemType == "article" }.map { it.itemId }.toSet()
    }

    val trendingQueries = remember(lang) {
        listOf(
            "Fayda",
            "ቴሌብር",
            "Gemini",
            "Kotlin",
            "ፊንቴክ",
            "AccuBattery",
            "Startups",
            "AI"
        )
    }

    val q = query.trim()

    val matchedArticles = remember(q, scopeFilter, sortByQuickRead, state.articles) {
        if (q.isEmpty() || (scopeFilter != "all" && scopeFilter != "articles")) {
            emptyList()
        } else {
            val base = SearchEngine.searchArticles(state.articles, q)
            if (sortByQuickRead) base.sortedBy { it.readingMinutes } else base
        }
    }

    val matchedApps = remember(q, scopeFilter, state.applications) {
        if (q.isEmpty() || (scopeFilter != "all" && scopeFilter != "apps")) emptyList()
        else SearchEngine.searchApps(state.applications, q)
    }

    val matchedTools = remember(q, scopeFilter, state.aiTools) {
        if (q.isEmpty() || (scopeFilter != "all" && scopeFilter != "ai_tools")) emptyList()
        else SearchEngine.searchAiTools(state.aiTools, q)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .testTag("search_back_button")
            ) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = lang.pick("Back", "ተመለስ"))
            }
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text(AppStrings.searchHint(lang)) },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Rounded.Close, contentDescription = lang.pick("Clear", "አጽዳ"))
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchSubmitted(query)
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .testTag("search_input_field")
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filters = listOf(
                "all" to lang.pick("All", "ሁሉም"),
                "articles" to lang.pick("Articles", "ጽሑፎች"),
                "apps" to lang.pick("Apps", "መተግበሪያዎች"),
                "ai_tools" to lang.pick("AI Tools", "የAI መሳሪያዎች")
            )
            items(filters, key = { it.first }) { (id, label) ->
                FilterChip(
                    selected = scopeFilter == id,
                    onClick = { scopeFilter = id },
                    label = { Text(label) }
                )
            }
            item(key = "sort_chip") {
                FilterChip(
                    selected = sortByQuickRead,
                    onClick = { sortByQuickRead = !sortByQuickRead },
                    leadingIcon = { Icon(Icons.Rounded.Sort, contentDescription = null) },
                    label = { Text(lang.pick("Quick reads first", "አጫጭር መጀመሪያ")) }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (q.isEmpty()) {
                if (state.preferences.recentSearches.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SectionHeaderRow(
                                title = lang.pick("Recent searches", "የቅርብ ጊዜ ፍለጋዎች"),
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(onClick = onClearRecentSearches) {
                                Text(lang.pick("Clear", "አጽዳ"))
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            state.preferences.recentSearches.forEach { recent ->
                                AssistChip(
                                    onClick = { query = recent },
                                    leadingIcon = { Icon(Icons.Rounded.History, contentDescription = null) },
                                    label = { Text(recent) }
                                )
                            }
                        }
                    }
                }

                item {
                    SectionHeaderRow(title = lang.pick("Trending bilingual searches", "ተወዳጅ ፍለጋዎች (EN / አማርኛ)"))
                    Spacer(Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        trendingQueries.forEach { topic ->
                            AssistChip(
                                onClick = {
                                    query = topic
                                    onSearchSubmitted(topic)
                                },
                                leadingIcon = { Icon(Icons.Rounded.TrendingUp, contentDescription = null) },
                                label = { Text(topic) }
                            )
                        }
                    }
                }
            } else if (matchedArticles.isEmpty() && matchedApps.isEmpty() && matchedTools.isEmpty()) {
                item {
                    EmptyEditorialState(
                        title = lang.pick("No results for \"$query\"", "ለ\"$query\" ውጤት አልተገኘም"),
                        subtitle = lang.pick(
                            "Try keywords in English or Amharic such as Fayda, ቴሌብር, AI, Kotlin, or ፊንቴክ.",
                            "በእንግሊዝኛ ወይም በአማርኛ እንደ Fayda፣ ቴሌብር፣ AI ወይም ፊንቴክ ያሉ ቃላትን ይሞክሩ።"
                        )
                    )
                }
            } else {
                if (matchedArticles.isNotEmpty()) {
                    item {
                        SectionHeaderRow(
                            title = lang.pick(
                                "Articles (${matchedArticles.size})",
                                "ጽሑፎች (${matchedArticles.size})"
                            )
                        )
                    }
                    items(matchedArticles, key = { it.id }) { article ->
                        SearchArticleResultCard(
                            article = article,
                            query = query,
                            language = lang,
                            isBookmarked = article.id in bookmarkedArticleIds,
                            onOpen = {
                                onSearchSubmitted(query)
                                onOpenArticle(article.id)
                            },
                            onToggleBookmark = { onToggleArticleBookmark(article) }
                        )
                    }
                }
                if (matchedApps.isNotEmpty()) {
                    item {
                        SectionHeaderRow(title = "${AppStrings.appsTab(lang)} (${matchedApps.size})")
                    }
                    items(matchedApps, key = { it.id }) { app ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSearchSubmitted(query)
                                    onOpenApp(app.id)
                                }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = SearchEngine.highlightMatches(app.name, query, MaterialTheme.colorScheme.primary),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = SearchEngine.highlightMatches(
                                        lang.pick(app.taglineEn, app.taglineAm),
                                        query,
                                        MaterialTheme.colorScheme.primary
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
                if (matchedTools.isNotEmpty()) {
                    item {
                        SectionHeaderRow(title = "${AppStrings.aiToolsTab(lang)} (${matchedTools.size})")
                    }
                    items(matchedTools, key = { it.id }) { tool ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSearchSubmitted(query)
                                    onOpenAiTool(tool.id)
                                }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = SearchEngine.highlightMatches(tool.name, query, MaterialTheme.colorScheme.primary),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = SearchEngine.highlightMatches(
                                        lang.pick(tool.taglineEn, tool.taglineAm),
                                        query,
                                        MaterialTheme.colorScheme.primary
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
