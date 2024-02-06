package com.example.myapplication.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.ui.article.ArticleDetailsScreen

/**
 * contains HomeViewModel
 */
@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel,
    isLargeExpandedScreen: Boolean,
    openDrawer: () -> Unit
) {
    // Collects values from this Flow and represents its latest value via State in a lifecycle-aware manner.
    // Every time there would be new value posted into the Flow the returned State will be updated causing recomposition of every State.value usage whenever the lifecycle is at least minActiveState.
    // UiState of the HomeScreen
    val uiState = homeViewModel.uiState.collectAsStateWithLifecycle()

    // store home list state outside of the decision into variable
    // otherwise if passed directly into HomeFeedScreen() then
    // returning from ArticleDetails scroll will not get remembered
    val homeListLazyState = rememberLazyListState()

    when(getHomeScreenType(uiState.value)) {
        HomeScreenType.HomeFeed -> {
            HomeFeedScreen(
                homeViewModelState = uiState.value,
                shouldShowTopAppBar = !isLargeExpandedScreen,
                onFavoriteToggledCallback = { homeViewModel.toggleFavorite(it) },
                onArticleClickedCallback = { homeViewModel.selectArticle(it) },
                onRefreshCallback = { homeViewModel.refreshPosts() },
                onErrorDismissCallback = { homeViewModel.errorShown(it) },
                openDrawerCallback = { openDrawer() },
                homeListLazyListState = homeListLazyState /* Side affect when returning from Article details -> rememberLazyListState()*/,
                snackbarHostState = remember { SnackbarHostState() },
                onSearchInputChanged = { homeViewModel.onSearchInputChanged(it) }
            )
        }
        HomeScreenType.HomeArticleDetails -> {
            val selectedPostId = uiState.value.selectedPostId
            uiState.value.postsFeed?.allPosts?.first {
                it.id == selectedPostId
            }?.let { post ->
                ArticleDetailsScreen(
                    post = post,
                    isFavorite = uiState.value.favorites.contains(post.id),
                    onToggleFavorite = { homeViewModel.toggleFavorite(post.id) },
                    isExpandedLargeScreen = isLargeExpandedScreen,
                    onBackCallback = { homeViewModel.interactedWithFeed() }
                )

                // Hardware device back handler
                // If we are just showing the detail, have a back press switch to the list.
                // This doesn't take anything more than notifying that we "interacted with the list"
                // since that is what drives the display of the feed
                BackHandler {
                    homeViewModel.interactedWithFeed()
                }
            }
        }
    }
}

private fun getHomeScreenType(uiState: HomeViewModel.HomeViewModelState): HomeScreenType {
    return when(uiState.getState()) {
        HomeViewModel.HomeViewModelState.State.HasPosts -> {
            if (uiState.isArticleOpen)
                HomeScreenType.HomeArticleDetails
            else
                HomeScreenType.HomeFeed
        }
        HomeViewModel.HomeViewModelState.State.NoPosts -> {
            HomeScreenType.HomeFeed
        }
    }
}

enum class HomeScreenType {
    HomeFeed,
    HomeArticleDetails
}