package com.example

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.core.localization.AppLanguage
import com.example.core.localization.AppStrings
import com.example.data.local.AddisPressDatabase
import com.example.data.local.LocalDataRepository
import com.example.data.preferences.PreferencesRepository
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AiToolDetailScreen
import com.example.ui.screens.AppDetailScreen
import com.example.ui.screens.ArticleDetailScreen
import com.example.ui.screens.DiscoverScreen
import com.example.ui.screens.EditorialStudioScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SavedScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.state.MainViewModel
import com.example.ui.theme.AddisPressTheme

object Routes {
    const val HOME = "home"
    const val EXPLORE = "explore"
    const val DISCOVER = "discover"
    const val SAVED = "saved"
    const val PROFILE = "profile"
    const val SEARCH = "search?query={query}"
    const val ARTICLE_DETAIL = "article/{articleId}"
    const val APP_DETAIL = "app/{appId}"
    const val AI_TOOL_DETAIL = "ai_tool/{toolId}"
    const val STUDIO = "studio"
    const val ADMIN = "admin"

    fun search(query: String = "") = "search?query=${Uri.encode(query)}"
    fun article(id: String) = "article/$id"
    fun app(id: String) = "app/$id"
    fun aiTool(id: String) = "ai_tool/$id"
}

private data class BottomTab(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val labelProvider: (AppLanguage) -> String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AddisPressAppShell()
        }
    }
}

@Composable
private fun AddisPressAppShell() {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                val appContext = context.applicationContext
                val db = AddisPressDatabase.getInstance(appContext)
                MainViewModel(
                    localRepository = LocalDataRepository(db.addisPressDao()),
                    preferencesRepository = PreferencesRepository(appContext)
                )
            }
        }
    )

    val state by viewModel.dashboardState.collectAsStateWithLifecycle()
    val selectedChip by viewModel.selectedHomeChip.collectAsStateWithLifecycle()
    val homeSearchQuery by viewModel.homeSearchQuery.collectAsStateWithLifecycle()
    val visibleCount by viewModel.homeVisibleCount.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.snackbarEvents.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    val lang = state.language

    AddisPressTheme(
        themeMode = state.preferences.themeMode,
        isAmharic = lang == AppLanguage.AM
    ) {
        val navController = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        val tabs = remember {
            listOf(
                BottomTab(Routes.HOME, Icons.Filled.Home, Icons.Outlined.Home, AppStrings::navHome),
                BottomTab(Routes.EXPLORE, Icons.Filled.Explore, Icons.Outlined.Explore, AppStrings::navExplore),
                BottomTab(Routes.DISCOVER, Icons.Filled.Widgets, Icons.Outlined.Widgets, AppStrings::navDiscover),
                BottomTab(Routes.SAVED, Icons.Filled.Bookmark, Icons.Outlined.BookmarkBorder, AppStrings::navSaved),
                BottomTab(Routes.PROFILE, Icons.Filled.Person, Icons.Outlined.Person, AppStrings::navProfile)
            )
        }
        val topLevelRoutes = remember { tabs.map { it.route }.toSet() }
        val showTopLevelNav = currentRoute in topLevelRoutes

        fun navigateTopLevel(route: String) {
            navController.navigate(route) {
                popUpTo(Routes.HOME) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val useRail = maxWidth >= 600.dp

            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                bottomBar = {
                    if (showTopLevelNav && !useRail) {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                        ) {
                            tabs.forEach { tab ->
                                val selected = currentRoute == tab.route
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { navigateTopLevel(tab.route) },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                            contentDescription = tab.labelProvider(lang)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = tab.labelProvider(lang),
                                            maxLines = 1
                                        )
                                    },
                                    modifier = Modifier.testTag("nav_${tab.route}")
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = if (showTopLevelNav && !useRail) innerPadding.calculateBottomPadding() else 0.dp)
                ) {
                    if (showTopLevelNav && useRail) {
                        NavigationRail(
                            containerColor = MaterialTheme.colorScheme.surface
                        ) {
                            tabs.forEach { tab ->
                                val selected = currentRoute == tab.route
                                NavigationRailItem(
                                    selected = selected,
                                    onClick = { navigateTopLevel(tab.route) },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                            contentDescription = tab.labelProvider(lang)
                                        )
                                    },
                                    label = { Text(tab.labelProvider(lang)) },
                                    modifier = Modifier.testTag("rail_${tab.route}")
                                )
                            }
                        }
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        NavHost(
                            navController = navController,
                            startDestination = Routes.HOME
                        ) {
                            composable(Routes.HOME) {
                                HomeScreen(
                                    state = state,
                                    selectedChip = selectedChip,
                                    searchQuery = homeSearchQuery,
                                    visibleCount = visibleCount,
                                    onSelectChip = viewModel::selectHomeChip,
                                    onSearchQueryChange = viewModel::setHomeSearchQuery,
                                    onSearchSubmitted = viewModel::addRecentSearch,
                                    onLoadMore = viewModel::loadMoreHomeArticles,
                                    onOpenSearch = { q -> navController.navigate(Routes.search(q)) },
                                    onOpenArticle = { id -> navController.navigate(Routes.article(id)) },
                                    onToggleBookmark = { article -> viewModel.toggleArticleBookmark(article) },
                                    onOpenStudio = { navController.navigate(Routes.STUDIO) },
                                    onOpenAdmin = { navController.navigate(Routes.ADMIN) },
                                    onAdImpression = viewModel::recordAdImpression,
                                    onAdClicked = viewModel::recordAdClick
                                )
                            }
                            composable(Routes.EXPLORE) {
                                ExploreScreen(
                                    state = state,
                                    onToggleFollowCategory = viewModel::toggleFollowedCategory,
                                    onOpenArticle = { id -> navController.navigate(Routes.article(id)) },
                                    onAdImpression = viewModel::recordAdImpression,
                                    onAdClicked = viewModel::recordAdClick
                                )
                            }
                            composable(Routes.DISCOVER) {
                                DiscoverScreen(
                                    state = state,
                                    onOpenApp = { id -> navController.navigate(Routes.app(id)) },
                                    onOpenAiTool = { id -> navController.navigate(Routes.aiTool(id)) },
                                    onToggleAppBookmark = { app -> viewModel.toggleAppBookmark(app) },
                                    onToggleAiToolBookmark = { tool -> viewModel.toggleAiToolBookmark(tool) },
                                    onAdImpression = viewModel::recordAdImpression,
                                    onAdClicked = viewModel::recordAdClick
                                )
                            }
                            composable(Routes.SAVED) {
                                SavedScreen(
                                    state = state,
                                    onOpenArticle = { id -> navController.navigate(Routes.article(id)) },
                                    onOpenApp = { id -> navController.navigate(Routes.app(id)) },
                                    onOpenAiTool = { id -> navController.navigate(Routes.aiTool(id)) },
                                    onRemoveBookmark = viewModel::removeBookmark,
                                    onMoveBookmarkToList = viewModel::moveBookmarkToList,
                                    onAddReadingList = viewModel::addReadingList,
                                    onClearHistory = viewModel::clearReadingHistory
                                )
                            }
                            composable(Routes.PROFILE) {
                                ProfileScreen(
                                    state = state,
                                    onUpdateDisplayName = viewModel::setDisplayName,
                                    onThemeModeChange = viewModel::setThemeMode,
                                    onLanguageChange = viewModel::setLanguage,
                                    onUpdateNotificationSettings = viewModel::updateNotificationSettings,
                                    onUpdateMonetizationSettings = viewModel::updateMonetizationSettings,
                                    onOpenStudio = { navController.navigate(Routes.STUDIO) },
                                    onOpenAdmin = { navController.navigate(Routes.ADMIN) },
                                    onClearAllLocalData = viewModel::clearAllLocalData
                                )
                            }
                            composable(
                                route = Routes.SEARCH,
                                arguments = listOf(
                                    navArgument("query") {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    }
                                )
                            ) { entry ->
                                val initialQuery = entry.arguments?.getString("query").orEmpty()
                                SearchScreen(
                                    state = state,
                                    initialQuery = initialQuery,
                                    onBack = { navController.popBackStack() },
                                    onSearchSubmitted = viewModel::addRecentSearch,
                                    onClearRecentSearches = viewModel::clearRecentSearches,
                                    onOpenArticle = { id -> navController.navigate(Routes.article(id)) },
                                    onToggleArticleBookmark = { article -> viewModel.toggleArticleBookmark(article) },
                                    onOpenApp = { id -> navController.navigate(Routes.app(id)) },
                                    onOpenAiTool = { id -> navController.navigate(Routes.aiTool(id)) }
                                )
                            }
                            composable(
                                route = Routes.ARTICLE_DETAIL,
                                arguments = listOf(navArgument("articleId") { type = NavType.StringType })
                            ) { entry ->
                                val articleId = entry.arguments?.getString("articleId").orEmpty()
                                ArticleDetailScreen(
                                    articleId = articleId,
                                    state = state,
                                    onBack = { navController.popBackStack() },
                                    onToggleBookmark = { article -> viewModel.toggleArticleBookmark(article) },
                                    onToggleLike = viewModel::toggleLike,
                                    onFontScaleChange = viewModel::setReaderFontScale,
                                    onRecordProgress = viewModel::recordReadingProgress,
                                    onSubmitReport = viewModel::submitReport,
                                    onOpenArticle = { id -> navController.navigate(Routes.article(id)) },
                                    onOpenApp = { id -> navController.navigate(Routes.app(id)) },
                                    onAdImpression = viewModel::recordAdImpression,
                                    onAdClicked = viewModel::recordAdClick
                                )
                            }
                            composable(
                                route = Routes.APP_DETAIL,
                                arguments = listOf(navArgument("appId") { type = NavType.StringType })
                            ) { entry ->
                                val appId = entry.arguments?.getString("appId").orEmpty()
                                AppDetailScreen(
                                    appId = appId,
                                    state = state,
                                    onBack = { navController.popBackStack() },
                                    onToggleBookmark = { app -> viewModel.toggleAppBookmark(app) }
                                )
                            }
                            composable(
                                route = Routes.AI_TOOL_DETAIL,
                                arguments = listOf(navArgument("toolId") { type = NavType.StringType })
                            ) { entry ->
                                val toolId = entry.arguments?.getString("toolId").orEmpty()
                                AiToolDetailScreen(
                                    toolId = toolId,
                                    state = state,
                                    onBack = { navController.popBackStack() },
                                    onToggleBookmark = { tool -> viewModel.toggleAiToolBookmark(tool) }
                                )
                            }
                            composable(Routes.STUDIO) {
                                EditorialStudioScreen(
                                    state = state,
                                    onBack = { navController.popBackStack() },
                                    onSaveStory = viewModel::saveEditorialSubmission,
                                    onDeleteStory = viewModel::deleteSubmission
                                )
                            }
                            composable(Routes.ADMIN) {
                                AdminDashboardScreen(
                                    state = state,
                                    onBack = { navController.popBackStack() },
                                    onOpenStudio = { navController.navigate(Routes.STUDIO) },
                                    onUpdateHomeLayout = viewModel::updateAdminHomeLayout,
                                    onUpdateTickerMessage = viewModel::updateAdminTickerMessage,
                                    onToggleArticleHidden = viewModel::toggleArticleHidden,
                                    onToggleArticleBreaking = viewModel::toggleArticleBreaking,
                                    onToggleArticleFeatured = viewModel::toggleArticleFeatured,
                                    onToggleArticleTrending = viewModel::toggleArticleTrending,
                                    onEditArticleOverride = viewModel::editArticleOverride,
                                    onToggleAppHidden = viewModel::toggleAppHidden,
                                    onToggleAppFeatured = viewModel::toggleAppFeatured,
                                    onToggleAppTrending = viewModel::toggleAppTrending,
                                    onAddCustomApp = viewModel::addCustomApp,
                                    onDeleteCustomApp = viewModel::deleteCustomApp,
                                    onToggleToolHidden = viewModel::toggleAiToolHidden,
                                    onToggleToolFeatured = viewModel::toggleAiToolFeatured,
                                    onAddCustomAiTool = viewModel::addCustomAiTool,
                                    onDeleteCustomTool = viewModel::deleteCustomAiTool,
                                    onUpdateMonetization = viewModel::updateMonetizationSettings,
                                    onToggleAdActive = viewModel::toggleAdActive,
                                    onCreateAdCampaign = viewModel::createAdCampaign,
                                    onDeleteAdCampaign = viewModel::deleteAdCampaign,
                                    onHideReportedArticle = viewModel::hideReportedArticle,
                                    onDismissReport = viewModel::dismissReport,
                                    onUpdateSubmissionStatus = viewModel::updateSubmissionStatus,
                                    onDeleteSubmission = viewModel::deleteSubmission,
                                    onResetAllOverrides = viewModel::resetAllAdminOverrides
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
