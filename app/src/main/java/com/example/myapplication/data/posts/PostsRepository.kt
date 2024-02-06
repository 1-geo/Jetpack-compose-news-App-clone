package com.example.myapplication.data.posts

import com.example.jetnews.model.Post
import com.example.jetnews.model.PostsFeed
import com.example.myapplication.data.Result
import kotlinx.coroutines.flow.Flow

interface PostsRepository {

    /**
     * get single Post
     */
    suspend fun getPost(postId: String): Result<Post>

    /**
     * get PostFeed
     */
    suspend fun getPostsFeed(): Result<PostsFeed>

    /**
     * toggle Post as Favorite
     */
    fun toggleFavorites(postId: String)

    /**
     * observe Posts that are favorited
     */
    fun observeFavorites(): Flow<Set<String>>

}