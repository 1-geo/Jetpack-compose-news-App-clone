package com.example.myapplication.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetnews.model.Post
import com.example.myapplication.R
import com.example.myapplication.data.posts.post2
import com.example.myapplication.data.posts.post3
import com.example.myapplication.ui.theme.NewsAppTheme
import com.example.myapplication.utils.BookmarkButton

// simple post
@Composable
fun PostCardSimple(
    postClick: (String) -> Unit,
    isFavorite: Boolean,
    toggleFavorites: () -> Unit,
    post: Post
) {
    Row(
        modifier = Modifier.clickable { postClick(post.id) }
    ) {
        PostImage(post = post, Modifier.padding(16.dp))
        Column (
            modifier = Modifier
                .padding(vertical = 10.dp)
                .weight(1f)
        ) {
            PostTitle(post = post, modifier = Modifier)
            PostAuthorAndTime(post, modifier = Modifier)
        }
        BookmarkButton(
            isFavorite = isFavorite,
            toggleFavorites = toggleFavorites,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}

// history post
@Composable
fun PostCardHistory(
    postClick: (String) -> Unit,
    post: Post
) {
    // state for showing dialog on more click
    var shouldOpenDialog by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier.clickable { postClick(post.id) }
    ) {

        PostImage(post = post, Modifier.padding(16.dp))
        Column (
            modifier = Modifier
                .padding(vertical = 10.dp)
                .weight(1f)
        ) {
            Text(
                text = stringResource(id = R.string.home_post_based_on_history),
                style = MaterialTheme.typography.labelMedium
            )
            PostTitle(post = post, modifier = Modifier)
            PostAuthorAndTime(post, modifier = Modifier)
        }
        IconButton(onClick = { shouldOpenDialog = true }) {
            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
        }


        if (shouldOpenDialog) {
            AlertDialog(
                modifier = Modifier.padding(20.dp),
                onDismissRequest = { shouldOpenDialog = false },
                title = {
                    Text(
                        text = stringResource(id = R.string.fewer_stories),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.fewer_stories_content),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                confirmButton = {
                    Text(
                        text = stringResource(id = R.string.agree),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(
                                15.dp
                            )
                            .clickable { shouldOpenDialog = false }
                    )
                })
        }
    }
}


// reuseable Composables.
@Composable
private fun PostImage(post: Post, modifier: Modifier) {
    Image(
        painter = painterResource(id = post.imageThumbId),
        contentDescription = null,
        modifier = modifier
            .size(42.dp)
            .clip(shape = MaterialTheme.shapes.small)
    )
}

@Composable
private fun PostTitle(post: Post, modifier: Modifier) {
    Text(
        text = post.title,
        style = MaterialTheme.typography.titleMedium,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@Composable
private fun PostAuthorAndTime(post: Post, modifier: Modifier) {
    Row {
        Text(
            text = stringResource(
            id = R.string.home_post_min_read,
            formatArgs = arrayOf(
                post.metadata.author.name,
                post.metadata.readTimeMinutes)
            ),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier
        )
    }
}


// Preview
@Preview("Simple post card")
@Composable
fun PreviewPostCardSimple() {
    NewsAppTheme {
        PostCardSimple({ }, false, { },post3)
    }
}

@Preview("Simple post card")
@Composable
fun PreviewPostCardHistory() {
    NewsAppTheme {
        PostCardHistory({ }, post2)
    }
}