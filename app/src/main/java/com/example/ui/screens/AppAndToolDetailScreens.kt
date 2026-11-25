package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.HighlightOff
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Shop
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.core.localization.AppStrings
import com.example.core.localization.pick
import com.example.data.model.AiTool
import com.example.data.model.AppEntry
import com.example.ui.components.SectionHeaderRow
import com.example.ui.components.coverDrawableForKey
import com.example.ui.state.MainDashboardState
import com.example.ui.theme.CoralRed
import com.example.ui.theme.EmeraldAccent
import com.example.ui.theme.WarmAmber

@Composable
fun AppDetailScreen(
    appId: String,
    state: MainDashboardState,
    onBack: () -> Unit,
    onToggleBookmark: (AppEntry) -> Unit
) {
    val lang = state.language
    val context = LocalContext.current
    val app = remember(appId, state.applications) {
        state.applications.firstOrNull { it.id == appId } ?: state.applications.first()
    }
    val isSaved = remember(state.bookmarks, app.id) {
        state.bookmarks.any { it.itemType == "application" && it.itemId == app.id }
    }

    fun openUrl(url: String) {
        runCatching {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
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
                    text = app.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { onToggleBookmark(app) }, modifier = Modifier.minimumInteractiveComponentSize()) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = AppStrings.save(lang),
                        tint = if (isSaved) WarmAmber else MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 720.dp),
                contentPadding = PaddingValues(18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = app.accentColor.copy(alpha = 0.16f),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.size(68.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = app.name.take(2).uppercase(),
                                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                                    color = app.accentColor
                                )
                            }
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(app.name, style = MaterialTheme.typography.headlineSmall)
                            Text(
                                text = "${app.developer} · ${lang.pick(app.categoryLabelEn, app.categoryLabelAm)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                item {
                    Image(
                        painter = painterResource(id = coverDrawableForKey(app.coverKey)),
                        contentDescription = app.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(RoundedCornerShape(16.dp))
                    )
                }

                item {
                    Text(
                        text = lang.pick(app.descriptionEn, app.descriptionAm),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { openUrl(app.playStoreUrl) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).height(48.dp)
                        ) {
                            Icon(Icons.Rounded.Shop, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Google Play")
                        }
                        OutlinedButton(
                            onClick = { openUrl(app.officialUrl) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).height(48.dp)
                        ) {
                            Icon(Icons.Rounded.OpenInNew, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(lang.pick("Website", "ድር ጣቢያ"))
                        }
                    }
                }

                item {
                    SectionHeaderRow(title = lang.pick("Key Features", "ዋና ባህሪያት"))
                    Spacer(Modifier.height(8.dp))
                    val features = if (lang.code == "am") app.featuresAm else app.featuresEn
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        features.forEach { feat ->
                            Text("• $feat", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = EmeraldAccent.copy(alpha = 0.1f),
                            border = BorderStroke(1.dp, EmeraldAccent.copy(alpha = 0.35f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Rounded.CheckCircleOutline, contentDescription = null, tint = EmeraldAccent, modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.width(6.dp))
                                    Text(lang.pick("Pros", "ጥቅሞች"), style = MaterialTheme.typography.titleSmall, color = EmeraldAccent)
                                }
                                Spacer(Modifier.height(6.dp))
                                (if (lang.code == "am") app.prosAm else app.prosEn).forEach {
                                    Text("• $it", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 2.dp))
                                }
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = CoralRed.copy(alpha = 0.1f),
                            border = BorderStroke(1.dp, CoralRed.copy(alpha = 0.35f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Rounded.HighlightOff, contentDescription = null, tint = CoralRed, modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.width(6.dp))
                                    Text(lang.pick("Cons", "ጉድለቶች"), style = MaterialTheme.typography.titleSmall, color = CoralRed)
                                }
                                Spacer(Modifier.height(6.dp))
                                (if (lang.code == "am") app.consAm else app.consEn).forEach {
                                    Text("• $it", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 2.dp))
                                }
                            }
                        }
                    }
                }

                item {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = lang.pick("Editorial Review", "የአዘጋጁ ግምገማ"),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = lang.pick(app.reviewEn, app.reviewAm),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AiToolDetailScreen(
    toolId: String,
    state: MainDashboardState,
    onBack: () -> Unit,
    onToggleBookmark: (AiTool) -> Unit
) {
    val lang = state.language
    val context = LocalContext.current
    val tool = remember(toolId, state.aiTools) {
        state.aiTools.firstOrNull { it.id == toolId } ?: state.aiTools.first()
    }
    val isSaved = remember(state.bookmarks, tool.id) {
        state.bookmarks.any { it.itemType == "ai_tool" && it.itemId == tool.id }
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
                    text = tool.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { onToggleBookmark(tool) }, modifier = Modifier.minimumInteractiveComponentSize()) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = AppStrings.save(lang),
                        tint = if (isSaved) WarmAmber else MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 720.dp),
                contentPadding = PaddingValues(18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Image(
                        painter = painterResource(id = coverDrawableForKey(tool.coverKey)),
                        contentDescription = tool.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(RoundedCornerShape(16.dp))
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(tool.name, style = MaterialTheme.typography.headlineMedium)
                            Text(
                                text = "${tool.provider} · ${lang.pick(tool.categoryLabelEn, tool.categoryLabelAm)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = tool.pricingTier,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = lang.pick(tool.descriptionEn, tool.descriptionAm),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                item {
                    Button(
                        onClick = {
                            runCatching {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(tool.officialUrl)))
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Icon(Icons.Rounded.OpenInNew, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(lang.pick("Open Official Website", "ዋናውን ድር ጣቢያ ክፈት"))
                    }
                }

                item {
                    SectionHeaderRow(title = lang.pick("Best Ethiopian Use Cases", "ዋና የአጠቃቀም ዘርፎች"))
                    Spacer(Modifier.height(8.dp))
                    val uses = if (lang.code == "am") tool.useCasesAm else tool.useCasesEn
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        uses.forEach { Text("• $it", style = MaterialTheme.typography.bodyMedium) }
                    }
                }

                item {
                    SectionHeaderRow(title = lang.pick("Quick Start Tutorial", "ፈጣን የአጠቃቀም መመሪያ"))
                    Spacer(Modifier.height(8.dp))
                    val steps = if (lang.code == "am") tool.tutorialStepsAm else tool.tutorialStepsEn
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        steps.forEachIndexed { idx, step ->
                            Text("${idx + 1}. $step", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
