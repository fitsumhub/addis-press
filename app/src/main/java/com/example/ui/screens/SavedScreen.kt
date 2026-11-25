package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.DriveFileMove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.core.localization.AppStrings
import com.example.core.localization.pick
import com.example.data.local.BookmarkEntity
import com.example.ui.components.EmptyEditorialState
import com.example.ui.components.coverDrawableForKey
import com.example.ui.state.MainDashboardState

@Composable
fun SavedScreen(
    state: MainDashboardState,
    onOpenArticle: (String) -> Unit,
    onOpenApp: (String) -> Unit,
    onOpenAiTool: (String) -> Unit,
    onRemoveBookmark: (BookmarkEntity) -> Unit,
    onMoveBookmarkToList: (String, String) -> Unit,
    onAddReadingList: (String) -> Unit,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = state.language
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Bookmarks, 1 = History
    var selectedListFilter by remember { mutableStateOf("All") }
    var showCreateListDialog by remember { mutableStateOf(false) }
    var moveTargetBookmark by remember { mutableStateOf<BookmarkEntity?>(null) }

    val readingLists = state.preferences.customReadingLists
    val filteredBookmarks = remember(state.bookmarks, selectedListFilter) {
        val base = state.bookmarks.sortedByDescending { it.updatedAtMillis }
        if (selectedListFilter == "All") base
        else base.filter { it.readingListName == selectedListFilter }
    }
    val sortedHistory = remember(state.history) {
        state.history.sortedByDescending { it.updatedAtMillis }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 104.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = AppStrings.savedTitle(lang),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = AppStrings.readableOffline(lang, state.bookmarks.size),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(14.dp))

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                    modifier = Modifier.testTag("saved_tab_bookmarks")
                ) {
                    Text("${AppStrings.bookmarksTab(lang)} (${state.bookmarks.size})")
                }
                SegmentedButton(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                    modifier = Modifier.testTag("saved_tab_history")
                ) {
                    Text("${AppStrings.readingHistoryTab(lang)} (${state.history.size})")
                }
            }
        }

        if (selectedTab == 0) {
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    item {
                        FilterChip(
                            selected = selectedListFilter == "All",
                            onClick = { selectedListFilter = "All" },
                            label = { Text(lang.pick("All", "ሁሉም")) }
                        )
                    }
                    items(readingLists, key = { it }) { listName ->
                        FilterChip(
                            selected = selectedListFilter == listName,
                            onClick = { selectedListFilter = listName },
                            label = { Text(listName) }
                        )
                    }
                    item {
                        FilterChip(
                            selected = false,
                            onClick = { showCreateListDialog = true },
                            leadingIcon = {
                                Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            },
                            label = { Text(lang.pick("New list", "አዲስ ዝርዝር")) },
                            modifier = Modifier.testTag("new_reading_list_chip")
                        )
                    }
                }
            }

            if (filteredBookmarks.isEmpty()) {
                item {
                    EmptyEditorialState(
                        title = AppStrings.savedEmptyTitle(lang),
                        subtitle = AppStrings.savedEmptySubtitle(lang)
                    )
                }
            } else {
                items(filteredBookmarks, key = { it.id }) { item ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("saved_item_${item.id}")
                            .clickable {
                                when (item.itemType) {
                                    "application" -> onOpenApp(item.itemId)
                                    "ai_tool" -> onOpenAiTool(item.itemId)
                                    else -> onOpenArticle(item.itemId)
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = coverDrawableForKey(item.coverKey)),
                                contentDescription = lang.pick(item.titleEn, item.titleAm),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${item.itemType.uppercase()} · ${item.readingListName}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = lang.pick(item.titleEn, item.titleAm),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            IconButton(
                                onClick = { moveTargetBookmark = item },
                                modifier = Modifier.minimumInteractiveComponentSize()
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.DriveFileMove,
                                    contentDescription = lang.pick("Move list", "ዝርዝር ቀይር"),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(
                                onClick = { onRemoveBookmark(item) },
                                modifier = Modifier.minimumInteractiveComponentSize()
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.DeleteOutline,
                                    contentDescription = lang.pick("Remove", "አስወግድ"),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        } else {
            if (sortedHistory.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onClearHistory) {
                            Text(lang.pick("Clear history", "ታሪክ አጽዳ"))
                        }
                    }
                }
            }

            if (sortedHistory.isEmpty()) {
                item {
                    EmptyEditorialState(
                        title = lang.pick("No reading history yet", "የንባብ ታሪክ የለም"),
                        subtitle = lang.pick(
                            "Articles you read will appear here with your reading progress.",
                            "ያነበቧቸው ጽሑፎች እና የንባብ ሂደትዎ እዚህ ይታያሉ።"
                        )
                    )
                }
            } else {
                items(sortedHistory, key = { it.articleId }) { hist ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenArticle(hist.articleId) }
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = lang.pick(hist.titleEn, hist.titleAm),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { (hist.progressPercent.toFloat() / 100f).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "${hist.progressPercent}% ${lang.pick("read", "ተነብቧል")}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    if (showCreateListDialog) {
        var listName by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showCreateListDialog = false },
            title = { Text(lang.pick("Create Reading List", "አዲስ የንባብ ዝርዝር")) },
            text = {
                OutlinedTextField(
                    value = listName,
                    onValueChange = { listName = it },
                    label = { Text(lang.pick("List name", "የዝርዝር ስም")) },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (listName.isNotBlank()) {
                            onAddReadingList(listName)
                            showCreateListDialog = false
                        }
                    }
                ) {
                    Text(lang.pick("Create", "ፍጠር"))
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateListDialog = false }) {
                    Text(lang.pick("Cancel", "ሰርዝ"))
                }
            }
        )
    }

    val moving = moveTargetBookmark
    if (moving != null) {
        AlertDialog(
            onDismissRequest = { moveTargetBookmark = null },
            title = { Text(lang.pick("Move to Reading List", "ወደ ሌላ ዝርዝር ውሰድ")) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    readingLists.forEach { targetList ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (moving.readingListName == targetList)
                                MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onMoveBookmarkToList(moving.id, targetList)
                                    moveTargetBookmark = null
                                }
                        ) {
                            Text(
                                text = targetList,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { moveTargetBookmark = null }) {
                    Text(lang.pick("Close", "ዝጋ"))
                }
            }
        )
    }
}
