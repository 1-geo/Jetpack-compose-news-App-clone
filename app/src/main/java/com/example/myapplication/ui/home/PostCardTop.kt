package com.example.myapplication.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetnews.model.Post
import com.example.myapplication.R
import com.example.myapplication.data.posts.post2
import com.example.myapplication.ui.theme.NewsAppTheme

@Composable
fun PostCardTop(
    post: Post,
    articleClicked: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .clickable { articleClicked(post.id) }
            .padding(16.dp)
    ) {

        Text(
            text = stringResource(id = R.string.home_top_section_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Image takes full screen. so provide min height
        Image(
            painter = painterResource(id = post.imageId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 180.dp)
                .clip(MaterialTheme.shapes.medium)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = post.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 8.dp),
            maxLines = 3
        )

        Text(
            text = post.metadata.author.name,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = stringResource(
                id = R.string.home_post_min_read,
                formatArgs = arrayOf(post.metadata.date, post.metadata.readTimeMinutes)
            ),
            style = MaterialTheme.typography.labelLarge
        )

    }
}


@Preview
@Composable
fun PreviewPostCardTop() {
    NewsAppTheme {
        PostCardTop(post = post2, { })
    }
}