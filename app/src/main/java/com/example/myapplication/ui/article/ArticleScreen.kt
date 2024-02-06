package com.example.myapplication.ui.article

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetnews.model.Post
import com.example.myapplication.R
import com.example.myapplication.data.posts.post3
import com.example.myapplication.ui.theme.NewsAppTheme
import com.example.myapplication.utils.BookmarkButton
import com.example.myapplication.utils.FavoriteButton
import com.example.myapplication.utils.ShareButton
import com.example.myapplication.utils.TextSettingsButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailsScreen(
    post: Post,
    isFavorite: Boolean,
    onToggleFavorite: (String) -> Unit,
    isExpandedLargeScreen: Boolean = false,
    onBackCallback: () -> Unit
) {
    var shouldShowUnimplementedError by rememberSaveable {
        mutableStateOf(false)
    }
    if (shouldShowUnimplementedError)
        FunctionalityNotAvailablePopup{
            shouldShowUnimplementedError = false
        }


    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topBarState)
    Scaffold(
        topBar = {
            TopAppBar(
                title = post.publication?.name.orEmpty(),
                navigationContent = {
                    if (!isExpandedLargeScreen) {
                        IconButton(onClick = onBackCallback) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                scrollBehavior
            )
        },
        bottomBar = {
            if (!isExpandedLargeScreen) {
                BottomAppBar {
                    FavoriteButton(onClick = { shouldShowUnimplementedError = true })
                    BookmarkButton(isFavorite = isFavorite, toggleFavorites = { onToggleFavorite(post.id) })
                    ShareButton(onClick = { shouldShowUnimplementedError = true })
                    TextSettingsButton(onClick = { shouldShowUnimplementedError = true })
                }
            }
        }
    ) { innerPadding ->
        val contentModifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)

        PostContent(
            post = post,
            contentModifier,
            innerPadding
        )
    }
}


// reusable composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    title: String,
    navigationContent: @Composable () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    CenterAlignedTopAppBar(
        title = {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.icon_article_background),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                )
                Column {
                    Text(
                        text = stringResource(R.string.published_in, title),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        },
        navigationIcon = navigationContent,
        scrollBehavior = scrollBehavior
    )
}

/**
 * Display a popup explaining functionality not available.
 *
 * @param onDismiss (event) request the popup be dismissed
 */
@Composable
private fun FunctionalityNotAvailablePopup(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = stringResource(id = R.string.article_functionality_not_available),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.close))
            }
        }
    )
}

@Preview
@Composable
private fun PreviewArticleDetailsScreen() {
    NewsAppTheme {
        ArticleDetailsScreen(post = post3, false, {}, false, {})
    }
}