package com.example.myapplication.ui.home

import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.jetnews.model.Post
import com.example.jetnews.model.PostsFeed
import com.example.myapplication.R
import com.example.myapplication.ui.NewsSnackbarHost
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


// HomeFeed
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFeedScreen(
    homeViewModelState: HomeViewModel.HomeViewModelState,
    shouldShowTopAppBar: Boolean,
    onFavoriteToggledCallback: (String) -> Unit,
    onArticleClickedCallback: (String) -> Unit,
    onRefreshCallback: () -> Unit,
    onErrorDismissCallback: (Long) -> Unit,
    openDrawerCallback: () -> Unit,
    homeListLazyListState: LazyListState,
    snackbarHostState: SnackbarHostState,
    searchInput: String = "",
    onSearchInputChanged: (String) -> Unit
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    Scaffold(
        snackbarHost = {
            NewsSnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            if (shouldShowTopAppBar)
                HomeTopAppBar(
                    topAppBarState = topAppBarState,
                    openDrawerCallback = openDrawerCallback
                )
        }
    ) { innerPadding ->
        val contentModifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)

        LoadingContent(
            isEmpty = when (homeViewModelState.getState()) {
                HomeViewModel.HomeViewModelState.State.HasPosts -> { false }
                HomeViewModel.HomeViewModelState.State.NoPosts -> { homeViewModelState.isLoading }
            },
            emptyContent = { FullScreenLoading() },
            isLoading = homeViewModelState.isLoading,
            onRefreshCallback = { onRefreshCallback() }
        ) {
            if (homeViewModelState.getState() == HomeViewModel.HomeViewModelState.State.HasPosts) {
                // display posts use content modifier to fire scroll events
                PostList(
                    postFeed = homeViewModelState.postsFeed!!,
                    favorites = homeViewModelState.favorites,
                    modifier = contentModifier,
                    padding = innerPadding,
                    onFavoriteToggledCallback = onFavoriteToggledCallback,
                    onArticleClickedCallback = onArticleClickedCallback,
                    homeListLazyListState = homeListLazyListState,
                    onSearchInputChanged = onSearchInputChanged
                )

            } else {
                if (homeViewModelState.errorMessages.isEmpty()) {
                    // show refresh option
                    TextButton(
                        onClick = { onRefreshCallback() },
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        Text(
                            text = stringResource(id = R.string.home_tap_to_load_content),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    // show error empty screen
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
                }
            }
        }
    }

}

@Composable
fun PostList(
    postFeed: PostsFeed,
    favorites: Set<String>,
    modifier: Modifier,
    padding: PaddingValues,
    onFavoriteToggledCallback: (String) -> Unit,
    onArticleClickedCallback: (String) -> Unit,
    homeListLazyListState: LazyListState,
    searchInput: String = "",
    onSearchInputChanged: (String) -> Unit
) {
    // TODO if expandedLargeScreen show Search

    LazyColumn(
        modifier = modifier,
        contentPadding = padding,
        state = homeListLazyListState
    ) {

        // highlighted
        item {
            PostCardTop(post = postFeed.highlightedPost, onArticleClickedCallback)
            PostListDivider()
        }

        // recommended
        item {
            PostSimpleSection(
                favorites = favorites,
                post = postFeed.recommendedPosts,
                onArticleClickedCallback = onArticleClickedCallback,
                onFavoriteToggledCallback = onFavoriteToggledCallback
            )
        }

        // popular
        item {
            PostPopularSection(
                postFeed.popularPosts,
                onArticleClickedCallback = onArticleClickedCallback
            )
        }

        // history
        item {
            PostHistorySection(
                post = postFeed.recentPosts,
                onArticleClickedCallback
            )
        }
    }

}
// reusable composables

@Composable
fun PostSimpleSection(
    favorites: Set<String>,
    post: List<Post>,
    onArticleClickedCallback: (String) -> Unit,
    onFavoriteToggledCallback: (String) -> Unit
    ) {
    Column {
        post.forEach{ post ->
            PostCardSimple(
                postClick = onArticleClickedCallback,
                isFavorite = favorites.contains(post.id),
                toggleFavorites = { onFavoriteToggledCallback(post.id) },
                post = post
            )
            PostListDivider()
        }
    }
}

@Composable
fun PostPopularSection(
    post: List<Post>,
    onArticleClickedCallback: (String) -> Unit
    ) {
    Column {
        Text(
            modifier = Modifier.padding(12.dp),
            text = stringResource(id = R.string.home_popular_section_title),
            style = MaterialTheme.typography.titleLarge
        )

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .height(IntrinsicSize.Max)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            post.forEach{ post ->
                PostCardPopular(
                    post = post,
                    articleClicked = onArticleClickedCallback
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        PostListDivider()
    }
}

@Composable
fun PostHistorySection(
    post: List<Post>,
    onArticleClickedCallback: (String) -> Unit
) {
    Column {
        post.forEach{
            PostCardHistory(postClick = onArticleClickedCallback, post = it)
            PostListDivider()
        }
    }
}

@Composable
private fun PostListDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 14.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    )
}

/**
 * Top App Bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopAppBar(
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior? = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState),
    openDrawerCallback: () -> Unit
) {
    val context = LocalContext.current
    CenterAlignedTopAppBar(
        title = {
            Image(
                painter = painterResource(id = R.drawable.ic_jetnews_wordmark),
                contentDescription = stringResource(id = R.string.app_name),
                contentScale = ContentScale.Inside,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            IconButton(onClick = { openDrawerCallback() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_jetnews_logo),
                    contentDescription = stringResource(id = R.string.cd_open_navigation_drawer)
                )
            }
        },
        actions = {
            IconButton(onClick = {
                Toast
                    .makeText(context, "Search is not yet Implemented.", LENGTH_LONG)
                    .show()
            }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(id = R.string.cd_search)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

/**
 * Util displays Loading if content is loading else displays error or content.
 */
@Composable
private fun LoadingContent(
    isEmpty: Boolean,
    emptyContent: @Composable () -> Unit,
    isLoading: Boolean,
    onRefreshCallback: () -> Unit,
    content: @Composable () -> Unit
) {
    if (isEmpty) {
        emptyContent()
    } else {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isLoading),
            onRefresh = { onRefreshCallback() },
            content = content
        )
    }
}

/**
 * Loading
 */
@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}