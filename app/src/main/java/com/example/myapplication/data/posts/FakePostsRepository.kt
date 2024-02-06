package com.example.myapplication.data.posts

import com.example.jetnews.model.Post
import com.example.jetnews.model.PostsFeed
import com.example.myapplication.data.Result
import com.example.myapplication.utils.addOrRemove
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.lang.IllegalStateException

/**
 * Repository with data stored inside MutableStateFlow.
 */
class FakePostsRepository: PostsRepository {

    private val favorites = MutableStateFlow<Set<String>>(setOf())

    private val postsFeed = MutableStateFlow<PostsFeed?>(null)

    override suspend fun getPost(postId: String): Result<Post> {
        return withContext(Dispatchers.IO) {
            val post = postsFeed.value?.allPosts?.find { it.id == postId }
            if (post == null)
                Result.Error(IllegalArgumentException())
            else
                Result.Success(post)
        }
    }

    override suspend fun getPostsFeed(): Result<PostsFeed> {
        return withContext(Dispatchers.IO) {
            // pretend like a network call
            delay(800)
            if (!shouldRandomlyError()) {
                postsFeed.update { posts }
                Result.Success(posts)
            } else {
                Result.Error(IllegalStateException())
            }
        }
    }

    override fun toggleFavorites(postId: String) {
        favorites.update {
            it.addOrRemove(postId)
        }
    }

    override fun observeFavorites(): Flow<Set<String>> {
        return favorites
    }

    private var i = 0
    private fun shouldRandomlyError(): Boolean {
        return ++i % 5 == 0
    }
}