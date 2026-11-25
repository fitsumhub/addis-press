package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.core.localization.AppStrings
import com.example.core.localization.pick
import com.example.data.catalog.EditorialCatalog
import com.example.ui.components.SectionHeaderRow
import com.example.ui.state.MainDashboardState

@Composable
fun EditorialStudioScreen(
    state: MainDashboardState,
    onBack: () -> Unit,
    onSaveStory: (
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
        readingMinutes: Int,
        status: String,
        onDone: () -> Unit
    ) -> Unit,
    onDeleteStory: (String) -> Unit
) {
    val lang = state.language
    var editingId by remember { mutableStateOf<String?>(null) }
    var titleEn by remember { mutableStateOf("") }
    var titleAm by remember { mutableStateOf("") }
    var summaryEn by remember { mutableStateOf("") }
    var contentEn by remember { mutableStateOf("") }
    var contentAm by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("technology") }
    var selectedCover by remember { mutableStateOf("ai") }
    var tagsInput by remember { mutableStateOf("Ethiopia, Tech") }
    var status by remember { mutableStateOf("published") }

    fun resetForm() {
        editingId = null
        titleEn = ""
        titleAm = ""
        summaryEn = ""
        contentEn = ""
        contentAm = ""
        selectedCategory = "technology"
        selectedCover = "ai"
        tagsInput = "Ethiopia, Tech"
        status = "published"
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.minimumInteractiveComponentSize()) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = lang.pick("Back", "ተመለስ"))
                }
                Text(
                    text = AppStrings.editorialStudio(lang),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                if (editingId != null) {
                    IconButton(onClick = ::resetForm) {
                        Icon(Icons.Rounded.Add, contentDescription = lang.pick("New story", "አዲስ ጽሑፍ"))
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = lang.pick(
                        "Write and publish bilingual Ethiopian tech stories directly to your AddisPress feed.",
                        "በእንግሊዝኛ እና በአማርኛ የቴክኖሎጂ ጽሑፎችን በመጻፍ ያሳትሙ።"
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                OutlinedTextField(
                    value = titleEn,
                    onValueChange = { titleEn = it },
                    label = { Text(lang.pick("English Headline *", "የእንግሊዝኛ ርዕስ *")) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("studio_title_en")
                )
            }

            item {
                OutlinedTextField(
                    value = titleAm,
                    onValueChange = { titleAm = it },
                    label = { Text(lang.pick("Amharic Headline (Optional)", "የአማርኛ ርዕስ (አማራጭ)")) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = summaryEn,
                    onValueChange = { summaryEn = it },
                    label = { Text(lang.pick("Short Summary", "አጭር ማጠቃለያ")) },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = contentEn,
                    onValueChange = { contentEn = it },
                    label = { Text(lang.pick("Article Content (English) *", "የጽሑፉ ዝርዝር (እንግሊዝኛ) *")) },
                    minLines = 5,
                    modifier = Modifier.fillMaxWidth().testTag("studio_content_en")
                )
            }

            item {
                OutlinedTextField(
                    value = contentAm,
                    onValueChange = { contentAm = it },
                    label = { Text(lang.pick("Article Content (Amharic)", "የጽሑፉ ዝርዝር (አማርኛ)")) },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text(lang.pick("Category", "ምድብ"), style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(EditorialCatalog.categories, key = { it.id }) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat.id,
                            onClick = { selectedCategory = cat.id },
                            label = { Text(lang.pick(cat.titleEn, cat.titleAm)) }
                        )
                    }
                }
            }

            item {
                Text(lang.pick("Cover Theme", "የሽፋን ምስል"), style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val covers = listOf("ai" to "AI Tech", "fayda" to "Digital ID", "fintech" to "Fintech", "apps" to "Apps")
                    items(covers, key = { it.first }) { (key, label) ->
                        FilterChip(
                            selected = selectedCover == key,
                            onClick = { selectedCover = key },
                            label = { Text(label) }
                        )
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = tagsInput,
                    onValueChange = { tagsInput = it },
                    label = { Text(lang.pick("Tags (comma separated)", "መለያ ቃላት (በኮማ የተለዩ)")) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text(lang.pick("Workflow Status", "የህትመት ሁኔታ"), style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val statuses = listOf("draft", "review", "published")
                    items(statuses, key = { it }) { st ->
                        FilterChip(
                            selected = status == st,
                            onClick = { status = st },
                            label = { Text(st.replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }
            }

            item {
                val canSubmit = titleEn.trim().length >= 3 && contentEn.trim().length >= 10
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = {
                            val words = contentEn.trim().split(Regex("\\s+")).size
                            val minutes = (words / 180).coerceAtLeast(1)
                            val tags = tagsInput.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                            onSaveStory(
                                editingId,
                                titleEn,
                                titleAm,
                                summaryEn,
                                "",
                                contentEn,
                                contentAm,
                                selectedCategory,
                                tags,
                                selectedCover,
                                minutes,
                                status
                            ) {
                                resetForm()
                            }
                        },
                        enabled = canSubmit,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(48.dp).testTag("studio_save_button")
                    ) {
                        Text(lang.pick("Save Story", "ጽሑፍ አስቀምጥ"))
                    }
                    if (editingId != null) {
                        OutlinedButton(
                            onClick = ::resetForm,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text(lang.pick("Cancel", "ሰርዝ"))
                        }
                    }
                }
            }

            if (state.submissions.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(10.dp))
                    SectionHeaderRow(title = lang.pick("Your Stories", "የእርስዎ ጽሑፎች"))
                }
                items(state.submissions, key = { it.id }) { sub ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (sub.status != "archived") {
                                    editingId = sub.id
                                    titleEn = sub.titleEn
                                    titleAm = sub.titleAm
                                    summaryEn = sub.summaryEn
                                    contentEn = sub.contentEn
                                    contentAm = sub.contentAm
                                    selectedCategory = sub.category
                                    selectedCover = sub.coverKey
                                    tagsInput = sub.tagsJoined
                                        .split(",")
                                        .map { it.trim() }
                                        .filter { it.isNotEmpty() }
                                        .joinToString(", ")
                                    status = sub.status
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${sub.status.uppercase()} · ${sub.category}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = lang.pick(sub.titleEn, sub.titleAm),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }
                            Spacer(Modifier.width(8.dp))
                            IconButton(
                                onClick = { onDeleteStory(sub.id) },
                                modifier = Modifier.minimumInteractiveComponentSize()
                            ) {
                                Icon(
                                    Icons.Rounded.DeleteOutline,
                                    contentDescription = lang.pick("Delete", "ሰርዝ"),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
